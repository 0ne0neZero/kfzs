package com.wishare.finance.domains.bill.event;

import com.wishare.finance.infrastructure.event.DispatcherType;
import com.wishare.finance.infrastructure.event.Event;
import com.wishare.finance.infrastructure.event.StreamEventDispatcher;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

/**
 * 账单行为事件
 *
 * @Author dxclay
 * @Date 2022/11/3
 * @Version 1.0
 */
@Getter
@Setter
@Event(dispatcher = StreamEventDispatcher.class, dispatcherType = DispatcherType.STREAM, name = "BILL_ACTION_OUTPUT")
public class BillBatchActionEvent<D> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账单行为
     */
    private BillAction action;

    /**
     * 账单id
     */
    private List<Long> billIds;

    /**
     * 账单类型
     */
    private Integer billType;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 上级收费单元ID
     */
    private String supCpUnitId;

    /**
     * 行为详细信息
     */
    private D detail;

    public static BillEventBuilder builder(BillAction action){
        return new BillEventBuilder(action);
    }

    public BillBatchActionEvent() {
    }

    private BillBatchActionEvent(BillAction action, List<Long> billIds, Integer billType, D detail , String tenantId, String supCpUnitId) {
        this.action = action;
        this.billIds = billIds;
        this.billType = billType;
        this.detail = detail;
        this.tenantId = tenantId;
        this.supCpUnitId = supCpUnitId;
    }

    /**
     * 新建
     * @param detail
     * @return
     */
    public static  <D> BillBatchActionEvent<D> create(List<Long> billIds, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.CREATED_BATCH, billIds, billType, detail, supCpUnitId);
    }

    /**
     * 调整
     * @param billIds
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    public static  <D> BillBatchActionEvent<D> adjust(List<Long> billIds, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.ADJUSTED_BATCH, billIds, billType, detail, supCpUnitId);
    }

    /**
     * 冲销
     * @param billIds
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    public static  <D> BillBatchActionEvent<D> reverse(List<Long> billIds, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.REVERSED_BATCH, billIds, billType, detail, supCpUnitId);
    }

    /**
     * 退款
     * @param billIds
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    public static  <D> BillBatchActionEvent<D> refund(List<Long> billIds, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.REFUND_BATCH, billIds, billType, detail, supCpUnitId);
    }

    /**
     * 作废
     * @param billIds
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    public static  <D> BillBatchActionEvent<D> invalid(List<Long> billIds, Integer billType, D detail, String tenantId, String supCpUnitId){
        return new BillBatchActionEvent<>(BillAction.INVALIDED_BATCH, billIds, billType, detail, tenantId, supCpUnitId);
    }

    /**
     * 开票
     * @param billIds
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    public static  <D> BillBatchActionEvent<D> invoiced(List<Long> billIds, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.INVOICED_BATCH, billIds, billType, detail, supCpUnitId);
    }

    /**
     * 结算
     * @param billIds
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    public static  <D> BillBatchActionEvent<D> settle(List<Long> billIds, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.SETTLED_BATCH, billIds, billType, detail, supCpUnitId);
    }

    /**
     * 计提
     * @param billIds
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    public static  <D> BillBatchActionEvent<D> approve(List<Long> billIds, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.APPROVED_BATCH, billIds, billType, detail, supCpUnitId);
    }

    /**
     * 获取事件信息
     * @param action
     * @param billIds
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    private static  <D> BillBatchActionEvent<D> getEvent(BillAction action, List<Long> billIds, Integer billType, D detail, String supCpUnitId){
        return BillBatchActionEvent.builder(action)
                .billIds(billIds)
                .billType(billType)
                .detail(detail)
                .tenantId(getRequestTenant())
                .supCpUnitId(supCpUnitId)
                .build();
    }

    /**
     * 获取请求租户
     * @return
     */
    private static String getRequestTenant(){
        return Optional.ofNullable(ThreadLocalUtil.curIdentityInfo()).map(IdentityInfo::getTenantId).get();
    }

    /**
     * 账单事件建造者
     * @param <D>
     */
    public static class BillEventBuilder<D>{
        private BillAction action;
        private List<Long> billIds;
        private Integer billType;
        private String tenantId;

        private String supCpUnitId;
        private D detail;

        private BillEventBuilder(BillAction action) {
            this.action = action;
        }

        public BillEventBuilder billIds(List<Long> billIds) {
            this.billIds = billIds;
            return this;
        }

        public BillEventBuilder billType(Integer billType) {
            this.billType = billType;
            return this;
        }

        public BillEventBuilder tenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public BillEventBuilder supCpUnitId(String supCpUnitId) {
            this.supCpUnitId = supCpUnitId;
            return this;
        }

        public BillEventBuilder detail(D detail) {
            this.detail = detail;
            return this;
        }

        public BillBatchActionEvent<D> build(){
            return new BillBatchActionEvent(action, billIds, billType, detail, tenantId, supCpUnitId);
        }
    }
    
}
