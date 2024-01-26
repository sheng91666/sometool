package io.github.sheng91666.sqlserver;


import com.alibaba.fastjson.JSON;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.rds.v3.RdsClient;
import com.huaweicloud.sdk.rds.v3.model.*;
import com.huaweicloud.sdk.rds.v3.region.RdsRegion;
import com.sometool.command.CloudApiCommand;
import com.sometool.exception.STRunTimeException;
import io.github.sheng91666.sqlserver.commond.ListEngineFlavorsCommand;
import io.github.sheng91666.sqlserver.commond.ListStorageTypesCommand;
import io.github.sheng91666.sqlserver.commond.StartInstanceEnlargeVolumeActionCommand;
import io.github.sheng91666.sqlserver.commond.StartResizeFlavorActionCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HuaweiSQLServerUtils {

    private static final Logger logger = LoggerFactory.getLogger(HuaweiSQLServerUtils.class);

    public RdsClient getRdsClient(CloudApiCommand command) {
        ICredential auth = new BasicCredentials()
                .withAk(command.getAk())
                .withSk(command.getSk());

        RdsClient client = RdsClient.newBuilder()
                .withCredential(auth)
                .withRegion(RdsRegion.valueOf(command.getRegionId()))
                .build();
        return client;
    }


    /**
     * 获取 mysql 实例列表
     */
    public Map<String, List<InstanceResponse>> getMysqlInstances(CloudApiCommand command) {
        Map<String, List<InstanceResponse>> hashMap = new HashMap<>();
        command.getRegionIds().forEach(item -> {
            List<InstanceResponse> instances = new ArrayList<>();
            boolean flag = true;
            int offset = 0;
            int limit = 50;

            do {
                ListInstancesRequest request = new ListInstancesRequest();
                request.withOffset(offset);
                request.withLimit(limit);
                if (!"".equals(command.getInstanceId()) && command.getInstanceId() != null) {
                    request.setId(command.getInstanceId());
                }
                try {
                    command.setRegionId(item);
                    ListInstancesResponse response = getRdsClient(command).listInstances(request);
                    instances.addAll(response.getInstances());

                    //处理分页
                    Integer totalCount = response.getTotalCount();
                    if (totalCount - (offset + limit) > 0) {
                        offset += limit;
                    } else {
                        flag = false;
                    }

                } catch (Exception e) {
                    flag = false;
                    e.printStackTrace();
                    logger.error("HuaweiMysqlUtils--getMysqlInstances--失败:{}", e.getMessage());
                }

            } while (flag);
            hashMap.put(item, instances);
        });
        return hashMap;
    }

    /**
     * 查询数据库版本
     */
    public List<LDatastore> listDatastores(CloudApiCommand command) {
        List<LDatastore> dataStores = new ArrayList<>();
        ListDatastoresRequest request = new ListDatastoresRequest();
        request.withDatabaseName(ListDatastoresRequest.DatabaseNameEnum.fromValue("SQLServer"));
        try {
            ListDatastoresResponse response = getRdsClient(command).listDatastores(request);
            dataStores = response.getDataStores();
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("listDatastores--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STRunTimeException(msg);
        }
        return dataStores;
    }

    /**
     * 获取规格列表
     */
    public List<Flavor> ListFlavors(CloudApiCommand command) {
        List<Flavor> flavors = new ArrayList<>();
        ListFlavorsRequest request = new ListFlavorsRequest();
        request.withDatabaseName(ListFlavorsRequest.DatabaseNameEnum.fromValue("SQLServer"));
        request.withVersionName(command.getVersion());
        try {
            ListFlavorsResponse response = getRdsClient(command).listFlavors(request);
            flavors = response.getFlavors();
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("ListFlavors--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STRunTimeException(msg);
        }
        return flavors;
    }

    /**
     * 获取磁盘类型
     */
    public List<Storage> ListStorageTypes(ListStorageTypesCommand command) {
        List<Storage> storageType = new ArrayList<>();

        ListStorageTypesRequest request = new ListStorageTypesRequest();
        request.withDatabaseName(ListStorageTypesRequest.DatabaseNameEnum.fromValue("SQLServer"));
        request.withVersionName(command.getVersion());
        request.withHaMode(ListStorageTypesRequest.HaModeEnum.fromValue(command.getHaMode()));
        try {
            ListStorageTypesResponse response = getRdsClient(command).listStorageTypes(request);
            storageType = response.getStorageType();
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("ListStorageTypes--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STRunTimeException(msg);
        }
        return storageType;
    }

    /**
     * 获取参数模板
     */
    public List<ConfigurationSummary> ListConfigurations(CloudApiCommand command) {
        List<ConfigurationSummary> summaryList = new ArrayList<>();
        ListConfigurationsRequest request = new ListConfigurationsRequest();
        try {
            ListConfigurationsResponse response = getRdsClient(command).listConfigurations(request);
            summaryList = response.getConfigurations();
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("ListConfigurations--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STRunTimeException(msg);
        }
        return summaryList;
    }


    /**
     * 获取实例可变更的规格
     */
    public ListEngineFlavorsResponse ListEngineFlavors(ListEngineFlavorsCommand command) {
        ListEngineFlavorsResponse response = new ListEngineFlavorsResponse();

        ListEngineFlavorsRequest request = new ListEngineFlavorsRequest();
        request.withInstanceId(command.getInstanceId());
        request.withAvailabilityZoneIds(command.getKey());
        request.withHaMode(command.getHaMode().toLowerCase());
        try {
            response = getRdsClient(command).listEngineFlavors(request);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("ListEngineFlavors--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STRunTimeException(msg);
        }
        return response;
    }


    /**
     * 创建实例
     */
    public CreateInstanceResponse CreateInstance(InstanceRequest body, CloudApiCommand command) {
        CreateInstanceResponse response = new CreateInstanceResponse();
        CreateInstanceRequest request = new CreateInstanceRequest();
        request.withBody(body);
        try {
            response = getRdsClient(command).createInstance(request);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("CreateInstance--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STRunTimeException(msg);
        }
        return response;
    }

    /**
     * 删除实例
     */
    public DeleteInstanceResponse DeleteInstance(CloudApiCommand command) {
        DeleteInstanceResponse response = new DeleteInstanceResponse();
        DeleteInstanceRequest request = new DeleteInstanceRequest();
        try {
            request.setInstanceId(command.getInstanceId());
            response = getRdsClient(command).deleteInstance(request);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("DeleteInstance--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STRunTimeException(msg);
        }
        return response;
    }

    /**
     * 变更规格
     */
    public StartResizeFlavorActionResponse StartResizeFlavorAction(StartResizeFlavorActionCommand command) throws STRunTimeException {
        StartResizeFlavorActionResponse response = new StartResizeFlavorActionResponse();
        StartResizeFlavorActionRequest request = new StartResizeFlavorActionRequest();
        request.withInstanceId(command.getInstanceId());
        ResizeFlavorRequest body = new ResizeFlavorRequest();
        ResizeFlavorObject resizeFlavorbody = new ResizeFlavorObject();
        resizeFlavorbody.withSpecCode(command.getKey()).withIsAutoPay(true);
        body.withResizeFlavor(resizeFlavorbody);
        request.withBody(body);
        try {
            response = getRdsClient(command).startResizeFlavorAction(request);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("StartResizeFlavorAction--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STRunTimeException(msg);
        }
        return response;
    }

    /**
     * 扩容磁盘
     */
    public StartInstanceEnlargeVolumeActionResponse StartInstanceEnlargeVolumeAction(StartInstanceEnlargeVolumeActionCommand command) {
        StartInstanceEnlargeVolumeActionResponse response = new StartInstanceEnlargeVolumeActionResponse();
        StartInstanceEnlargeVolumeActionRequest request = new StartInstanceEnlargeVolumeActionRequest();
        request.setInstanceId(command.getInstanceId());
        EnlargeVolumeRequestBody body = new EnlargeVolumeRequestBody();
        EnlargeVolumeObject enlargeVolumebody = new EnlargeVolumeObject();
        enlargeVolumebody.withSize(command.getSize()).withIsAutoPay(true);
        body.withEnlargeVolume(enlargeVolumebody);
        request.withBody(body);
        try {
            response = getRdsClient(command).startInstanceEnlargeVolumeAction(request);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("StartInstanceEnlargeVolumeAction--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STRunTimeException(msg);
        }
        return response;
    }


    /**
     * ListCollations 查询SQLServer可用字符集
     *
     * @param command
     * @return
     */
    public ListCollationsResponse ListCollations(CloudApiCommand command) {
        ListCollationsResponse response = new ListCollationsResponse();

        ListCollationsRequest request = new ListCollationsRequest();
        try {
            response = getRdsClient(command).listCollations(request);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("ListCollations--失败--参数=%s 失败原因=%s ", JSON.toJSONString(command), e.getMessage());
            throw new STRunTimeException(msg);
        }
        return response;
    }


}
