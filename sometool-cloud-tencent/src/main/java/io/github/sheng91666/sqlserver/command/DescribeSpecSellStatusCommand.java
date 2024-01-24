package io.github.sheng91666.sqlserver.command;

import com.sometool.command.CloudApiCommand;
import com.sometool.util.BeanCopyUtils;
import com.tencentcloudapi.sqlserver.v20180328.models.DescribeSpecSellStatusRequest;
import lombok.Data;

@Data
public class DescribeSpecSellStatusCommand extends CloudApiCommand {

    private DescribeSpecSellStatusRequest request;

    public DescribeSpecSellStatusRequest toReq() {
        DescribeSpecSellStatusRequest req = new DescribeSpecSellStatusRequest();
        BeanCopyUtils.copyByCglibBeanCopier(req, this.request);
        req.setCurrency("CNY"); //默认人民币

        return req;
    }

}
