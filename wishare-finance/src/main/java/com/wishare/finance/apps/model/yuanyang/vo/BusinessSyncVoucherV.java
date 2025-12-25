package com.wishare.finance.apps.model.yuanyang.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BusinessSyncVoucherV {

    @ApiModelProperty(value = "同步结果")
    private boolean success;

    @ApiModelProperty(value = "业务系统交易单号")
    private String bizTransactionNo;

    @ApiModelProperty(value = "凭证结果信息")
    private List<BusinessVoucherV> vouchers = new ArrayList<>();

    public BusinessSyncVoucherV() {
    }

    public BusinessSyncVoucherV setBizTransactionNo(String bizTransactionNo) {
        this.bizTransactionNo = bizTransactionNo;
        return this;
    }

    public BusinessSyncVoucherV addVoucherInfo(List<String> payIds, String syncSystemVoucherNo, String voucherNo){
        vouchers.add(new BusinessVoucherV(payIds, syncSystemVoucherNo, voucherNo));
        return this;
    }

    public BusinessSyncVoucherV setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public BusinessSyncVoucherV setVouchers(List<BusinessVoucherV> vouchers) {
        this.vouchers = vouchers;
        return this;
    }

    @Getter
    @Setter
    @ApiModel("同步凭证结果信息")
    public static class BusinessVoucherV {

        @ApiModelProperty(value = "同步系统凭证编号")
        private String syncSystemVoucherNo;
        @ApiModelProperty(value = "中台凭证编号")
        private String voucherNo;

        @ApiModelProperty(value = "支付id")
        private List<String> payIds;

        public BusinessVoucherV() {
        }

        public BusinessVoucherV(List<String> payIds, String syncSystemVoucherNo, String voucherNo) {
            this.payIds = payIds;
            this.syncSystemVoucherNo = syncSystemVoucherNo;
            this.voucherNo = voucherNo;
        }

    }

}
