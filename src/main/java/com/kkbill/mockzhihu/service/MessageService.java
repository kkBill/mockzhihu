package com.kkbill.mockzhihu.service;

import com.kkbill.mockzhihu.dao.MessageDao;
import com.kkbill.mockzhihu.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageDao messageDao;

    @Autowired
    private SensitiveWordsFilterService sensitiveWordsFilterService;

    public int addMessage(Message message) {
        //凡是有关ugc的内容，都要先进行过滤
        //ugc=user generated content
        message.setContent(sensitiveWordsFilterService.filter(message.getContent()));
        return messageDao.addMessage(message);
    }

    public List<Message> getSingleConversationDetail(String conversationId, int offset, int limit) {
        return messageDao.getSingleConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getAllConversationList(int userId, int offset, int limit) {
        return messageDao.getAllConversationList(userId, offset, limit);
    }

    public int getUnreadConversationCount(int userId, String conversionId) {
        return messageDao.getUnreadConversationCount(userId, conversionId);
    }
}
