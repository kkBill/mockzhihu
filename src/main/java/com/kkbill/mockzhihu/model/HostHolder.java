package com.kkbill.mockzhihu.model;

import org.springframework.stereotype.Component;

//存放取出来的user
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }

}
