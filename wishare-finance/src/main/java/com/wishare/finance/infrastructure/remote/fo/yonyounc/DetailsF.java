package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/5
 * @Description:
 */
@Getter
@Setter
@ApiModel("分录")
public class DetailsF {

    @ApiModelProperty("分录号")
    private String detailindex;

    @ApiModelProperty("摘要 非空")
    @NotBlank(message = "分录摘要不能为空")
    private String explanation;

    @ApiModelProperty("业务日期 可空")
    private String verifydate;

    @ApiModelProperty("单价 可空")
    private String price;

    @ApiModelProperty("折本汇率 可空")
    private String excrate2;

    @ApiModelProperty("借方数量 可空")
    private String debitquantity;

    @ApiModelProperty("原币借方金额 可空")
    private String debitamount;

    @ApiModelProperty("本币借方金额 可空")
    private String localdebitamount;

    @ApiModelProperty("集团本币借方金额 可空")
    private String groupdebitamount;

    @ApiModelProperty("全局本币借方金额 可空")
    private String globaldebitamount;

    @ApiModelProperty(value = "币种 非空",required = true)
    @NotBlank(message = "分录币种不能为空")
    private String pk_currtype;

    @ApiModelProperty(value = "科目 非空",required = true)
    @NotBlank(message = "分录科目不能为空")
    private String pk_accasoa;

    @ApiModelProperty("所属二级核算单位 可空 （组织）")
    private String pk_unit;

    @ApiModelProperty("所属二级核算单位 版本可空 （组织）")
    private String pk_unit_v;

    @ApiModelProperty("欧盟vat导入")
    private VatdetailF vatdetail;

    @ApiModelProperty("贷方数量 可空")
    private String creditquantity;

    @ApiModelProperty("原币贷方金额 可空")
    private String creditamount;

    @ApiModelProperty("本币贷方金额 可空")
    private String localcreditamount;

    @ApiModelProperty("集团本币贷方金额 可空")
    private String groupcreditamount;

    @ApiModelProperty("全局本币贷方金额 可空")
    private String globalcreditamount;

    @ApiModelProperty("辅助核算")
    private List<AssF> ass;

    @ApiModelProperty("现金流量")
    private List<CashFlowF> cashFlow;


}
