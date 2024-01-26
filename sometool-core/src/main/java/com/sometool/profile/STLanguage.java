package com.sometool.profile;

public enum STLanguage {
    ZH_CN("zh-CN"),
    EN_US("en-US");

    private final String lang;

    STLanguage(String lang) {
        this.lang = lang;
    }

    @Override
    public String toString() {
        return this.lang;
    }

    public String getValue() {
        return this.lang;
    }
}
