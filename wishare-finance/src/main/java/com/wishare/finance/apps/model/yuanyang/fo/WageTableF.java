package com.wishare.finance.apps.model.yuanyang.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**工资子表
 * @author szh
 * @date 2024/7/18 15:39
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WageTableF {


    /**
     * 业务类型编码
     */
    @Length(max= 64,message="业务类型编码长度不能超过64")
    @ApiModelProperty("业务类型编码")
    private String businessTypeCode;
    /**
     * 业务类型名称
     */
    @Length(max= 100,message="业务类型名称长度不能超过100")
    @ApiModelProperty("业务类型名称")
    private String businessTypeName;

    /**
     * 成本中心编码
     */
    @Length(max= 64,message="核算成本中心编码长度不能超过64")
    @ApiModelProperty("核算成本中心编码")
    private String costCenterCode;
    /**
     * 成本中心名称
     */
    @Length(max= 100,message="核算成本中心名称长度不能超过100")
    @ApiModelProperty("核算成本中心名称")
    private String costCenterName;

    /**
     * 部门编码
     */
    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("部门编码")
    private String orgCode;
    /**
     * 部门名称
     */
    @Length(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("部门名称")
    private String orgName;


    @ApiModelProperty(value = "税率(%)")
    private String taxRate;
    @ApiModelProperty(value = "应发工资")
    private String wagesPayable;


    @ApiModelProperty(value = "扣缴养老")
    private String withholdingPension;
    @ApiModelProperty(value = "扣缴失业")
    private String unemployment;
    @ApiModelProperty(value = "扣缴医疗")
    private String medical;
    @ApiModelProperty(value = "扣缴住房")
    private String lodging;
    @ApiModelProperty(value = "扣缴其它")
    private String withholdOther;
    @ApiModelProperty(value = "代扣个税")
    private String personalIncomeTax;
    @ApiModelProperty(value = "个税补退")
    private String incomeTaxRefund;
    @ApiModelProperty(value = "扣缴合计")
    private String withholdingTotal;
    @ApiModelProperty(value = "代扣工会会费")
    private String unionDues;

    @ApiModelProperty(value = "实发工资")
    private String takeHomePay;
    @ApiModelProperty(value = "激励合计")
    private String excitationTotal;

}
