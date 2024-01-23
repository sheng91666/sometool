package io.github.sheng91666.sqlserver.commond;

import com.sometool.command.CloudApiCommand;
import lombok.Data;

@Data
public class ListEngineFlavorsCommand extends CloudApiCommand {
    private String key;

    private String haMode;

}
