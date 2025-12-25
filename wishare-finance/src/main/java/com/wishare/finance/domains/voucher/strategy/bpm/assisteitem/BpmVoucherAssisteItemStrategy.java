package com.wishare.finance.domains.voucher.strategy.bpm.assisteitem;

import com.wishare.finance.apps.model.yuanyang.fo.BusinessProcessHandleF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.consts.enums.bpm.CustomerExcludeBIllType;
import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmAssisteItemEnum;
import com.wishare.finance.domains.voucher.entity.VoucherTemplateEntryAssiste;
import com.wishare.finance.domains.voucher.repository.yuanyang.YyNccCustomerRelRepository;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import org.apache.commons.lang3.StringUtils;

public interface BpmVoucherAssisteItemStrategy {

    /**
     * 辅助核算类型
     * @return
     */
    VoucherTemplateBpmAssisteItemEnum type();

    /**
     * 根据辅助核算id获取辅助核算信息
     * @param accountBook 凭证业务单据
     * @return
     */
    AssisteItemOBV getByBus(VoucherTemplateEntryAssiste entryAssisteItem, BusinessProcessHandleF businessProcessHandleF,
                            ProcessAccountBookF accountBook, int index);

    default void handleCustomerInfo(VoucherTemplateEntryAssiste entryAssisteItem, String creditCode, AssisteItemOBV itemOBV, String BusinessBillTypeCode) {
        // 符合条件的业务类型以及是客商的辅助核算时
        if (AssisteItemTypeEnum.客商.equalsByAscCode(entryAssisteItem.getAscCode()) && !CustomerExcludeBIllType.excludeType.contains(BusinessBillTypeCode)) {
            if (StringUtils.isBlank(creditCode)) {
                throw BizException.throw300("未填写信用代码");
            }
            YyNccCustomerRelRepository yyNccCustomerRelRepository = Global.ac.getBean(YyNccCustomerRelRepository.class);
            String nccCustomerCode = yyNccCustomerRelRepository.getNccCustomerCode(itemOBV, creditCode);
            itemOBV.setCode(nccCustomerCode);
        }
    }

}
