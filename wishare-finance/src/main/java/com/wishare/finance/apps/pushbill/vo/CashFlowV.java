package com.wishare.finance.apps.pushbill.vo;

import com.wishare.finance.domains.mdm.entity.Mdm11E;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 现金流量
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@Data
@ApiModel(value="支付申请单-现金流量")
public class CashFlowV implements Serializable {

    @ApiModelProperty(value = "ID")
    private String ID;
    @ApiModelProperty(value = "编号")
    private String CODE;
    @ApiModelProperty(value = "中文简体名称")
    private String NAME_CHS;
    @ApiModelProperty(value = "中文全称")
    private String FULL_NAME_CHS;


    public static CashFlowV transferByMdm11(Mdm11E mdm11E){
        CashFlowV cashFlowV = new CashFlowV();
        cashFlowV.setID(mdm11E.getId());
        cashFlowV.setNAME_CHS(mdm11E.getCusitemNameChs());
        cashFlowV.setFULL_NAME_CHS(mdm11E.getCusitemFullnameChs());
        return cashFlowV;
    }
}
