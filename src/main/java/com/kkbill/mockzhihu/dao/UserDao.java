package com.kkbill.mockzhihu.dao;


import com.kkbill.mockzhihu.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDao {
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS, ") values(#{name}, #{password}, #{salt}, #{head_url})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, "from", TABLE_NAME, "where id=#{id}"})
    User getUserById(int id);

    @Select({"select ", SELECT_FIELDS, "from", TABLE_NAME, "where name=#{name}"})
    User getUserByName(String name);

    @Update({"update", TABLE_NAME, "set password = #{password} where id = #{id}"})
    void updatePassword(User user);

    @Delete({"delete from", TABLE_NAME, "where id = #{id}"})
    void deleteUserById(int id);
}
