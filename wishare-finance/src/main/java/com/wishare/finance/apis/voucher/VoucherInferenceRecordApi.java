package com.wishare.finance.apis.voucher;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wishare.finance.apps.model.voucher.fo.VoucherInferenceRecordF;
import com.wishare.finance.apps.model.voucher.vo.VoucherInferenceRecordV;
import com.wishare.finance.apps.service.voucher.VoucherInferenceRecordAppService;
import com.wishare.finance.domains.voucher.entity.Voucher;
import com.wishare.finance.domains.voucher.repository.VoucherInfoRepository;
import com.wishare.finance.domains.voucher.service.VoucherDomainService;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ErrMsg;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 推凭记录
 * @author: pgq
 * @since: 2022/10/11 10:37
 * @version: 1.0.0
 */
@Slf4j
@Api(tags = {"凭证推凭记录"})
@RestController
@Deprecated
@RequestMapping("/voucher/inference")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherInferenceRecordApi {

    private final VoucherInferenceRecordAppService voucherInferenceRecordAppService;
    private final VoucherInfoRepository voucherInfoRepository;
    private final VoucherDomainService voucherDomainService;


    @PostMapping("/page")
    @ApiOperation(value = "获取推凭记录(分页)", notes = "获取推凭记录(分页)")
    public PageV<VoucherInferenceRecordV> queryPage(@Validated @RequestBody PageF<SearchF<?>> form) {

        return voucherInferenceRecordAppService.queryPage(form);
    }

    @PostMapping("/add")
    @ApiModelProperty(value = "新增推凭记录", notes = "新增推凭记录")
    public Long add(@Validated @RequestBody VoucherInferenceRecordF form) {

        return voucherInferenceRecordAppService.add(form);
    }

    /**
     * 根据传入id删除推凭记录
     * @param ids
     */
    @PostMapping("/deleteByIds")
    @ApiOperation(value = "删除推凭记录",notes = "删除推凭记录")
    public Boolean deleteByIds(@RequestParam List<Long> ids){
        ErrorAssertUtil.notEmptyThrow400(ids, ErrorMessage.VOUCHERINFERENCE_NOT_EXIST);
        //先查询是否有数据，如果没有数据，就跳过删除操作
        boolean searchResult = voucherDomainService.selectVoucherInfoAndBusinessDetail(ids);
        ErrorAssertUtil.isTrueThrow300(searchResult,ErrorMessage.VOUCHERINFO_OR_DETAIL_NOT_FOUND);

        List<Voucher> list = voucherInfoRepository.listByIds(ids);
        Integer i1 = voucherDomainService.deleteByIds(ids);
        int i2 =0;
        for (Voucher voucher : list) {
            Integer i = voucherDomainService.deleteBusinessDetailByIds(Collections.singletonList(voucher.getId()),
                    voucher.getAccountBookId());
            i2=i2+i;
        }
        if(i1 > 0 || i2 > 0){
            return true;
        }
        ErrorAssertUtil.isFalseThrow402(i1 > 0 || i2 > 0,ErrorMessage.VOUCHERINFERENCE_DELETE_FAIL);
        return false;
    }

}
