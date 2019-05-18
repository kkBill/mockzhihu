package com.kkbill.mockzhihu.model;

/**
 * 定义实体类型
 * 对于问答型网站，主要分为3个实体类型：用户，问题，和评论。
 * 对问题的回答，对问题的评论，以及对评论的回复等都可以看作是评论
 */
public class EntityType {
    public static int ENTITY_QUESTION = 1;
    public static int ENTITY_COMMENT = 2;
    public static int ENTITY_USER = 3;
}