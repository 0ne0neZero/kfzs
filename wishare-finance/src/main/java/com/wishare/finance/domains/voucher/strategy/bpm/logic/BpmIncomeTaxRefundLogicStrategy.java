// package com.wishare.finance.domains.voucher.strategy.bpm.logic;
//
// import cn.hutool.core.util.StrUtil;
// import com.alibaba.fastjson.JSONObject;
// import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
// import com.wishare.finance.apps.model.yuanyang.fo.SocialSecurityTableInfoF;
// import com.wishare.finance.apps.model.yuanyang.fo.WageTableF;
// import com.wishare.finance.domains.voucher.consts.enums.bpm.BusinessBillTypeEnum;
// import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmLogicCodeEnum;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Component;
//
// import java.math.BigDecimal;
// import java.util.Objects;
//
// @Component
// @Slf4j
// public class BpmIncomeTaxRefundLogicStrategy implements BpmLogicStrategy {
//
//     @Override
//     public VoucherTemplateBpmLogicCodeEnum logicCode() {
//         return VoucherTemplateBpmLogicCodeEnum.个税补退;
//     }
//
//     @Override
//     public BigDecimal logicValue(ProcessAccountBookF accountBook, int index, String bpmBillTypeCode) {
//         String string;
//         if (BusinessBillTypeEnum.WAGE_SET.contains(bpmBillTypeCode)) {
//             WageTableF wageTableF = accountBook.getWageTableInfo().get(index);
//             string = wageTableF.getIncomeTaxRefund();
//         } else if (BusinessBillTypeEnum.SOCIAL_SET.contains(bpmBillTypeCode)) {
//             SocialSecurityTableInfoF infoF = accountBook.getSocialSecurityTableInfo().get(index);
//             string = infoF.getIncomeTaxRefund();
//         } else {
//             string = "0";
//             log.error("BpmLogicStrategy取值异常,入参:{},index-{},bpmBillTypeCode-{},logicCode-{}", JSONObject.toJSONString(accountBook), index, bpmBillTypeCode, VoucherTemplateBpmLogicCodeEnum.个税补退.getValue());
//         }
//         return StrUtil.isBlank(string) ? BigDecimal.ZERO : new BigDecimal(string);
//     }
// }
