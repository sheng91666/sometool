package io.github.sheng91666.sqlserver.command;



import com.sometool.command.CloudApiCommand;
import com.tencentcloudapi.sqlserver.v20180328.models.ResourceTag;
import lombok.Data;

@Data
public class CreateBasicDBInstancesCommand extends CloudApiCommand {

    /**
     * 实例可用区，类似ap-guangzhou-1（广州一区）；实例可售卖区域可以通过接口DescribeZones获取
     */
    private String zone;

    /**
     * 实例的CPU核心数
     */
    private Long cpu;

    /**
     * 实例内存大小，单位GB
     */
    private Long memory;

    /**
     * 实例磁盘大小，单位GB
     */
    private Long storage;

    /**
     * VPC子网ID，形如subnet-bdoe83fa
     */
    private String subnetId;

    /**
     * VPC网络ID，形如vpc-dsp338hz
     */
    private String vpcId;

    /**
     * 购买实例的宿主机类型，CLOUD_PREMIUM-虚拟机高性能云盘，CLOUD_SSD-虚拟机SSD云盘,CLOUD_HSSD-虚拟机加强型SSD云盘，CLOUD_TSSD-虚拟机极速型SSD云盘，CLOUD_BSSD-虚拟机通用型SSD云盘
     */
    private String machineType;

    /**
     * 付费模式，取值支持 PREPAID（预付费），POSTPAID（后付费）。
     */
    private String instanceChargeType;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 本次购买几个实例，默认值为1。取值不超过10
     */
    private Long goodsNum;

    /**
     * sqlserver版本，目前所有支持的版本有：2008R2 (SQL Server 2008 R2 Enterprise)，2012SP3 (SQL Server 2012 Enterprise)，201202 (SQL Server 2012 Standard)，2014SP2 (SQL Server 2014 Enterprise)，201402 (SQL Server 2014 Standard)，2016SP1 (SQL Server 2016 Enterprise)，201602 (SQL Server 2016 Standard)，2017 (SQL Server 2017 Enterprise)，201702 (SQL Server 2017 Standard)，2019 (SQL Server 2019 Enterprise)，201902 (SQL Server 2019 Standard)。每个地域支持售卖的版本不同，可通过DescribeProductConfig接口来拉取每个地域可售卖的版本信息。不填，默认为版本2008R2。
     */
    private String dbVersion;

    /**
     * 购买实例周期，默认取值为1，表示一个月。取值不超过48
     */
    private Long period;

    /**
     * 安全组列表，填写形如sg-xxx的安全组ID
     */
    private String[] securityGroupList;

    /**
     * 自动续费标志：0-正常续费  1-自动续费，默认为1自动续费。只在购买预付费实例时有效。
     */
    private Long autoRenewFlag;

    /**
     * 是否自动使用代金券；1 - 是，0 - 否，默认不使用
     */
    private Long autoVoucher;

    /**
     * 代金券ID数组，目前单个订单只能使用一张
     */
    private String[] voucherIds;

    /**
     * 可维护时间窗配置，以周为单位，表示周几允许维护，1-7分别代表周一到周末
     */
    private Long[] weekly;

    /**
     * 可维护时间窗配置，每天可维护的开始时间
     */
    private String startTime;

    /**
     * 可维护时间窗配置，持续时间，单位：小时
     */
    private Long span;

    /**
     * 新建实例绑定的标签集合
     */
    private ResourceTag[] resourceTags;

    /**
     * 系统字符集排序规则，默认：Chinese_PRC_CI_AS
     */
    private String collation;

    /**
     * 系统时区，默认：China Standard Time
     */
    private String timeZone;
}
