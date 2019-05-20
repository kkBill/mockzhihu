package com.kkbill.mockzhihu.async;

import com.alibaba.fastjson.JSONObject;
import com.kkbill.mockzhihu.util.JedisUtil;
import com.kkbill.mockzhihu.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


//事件生产者，把事件放入队列中就不管了
@Service
public class EventProducer {
    @Autowired
    private JedisUtil jedisUtil;

    public boolean sendEvent(EventModel eventModel){
        try{
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisUtil.lpush(key,json);
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
