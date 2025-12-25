package com.wishare.finance.apis.voucher;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.voucher.fo.AddVoucherSchemeF;
import com.wishare.finance.apps.model.voucher.fo.DeleteVoucherSchemeF;
import com.wishare.finance.apps.model.voucher.fo.EnableVoucherSchemeF;
import com.wishare.finance.apps.model.voucher.fo.RelateSchemeVoucherRuleF;
import com.wishare.finance.apps.model.voucher.vo.VoucherSchemeV;
import com.wishare.finance.apps.service.voucher.VoucherSchemeAppService;
import com.wishare.finance.domains.voucher.command.UpdateVoucherSchemeCommand;
import com.wishare.finance.domains.voucher.entity.VoucherScheme;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeOrgOBV;
import com.wishare.finance.domains.voucher.repository.VoucherSchemeOrgRepository;
import com.wishare.finance.domains.voucher.repository.VoucherSchemeRepository;
import com.wishare.finance.domains.voucher.repository.VoucherSchemeRuleRepository;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
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

import java.util.Objects;

/**
 * 凭证核算方案接口
 * @author dxclay
 * @since  2023/4/3
 * @version 1.0
 */
@Api(tags = {"凭证核算方案"})
@RestController
@RequestMapping("/voucher/scheme")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherSchemeApi {

    private final VoucherSchemeRepository voucherSchemeRepository;
    private final VoucherSchemeAppService voucherSchemeAppService;
    private final VoucherSchemeOrgRepository voucherSchemeOrgRepository;
    private final VoucherSchemeRuleRepository voucherSchemeRuleRepository;

    @PostMapping("/add")
    @ApiOperation(value = "新增核算方案")
    public Long addVoucherScheme(@RequestBody @Validated AddVoucherSchemeF addVoucherSchemeF){
        return voucherSchemeAppService.addVoucherScheme(addVoucherSchemeF);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新核算方案")
    public Boolean updateVoucherScheme(@RequestBody @Validated UpdateVoucherSchemeCommand updateVoucherSchemeCommand){
        return voucherSchemeAppService.updateVoucherScheme(updateVoucherSchemeCommand);
    }

    @PostMapping("/enable")
    @ApiOperation(value = "启用禁用凭证核算方案")
    public Boolean enable(@RequestBody @Validated EnableVoucherSchemeF enableVoucherSchemeF){
        return voucherSchemeAppService.enableScheme(enableVoucherSchemeF);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除凭证核算方案")
    public Boolean delete(@RequestBody @Validated DeleteVoucherSchemeF deleteVoucherSchemeF){
        return voucherSchemeAppService.deleteScheme(deleteVoucherSchemeF);
    }

    @PostMapping("/relateScheme")
    @ApiOperation(value = "关联核算方案规则")
    public boolean relateScheme(@RequestBody @Validated RelateSchemeVoucherRuleF relateSchemeVoucherRuleF){
        return voucherSchemeAppService.relateRule(relateSchemeVoucherRuleF);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "根据id查询凭证核算方案详情")
    public VoucherSchemeV getDetailById(@RequestParam("voucherSchemeId") @ApiParam("凭证模板id") Long voucherSchemeId){
        if (Objects.nonNull(voucherSchemeId)){
            VoucherScheme voucherScheme = voucherSchemeRepository.getById(voucherSchemeId);
            if (Objects.nonNull(voucherScheme)){
                voucherScheme.setOrgs(Global.mapperFacade.mapAsList(voucherSchemeOrgRepository.listBySchemeId(voucherScheme.getId()), VoucherSchemeOrgOBV.class));
                voucherScheme.setRules(voucherSchemeRuleRepository.obvListBySchemeId(voucherScheme.getId()));
            }
            return Global.mapperFacade.map(voucherScheme, VoucherSchemeV.class);
        }
        return null;
    }


    @PostMapping("/page")
    @ApiOperation(value = "分页查询核算方案")
    public PageV<VoucherSchemeV> getPage(@RequestBody @Validated PageF<SearchF<?>> searchFPageF){
        Page<VoucherScheme> page = voucherSchemeRepository.getPage(searchFPageF);
        PageV<VoucherSchemeV> voucherSchemeVPageV = RepositoryUtil.convertPage(page, VoucherSchemeV.class);
        for (VoucherSchemeV record : voucherSchemeVPageV.getRecords()) {
            record.setOrgs(Global.mapperFacade.mapAsList(voucherSchemeOrgRepository.listBySchemeId(record.getId()), VoucherSchemeOrgOBV.class));
        }
        return voucherSchemeVPageV;
    }

    //@PostMapping("/orgPage")
    //@ApiOperation(value = "分页查询财务组织")
    //public PageV<VoucherSchemeOrgOBV> getOrgPage(@RequestBody @Validated PageF<SearchF<?>> searchFPageF){
    //    return voucherSchemeRepository.getOrgPage(searchFPageF);
    //}

}
