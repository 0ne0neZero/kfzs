// package com.wishare.finance.domains.voucher.strategy.bpm.logic;
//
// import cn.hutool.core.util.StrUtil;
// import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
// import com.wishare.finance.apps.model.yuanyang.fo.WageTableF;
// import com.wishare.finance.domains.voucher.consts.enums.bpm.VoucherTemplateBpmLogicCodeEnum;
// import org.springframework.stereotype.Component;
//
// import java.math.BigDecimal;
// import java.util.List;
//
// @Component
// public class BpmWagesPayableLogicStrategy implements BpmLogicStrategy {
//
//     @Override
//     public VoucherTemplateBpmLogicCodeEnum logicCode() {
//         return VoucherTemplateBpmLogicCodeEnum.应发工资;
//     }
//
//     @Override
//     public BigDecimal logicValue(ProcessAccountBookF accountBook, int index,String bpmBillTypeCode) {
//         List<WageTableF> chargeDetails = accountBook.getWageTableInfo();
//         WageTableF chargeDetailF = chargeDetails.get(index);
//         return StrUtil.isBlank(chargeDetailF.getWagesPayable()) ? BigDecimal.ZERO :
//                 new BigDecimal(chargeDetailF.getWagesPayable());
//     }
// }
