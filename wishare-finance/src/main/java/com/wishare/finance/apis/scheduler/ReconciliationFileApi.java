package com.wishare.finance.apis.scheduler;

import com.wishare.finance.apps.model.reconciliationFile.fo.ReconcileAccountF;
import com.wishare.finance.apps.service.scheduler.ReconciliationFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"对账文件手动拉取API"})
@RestController
@RequestMapping("/reconciliation")
@RequiredArgsConstructor
public class ReconciliationFileApi {

    private final ReconciliationFileService reconciliationFileService;

    @PostMapping("/getChannelAccount")
    @ApiOperation(value = "手动拉取拉取第三方账单", notes = "手动拉取拉取第三方账单")
    public Boolean getChannelAccount(@RequestBody ReconcileAccountF reconcileAccountF) {
        return reconciliationFileService.getChannelAccount(reconcileAccountF);
    }
}
