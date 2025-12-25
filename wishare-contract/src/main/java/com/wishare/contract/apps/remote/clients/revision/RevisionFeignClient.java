package com.wishare.contract.apps.remote.clients.revision;

import com.wishare.contract.apps.remote.fo.org.CustomerMutualF;
import com.wishare.contract.apps.remote.fo.org.SupplierMutualF;
import com.wishare.starter.annotations.OpenFeignClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @version 1.0.0
 * @Description： 远程接口-用于处理合同改版-公民供应撒好难过客户&支出收入合同的交互业务逻辑
 * @Author： chentian
 * @since： 2023/7/4  19:55
 */
@OpenFeignClient(contextId = "revision", name = "wishare-org", serverName = "组织中心", path = "/org")
public interface RevisionFeignClient {

    @ApiOperation(value = "合同模块合同状态更新后同步更新供应商关联的合同数量", notes = "合同模块合同状态更新后同步更新供应商关联的合同数量")
    @PostMapping("/revision/mutualSupplierFormContract")
    Boolean mutualSupplierFormContract(@RequestBody SupplierMutualF mutualF);

    @ApiOperation(value = "合同模块合同状态更新后同步更新客户关联的合同数量", notes = "合同模块合同状态更新后同步更新客户关联的合同数量")
    @PostMapping("/revision/mutualCustomerFormContract")
    Boolean mutualCustomerFormContract(@RequestBody CustomerMutualF mutualF);

}
