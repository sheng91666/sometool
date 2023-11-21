package com.sometool.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.HashMap;
import java.util.Map;

/**
 * User: caisheng
 * Date: 2022/6/13 10:26 AM
 * Email: 844715586@qq.com
 */
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;


    public R setData(Object data) {
        put("data", data);
        return this;
    }

    //利用fastjson进行反序列化
    public <T> T getData(TypeReference<T> typeReference) {
        Object data = get("data");    //默认是map
        String jsonString = JSON.toJSONString(data);
        T t = JSON.parseObject(jsonString, typeReference);
        return t;
    }

    //利用fastjson进行反序列化
    public <T> T getData(String key, TypeReference<T> typeReference) {
        Object data = get(key);    //默认是map
        String jsonString = JSON.toJSONString(data);
        T t = JSON.parseObject(jsonString, typeReference);
        return t;
    }


    public R() {
        put("code", 0);
        put("msg", "success");
    }

    public static R error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Integer getCode() {

        return (Integer) this.get("code");
    }


}
