package io.github.sheng91666.sqlserver.command;

import com.sometool.command.CloudApiCommand;
import com.tencentcloudapi.sqlserver.v20180328.models.CreateCloudDBInstancesRequest;
import lombok.Data;

@Data
public class CreateCloudDBInstancesCommand extends CloudApiCommand {


    private CreateCloudDBInstancesRequest request;

    public CreateCloudDBInstancesRequest toReq() {
        return this.request;
    }
}
