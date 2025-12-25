package com.wishare.finance.apps.service.pushbill;

import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillFileZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherBillFileZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherPushBillDxZJRepository;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.VoucherPushBillZJRepository;
import com.wishare.starter.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherBillFileZJService {
    private final VoucherBillFileZJRepository voucherBillFileZJRepository;
    private final VoucherPushBillZJRepository voucherPushBillZJRepository;
    private final VoucherPushBillDxZJRepository voucherPushBillDxZJRepository;

    public Boolean deleteById(Long id){
        VoucherBillFileZJ byId = voucherBillFileZJRepository.getById(id);
        if (null == byId) {
            throw new BizException(403,"该附件不存在!");
        }
        VoucherBillZJ voucherBillZJ = voucherPushBillZJRepository.getById(byId.getVoucherBillId());
        if (!StringUtils.isEmpty(voucherBillZJ.getProcInstId()) || voucherBillZJ.getApproveState() == 2) {
            throw new BizException(403,"发起审批前允许删除!");
        }
        return  voucherBillFileZJRepository.deleteById(id);
    }

    public Boolean dxDeleteById(Long id){
        VoucherBillFileZJ byId = voucherBillFileZJRepository.getById(id);
        if (null == byId) {
            throw new BizException(403,"该附件不存在!");
        }
        VoucherBillDxZJ voucherBillDxZJ = voucherPushBillDxZJRepository.getById(byId.getVoucherBillId());
        if (!StringUtils.isEmpty(voucherBillDxZJ.getProcInstId())) {
            throw new BizException(403,"发起审批前允许删除!");
        }
        return  voucherBillFileZJRepository.deleteById(id);
    }
}
