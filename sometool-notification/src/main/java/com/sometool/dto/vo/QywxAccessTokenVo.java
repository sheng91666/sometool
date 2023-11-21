package com.sometool.dto.vo;


import lombok.Data;

@Data
public class QywxAccessTokenVo extends QywxVo {

    private String access_token;

    private Long expires_in;

}
