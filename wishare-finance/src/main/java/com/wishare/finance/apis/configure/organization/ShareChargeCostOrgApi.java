package com.wishare.finance.apis.configure.organization;

import com.wishare.finance.apps.model.configure.organization.fo.ShareChargeCostOrgF;
import com.wishare.finance.apps.service.configure.organization.ShareChargeCostOrgAppService;
import com.wishare.finance.domains.configure.organization.dto.ShareChargeCostOrgDto;
import com.wishare.finance.domains.configure.organization.entity.ShareChargeCostOrgE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dongpeng
 * @date 2023/7/22
 * @Description:
 */
@Api(tags = {"分成费项信息管理"})
@RestController
@RequestMapping("/shareChargeCostOrg")
@RequiredArgsConstructor
public class ShareChargeCostOrgApi {

    private final ShareChargeCostOrgAppService shareChargeCostOrgAppService;


    @GetMapping("/queryShareChargeCostCenterRelation/{costOrgId}")
    @ApiOperation(value = "查询分成费项与成本中心关联关系", notes = "查询分成费项与成本中心关联关系")
    public List<ShareChargeCostOrgDto> queryShareChargeCostCenterRelation(@PathVariable("costOrgId") @ApiParam("成本中心id")Long costOrgId) {
        return shareChargeCostOrgAppService.queryShareChargeCostCenterRelation(costOrgId);
    }

    @PostMapping("/editShareChargeCostCenterRelation")
    @ApiOperation(value = "编辑分成费项关联关系", notes = "编辑分成费项关联关系")
    public Boolean editShareChargeCostCenterRelation(@Validated @RequestBody ShareChargeCostOrgF shareChargeCostOrgF) {
        return shareChargeCostOrgAppService.editShareChargeCostCenterRelation(shareChargeCostOrgF);
    }

}
