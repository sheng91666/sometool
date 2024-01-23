package io.github.sheng91666.sqlserver.commond;

import com.sometool.command.CloudApiCommand;
import lombok.Data;

@Data
public class StartResizeFlavorActionCommand extends CloudApiCommand {
    private String key;
}
