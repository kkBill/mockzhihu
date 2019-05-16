package com.kkbill.mockzhihu.entities;

import org.springframework.stereotype.Component;

import java.util.Date;

//用于记录用户的登录状态，关联userId和ticket
@Component
public class LoginTicket {
    private int id;
    private int user_id;
    private Date expired;
    private int status; //数据库中的默认值为0
    private String ticket;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
