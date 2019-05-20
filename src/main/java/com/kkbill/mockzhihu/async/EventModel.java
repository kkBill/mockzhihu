package com.kkbill.mockzhihu.async;

import java.util.HashMap;
import java.util.Map;

public class EventModel {
    private EventType type;//事件类型，比如点赞
    private int actorId;//事件的触发者，比如谁点了赞
    private int entityType;//事件对象，比如给什么点了赞
    private int entityId;//事件对象，比如给什么点了赞
    private int entityOwnerId;//事件主体

    private Map<String,String> exts = new HashMap<>();//扩展字段，便于存放数据




    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    public EventModel setExts(Map<String, String> exts) {
        this.exts = exts;
        return this;
    }

    public EventModel setExts(String key,String value){
        exts.put(key,value);
        return this;
    }

    public String getExt(String key){
        return exts.get(key);
    }
}
