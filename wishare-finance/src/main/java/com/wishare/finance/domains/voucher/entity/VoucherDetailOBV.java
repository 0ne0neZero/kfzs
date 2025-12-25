package com.wishare.finance.domains.voucher.entity;

import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.consts.enums.VoucherLoanTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分录详情
 * @author dxclay
 * @version 1.0
 * @since 2023/3/10
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class VoucherDetailOBV {

    @ApiModelProperty(value = "借贷类型： credit贷方， debit借方")
    private String type;

    @ApiModelProperty(value = "科目id")
    private Long subjectId;

    @ApiModelProperty(value = "科目编码")
    private String subjectCode;

    @ApiModelProperty(value = "科目名称")
    private String subjectName;

    @ApiModelProperty(value = "原币金额（单位：分）")
    private Long originalAmount;

    @ApiModelProperty(value = "含税金额（单位：分）")
    private Long incTaxAmount;

    @ApiModelProperty(value = "贷方金额（单位：分）")
    private Long creditAmount = 0L;

    @ApiModelProperty(value = "借方金额（单位：分）")
    private Long debitAmount = 0L;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项编码")
    private String chargeItemCode;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "现金流量项")
    private List<CashFlowOBV> cashFlows;

    @ApiModelProperty(value = "辅助核算项")
    private List<AssisteItemOBV> assisteItems;

    @ApiModelProperty(value = "现金流量id")
    private Long cashFlowId;

    /**
     * 设置借贷方金额
     * @param amount
     */
    public void putAmount(long amount){
        this.originalAmount = amount;
        //要取实际支付金额
        //this.incTaxAmount = amount;
        if (VoucherLoanTypeEnum.借方.equalsByCode(this.type)) {
            this.debitAmount = amount;
        } else {
            this.creditAmount = amount;
        }
    }

    public String findAssisteItemString() {
        if (CollectionUtils.isEmpty(assisteItems)) {
            return "";
        } else {
            assisteItems.sort(Comparator.comparing(AssisteItemOBV::getAscName));
            return assisteItems.stream().map(item -> item.getAscName() + item.getCode()).collect(Collectors.joining());
        }
    }

    /**
     * 比较辅助核算项是否一致
     * @param assisteItems 另一组辅助核算项
     * @return
     */
    public boolean hasSameAssisteItems(List<AssisteItemOBV> assisteItems) {
        if (CollectionUtils.isNotEmpty(this.assisteItems) && CollectionUtils.isNotEmpty(assisteItems)) {
            if (assisteItems.size() == this.assisteItems.size()) {
                return new HashSet<>(this.assisteItems).containsAll(assisteItems);
            }
        }
        return false;
    }

    public String getCashFlowCode() {
        String code = "";
        if (CollectionUtils.isNotEmpty(cashFlows)) {
            CashFlowOBV cashFlowOBV = cashFlows.get(0);
            code = cashFlowOBV.getCode();
        }
        return code;
    }



    public boolean doesTheAmountExist(Long Amount) {
        return Amount != 0;
    }
}
