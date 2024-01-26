package io.github.sheng91666.vo;

import lombok.Data;

import java.util.List;

@Data
public class JenkinsApiVo {

    private List<WorkflowJobVo> jobs;

    //view 无color字段，借用WorkflowJobVo。
    private List<WorkflowJobVo> views;

    private String url;

    private Boolean useCrumbs;

    private Boolean useSecurity;


}
