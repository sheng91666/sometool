package io.github.sheng91666.sqlserver.commond;

import com.sometool.command.CloudApiCommand;
import lombok.Data;

@Data
public class ListStorageTypesCommand extends CloudApiCommand {
    private String databaseName;

    private String haMode;
}
