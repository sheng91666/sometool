package io.github.sheng91666.sqlserver.command;

import com.sometool.command.CloudApiCommand;
import com.tencentcloudapi.sqlserver.v20180328.models.DescribeInstanceByOrdersRequest;
import lombok.Data;

@Data
public class DescribeInstanceByOrdersCommand extends CloudApiCommand {

    private DescribeInstanceByOrdersRequest request;

    public DescribeInstanceByOrdersRequest toReq() {
        return this.request;
    }

}
