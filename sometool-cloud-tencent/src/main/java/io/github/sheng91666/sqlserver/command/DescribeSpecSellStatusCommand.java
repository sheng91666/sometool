package io.github.sheng91666.sqlserver.command;

import com.sometool.command.CloudApiCommand;
import lombok.Data;

import java.util.List;

@Data
public class DescribeSpecSellStatusCommand extends CloudApiCommand {

    /**
     * 可用区英文ID，形如ap-guangzhou-3
     */
    private String zone;

    /**
     * 实例规格ID，可通过DescribeProductConfig接口获取。
     */
    private List<Long> specIds;

    /**
     * 数据库版本信息，可通过DescribeProductConfig接口获取。
     */
    private String dbVersion;

    /**
     * 产品ID，可通过DescribeProductConfig接口获取。
     */
    private Long pid;

    /**
     * 付费模式，POST-按量计费 PRE-包年包月
     */
    private String payMode;

    /**
     * 付费模式，CNY-人民币 USD-美元
     */
    private String currency = "CNY";

    /**
     * CLOUD_SSD...
     */
    private String machineType;

}
