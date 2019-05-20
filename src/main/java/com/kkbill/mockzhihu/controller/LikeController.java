package com.kkbill.mockzhihu.controller;

import com.kkbill.mockzhihu.async.EventModel;
import com.kkbill.mockzhihu.async.EventProducer;
import com.kkbill.mockzhihu.async.EventType;
import com.kkbill.mockzhihu.model.Comment;
import com.kkbill.mockzhihu.model.EntityType;
import com.kkbill.mockzhihu.model.HostHolder;
import com.kkbill.mockzhihu.service.CommentService;
import com.kkbill.mockzhihu.service.LikeService;
import com.kkbill.mockzhihu.util.MockZhihuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private EventProducer eventProducer;

    @PostMapping(value = "/like")
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser()==null){
            return MockZhihuUtil.getJSONString(999);
        }
        Comment comment = commentService.getCommentById(commentId);

        EventModel event = new EventModel();
        event.setType(EventType.LIKE);//事件的类型是点赞
        event.setActorId(hostHolder.getUser().getId());//事件的触发者
        event.setEntityId(commentId);//点赞的对象
        event.setEntityType(EntityType.ENTITY_COMMENT);//点赞的对象是一条评论
        event.setEntityOwnerId(comment.getUser_id());//被点赞的用户
        event.setExts("questionId",String.valueOf(comment.getEntity_id()));

        eventProducer.sendEvent(event);

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return MockZhihuUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @PostMapping(value = "/dislike")
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser()==null){
            return MockZhihuUtil.getJSONString(999);
        }
        long likeCount = likeService.dislike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return MockZhihuUtil.getJSONString(0,String.valueOf(likeCount));
    }
}
