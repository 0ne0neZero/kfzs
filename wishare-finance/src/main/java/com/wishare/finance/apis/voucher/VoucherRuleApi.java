package com.wishare.finance.apis.voucher;

import cn.hutool.core.collection.CollUtil;
import com.wishare.finance.apps.model.voucher.fo.AddVoucherRuleF;
import com.wishare.finance.apps.model.voucher.fo.DeleteVoucherRuleF;
import com.wishare.finance.apps.model.voucher.fo.EnableVoucherRuleF;
import com.wishare.finance.apps.model.voucher.fo.RunVoucherRuleF;
import com.wishare.finance.apps.model.voucher.fo.UpdateVoucherRuleF;
import com.wishare.finance.apps.model.voucher.vo.*;
import com.wishare.finance.apps.service.voucher.VoucherRuleAppService;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleConditionTypeEnum;
import com.wishare.finance.domains.voucher.dto.VoucherSchemeRuleDto;
import com.wishare.finance.domains.voucher.entity.VoucherBusinessDetail;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOptionOBV;
import com.wishare.finance.domains.voucher.repository.VoucherBusinessDetailRepository;
import com.wishare.finance.domains.voucher.repository.VoucherRuleRecordRepository;
import com.wishare.finance.domains.voucher.repository.VoucherRuleRepository;
import com.wishare.finance.domains.voucher.repository.VoucherSchemeRuleRepository;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherBusinessDetailMapper;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 凭证规则接口
 * @author dxclay
 * @since  2023/4/4
 * @version 1.0
 */
