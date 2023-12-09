package com.sometool;

import com.alibaba.fastjson.JSON;
import com.sometool.dto.request.QywxMessageRequest;
import com.sometool.dto.request.QywxRequest;
import com.sometool.dto.vo.QywxAccessTokenVo;
import com.sometool.dto.vo.QywxMessageVo;
import com.sometool.dto.vo.QywxUserInfoVo;
import okhttp3.*;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 企业微信服务
 * 消息通知api文档 https://developer.work.weixin.qq.com/document/path/90236#%E6%8E%A5%E5%8F%A3%E5%AE%9A%E4%B9%89
 */
public class QywxService {


    /**
     * 获取access token
     *
     * @return
     * @throws Exception
     */
    public static QywxAccessTokenVo getQywxAccessToken(QywxRequest req) throws Exception {
        if (ObjectUtils.isEmpty(req.getCorpId())) {
            throw new Exception("no CORP_ID!");
        }

        if (ObjectUtils.isEmpty(req.getCorpSecret())) {
            throw new Exception("no CORP_SECRET!");
        }

        QywxAccessTokenVo accessTokenVo = new QywxAccessTokenVo();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + req.getCorpId() + "&corpsecret=" + req.getCorpSecret())
                .get()
                .build();

        Response response = client.newCall(request).execute();
        if (200 == response.code()) {
            accessTokenVo = JSON.parseObject(response.body().string(), QywxAccessTokenVo.class);
        } else {
            accessTokenVo.setErrcode(response.code());
            accessTokenVo.setErrmsg(response.message());
        }

        return accessTokenVo;
    }

    /**
     * 发送文本应用通知
     * 必填参数：agentId，message，toUser or toUserList，CORP_ID，CORP_SECRET
     *
     * @param req
     * @return
     * @throws Exception
     */
    public static QywxMessageVo sendTextMessage(QywxMessageRequest req) throws Exception {
        QywxMessageRequest paramReq = req.getParamReq();
        if (ObjectUtils.isEmpty(paramReq.getAgentid())) {
            throw new Exception("no agentId!");
        }

        if (ObjectUtils.isEmpty(paramReq.getMessage())) {
            throw new Exception("no message content!");
        }

        if (ObjectUtils.isEmpty(req.getToUserList()) && ObjectUtils.isEmpty(req.getTouser())) {
            throw new Exception("need toUser or toUserList");
        }

        QywxMessageVo messageVo = sendMessage(paramReq);
        return messageVo;
    }


    /**
     * 发送文本卡片应用通知
     * 必填参数：textcard，toUser or toUserList，agentId,CORP_ID，CORP_SECRET
     *
     * @param req
     * @return
     * @throws Exception
     */
    public static QywxMessageVo sendTextCardMessage(QywxMessageRequest req) throws Exception {
        QywxMessageRequest paramReq = req.getParamReq();
        if (ObjectUtils.isEmpty(paramReq.getAgentid())) {
            throw new Exception("no agentId!");
        }

        if (ObjectUtils.isEmpty(paramReq.getTextcard())) {
            throw new Exception("no message content or no TextCard!");
        }

        if (ObjectUtils.isEmpty(req.getToUserList()) && ObjectUtils.isEmpty(req.getTouser())) {
            throw new Exception("need toUser or toUserList");
        }

        QywxMessageVo messageVo = sendMessage(paramReq);
        return messageVo;
    }

    private static QywxMessageVo sendMessage(QywxMessageRequest paramReq) throws Exception {
        QywxAccessTokenVo accessToken = getQywxAccessToken(paramReq);

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(paramReq));
        Request request = new Request.Builder()
                .url("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken.getAccess_token())
                .post(body)
                .addHeader("content-type", "application/json")
                .build();
        Response response = client.newCall(request).execute();

        QywxMessageVo messageVo = new QywxMessageVo();
        if (200 == response.code()) {
            messageVo = JSON.parseObject(response.body().string(), QywxMessageVo.class);
        } else {
            messageVo.setErrcode(response.code());
            messageVo.setErrmsg(response.message());
        }
        return messageVo;
    }


    public QywxUserInfoVo getUserInfo(QywxRequest req, String code) throws Exception {
        QywxAccessTokenVo tokenVo = getQywxAccessToken(req);
        if (ObjectUtils.isEmpty(tokenVo.getAccess_token())) {
            throw new Exception(tokenVo.getErrmsg());
        }

        String url = String.format("https://qyapi.weixin.qq.com/cgi-bin/auth/getuserinfo?access_token=%s&code=%s", tokenVo.getAccess_token(), code);
        QywxUserInfoVo userInfoVo = new QywxUserInfoVo();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        if (200 == response.code()) {
            userInfoVo = JSON.parseObject(response.body().string(), QywxUserInfoVo.class);
        } else {
            userInfoVo.setErrmsg(response.message());
            userInfoVo.setErrcode(response.code());
        }
        return userInfoVo;
    }

}
