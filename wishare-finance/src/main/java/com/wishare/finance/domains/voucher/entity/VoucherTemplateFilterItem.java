package com.wishare.finance.domains.voucher.entity;

import com.wishare.finance.domains.voucher.consts.enums.FilterTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.LogicTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author szh
 * @date 2024/4/18 16:01
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherTemplateFilterItem {
    /**
     * {@linkplain FilterTypeEnum}
     */
    @ApiModelProperty("过滤类型org-部门，taxRate-税率")
    private FilterTypeEnum filterType;

    /**
     * {@linkplain LogicTypeEnum}
     */
    @ApiModelProperty("包含contain，不包含noContain，等于equal")
    private LogicTypeEnum logicType;

    /**
     * 部门编码
     */
    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("部门编码")
    private List<String> deptCode;
    /**
     * 部门名称
     */
    @Length(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("部门名称")
    private List<String> deptName;


    /**
     * 税率编码
     */
    @Length(max= 64,message="税率编码长度不能超过64")
    @ApiModelProperty("税率编码")
    private List<String> taxRateCode;
    /**
     * 税率
     */
    @ApiModelProperty("税率")
    private List<BigDecimal> taxRate;
}
