package com.kkbill.mockzhihu.controller;


import com.kkbill.mockzhihu.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginAndRegisterController {

    private static final Logger logger = LoggerFactory.getLogger(LoginAndRegisterController.class);

    @Autowired
    UserService userService;

    //注册
    @PostMapping(value = "/reg")
    public String register(Model model,
                           HttpServletResponse response,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password) {

        Map<String, String> map = userService.register(username, password);
        try {
            if (map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                return "redirect:/";
            } else {
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
        } catch (Exception e) {
            logger.error("注册错误" + e.getMessage());
            return "login";
        }

    }

    @GetMapping(value = "/reglogin")
    public String reglogin(Model model, @RequestParam(value = "next", required = false) String next ) {
        model.addAttribute("next",next);
        return "login";
    }

    //登录
    @PostMapping(value = "/login")
    public String login(Model model,
                        HttpServletResponse response,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                        @RequestParam(value = "next", required = false) String next) {

        Map<String, String> map = userService.login(username, password);
        try {
            if (map.containsKey("ticket")) { //登录成功
                Cookie cookie = new Cookie("ticket", map.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                if (!StringUtils.isEmpty(next)) {
                    return "redirect:" + next;
                }
                return "redirect:/";
            } else { //登录错误
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }

        } catch (Exception e) {
            logger.error("登录错误" + e.getMessage());
            return "login";
        }

    }

    //登出
    @GetMapping(value = "/logout")
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);//改变ticket的状态，即表示无效
        return "redirect:/";
    }



}