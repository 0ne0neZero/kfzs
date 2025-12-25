package com.wishare.finance.apis.configure.organization;

import com.wishare.finance.apps.model.configure.organization.fo.*;
import com.wishare.finance.apps.model.configure.organization.vo.StatutoryBodyAccountSimpleV;
import com.wishare.finance.apps.model.configure.organization.vo.StatutoryBodyAccountV;
import com.wishare.finance.apps.model.reconciliation.vo.FlowImportV;
import com.wishare.finance.apps.service.configure.organization.StatutoryBodyAccountAppService;
import com.wishare.finance.domains.configure.organization.dto.BankAccountCostOrgDto;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author xujian
 * @date 2022/7/26
 * @Description:
 */
@Api(tags = {"法定单位银行账户管理"})
@RestController
@RequestMapping("/statutoryBodyAccount")
@RequiredArgsConstructor
public class StatutoryBodyAccountApi {

    private final StatutoryBodyAccountAppService statutoryBodyAccountAppService;


    @ApiOperation(value = "新增银行账户", notes = "新增银行账户")
    @PostMapping("/add")
    public Long addStatutoryBodyAccount(@RequestBody @Validated AddStatutoryBodyAccountF form) {
        Long statutoryBodyAccountId = statutoryBodyAccountAppService.addStatutoryBodyAccount(form);
        return statutoryBodyAccountId;
    }

    @ApiOperation(value = "修改银行账户", notes = "修改银行账户")
    @PostMapping("/update")
    public Long updateStatutoryBodyAccount(@RequestBody @Validated UpdateStatutoryBodyAccountF form) {
        return statutoryBodyAccountAppService.updateStatutoryBodyAccount(form);
    }

    @ApiOperation(value = "删除银行账户", notes = "删除银行账户")
    @DeleteMapping("/delete/{id}")
    public Boolean deleteStatutoryBodyAccount(@PathVariable("id") Long id) {
        return statutoryBodyAccountAppService.deleteStatutoryBodyAccount(id);
    }

    @ApiOperation(value = "根据id启用或禁用银行账户", notes = "根据id启用或禁用银行账户")
    @PostMapping("/enable/{id}/{disableState}")
    public Boolean enable(@PathVariable("id") Long id, @PathVariable("disableState") Integer disableState) {
        if (null == disableState || null == DataDisabledEnum.valueOfByCode(disableState)) {
            throw BizException.throw400(ErrMsgEnum.DISABLE_STATE_EXCEPTION.getErrMsg());
        }
        return statutoryBodyAccountAppService.enable(id, disableState);
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询银行账户列表", notes = "分页查询银行账户列表")
    public PageV<StatutoryBodyAccountV> queryPage(@Validated @RequestBody PageF<SearchF<StatutoryBodyAccountPageF>> form) {
        return statutoryBodyAccountAppService.queryPage(form);
    }

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页查询银行账户列表（成本中心关联用）", notes = "分页查询银行账户列表（成本中心关联用）")
    public PageV<StatutoryBodyAccountSimpleV> queryPageForBind(@Validated @RequestBody PageF<SearchF<?>> form) {
        return statutoryBodyAccountAppService.queryPageForBind(form);
    }

    @GetMapping("/queryCostCenterRelation/{costOrgId}")
    @ApiOperation(value = "查询成本中心关联关系", notes = "查询成本中心关联关系")
    public List<BankAccountCostOrgDto> queryCostCenterRelation(@PathVariable("costOrgId") @ApiParam("成本中心id")Long costOrgId) {
        return statutoryBodyAccountAppService.queryCostCenterRelation(costOrgId);
    }

    @PostMapping("/editCostCenterRelation")
    @ApiOperation(value = "编辑成本中心关联关系", notes = "编辑成本中心关联关系")
    public Boolean editCostCenterRelation(@Validated @RequestBody BankAccountCostOrgF bankAccountCostOrgF) {
        return statutoryBodyAccountAppService.editCostCenterRelation(bankAccountCostOrgF);
    }

    @PostMapping("/list")
    @ApiOperation(value = "根据条件查询银行账户列表", notes = "根据条件查询银行账户列表")
    public List<StatutoryBodyAccountV> queryList(@Validated @RequestBody StatutoryBodyAccountListF form) {
        return statutoryBodyAccountAppService.queryList(form);
    }

    @ApiOperation(value = "根据id获取银行账户详情", notes = "根据id获取银行账户详情")
    @GetMapping("/detail/{id}")
    public StatutoryBodyAccountV detailStatutoryBodyAccount(@PathVariable("id") Long id) {
        StatutoryBodyAccountV result = statutoryBodyAccountAppService.detailStatutoryBodyAccount(id);
        return result;
    }

    @ApiOperation(value = "导入银行账户（远洋）", notes = "导入银行账户")
    @ApiImplicitParam(name = "file", value = "文件", dataType = "__File", allowMultiple = true, paramType = "query", dataTypeClass = MultipartFile.class)
    @PostMapping("/import")
    public Boolean importFlow(@RequestParam("file") MultipartFile file){
        return statutoryBodyAccountAppService.importStatutoryBodyAccount(file);
    }

}
