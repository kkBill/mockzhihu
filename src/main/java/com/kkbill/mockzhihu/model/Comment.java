package com.kkbill.mockzhihu.model;

import java.util.Date;

public class Comment {
    private int id;
    private int user_id;//发表该评论的用户id
    private int entity_type;//表示被评论对象的类型，可以是问题，也可以是评论本身，
    private int entity_id;//表示被评论对象的id
    private String content;
    private Date created_date;
    private boolean status;//默认值为0。用于表示是否删除。数据库中删除一条评论，并不是真正删除而是更改该条评论的状态


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getEntity_type() {
        return entity_type;
    }

    public void setEntity_type(int entity_type) {
        this.entity_type = entity_type;
    }

    public int getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(int entity_id) {
        this.entity_id = entity_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
