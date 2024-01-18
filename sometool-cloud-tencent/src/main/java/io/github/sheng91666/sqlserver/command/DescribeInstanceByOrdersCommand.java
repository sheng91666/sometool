package io.github.sheng91666.sqlserver.command;

import com.sometool.command.CloudApiCommand;
import lombok.Data;

import java.util.List;

@Data
public class DescribeInstanceByOrdersCommand extends CloudApiCommand {

    private List<String> dealNames;

}
