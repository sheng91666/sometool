package io.github.sheng91666.sqlserver;


import com.alibaba.fastjson.JSON;
import com.sometool.command.CloudApiCommand;
import com.sometool.exception.STException;
import com.sometool.util.BeanCopyUtils;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sqlserver.v20180328.SqlserverClient;
import com.tencentcloudapi.sqlserver.v20180328.models.*;
import io.github.sheng91666.sqlserver.command.*;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class TencentSqlServerUtils {

    /**
     * 获取client
     *
     * @param command
     * @return
     */
    public SqlserverClient getSqlserverClient(CloudApiCommand command) {
        Credential cred = new Credential(command.getAk(), command.getSk());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("sqlserver.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        SqlserverClient sqlserverClient = new SqlserverClient(cred, command.getRegion(), clientProfile);
        return sqlserverClient;
    }


    /**
     * 查询所有的实例
     *
     * @param command
     * @return
     */
    public List<DBInstance> DescribeDBInstances(CloudApiCommand command) {
        return command.getRegionIds().stream().flatMap(region -> {
            List<DBInstance> regionAllIns = new ArrayList<>();
            Long offset = 0L;
            Long limit = 100L;
            boolean flag = true;
            while (flag) {
                try {
                    DescribeDBInstancesRequest req = new DescribeDBInstancesRequest();
                    req.setOffset(offset);
                    req.setLimit(limit);
                    if (!ObjectUtils.isEmpty(command.getInstanceIds())) {
                        req.setInstanceIdSet(command.getInstanceIds());
                    }

                    command.setRegion(region);
                    DescribeDBInstancesResponse resp = getSqlserverClient(command).DescribeDBInstances(req);
                    regionAllIns.addAll(Arrays.asList(resp.getDBInstances()));

                    if (resp.getTotalCount() - (offset + limit) > 0) {
                        offset += 1;
                    } else {
                        flag = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return regionAllIns.stream();
        }).collect(Collectors.toList());
    }

    /**
     * 回收实例
     *
     * @param command
     */
    public RecycleDBInstanceResponse RecycleDBInstance(CloudApiCommand command) {
        RecycleDBInstanceResponse resp = new RecycleDBInstanceResponse();
        try {
            RecycleDBInstanceRequest req = new RecycleDBInstanceRequest();
            req.setInstanceId(command.getInstanceId());
            resp = getSqlserverClient(command).RecycleDBInstance(req);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("RecycleDBInstance--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STException(msg);
        }
        return resp;
    }

    public List<ZoneInfo> DescribeZones(CloudApiCommand command) {
        List<ZoneInfo> result = new ArrayList<>();
        try {
            DescribeZonesRequest req = new DescribeZonesRequest();
            DescribeZonesResponse resp = getSqlserverClient(command).DescribeZones(req);
            result = Arrays.asList(resp.getZoneSet());
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("DescribeZones--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STException(msg);
        }
        return result;
    }

    public List<SpecInfo> DescribeProductConfig(CloudApiCommand command) {
        DescribeProductConfigResponse resp = new DescribeProductConfigResponse();
        try {
            DescribeProductConfigRequest req = new DescribeProductConfigRequest();
            req.setZone(command.getZones().get(0));
            req.setInstanceType(command.getInstanceType());
            resp = getSqlserverClient(command).DescribeProductConfig(req);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("DescribeProductConfig--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STException(msg);
        }
        return new ArrayList<>(Arrays.asList(resp.getSpecInfoList()));
    }

    public List<SpecSellStatus> DescribeSpecSellStatus(DescribeSpecSellStatusCommand command) {
        DescribeSpecSellStatusResponse resp = new DescribeSpecSellStatusResponse();
        try {
            DescribeSpecSellStatusRequest req = new DescribeSpecSellStatusRequest();
            req.setZone(command.getZone());
            req.setSpecIdSet(command.getSpecIds().toArray(new Long[]{}));
            req.setDBVersion(command.getDbVersion());
            req.setPid(command.getPid());
            req.setPayMode(command.getPayMode());
            req.setCurrency(command.getCurrency());
            resp = getSqlserverClient(command).DescribeSpecSellStatus(req);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("SpecSellStatus--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STException(msg);
        }
        return new ArrayList<>(Arrays.asList(resp.getDescribeSpecSellStatusSet()));
    }

    /**
     * 查询实例支持的字符集和时区
     * https://cloud.tencent.com/document/api/238/101735
     *
     * @param command
     * @return
     */
    public DescribeCollationTimeZoneResponse DescribeCollationTimeZone(CloudApiCommand command) {
        DescribeCollationTimeZoneResponse resp = new DescribeCollationTimeZoneResponse();
        try {
            DescribeCollationTimeZoneRequest req = new DescribeCollationTimeZoneRequest();
            req.setMachineType(command.getMachineType());
            resp = getSqlserverClient(command).DescribeCollationTimeZone(req);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("DescribeCollationTimeZone--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STException(msg);
        }
        return resp;
    }


    /**
     * 创建高可用实例 (本地盘)
     * https://console.cloud.tencent.com/api/explorer?Product=sqlserver&Version=2018-03-28&Action=CreateDBInstances
     *
     * @param command
     * @return
     */
    public CreateDBInstancesResponse CreateDBInstances(CreateDBInstancesCommand command) {
        CreateDBInstancesResponse resp = new CreateDBInstancesResponse();
        try {
            CreateDBInstancesRequest req = new CreateDBInstancesRequest();
            BeanCopyUtils.copyByCglibBeanCopier(req, command);
            req.setDBVersion(command.getDbVersion());
            resp = getSqlserverClient(command).CreateDBInstances(req);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("CreateDBInstances--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STException(msg);
        }
        return resp;
    }

    /**
     * 创建基础版实例 (云盘)
     * https://console.cloud.tencent.com/api/explorer?Product=sqlserver&Version=2018-03-28&Action=CreateBasicDBInstances
     *
     * @param command
     * @return
     */
    public CreateBasicDBInstancesResponse CreateBasicDBInstances(CreateBasicDBInstancesCommand command) {
        CreateBasicDBInstancesResponse resp = new CreateBasicDBInstancesResponse();
        try {
            CreateBasicDBInstancesRequest req = new CreateBasicDBInstancesRequest();
            BeanCopyUtils.copyByCglibBeanCopier(req, command);
            req.setDBVersion(command.getDbVersion());
            resp = getSqlserverClient(command).CreateBasicDBInstances(req);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("CreateBasicDBInstances--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STException(msg);
        }
        return resp;
    }

    /**
     * 创建高可用实例 (云盘)
     * https://console.cloud.tencent.com/api/explorer?Product=sqlserver&Version=2018-03-28&Action=CreateCloudDBInstances
     *
     * @param command
     * @return
     */
    public CreateCloudDBInstancesResponse CreateCloudDBInstances(CreateCloudDBInstancesCommand command) {
        CreateCloudDBInstancesResponse resp = new CreateCloudDBInstancesResponse();
        try {
            CreateCloudDBInstancesRequest req = new CreateCloudDBInstancesRequest();
            BeanCopyUtils.copyByCglibBeanCopier(req, command);
            req.setDBVersion(command.getDbVersion());
            resp = getSqlserverClient(command).CreateCloudDBInstances(req);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("CreateCloudDBInstances--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STException(msg);
        }

        return resp;
    }

    /**
     * 本接口（DescribeInstanceByOrders）用于根据订单号查询资源ID
     * 接口请求域名： sqlserver.tencentcloudapi.com 。
     * 默认接口请求频率限制：20次/秒。
     *
     * @param command
     * @return
     */
    public DescribeInstanceByOrdersResponse DescribeInstanceByOrders(DescribeInstanceByOrdersCommand command) {
        DescribeInstanceByOrdersResponse resp = new DescribeInstanceByOrdersResponse();
        try {
            DescribeInstanceByOrdersRequest req = new DescribeInstanceByOrdersRequest();
            req.setDealNames(command.getDealNames().toArray(new String[]{}));
            resp = getSqlserverClient(command).DescribeInstanceByOrders(req);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("DescribeInstanceByOrders--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STException(msg);
        }
        return resp;
    }

    /**
     * 本接口（ModifyDBInstanceName）用于修改实例名字。
     * 接口请求域名： sqlserver.tencentcloudapi.com 。
     * 默认接口请求频率限制：20次/秒。
     *
     * @param command
     * @return
     */
    public ModifyDBInstanceNameResponse ModifyDBInstanceName(ModifyDBInstanceNameCommand command) {
        ModifyDBInstanceNameResponse resp = new ModifyDBInstanceNameResponse();
        try {
            ModifyDBInstanceNameRequest req = new ModifyDBInstanceNameRequest();
            req.setInstanceId(command.getInstanceId());
            req.setInstanceName(command.getInstanceName());
            resp = getSqlserverClient(command).ModifyDBInstanceName(req);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("ModifyDBInstanceName--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STException(msg);
        }

        return resp;
    }

    /**
     * 本接口（UpgradeDBInstance）用于升级实例
     * 接口请求域名： sqlserver.tencentcloudapi.com 。
     * 默认接口请求频率限制：20次/秒。
     *
     * @param command
     * @return
     */
    public UpgradeDBInstanceResponse UpgradeDBInstance(UpgradeDBInstanceCommand command) {
        UpgradeDBInstanceResponse resp = new UpgradeDBInstanceResponse();
        try {
            UpgradeDBInstanceRequest req = new UpgradeDBInstanceRequest();
            req.setInstanceId(command.getInstanceId());
            req.setMemory(command.getMemory());
            req.setStorage(command.getStorage());
            req.setCpu(command.getCpu());
            req.setDBVersion(command.getDbVersion());
            if (!ObjectUtils.isEmpty(command.getHAType())) {
                req.setHAType(command.getHAType());
            }
            if (!ObjectUtils.isEmpty(command.getMultiZones())) {
                req.setMultiZones(command.getMultiZones());
            }
            req.setWaitSwitch(command.getWaitSwitch());
            resp = getSqlserverClient(command).UpgradeDBInstance(req);

        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("UpgradeDBInstance--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STException(msg);
        }
        return resp;
    }


    /**
     * 本接口(TerminateDBInstance)用于主动隔离实例，使得实例进入回收站。
     * 接口请求域名： sqlserver.tencentcloudapi.com 。
     * 默认接口请求频率限制：20次/秒。
     *
     * @param command
     * @return
     */
    public TerminateDBInstanceResponse TerminateDBInstance(CloudApiCommand command) {
        TerminateDBInstanceResponse resp = new TerminateDBInstanceResponse();
        try {
            TerminateDBInstanceRequest req = new TerminateDBInstanceRequest();
            req.setInstanceIdSet(command.getInstanceIds());
            resp = getSqlserverClient(command).TerminateDBInstance(req);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("TerminateDBInstance--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STException(msg);
        }

        return resp;
    }


    /**
     * 本接口（DeleteDBInstance）用于释放回收站中的SQL server实例(立即下线)。释放后的实例将保存一段时间后物理销毁。其发布订阅将自动解除，其ro副本将自动释放。
     * 接口请求域名： sqlserver.tencentcloudapi.com 。
     * 默认接口请求频率限制：20次/秒。
     *
     * @param command
     * @return
     */
    public DeleteDBInstanceResponse DeleteDBInstance(CloudApiCommand command) {
        DeleteDBInstanceResponse resp = new DeleteDBInstanceResponse();
        try {
            DeleteDBInstanceRequest req = new DeleteDBInstanceRequest();
            req.setInstanceId(command.getInstanceId());
            resp = getSqlserverClient(command).DeleteDBInstance(req);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("DeleteDBInstance--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STException(msg);
        }
        return resp;
    }

}
