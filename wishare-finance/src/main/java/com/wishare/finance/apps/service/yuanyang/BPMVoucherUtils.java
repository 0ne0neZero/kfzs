package com.wishare.finance.apps.service.yuanyang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.domains.voucher.consts.enums.VoucherLoanTypeEnum;
import com.wishare.finance.domains.voucher.entity.Voucher;
import com.wishare.finance.domains.voucher.entity.VoucherDetailOBV;
import com.wishare.starter.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * @author szh
 * @date 2024/6/18 9:51
 */

@Slf4j
public class BPMVoucherUtils {

    public static void checkAmount(Voucher voucher){
        if (ObjectUtil.isNull(voucher)){
            return;
        }
        List<VoucherDetailOBV> list = voucher.getDetails();
        if (CollUtil.isEmpty(list)){
            return;
        }
        for (VoucherDetailOBV obv : list) {
            if (obv.getType().equals(VoucherLoanTypeEnum.借方.getCode())){
                //debit
                Long amount = obv.getOriginalAmount();
                Long debitAmount = obv.getDebitAmount();
                if (!Objects.equals(amount, debitAmount)){
                    log.error("预制凭证借贷方不相等:{}", JSONObject.toJSONString(voucher));
                    throw new BizException(401,"预制凭证借贷方不相等，请检查单据数据");
                }
            }else {
                //credit
                Long amount = obv.getOriginalAmount();
                Long creditAmount = obv.getCreditAmount();
                if (!Objects.equals(amount, creditAmount)){
                    log.error("预制凭证借贷方不相等:{}", JSONObject.toJSONString(voucher));
                    throw new BizException(401,"预制凭证借贷方不相等，请检查单据数据");
                }
            }
        }

        long creditAmount = list.stream().mapToLong(VoucherDetailOBV::getCreditAmount).sum();
        long debit = list.stream().mapToLong(VoucherDetailOBV::getDebitAmount).sum();
        if (creditAmount!=debit){
            log.error("预制凭证借贷方不相等:{}", JSONObject.toJSONString(voucher));
            throw new BizException(401,"预制凭证借贷方总金额不相等，请检查单据数据");
        }

    }
}
