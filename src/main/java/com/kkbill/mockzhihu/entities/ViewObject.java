package com.kkbill.mockzhihu.entities;

import java.util.HashMap;
import java.util.Map;

public class ViewObject {

    private Map<String,Object> objectMap = new HashMap<>();

    public void setObject(String key, Object value){
        objectMap.put(key,value);
    }

    public Object getObject(String key){
        return objectMap.get(key);
    }
}
