package com.kkbill.mockzhihu.service;

import com.kkbill.mockzhihu.dao.CommentDao;
import com.kkbill.mockzhihu.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private SensitiveWordsFilterService sensitiveWordsFilterService;

    public int addComment(Comment comment){
        //在发表comment前也应该对其内容进行过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveWordsFilterService.filter(comment.getContent()));
        //经过过滤后才真正向数据库中写入
        return commentDao.addComment(comment) > 0 ? comment.getId() : 0;
    }

    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDao.getCommentByEntity(entityId, entityType);
    }

    public int getCommentCount(int entityId, int entityType) {
        return commentDao.getCommentCount(entityId, entityType);
    }

    public int getUserCommentCount(int userId) {
        return commentDao.getUserCommentCount(userId);
    }

    public boolean deleteComment(int commentId) {
        return commentDao.updateStatus(commentId, 1) > 0;//status默认为0
    }

    public Comment getCommentById(int id) {
        return commentDao.getCommentById(id);
    }
}
