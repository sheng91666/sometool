package com.sometool.command;

import lombok.Data;

import java.util.List;

/**
 * User: caisheng
 * Date: 2022/9/22 16:51
 * Email: 844715586@qq.com
 */
@Data
public class CloudApiCommand extends BaseCommand {
    private String ak;
    private String sk;
    private String endPoint;

    private String regionId;
    private String regionName;
    private List<String> regionIds;

    private String instanceId;
    private String[] instanceIds;

    private List<String> zones;

    private String version;

    private String cloud;
    private String cloudName;

    private String instanceType;
    private String machineType;


    private String param1;
    private String param2;
    private String param3;


    public CloudApiCommand() {

    }

    public CloudApiCommand(String ak, String sk, List<String> regionIds) {
        this.ak = ak;
        this.sk = sk;
        this.regionIds = regionIds;
    }

    public CloudApiCommand(String ak, String sk, String endPoint, List<String> regionIds) {
        this.ak = ak;
        this.sk = sk;
        this.endPoint = endPoint;
        this.regionIds = regionIds;
    }

    public CloudApiCommand toCommand() {
        CloudApiCommand command = new CloudApiCommand();
        command.setCloud(this.cloud);
        command.setAk(this.ak);
        command.setSk(this.sk);
        command.setInstanceId(this.instanceId);
        command.setInstanceIds(this.instanceIds);
        command.setRegionId(this.regionId);
        command.setRegionName(this.regionName);
        command.setRegionIds(this.regionIds);

        return command;
    }
}
