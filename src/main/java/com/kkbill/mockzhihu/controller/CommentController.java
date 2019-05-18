package com.kkbill.mockzhihu.controller;

import com.kkbill.mockzhihu.model.Comment;
import com.kkbill.mockzhihu.model.EntityType;
import com.kkbill.mockzhihu.model.HostHolder;
import com.kkbill.mockzhihu.service.CommentService;
import com.kkbill.mockzhihu.service.QuestionService;
import com.kkbill.mockzhihu.util.MockZhihuUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionService questionService;


    @PostMapping(value = "/addComment")
    public String addComment(int questionId, String content){//对问题questionId的评论
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setEntity_id(questionId);
            comment.setEntity_type(EntityType.ENTITY_QUESTION);//表示当前评论是对问题的回答，即对应的实体是Question
            comment.setCreated_date(new Date());

            //这里的设计还有待调整
            if(hostHolder.getUser()!=null){
                comment.setUser_id(hostHolder.getUser().getId());
            }else{
                comment.setUser_id(MockZhihuUtil.ANONYMOUS_USERID);//设置为匿名用户
            }

            commentService.addComment(comment);

            //添加新的评论后，及时更新评论数
            int count = commentService.getCommentCount(comment.getEntity_id(),comment.getEntity_type());
            questionService.updateCommentCount(questionId,count);


        } catch (Exception e){
            logger.error("增加评论失败:"+e.getMessage());
        }
        return "redirect:/question/"+questionId;
    }

}
