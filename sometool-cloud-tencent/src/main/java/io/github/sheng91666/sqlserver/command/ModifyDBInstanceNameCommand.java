package io.github.sheng91666.sqlserver.command;

import com.sometool.command.CloudApiCommand;
import lombok.Data;

@Data
public class ModifyDBInstanceNameCommand extends CloudApiCommand {
    private String instanceId;

    private String instanceName;
}
