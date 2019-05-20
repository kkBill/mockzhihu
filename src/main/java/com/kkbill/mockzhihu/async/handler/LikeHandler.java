package com.kkbill.mockzhihu.async.handler;

import com.kkbill.mockzhihu.async.EventHandler;
import com.kkbill.mockzhihu.async.EventModel;
import com.kkbill.mockzhihu.async.EventType;
import com.kkbill.mockzhihu.model.Message;
import com.kkbill.mockzhihu.model.User;
import com.kkbill.mockzhihu.service.MessageService;
import com.kkbill.mockzhihu.service.UserService;
import com.kkbill.mockzhihu.util.MockZhihuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandler(EventModel eventModel){
        Message message = new Message();
        message.setFrom_id(MockZhihuUtil.SYSTEMCONTROLLER_USERID);
        message.setTo_id(eventModel.getEntityOwnerId());
        message.setCreated_date(new Date());
        User user = userService.getUserById(eventModel.getActorId());
        message.setContent("用户["+user.getName()+"]赞了你的评论。");
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
