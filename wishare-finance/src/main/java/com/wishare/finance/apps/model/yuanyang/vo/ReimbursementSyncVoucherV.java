package com.wishare.finance.apps.model.yuanyang.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 同步报销凭证结果
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/5/30
 */
@Getter
@Setter
@ApiModel("同步报销凭证结果")
public class ReimbursementSyncVoucherV {

    @ApiModelProperty(value = "同步结果", required = true)
    private boolean success;

    @ApiModelProperty(value = "凭证结果信息")
    private List<ReimburseVoucherV> vouchers = new ArrayList<>();

    public ReimbursementSyncVoucherV() {
    }

    public ReimbursementSyncVoucherV addVoucherInfo(String bizTransactionNo, String syncSystemVoucherNo, String voucherNo){
        vouchers.add(new ReimburseVoucherV(bizTransactionNo, syncSystemVoucherNo, voucherNo));
        return this;
    }

    public ReimbursementSyncVoucherV setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public ReimbursementSyncVoucherV setVouchers(List<ReimburseVoucherV> vouchers) {
        this.vouchers = vouchers;
        return this;
    }

    @Getter
    @Setter
    @ApiModel("同步报销凭证结果信息")
    public static class ReimburseVoucherV{

        @ApiModelProperty(value = "业务系统交易单号", required = true)
        private String bizTransactionNo;

        @ApiModelProperty(value = "同步系统凭证编号")
        private String syncSystemVoucherNo;

        @ApiModelProperty(value = "凭证编号")
        private String voucherNo;

        public ReimburseVoucherV() {
        }

        public ReimburseVoucherV(String bizTransactionNo, String syncSystemVoucherNo, String voucherNo) {
            this.bizTransactionNo = bizTransactionNo;
            this.syncSystemVoucherNo = syncSystemVoucherNo;
            this.voucherNo = voucherNo;
        }

        public String getBizTransactionNo() {
            return bizTransactionNo;
        }

        public void setBizTransactionNo(String bizTransactionNo) {
            this.bizTransactionNo = bizTransactionNo;
        }

        public String getSyncSystemVoucherNo() {
            return syncSystemVoucherNo;
        }

        public void setSyncSystemVoucherNo(String syncSystemVoucherNo) {
            this.syncSystemVoucherNo = syncSystemVoucherNo;
        }

        public String getVoucherNo() {
            return voucherNo;
        }

        public void setVoucherNo(String voucherNo) {
            this.voucherNo = voucherNo;
        }
    }

}
