package com.kkbill.mockzhihu.service;

import com.kkbill.mockzhihu.dao.LoginTicketDao;
import com.kkbill.mockzhihu.dao.UserDao;
import com.kkbill.mockzhihu.entities.LoginTicket;
import com.kkbill.mockzhihu.entities.User;
import com.kkbill.mockzhihu.util.MockZhihuUtil;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;


@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketDao loginTicketDao;


    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }


    //用户注册
    public Map<String, String> register(String username, String password) {
        Map<String, String> map = new HashMap<>();
        //检查username和password是否合法
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            map.put("msg", "用户名或密码为空，请重新注册");
            return map;
        } else if (password.length() < 8) {
            map.put("msg", "密码不能少于8位");
            return map;
        }
        User user = userDao.getUserByName(username);
        if (user != null) {
            map.put("msg", "您注册的用户已存在，请重新注册");
            return map;
        }

        //到达这里，说明username和password都符合条件，故开始正常注册
        user = new User();
        user.setName(username);
        user.setHead_url(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));//给用户随机生成一张图作头像
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));//随机截取字符串作为salt值
        user.setPassword(MockZhihuUtil.MyMD5(password + user.getSalt()));
        userDao.addUser(user);

        //注册成功，关联用户id和ticket。即注册成功后就立即登录
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }

    //用户登录
    public Map<String, String> login(String username, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            map.put("msg", "用户名或密码为空，请重新登录");
            return map;
        }
        User user = null;
        user = userDao.getUserByName(username);
        if (user == null) {
            map.put("msg", "该用户不存在，请重新登录");
            return map;
        }
        //验证密码
        String encryptedPassword = user.getPassword();
        if (!encryptedPassword.equals(MockZhihuUtil.MyMD5(password + user.getSalt()))) {
            map.put("msg", "密码错误，请重新登录");
            return map;
        }

        //登录成功，关联用户id和ticket
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);//把与该用户相关联的ticket放入map中传入controller，从而下发之浏览器

        return map;
    }

    //用户登出，只需要改变当前用户的ticket为无效
    public void logout(String ticket){
        loginTicketDao.updateStatus(ticket,1);
    }


    //根据用户id生成ticket，进行彼此关联
    private String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(3600 * 24 * 10 + now.getTime());
        loginTicket.setExpired(now);//设置过期时间
        loginTicket.setStatus(0);//状态为0表示有效
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-", ""));
        loginTicketDao.addTicket(loginTicket);//添加到数据库中
        return loginTicket.getTicket();
    }


}
