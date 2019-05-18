package com.kkbill.mockzhihu.controller;

import com.kkbill.mockzhihu.model.HostHolder;
import com.kkbill.mockzhihu.model.Message;
import com.kkbill.mockzhihu.model.User;
import com.kkbill.mockzhihu.model.ViewObject;
import com.kkbill.mockzhihu.service.MessageService;
import com.kkbill.mockzhihu.service.UserService;
import com.kkbill.mockzhihu.util.MockZhihuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.beans.Expression;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    ////用于显示当前用户所有的消息列表
    @GetMapping("/msg/list")
    public String getConversationList(Model model) {
        try {
            if(hostHolder.getUser()==null){ //当前用户未登录，则跳转到登录页面
                return "redirect:/reglogin";
            }
            int localUserId = hostHolder.getUser().getId();
            List<Message> messageList = messageService.getAllConversationList(localUserId, 0, 10);
            List<ViewObject> vos = new ArrayList<>();
            for (Message msg : messageList) {
                ViewObject vo = new ViewObject();
                vo.setObject("message", msg);
                int targetId = (msg.getFrom_id() == localUserId) ? msg.getTo_id() : msg.getFrom_id();
                User targetUser = userService.getUserById(targetId);
                vo.setObject("targetUser",targetUser);
                vo.setObject("unread",messageService.getUnreadConversationCount(localUserId,msg.getConversation_id()));

                vos.add(vo);
            }

            model.addAttribute("vos",vos);

        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }

        return "letter";
    }

    //用于显示当前用户与某一用户的对话信息
    @GetMapping("/msg/detail")
    public String getConversationDetail(Model model,
                                        @RequestParam("conversationId") String conversationId) {

        try {
            List<Message> conversationList = messageService.getSingleConversationDetail(conversationId, 0, 10);
            List<ViewObject> vos = new ArrayList<>();
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.setObject("message", msg);
                User user = userService.getUserById(msg.getFrom_id());
                if (user == null) {
                    continue;
                }
                vo.setObject("user", user);
                vos.add(vo);
            }
            model.addAttribute("vos", vos);
        } catch (Exception e) {
            logger.error("获取详情消息失败" + e.getMessage());
        }

        return "letterDetail";
    }


    //通过弹窗向其他用户发送信息
    @PostMapping(value = "/msg/addMessage")
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {

        try {
            if (hostHolder.getUser() == null) {
                return MockZhihuUtil.getJSONString(999, "未登录");//999表示直接跳转到登录页面，这是前端代码约定好的
            }
            User toUser = userService.getUserByName(toName);
            if (toUser == null) {
                return MockZhihuUtil.getJSONString(1, "发送的用户不存在");
            }

            Message message = new Message();
            message.setContent(content);
            message.setFrom_id(hostHolder.getUser().getId());
            message.setTo_id(toUser.getId());
            message.setCreated_date(new Date());

            messageService.addMessage(message);
            return MockZhihuUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("发送站内信失败" + e.getMessage());
            return MockZhihuUtil.getJSONString(1, "发送站内信失败");
        }
    }


}
