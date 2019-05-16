package com.kkbill.mockzhihu.configuration;

import com.kkbill.mockzhihu.interceptor.LoginRequiredInterceptor;
import com.kkbill.mockzhihu.interceptor.TicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 想要让自定义的拦截器起作用，则必须实现这个配置类
 */
@Component
public class MockZhihuWebConfiguration implements WebMvcConfigurer {

    @Autowired
    private TicketInterceptor ticketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ticketInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/user/*");//
    }
}
