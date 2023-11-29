package com.sometool;

import com.alibaba.fastjson.JSON;
import com.sometool.dto.request.QywxMessageRequest;
import com.sometool.dto.vo.QywxMessageVo;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        sendMessage2Text();
    }

    /**
     * 发送文本卡片消息
     */
    public static void sendMessage2TextCard() {
        try {
            QywxMessageRequest qywxMessageRequest = new QywxMessageRequest("xxxx", "xxxx", "xxxxx");
            qywxMessageRequest.setTouser("xxxxx");
            qywxMessageRequest.setMsgtype("textcard");

            String title = "张三 申请访问分享链接";
            String url = "https://www.baidu.com";
            String description = "Hi,我是xx部门的xxx，请帮我开通这个链接的权限。";
            QywxMessageRequest.TextCard textCard = new QywxMessageRequest.TextCard(title, description, url, "");
            qywxMessageRequest.setTextcard(textCard);

            QywxMessageVo qywxMessageVo = new QywxService().sendTextMessage(qywxMessageRequest);

            System.out.println(JSON.toJSONString(qywxMessageVo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public static void sendMessage2Text() {
        try {
            QywxMessageRequest qywxMessageRequest = new QywxMessageRequest("xxx", "xxx", "xxx");
            qywxMessageRequest.setTouser("xxx");
            //或者多个人
            List<String> toUsers = new ArrayList<>();
            toUsers.add("aaa");
            toUsers.add("bbb");
            qywxMessageRequest.setToUserList(toUsers);
            qywxMessageRequest.setMessage("嗨嗨嗨~这是文本信息");

            QywxMessageVo qywxMessageVo = new QywxService().sendTextMessage(qywxMessageRequest);

            System.out.println(JSON.toJSONString(qywxMessageVo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}