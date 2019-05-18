package com.kkbill.mockzhihu.dao;


import com.kkbill.mockzhihu.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CommentDao {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " content, user_id, entity_id, entity_type, created_date, status ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values ( #{content}, #{user_id}, #{entity_id}, #{entity_type}, #{created_date}, #{status})"})
    int addComment(Comment comment);//这个返回值是怎么确定的？

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME, "where id=#{id}"})
    Comment getCommentById(int id);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(int entityId, int entityType);

    @Select({"select", SELECT_FIELDS, "from", TABLE_NAME,
            " where entity_id=#{entity_id} and entity_type=#{entity_type} order by created_date desc"})
    List<Comment> getCommentByEntity(int entity_id, int entity_type);

    @Update({"update ", TABLE_NAME, " set status=#{status} where id=#{id}"})
    int updateStatus(int id, int status);

    //??
    @Select({"select count(*) from ", TABLE_NAME, " where user_id=#{user_id}"})
    int getUserCommentCount(int user_id);
}