@Api(tags = {"凭证规则"})
@RestController
@RequestMapping("/voucher/rule")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherRuleApi {

    private final VoucherRuleAppService voucherRuleAppService;
    private final VoucherRuleRepository voucherRuleRepository;
    private final VoucherSchemeRuleRepository voucherSchemeRuleRepository;

    private final VoucherRuleRecordRepository voucherRuleRecordRepository;
    private final VoucherBusinessDetailMapper voucherBusinessDetailMapper;
    private final VoucherBusinessDetailRepository voucherBusinessDetailRepository;

    @PostMapping("/add")
    @ApiOperation(value = "新增凭证规则")
    public Long addVoucherRule(@RequestBody @Validated AddVoucherRuleF addVoucherRuleF) {
        //只有触发事件为冲销作废的时候，不校验凭证模板id
        if (!addVoucherRuleF.getEventType().equals(VoucherEventTypeEnum.作废.getCode()) &&
                !addVoucherRuleF.getEventType().equals(VoucherEventTypeEnum.冲销.getCode())){
            if (addVoucherRuleF.getVoucherTemplateId() == null){
                throw BizException.throw400("凭证模板id不能为空");
            }
        }
        return voucherRuleAppService.addRule(addVoucherRuleF);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新凭证规则")
    public Boolean addVoucherRule(@RequestBody @Validated UpdateVoucherRuleF updateVoucherRuleF) {
        return voucherRuleAppService.updateRule(updateVoucherRuleF);
    }

    @PostMapping("/enable")
    @ApiOperation(value = "启用禁用凭证规则")
    public Boolean enable(@RequestBody @Validated EnableVoucherRuleF enableVoucherRuleF){
        return voucherRuleAppService.enableVoucherRule(enableVoucherRuleF);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除凭证规则")
    public boolean delete(@RequestBody @Validated DeleteVoucherRuleF deleteVoucherTemplateF){
        return voucherRuleAppService.deleteVoucherRule(deleteVoucherTemplateF);
    }

    @PostMapping("/execute")
    @ApiOperation(value = "运行凭证规则")
    public boolean execute(@RequestBody @Validated RunVoucherRuleF runVoucherRuleF){
        return voucherRuleAppService.executeVoucherRule(runVoucherRuleF);
    }

    @GetMapping("/query/conditionOptions")
    @ApiOperation(value = "查询过滤条件列表")
    public List<VoucherRuleConditionOptionOBV> getConditionOptions(@RequestParam("conditionType")@ApiParam(value = "凭证规则id", required = true) @NotNull(message = "凭证规则筛选条件类型不能为空") Integer conditionType){
        return voucherRuleAppService.getConditionOptions(VoucherRuleConditionTypeEnum.valueOfByCode(conditionType));
    }

    @GetMapping("/detail")
    @ApiOperation(value = "根据规则id查询规则详情")
    public VoucherRuleV getDetailById(@RequestParam("voucherRuleId") @ApiParam(value = "凭证规则id",required = true) @NotNull(message = "凭证规则id不能为空") Long voucherRuleId){
        return Global.mapperFacade.map(voucherRuleRepository.getById(voucherRuleId), VoucherRuleV.class);
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询凭证规则")
    public PageV<VoucherRulePageV> getPage(@RequestBody @Validated PageF<SearchF<?>> searchFPageF) {
        PageV<VoucherRulePageV> voucherRuleVPage = RepositoryUtil.convertPage(voucherRuleRepository.getPage(searchFPageF), VoucherRulePageV.class);
        List<VoucherRulePageV> records = voucherRuleVPage.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            List<VoucherSchemeRuleDto> voucherSchemeRules = voucherSchemeRuleRepository.dtoLListByRuleIds(records.stream().map(VoucherRulePageV::getId).collect(Collectors.toList()));
            Map<Long, List<VoucherSchemeRuleDto>> voucherSchemeRuleMap = voucherSchemeRules.stream().collect(Collectors.groupingBy(VoucherSchemeRuleDto::getVoucherRuleId));
            records.forEach(record -> {
                List<VoucherSchemeRuleDto> voucherSchemeRuleDtos = voucherSchemeRuleMap.get(record.getId());
                record.setSchemes(CollectionUtils.isNotEmpty(voucherSchemeRuleDtos) ? voucherSchemeRuleDtos.stream().map(i -> new BaseVoucherSchemeRuleV(i.getVoucherSchemeId(),
                            i.getVoucherSchemeName())).collect(Collectors.toList()) : new ArrayList<>());
            });
        }
        return voucherRuleVPage;
    }

    @PostMapping("/pageByScheme")
    @ApiOperation(value = "分页查询凭证规则（包含方案查询）")
    public PageV<VoucherRulePageV> getPageByScheme(@RequestBody @Validated PageF<SearchF<?>> searchFPageF) {
        PageV<VoucherRulePageV> voucherRuleVPage = RepositoryUtil.convertPage(voucherRuleRepository.getPageByScheme(searchFPageF), VoucherRulePageV.class);
        List<VoucherRulePageV> records = voucherRuleVPage.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            List<VoucherSchemeRuleDto> voucherSchemeRules = voucherSchemeRuleRepository.dtoLListByRuleIds(records.stream().map(VoucherRulePageV::getId).collect(Collectors.toList()));
            Map<Long, List<VoucherSchemeRuleDto>> voucherSchemeRuleMap = voucherSchemeRules.stream().collect(Collectors.groupingBy(VoucherSchemeRuleDto::getVoucherRuleId));
            records.forEach(record -> {
                List<VoucherSchemeRuleDto> voucherSchemeRuleDtos = voucherSchemeRuleMap.get(record.getId());
                record.setSchemes(CollectionUtils.isNotEmpty(voucherSchemeRuleDtos) ? voucherSchemeRuleDtos.stream().map(i -> new BaseVoucherSchemeRuleV(i.getVoucherSchemeId(),
                        i.getVoucherSchemeName())).collect(Collectors.toList()) : new ArrayList<>());
            });
        }
        return voucherRuleVPage;
    }

    @PostMapping("/runPage")
    @ApiOperation(value = "分页查询凭证规则运行记录")
    public PageV<VoucherRuleRecordV> getRunPage(@RequestBody @Validated PageF<SearchF<?>> searchPageF) {
        return RepositoryUtil.convertPage(voucherRuleRecordRepository.pageBySearch(searchPageF), VoucherRuleRecordV.class);
    }
    @GetMapping("/fillInAccountBookId")
    public String fillInAccountBookId() {
        List<VoucherMiniV> vList = voucherBusinessDetailMapper.fillInAccountBookId();

        for (VoucherMiniV miniV : vList) {
            voucherBusinessDetailMapper.upAccountBookId(miniV.getId(),miniV.getAccountBookId());
        }

        return "fillInAccountBookId执行完毕";
    }


}
