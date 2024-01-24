package io.github.sheng91666.sqlserver.command;

import com.sometool.command.CloudApiCommand;
import com.tencentcloudapi.sqlserver.v20180328.models.CreateDBInstancesRequest;
import lombok.Data;

@Data
public class CreateDBInstancesCommand extends CloudApiCommand {
    private CreateDBInstancesRequest request;

    public CreateDBInstancesRequest toReq() {
        return this.request;
    }


}
