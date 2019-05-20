package com.kkbill.mockzhihu.service;

import com.kkbill.mockzhihu.util.JedisUtil;
import com.kkbill.mockzhihu.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private JedisUtil jedisUtil;

    //返回点赞的数量
    public long getLikeCount(int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        return jedisUtil.scard(likeKey);
    }

    //获取当前user点赞或点踩的状态
    //如果当前用户对某一评论/回答点赞了，就放在likeKey的集合中
    public int getLikeStatus(int userId, int entityType, int entityId){
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        if(jedisUtil.sismember(likeKey,String.valueOf(userId))){
            return 1;//点赞
        }
        String dislikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);
        if(jedisUtil.sismember(dislikeKey,String.valueOf(userId))){
            return -1;//点踩
        }
        return 0;//不赞也不踩
    }

    //用户user对某一对象点赞，并返回更改后点赞的人数
    public long like(int userId, int entityType, int entityId){
        //把该用户加入到likeKey集合中
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisUtil.sadd(likeKey,String.valueOf(userId));
        //同时把该用户从disLikeKey集合中移除
        String dislikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisUtil.srem(dislikeKey,String.valueOf(userId));
        return jedisUtil.scard(likeKey);
    }

    //用户user对某一对象点踩，但仍然返回点赞的人数，因为前端约定好不显示点踩的数目，只显示点赞的人数
    public long dislike(int userId, int entityType, int entityId){
        String dislikeKey = RedisKeyUtil.getDisLikeKey(entityType,entityId);
        jedisUtil.sadd(dislikeKey,String.valueOf(userId));
        String likeKey = RedisKeyUtil.getLikeKey(entityType,entityId);
        jedisUtil.srem(likeKey,String.valueOf(userId));

        return jedisUtil.scard(likeKey);
    }

}
