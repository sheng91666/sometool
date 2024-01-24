package io.github.sheng91666.command;

import lombok.Data;

@Data
public class JenkinsApiCommand {

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 构建id
     */
    private Integer buildNumber;

}
