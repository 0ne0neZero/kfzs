// package com.wishare.finance.domains.voucher.strategy.bpm.logic;
//
// import cn.hutool.core.util.StrUtil;
// import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
// import com.wishare.finance.apps.model.yuanyang.fo.SocialSecurityTableInfoF;
// import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmLogicCodeEnum;
// import org.springframework.stereotype.Component;
//
// import java.math.BigDecimal;
// import java.util.List;
//
// @Component
// public class BpmCorporationMedicalLogicStrategy implements BpmLogicStrategy {
//
//     @Override
//     public VoucherTemplateBpmLogicCodeEnum logicCode() {
//         return VoucherTemplateBpmLogicCodeEnum.公司承担医疗保险;
//     }
//
//     @Override
//     public BigDecimal logicValue(ProcessAccountBookF accountBook, int index,String bpmBillTypeCode) {
//         List<SocialSecurityTableInfoF> chargeDetails = accountBook.getSocialSecurityTableInfo();
//         SocialSecurityTableInfoF chargeDetailF = chargeDetails.get(index);
//
//         String string = chargeDetailF.getCorporationMedical();
//         return StrUtil.isBlank(string) ? BigDecimal.ZERO : new BigDecimal(string);
//     }
// }
