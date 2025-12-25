package com.wishare.finance.domains.bill.dto;

import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@ApiModel("流水认领账单")
public class FlowBillPageDto {
    private  Long billId;


    private String statutoryBodyName;


    private String gatherDetailIds;

    /**
     * 费项名称
     */
    private String chargeItemName;


    /**
     * 上级收费单元名称
     */
    private String supCpUnitName;

    /**
     * 收费单元名称
     */
    private String cpUnitName;


    /**
     * 账单编号
     */
    private String billNo;


    /**
     * 账单金额
     */
    private Long totalAmount = 0L;

    /**
     * 缴费时间
     */
    private LocalDateTime payTime;

    /**
     * 收费对象名称
     */
    private String customerName;

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("结算方式")
    private String payChannel;

    @ApiModelProperty("结算方式名称")
    private String payChannelName;

    /**
     * 成本中心id
     */
    private Long costCenterId;

    /**
     * 成本中心名称
     */
    private String costCenterName;

    private Integer billType;

    /**
     * 系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统
     */
    private Integer sysSource;
    public String getPayChannelName(){
        if (this.payChannel!=null){
            return SettleChannelEnum.valueNameOfByCode(payChannel);
        }
        return "";
    }

}
