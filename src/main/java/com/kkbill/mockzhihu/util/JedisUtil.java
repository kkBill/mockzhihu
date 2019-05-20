package com.kkbill.mockzhihu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Service
public class JedisUtil implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisUtil.class);
    private JedisPool pool;

    //初始化连接池，供调用
    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/1");
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常：" + e.getMessage());
        } finally { //finally子句回收资源
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    //从集合key中移除value，如果集合中原本并不存在value，则返回0；若存在，则返回移除的个数
    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常：" + e.getMessage());
        } finally { //finally子句回收资源
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    //返回一个集合中包含的key的数量
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常：" + e.getMessage());
        } finally { //finally子句回收资源
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    //如果k-v是集合中的元素，则返回true
    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("发生异常：" + e.getMessage());
        } finally { //finally子句回收资源
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    //移出并获取列表的最后一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    //向队列中插入元素
    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常：" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }
//    public static void MyTest(){
//        Jedis jedis = new Jedis("redis://localhost:6379/1");
//        jedis.flushDB();
//        jedis.sadd("myset","v1");
//        jedis.sadd("myset","v2");
//        jedis.sadd("myset","v1");
//
//        System.out.println(jedis.scard("myset"));
//        jedis.close();
//    }
//
//    public static void main(String[] args) {
//        MyTest();
//    }


}
