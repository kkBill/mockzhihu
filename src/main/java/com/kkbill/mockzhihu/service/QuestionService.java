package com.kkbill.mockzhihu.service;

import com.kkbill.mockzhihu.dao.QuestionDao;
import com.kkbill.mockzhihu.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private SensitiveWordsFilterService sensitiveWordsFilterService;

    public Question getQuestionById(int id){
        return questionDao.getQuestionById(id);
    }

    public List<Question> getLatestQuestions(int userId,int offset, int limit){
        return questionDao.selectLatestQuestions(userId,offset,limit);
    }

    public int addQuestion(Question question){
        //HTML标签过滤 --> 试了下，好像不加这一步也能过滤HTML标签，是Chrome浏览器自动过滤了吗？
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));

        //敏感词过滤
        question.setTitle(sensitiveWordsFilterService.filter(question.getTitle()));
        question.setContent(sensitiveWordsFilterService.filter(question.getContent()));

        //如果成功插入问题，则返回问题id;否则返回0
        return questionDao.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public void updateCommentCount(int qid, int comment_count){
        questionDao.updateCommentCount(qid, comment_count);
    }
}
