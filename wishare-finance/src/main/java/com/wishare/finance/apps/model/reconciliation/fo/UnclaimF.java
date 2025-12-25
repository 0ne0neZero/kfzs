package com.wishare.finance.apps.model.reconciliation.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 对账解除认领信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/25
 */
@Getter
@Setter
@ApiModel("对账解除认领信息")
public class UnclaimF {

    @ApiModelProperty(value = "对账详情id")
    private String reconcileDetailId;

    private String billNo;

    @ApiModelProperty(value = "上级收费单元ID")
    private String supCpUnitId;


    @ApiModelProperty(value = "账单号list信息")
    private List<String> billNoList;

    @ApiModelProperty(value = "对账详情idList")
    private List<String> reconcileDetailIdList;

}
