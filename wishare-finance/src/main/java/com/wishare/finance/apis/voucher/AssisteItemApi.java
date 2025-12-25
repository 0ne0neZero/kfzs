package com.wishare.finance.apis.voucher;

import com.wishare.finance.apps.model.fangyuan.vo.BusinessUnitSyncDto;
import com.wishare.finance.apps.model.voucher.fo.AssisteBizTypeF;
import com.wishare.finance.apps.model.voucher.fo.AssisteInoutclassF;
import com.wishare.finance.apps.model.voucher.fo.AssisteOrgDeptF;
import com.wishare.finance.apps.model.voucher.fo.AssisteOrgF;
import com.wishare.finance.apps.service.voucher.AssisteItemAppService;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 辅助核算管理
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/11
 */
@Api(tags = {"辅助核算管理"})
@RestController
@RequestMapping("/assisteItem")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssisteItemApi {

    private final AssisteItemAppService assisteItemAppService;

    @GetMapping("/list")
    @ApiOperation(value = "查询辅助核算列表", notes = "查询辅助核算列表")
    public List<AssisteItemOBV> getAssisteItem(@ApiParam("辅助核算名称") @RequestParam(value = "name") String name,
                                               @ApiParam("辅助核算编码") @RequestParam("code") String code,
                                               @ApiParam("法定单位id") @RequestParam("sbId") String sbId,
                                               @ApiParam("辅助核算类型： 1部门，2业务单元，3收支项目， 4业务类型") @RequestParam("type") Integer type) {
        return assisteItemAppService.getBaseAssisteItem(name, code, type, sbId);
    }

    @PostMapping("/syncBatchAssisteBizType")
    @ApiOperation(value = "批量新增辅助核算（业务类型）", notes = "批量新增辅助核算（业务类型）")
    public Boolean syncBatchAssisteBizType(@Validated @RequestBody @Size(max = 200, message = "批量新增辅助核算最大仅允许200条数据") List<AssisteBizTypeF> assisteBizTypeFS) {
        return assisteItemAppService.syncBatchAssisteBizType(assisteBizTypeFS);
    }


    /** wishare-external（定时调用) 功能：同步业务单元
     * @param assisteOrgFS 入参
     * @return 涉及的表  assiste_org/ business_unit
     */
    @PostMapping("/syncBatchAssisteOrg")
    @ApiOperation(value = "批量新增辅助核算（业务单元）", notes = "批量新增辅助核算（业务单元）")
    public List<BusinessUnitSyncDto> syncBatchAssisteOrg(@Validated @RequestBody @Size(max = 200, message = "批量新增辅助核算最大仅允许200条数据")List<AssisteOrgF> assisteOrgFS) {
        return assisteItemAppService.syncBatchAssisteOrg(assisteOrgFS);
    }

    @PostMapping("/syncBatchAssisteOrgDept")
    @ApiOperation(value = "批量新增辅助核算（部门）", notes = "批量新增辅助核算（部门）")
    public Boolean syncBatchAssisteOrgDept(@Validated @RequestBody @Size(max = 200, message = "批量新增辅助核算最大仅允许200条数据")List<AssisteOrgDeptF> assisteOrgDeptFS) {
        return assisteItemAppService.syncBatchAssisteOrgDept(assisteOrgDeptFS);
    }

    @PostMapping("/syncBatchAssisteInoutclass")
    @ApiOperation(value = "批量新增辅助核算（收支项目）", notes = "批量新增辅助核算（收支项目）")
    public Boolean syncBatchAssisteInoutclass(@Validated @RequestBody @Size(max = 200, message = "批量新增辅助核算最大仅允许200条数据")List<AssisteInoutclassF> assisteInoutclassFS) {
        return assisteItemAppService.syncBatchAssisteInoutclass(assisteInoutclassFS);
    }

}
