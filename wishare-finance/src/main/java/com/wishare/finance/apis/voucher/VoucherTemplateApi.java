package com.wishare.finance.apis.voucher;

import com.wishare.finance.apps.model.voucher.fo.AddVoucherTemplateF;
import com.wishare.finance.apps.model.voucher.fo.DeleteVoucherTemplateF;
import com.wishare.finance.apps.model.voucher.fo.EnableVoucherTemplateF;
import com.wishare.finance.apps.model.voucher.fo.UpdateVoucherTemplateF;
import com.wishare.finance.apps.model.voucher.vo.VoucherRuleTemplateV;
import com.wishare.finance.apps.model.voucher.vo.VoucherTemplateV;
import com.wishare.finance.apps.service.voucher.VoucherTemplateAppService;
import com.wishare.finance.domains.voucher.entity.VoucherRuleTemplate;
import com.wishare.finance.domains.voucher.entity.VoucherSchemeOrgOBV;
import com.wishare.finance.domains.voucher.repository.VoucherRuleTemplateRepository;
import com.wishare.finance.domains.voucher.repository.VoucherTemplateRepository;
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

import java.util.List;
import java.util.Objects;

/**
 * 凭证模板接口
 * @author dxclay
 * @since  2023/4/3
 * @version 1.0
 */
@Api(tags = {"凭证模板"})
@RestController
@RequestMapping("/voucher/template")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherTemplateApi {

    private final VoucherTemplateRepository voucherTemplateRepository;
    private final VoucherTemplateAppService voucherTemplateAppService;
    private final VoucherRuleTemplateRepository voucherRuleTemplateRepository;

    @PostMapping("/add")
    @ApiOperation(value = "新增凭证模板")
    public Long addVoucherScheme(@RequestBody @Validated AddVoucherTemplateF addVoucherTemplateF){
        return voucherTemplateAppService.addTemplate(addVoucherTemplateF);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新凭证模板")
    public Boolean updateVoucherScheme(@RequestBody @Validated UpdateVoucherTemplateF updateVoucherTemplateF){
        return voucherTemplateAppService.updateTemplate(updateVoucherTemplateF);
    }

    @PostMapping("/enable")
    @ApiOperation(value = "启用禁用凭证模板")
    public Boolean enable(@RequestBody @Validated EnableVoucherTemplateF enableVoucherTemplateF){
        return voucherTemplateAppService.enableTemplate(enableVoucherTemplateF);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除凭证模板")
    public Boolean delete(@RequestBody @Validated DeleteVoucherTemplateF deleteVoucherTemplateF){
        return voucherTemplateAppService.deleteTemplate(deleteVoucherTemplateF);
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询凭证模板")
    public PageV<VoucherTemplateV> getPage(@RequestBody @Validated PageF<SearchF<?>> searchFPageF){
        return RepositoryUtil.convertPage(voucherTemplateRepository.getPage(searchFPageF), VoucherTemplateV.class);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "根据id查询凭证模板详情")
    public VoucherTemplateV getDetailById(@RequestParam("voucherTemplateId") @ApiParam("凭证模板id") Long voucherTemplateId){
        if (Objects.isNull(voucherTemplateId)){
            return null;
        }
        return Global.mapperFacade.map(voucherTemplateRepository.getById(voucherTemplateId), VoucherTemplateV.class);
    }

    @GetMapping("/getRuleTemps")
    @ApiOperation(value = "查询模板是否被引用")
    public List<VoucherRuleTemplateV> getRuleTemps(@RequestParam("voucherTemplateId") @ApiParam("凭证模板id") Long voucherTemplateId){
        return Global.mapperFacade.mapAsList(voucherRuleTemplateRepository.getRuleTemps(voucherTemplateId), VoucherRuleTemplateV.class);
    }

}
