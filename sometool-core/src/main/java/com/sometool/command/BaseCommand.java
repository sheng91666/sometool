package com.sometool.command;

import lombok.Data;

/**
 * User: caisheng
 * Date: 2022/9/9 11:08
 * Email: 844715586@qq.com
 */
@Data
public class BaseCommand {

    private Integer page;

    private Integer limit;

    private Long beginTime;

    private Long endTime;
}
