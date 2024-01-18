package io.github.sheng91666.sqlserver.command;

import com.sometool.command.CloudApiCommand;
import lombok.Data;

@Data
public class UpgradeDBInstanceCommand extends CloudApiCommand {
    /**
     * 实例ID，形如mssql-j8kv137v
     */
    private String instanceId;

    /**
     * 实例升级后内存大小，单位GB，其值不能小于当前实例内存大小
     */
    private Long memory;

    /**
     * 实例升级后磁盘大小，单位GB，其值不能小于当前实例磁盘大小
     */
    private Long storage;

    /**
     * 是否自动使用代金券，0 - 不使用；1 - 默认使用。取值默认为0
     */
    private Long autoVoucher;

    /**
     * 代金券ID，目前单个订单只能使用一张代金券
     */
    private String[] voucherIds;

    /**
     * 实例升级后的CPU核心数
     */
    private Long cpu;

    /**
     * 升级sqlserver的版本，目前支持：2008R2（SQL Server 2008 Enterprise），2012SP3（SQL Server 2012 Enterprise）版本等。每个地域支持售卖的版本不同，可通过DescribeProductConfig接口来拉取每个地域可售卖的版本信息，版本不支持降级，不填则不修改版本
     */

    private String dbVersion;

    /**
     * 升级sqlserver的高可用架构,从镜像容灾升级到always on集群容灾，仅支持2017及以上版本且支持always on高可用的实例，不支持降级到镜像方式容灾，CLUSTER-升级为always on容灾，不填则不修改高可用架构
     */
    private String hAType;

    /**
     * 修改实例是否为跨可用区容灾，SameZones-修改为同可用区 MultiZones-修改为跨可用区
     */
    private String multiZones;

    /**
     * 执行变配的方式，默认为 1。支持值包括：0 - 立刻执行，1 - 维护时间窗执行
     */
    private Long waitSwitch = 1L;
}
