package com.wishare.finance.apps.service.voucher;

import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.ActionEventEnum;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherE;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import com.wishare.tools.starter.fo.search.Field;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: pgq
 * @since: 2022/11/15 14:21
 * @version: 1.0.0
 */
public interface IVoucherInferenceAppService {

    /**
     * 事件类型，用于区分子类
     * @return
     */
    EventTypeEnum getEventType();

    /**
     * 推凭
     * @param voucherRule
     * @param isSingle
     */
    void inference(VoucherRuleE voucherRule, boolean isSingle);

    /**
     * 获取账单状态
     * @param record
     * @param fieldList
     */
    void getBillStatus(VoucherRuleE record, List<Field> fieldList);

    /**
     * 构建 计算推凭金额
     * @param bill
     * @param billType
     * @return
     */
    Long generateInferenceAmount(BillInferenceV bill, BillTypeEnum billType);

    /**
     * 手动运行推凭
     * @param ruleId
     * @return
     */
    Boolean runInferenceByRule(Long ruleId);

    /**
     * 单个推凭 即时推送
     * @param billId  账单id
     * @param billTypeEnum   账单类型参考 {@link BillTypeEnum}
     * @param actionEventEnum 时间类型参考 {@link ActionEventEnum}
     * @param amount 外来金额（推凭的金额可传递）  默认按规则查询
     * @return 凭证号
     */
    String singleInference(Long billId, BillTypeEnum billTypeEnum, ActionEventEnum actionEventEnum, Long amount, String supCpUnitId);

  /**
     * 单个推凭 即时推送
     * @param billIds  账单id集合
     * @param billTypeEnum   账单类型参考 {@link BillTypeEnum}
     * @param actionEventEnum 时间类型参考 {@link ActionEventEnum}
     * @param amount 外来金额（推凭的金额可传递）  默认按规则查询
     * @param supCpUnitId 上级收费单元ID
     * @param needBack
   * @return 凭证号
     */
  List<String> batchSingleInference(List<Long> billIds, BillTypeEnum billTypeEnum, ActionEventEnum actionEventEnum, Long amount, boolean needBack, String supCpUnitId);

    /**
     * 判断单个账单是否符合推凭
     * @param bill
     * @param billTypeEnum
     * @return
     */
    Boolean judgeSingleBillStatus(BillInferenceV bill, BillTypeEnum billTypeEnum, String supCpUnitId);

    /**
     * 根据不同的事件查询不使用不同类型的账单作为推凭
     * @param eventType
     * @param fieldList
     * @param conditions
     * @param record
     * @param isSingle
     */
    void inferenceByBillType(Integer eventType, List<Field> fieldList, String conditions, VoucherRuleE record, boolean isSingle);

    void doAfterInfer();

    /**
     * 批量推送预制凭证
     * @param voucherList
     * @return
     */
    Map<String, Object> inferPrefabricationVoucher(List<VoucherE> voucherList);
}
