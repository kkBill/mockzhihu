package com.kkbill.mockzhihu.controller;

import com.kkbill.mockzhihu.model.*;
import com.kkbill.mockzhihu.service.CommentService;
import com.kkbill.mockzhihu.service.QuestionService;
import com.kkbill.mockzhihu.service.UserService;
import com.kkbill.mockzhihu.util.MockZhihuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    //展示单个问题
    @RequestMapping(value = "/question/{qid}", method = {RequestMethod.GET})
    public String questionDetail(Model model, @PathVariable("qid") int qid) {
        Question question = questionService.getQuestionById(qid);

        //把问题和该问题的评论放入视图
        model.addAttribute("question", question);
        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> vos = new ArrayList<>();
        for (Comment comment : commentList) {
            ViewObject vo = new ViewObject();
            vo.setObject("comment", comment);//评论
            vo.setObject("user", userService.getUserById(comment.getUser_id()));//发表该评论的用户
            vos.add(vo);
        }
        model.addAttribute("vos", vos);

        return "detail";
    }



    /**
     * @param title 这是用户在客户端输入的问题标题
     * @param content 这是问题的内容
     * @return 返回值为什么是一个 Json格式的字符串呢？这个我不懂！！！是和前端js代码有关吗？这里为什么又是@ResponseBody
     */
    @PostMapping(value = "/question/add")
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {

        try {
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setComment_count(0);
            question.setCreated_date(new Date());
            if (hostHolder.getUser() == null) {
                return MockZhihuUtil.getJSONString(999);
            } else {
                question.setUser_id(hostHolder.getUser().getId());
            }

            if (questionService.addQuestion(question) > 0) {
                return MockZhihuUtil.getJSONString(0);//code为0表示成功
            }
        } catch (Exception e) {
            logger.error("增加问题失败" + e.getMessage());
        }
        return MockZhihuUtil.getJSONString(1, "失败");//code非0表示失败
    }

}
