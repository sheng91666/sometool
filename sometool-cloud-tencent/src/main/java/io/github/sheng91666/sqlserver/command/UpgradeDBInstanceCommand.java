package io.github.sheng91666.sqlserver.command;

import com.sometool.command.CloudApiCommand;
import com.sometool.util.BeanCopyUtils;
import com.tencentcloudapi.sqlserver.v20180328.models.UpgradeDBInstanceRequest;
import lombok.Data;

@Data
public class UpgradeDBInstanceCommand extends CloudApiCommand {

    private UpgradeDBInstanceRequest request;

    public UpgradeDBInstanceRequest toReq() {
        UpgradeDBInstanceRequest req = new UpgradeDBInstanceRequest();
        BeanCopyUtils.copyByCglibBeanCopier(req, this.request);
        req.setWaitSwitch(1L); //默认 维护时间窗执行

        return req;
    }

}
