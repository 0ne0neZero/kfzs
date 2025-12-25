package com.wishare.contract.apps.fo.revision.pay;

import com.wishare.contract.domains.vo.settle.PayConcludePlanV2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @author longhuadmin
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel("结算计划V2-新增或编辑的请求信息")
public class SettlementPlanEditV {

    @ApiModelProperty("合同id")
    @NotBlank(message = "合同ID不能为空")
    private String contractId;

    @ApiModelProperty("结算计划")
    @NotNull(message = "结算计划不能为空")
    private List<List<PayConcludePlanV2>> planV2List;

}
