package com.wishare.finance.domains.bill.event;

import com.wishare.finance.infrastructure.event.DispatcherType;
import com.wishare.finance.infrastructure.event.Event;
import com.wishare.finance.infrastructure.event.StreamEventDispatcher;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Optional;

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
public class BillActionEvent<D> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账单行为
     */
    private BillAction action;

    /**
     * 账单id
     */
    private Long billId;

    /**
     * 收款单id
     */
    private Long gatherBillId;

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
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 行为详细信息
     */
    private D detail;

    public static BillEventBuilder builder(BillAction action){
        return new BillEventBuilder(action);
    }

    public BillActionEvent() {
    }

    private BillActionEvent(BillAction action, Long billId, Integer billType, D detail , String tenantId) {
        this.action = action;
        this.billId = billId;
        this.billType = billType;
        this.detail = detail;
        this.tenantId = tenantId;
    }

    private BillActionEvent(BillAction action, Long billId, Long gatherBillId, Integer billType, D detail , String tenantId, String supCpUnitId) {
        this.action = action;
        this.billId = billId;
        this.gatherBillId = gatherBillId;
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
    public static  <D> BillActionEvent<D> create(Long billId, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.CREATED, billId, billType, detail, supCpUnitId);
    }

    /**
     * 调整
     * @param billId
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    public static  <D> BillActionEvent<D> adjust(Long billId, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.ADJUSTED, billId, billType, detail, supCpUnitId);
    }

    /**
     * 冲销
     * @param billId
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    public static  <D> BillActionEvent<D> reverse(Long billId, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.REVERSED, billId, billType, detail, supCpUnitId);
    }

    /**
     * 收款单冲销
     * @param gatherBillId
     * @param supCpUnitId
     * @return
     * @param <D>
     */
    public static <D> BillActionEvent<D> gatherBillReverse(Long gatherBillId, String supCpUnitId){
        return getEvent(BillAction.GATHER_BILL_REVERSED, null, gatherBillId, null, null, supCpUnitId);
    }

    /**
     * 退款
     * @param billId
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    public static  <D> BillActionEvent<D> refund(Long billId, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.REFUND, billId, billType, detail, supCpUnitId);
    }

    /**
     * 作废
     * @param billId
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    public static  <D> BillActionEvent<D> invalid(Long billId, Integer billType, D detail, String tenantId){
        return new BillActionEvent<>(BillAction.INVALIDED, billId, billType, detail, tenantId);
    }

    /**
     * 开票
     * @param billId
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    public static  <D> BillActionEvent<D> invoiced(Long billId, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.INVOICED, billId, billType, detail, supCpUnitId);
    }

    /**
     * 结算
     * @param billId
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    public static  <D> BillActionEvent<D> settle(Long billId, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.SETTLED, billId, billType, detail, supCpUnitId);
    }

    /**
     * 审核(计提)
     * @param billId
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    public static  <D> BillActionEvent<D> approved(Long billId, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.APPROVED, billId, billType, detail, supCpUnitId);
    }

    /**
     * 审核(计提)
     * @param billId
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    public static  <D> BillActionEvent<D> reverseApproved(Long billId, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.REVERSE_APPROVED, billId, billType, detail, supCpUnitId);
    }


    /**
     * 流水认领
     * @param billId
     * @param billType
     * @param detail
     * @param <D>
     * @return
     */
    public static  <D> BillActionEvent<D> flowClaim(Long billId, Integer billType, D detail, String supCpUnitId){
        return getEvent(BillAction.FLOW_CLAIM, billId, billType, detail, supCpUnitId);
    }


    /**
     * 获取事件信息
     * @param action
     * @param billId
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    private static  <D> BillActionEvent<D> getEvent(BillAction action, Long billId, Integer billType, D detail, String supCpUnitId){

        return BillActionEvent.builder(action)
                .billId(billId)
                .billType(billType)
                .detail(detail)
                .eventId(IdentifierFactory.generateNSUUID())
                .tenantId(getRequestTenant())
                .supCpUnitId(supCpUnitId)
                .build();
    }

    /**
     * 获取事件信息
     * @param action
     * @param billId
     * @param billType
     * @param detail
     * @return
     * @param <D>
     */
    private static  <D> BillActionEvent<D> getEvent(BillAction action, Long billId, Long gatherBillId, Integer billType, D detail, String supCpUnitId){

        return BillActionEvent.builder(action)
                .billId(billId)
                .gatherBillId(gatherBillId)
                .billType(billType)
                .detail(detail)
                .eventId(IdentifierFactory.generateNSUUID())
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


    public String getUniqueMessageKey() {
        if (BillAction.GATHER_BILL_REVERSED == action) {
            return String.valueOf(timestamp) + gatherBillId;
        }
        return String.valueOf(timestamp) + billId + billType;
    }

    /**
     * 账单事件建造者
     * @param <D>
     */
    public static class BillEventBuilder<D>{
        private BillAction action;
        private Long billId;
        private Long gatherBillId;
        private Integer billType;
        private String tenantId;

        private String supCpUnitId;
        private String eventId;
        private D detail;

        private BillEventBuilder(BillAction action) {
            this.action = action;
        }

        public BillEventBuilder eventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public BillEventBuilder billId(Long billId) {
            this.billId = billId;
            return this;
        }

        public BillEventBuilder gatherBillId(Long gatherBillId) {
            this.gatherBillId = gatherBillId;
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

        public BillActionEvent<D> build(){
            return new BillActionEvent(action, billId, gatherBillId, billType, detail, tenantId, supCpUnitId);
        }
    }
    
}
