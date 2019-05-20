package com.kkbill.mockzhihu.service;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveWordsFilterService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveWordsFilterService.class);

    //定义敏感词字典树的结点以及方法
    private class TireNode {
        //判断是否是叶结点（即敏感词的结尾）
        private boolean end = false;

        //存放字符ch对应的子节点
        Map<Character, TireNode> subNodes = new HashMap<>();

        //向字符ch中增加子节点
        void addSubNode(Character ch, TireNode node) {
            subNodes.put(ch, node);
        }

        //获取字典树中字符ch的下一个节点
        TireNode getSubNode(Character ch) {
            return subNodes.get(ch);
        }

        //
        boolean isEnd() {
            return end;
        }

        void setEnd(boolean end) {
            this.end = end;
        }

        public int getSubNodeCount() {
            return subNodes.size();
        }
    }

    /**
     * 构造敏感词字典树
     */
    private TireNode rootNode = new TireNode();

    //判断字符ch是否是符号，用于过滤特殊字符
    private boolean isSymbol(char ch) {
        int ic = (int) ch;

        // 0x2E80-0x9FFF 属于东亚文字范围 --> 硬知识
        return (!CharUtils.isAsciiAlphanumeric(ch)) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    //解析敏感词文件，向字典树中添加节点
    private void addTireNode(String line) {

        //tempNode为遍历的结点，初始化为根节点
        TireNode tempNode = rootNode;

        for (int i = 0; i < line.length(); i++) {
            Character ch = line.charAt(i);
            if (isSymbol(ch)) {
                continue;
            }
            TireNode node = tempNode.getSubNode(ch);
            if (node == null) { //表示当前字典树中还没有插入过节点
                node = new TireNode();
                tempNode.addSubNode(ch, node);
            }
            tempNode = node;
            if (i == line.length() - 1) {
                tempNode.setEnd(true);
            }
        }
    }

    //核心
    //对普通的文本进行敏感词的过滤
    public String filter(String text) {
        if (StringUtils.isEmpty(text)) {//空文本，直接返回
            return text;
        }

        String replaceWords = "***";
        StringBuilder result = new StringBuilder();
        TireNode tempNode = rootNode;
        int begin = 0;
        int position = 0; //用于与字典树的指针进行字符比较

        while (position < text.length()) {
            char ch = text.charAt(position);
            if (isSymbol(ch)) {
                if (tempNode == rootNode) {
                    result.append(ch);
                    begin++;
                }
                position++;
                continue;
            }

            tempNode = tempNode.getSubNode(ch);
            if (tempNode == null) { //不存在以字符ch开始的敏感词
                result.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            } else if (tempNode.isEnd()) { //发现敏感词
                result.append(replaceWords);
                begin = position + 1;
                position = begin;
                tempNode = rootNode;
            } else {
                position++;
            }
        }

        //最后一部分记得加进去
        result.append(text.substring(begin));
        return result.toString();
    }

    //读取敏感词文件，进行初始化，即-->构造敏感词字典树
    @Override
    public void afterPropertiesSet() throws Exception {
        rootNode = new TireNode();
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();//This method returns a copy of the string, with leading and trailing whitespace omitted.-->"  hello world  " =>"hello world"
                addTireNode(line);
            }
            reader.close();

        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }
}
