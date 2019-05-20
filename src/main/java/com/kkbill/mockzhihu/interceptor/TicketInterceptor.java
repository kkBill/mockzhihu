package com.kkbill.mockzhihu.interceptor;

import com.kkbill.mockzhihu.dao.LoginTicketDao;
import com.kkbill.mockzhihu.dao.UserDao;
import com.kkbill.mockzhihu.model.HostHolder;
import com.kkbill.mockzhihu.model.LoginTicket;
import com.kkbill.mockzhihu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@Component
public class TicketInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginTicketDao loginTicketDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private HostHolder hostHolder;

    //在用户每次提交请求，服务器端在对其进行解析之前，调用preHandle()方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = null;

        //获取请求中cookie里的ticket值
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("ticket")) {
                    ticket = cookie.getValue();
                    break;
                }
            }
        }

        //如果ticket不为空，则从数据库中获取对应的LoginTicket，该数据结构中记录了ticket和对应的user的信息
        //从而把对应的user取出
        if (ticket != null) {
            LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
            //验证该ticket是否有效
            if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0) {
                return true;
            }

            User user = userDao.getUserById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }

        return true;
    }

    //在渲染之前把当前用户存入模型数据中，供前端调用
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    //清理资源
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
