package com.wishare.finance.domains.configure.chargeitem.command.chargeitem;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ChargeItemBusinessCommand {
    /**
     * 单据类型id
     */
    @ApiModelProperty(value = "单据类型id")
    private String documentId;

    /**
     * 单据类型编码
     */
    @ApiModelProperty(value = "单据类型编码")
    private String documentCode;
    /**
     * 单据类型名称
     */
    @ApiModelProperty(value = "单据类型名称")
    private String documentName;
    /**
     * 业务类型id
     */
    @ApiModelProperty(value = "业务类型id")
    private List<String> businessTypeId;

    /**
     * 业务类型编码
     */
    @ApiModelProperty(value = "业务类型编码")
    private List<String> BusinessTypeCode;
    /**
     * 业务类型名称
     */
    @ApiModelProperty(value = "业务类型名称")
    private List<String> BusinessTypeName;

    /**
     * 变动名称
     */
    @ApiModelProperty(value = "变动名称")
    private String changeName;

    /**
     * 变动编码
     */
    @ApiModelProperty(value = "变动编码")
    private String changeCode;

    /**
     * 款项id
     */
    @ApiModelProperty(value = "款项id")
    private String paymentId;

    /**
     * 款项名称
     */
    @ApiModelProperty(value = "款项名称")
    private String paymentName;

    /**
     * 款项编码
     */
    @ApiModelProperty(value = "款项编码")
    private String paymentCode;

    /**
     * 实签款项id
     **/
    @ApiModelProperty(value = "V2.14 实签款项id")
    private String signedPaymentId;

    /**
     * 实签-款项名称-编码
     **/
    @ApiModelProperty(value = "V2.14 实签-款项名称-编码")
    private String signedPaymentCode;

    /**
     * 对下结算单-结算-款项名称-名称
     **/
    @ApiModelProperty(value = "V2.14 实签-款项名称-名称")
    private String signedPaymentName;

    /**
     * 成本科目口径-id
     **/
    @ApiModelProperty(value = "V2.14-2 成本科目口径-id")
    private String costPaymentId;

    /**
     * 成本科目口径-编码
     **/
    @ApiModelProperty(value = "V2.14-2 成本科目口径-编码")
    private String costPaymentCode;

    /**
     * 成本科目口径-款项名称-名称
     **/
    @ApiModelProperty(value = "V2.14-2 实签-款项名称-名称")
    private String costPaymentName;
}
