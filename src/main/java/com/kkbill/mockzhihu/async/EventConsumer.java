package com.kkbill.mockzhihu.async;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kkbill.mockzhihu.util.JedisUtil;
import com.kkbill.mockzhihu.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异步消息队列的核心类
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType, List<EventHandler>> config = new HashMap<>();//注册事件以及对应的处理器
    private ApplicationContext applicationContext;

    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 这个函数是什么作用？
     * 在Spring容器初始化完这个bean后，就会执行这个函数
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //从容器中获取所有实现了EventHandler的类
        //
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    //从队列中取出事件进行处理
                    //brpop的list的第一个值是key,后面才是对应存在这个key中的value，这个要注意一下
                    List<String> events = jedisUtil.brpop(0, key);//timeout设为0，表示一致阻塞

                    for (String event : events) {
                        if (event.equals(key)) {//过滤掉key
                            continue;
                        }

                        //通过json把string解析成具体的类对象
                        EventModel eventModel = JSON.parseObject(event, EventModel.class);
                        if (!config.containsKey(eventModel.getType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }

                        for (EventHandler handler : config.get(eventModel.getType())) {
                            handler.doHandler(eventModel);
                        }
                    }

                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


//    public static void main(String[] args) {
//        Jedis jedis = new Jedis("redis://localhost:6379/2");
//        jedis.lpush("mylist","aaa");
//        jedis.lpush("mylist","bbb");
//        jedis.lpush("mylist","ccc");
//        while (true){
//            List<String> list = jedis.brpop(1,"mylist");
//            System.out.println(list);
//        }
//
//    }

}
