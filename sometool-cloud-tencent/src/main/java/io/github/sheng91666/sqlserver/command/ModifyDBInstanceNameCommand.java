package io.github.sheng91666.sqlserver.command;

import com.sometool.command.CloudApiCommand;
import com.tencentcloudapi.sqlserver.v20180328.models.ModifyDBInstanceNameRequest;
import lombok.Data;

@Data
public class ModifyDBInstanceNameCommand extends CloudApiCommand {

    private ModifyDBInstanceNameRequest request;

    public ModifyDBInstanceNameRequest toReq() {
        return this.request;
    }

}
