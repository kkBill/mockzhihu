package com.kkbill.mockzhihu;


import com.kkbill.mockzhihu.dao.QuestionDao;
import com.kkbill.mockzhihu.dao.UserDao;
import com.kkbill.mockzhihu.entities.Question;
import com.kkbill.mockzhihu.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserDao userDao;
    @Autowired
    QuestionDao questionDao;
//    @Autowired
//    FollowService followService;

    @Test
    public void initDatabase() {
        Random random = new Random();

        for(int i=0; i<11; ++i){
            User user = new User();
            user.setHead_url(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d",i+1));
            user.setPassword("1111");
            user.setSalt("");
            userDao.addUser(user);

//            //互相关注的测试数据的生成
//            for (int j = 0; j< i; ++j){
//                followService.follow(j+1, EntityType.ENTITY_USER, i+1);
//            }

            Question question = new Question();
            question.setComment_count(i+1);
            Date date = new Date();
            date.setTime(date.getTime() + 60*60*1000*i);
            question.setCreated_date(date);
            question.setUser_id(i+1);
            question.setTitle(String.format("Title%d",i+1));
            question.setContent(String.format("Content xxxxxx %d",i+1));
            questionDao.addQuestion(question);
        }
    }
}