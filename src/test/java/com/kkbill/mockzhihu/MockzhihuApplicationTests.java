package com.kkbill.mockzhihu;

import com.kkbill.mockzhihu.dao.QuestionDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MockzhihuApplicationTests {

    @Autowired
    private QuestionDao questionDao;

    @Test
    public void contextLoads() {
        //System.out.println(questionDao.selectLatestQuestions(0,0,10));
//        String txt = "浙江大学";
//        for(int i=0;i<txt.length();i++){
//            System.out.println(txt.charAt(i)+"-");
//        }
//
//        String Str = "   Welcome to Tutorialspoint.com   ";
//        System.out.println("*"+Str.trim()+"*"); //*Welcome to Tutorialspoint.com*
    }

}
