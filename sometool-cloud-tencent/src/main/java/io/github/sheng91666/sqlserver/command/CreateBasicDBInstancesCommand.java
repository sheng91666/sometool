package io.github.sheng91666.sqlserver.command;


import com.sometool.command.CloudApiCommand;
import com.tencentcloudapi.sqlserver.v20180328.models.CreateBasicDBInstancesRequest;
import lombok.Data;

@Data
public class CreateBasicDBInstancesCommand extends CloudApiCommand {

    private CreateBasicDBInstancesRequest request;

    public CreateBasicDBInstancesRequest toReq() {
        return this.request;
    }

}
