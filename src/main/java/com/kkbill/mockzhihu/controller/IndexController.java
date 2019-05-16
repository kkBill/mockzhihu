package com.kkbill.mockzhihu.controller;

import com.kkbill.mockzhihu.dao.QuestionDao;
import com.kkbill.mockzhihu.entities.HostHolder;
import com.kkbill.mockzhihu.entities.Question;
import com.kkbill.mockzhihu.entities.User;
import com.kkbill.mockzhihu.entities.ViewObject;
import com.kkbill.mockzhihu.service.QuestionService;
import com.kkbill.mockzhihu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 */
@Controller
public class IndexController {
    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HostHolder hostHolder;


    /**
     *
     * 可重用getLatestQuestions()方法是因为在QuestionDao.xml中定义的查询语句实现了条件判断
     * 如果userId等于0，则查询出(至多)limit个问题；
     * 如果userId不等于0，则仅查询出对应的一个问题
     */
    private List<ViewObject> getQuestions(int userId, int offset, int limit){
        List<Question> questionList = questionService.getLatestQuestions(userId,offset,limit);
        List<ViewObject> viewObjects = new ArrayList<>();
        for(Question question : questionList){
            ViewObject vo = new ViewObject();
            vo.setObject("question",question);
            vo.setObject("user",userService.getUserById(question.getUser_id()));
            viewObjects.add(vo);
        }
        return viewObjects;
    }

    //首页，最多展示limit个问题
    @GetMapping(value = {"/", "/index"})
    public String index(Model model){
        model.addAttribute("viewObjects",getQuestions(0,0,10));
        return "index";
    }

    //仅展示userId的问题
    @GetMapping(value = "/user/{userId}")
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("viewObjects",getQuestions(userId,0,10));
        return "index";
    }

}
