package com.kkbill.mockzhihu.dao;

import com.kkbill.mockzhihu.model.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MessageDao {

    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id, to_id, content, created_date, has_read, conversation_id ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values ( #{from_id}, #{to_id}, #{content}, #{created_date}, #{has_read}, #{conversation_id})"})
    int addMessage(Message message);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where id=#{id}"})
    Message getMessageById(int id);

    //这里为什么是to_id=userId？ 还不懂
    @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getUnreadConversationCount(int userId, String conversationId);

    //获取当前用户与某一个特定用户对话的消息列表（按时间排序，最近的放前面，由于后加入的记录id大，因此，可用id进行排序，效果和利用created_date进行排序是一样的）
    //当前用户与和他对话的用户的user_id一起构成conversation_id
    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where conversation_id=#{conversationId} order by id desc limit #{offset}, #{limit}"})
    List<Message> getSingleConversationDetail(String conversationId, int offset, int limit);

    //难点-->分析见笔记
    //获取userId相关的所有对话列表(关键)
    //因为字段id实际上不存储有价值的信息，因此把count(id)的值存放在id中
    @Select({"select", INSERT_FIELDS, ", count(id) as id from (select * from", TABLE_NAME,
            "where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id order by created_date desc limit #{offset}, #{limit}"})
    List<Message> getAllConversationList(int userId, int offset, int limit);


//    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id  order by created_date desc limit #{offset}, #{limit}"})
//    List<Message> getConversationList(@Param("userId") int userId,
//                                      @Param("offset") int offset, @Param("limit") int limit);

}
