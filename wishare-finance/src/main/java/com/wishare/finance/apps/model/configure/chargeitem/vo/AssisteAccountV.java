package com.wishare.finance.apps.model.configure.chargeitem.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xujian
 * @date 2022/12/2
 * @Description:
 */
@Getter
@Setter
@ApiModel("辅助核算Vo")
@EqualsAndHashCode
public class AssisteAccountV {

    @ApiModelProperty("主键id")
    @ExcelIgnore
    private Long id;

    @ApiModelProperty(value = "辅助核算类型")
    private Integer type;

    @ApiModelProperty("辅助核算编码")
    @ExcelProperty(value = "辅助核算编码", index = 0)
    private String asAcCode;

    @ApiModelProperty("辅助核算项目")
    @ExcelProperty(value = "辅助核算项目", index = 1)
    private String asAcItem;

    @ApiModelProperty("辅助核算对象")
    @ExcelProperty(value = "辅助核算对象", index = 2)
    private String asAcTarget;

    @ApiModelProperty("参照部门")
    @ExcelProperty(value = "参照部门", index = 3)
    private String referenceName;

    @ApiModelProperty("输入长度")
    @ExcelProperty(value = "输入长度", index = 4)
    private String enterLength;

    @ApiModelProperty("精度")
    @ExcelProperty(value = "精度", index = 5)
    private String accuracy;

    @ExcelIgnore
    @ApiModelProperty("系统来源：1 收费系统 2合同系统 22 用友ncc")
    private String sysSource;

    @ApiModelProperty("系统来源描述：1 收费系统 2合同系统 22 用友ncc")
    @ExcelProperty(value = "系统来源", index = 6)
    private String sysSourceStr;

    public String getSysSourceStr() {
        if (StringUtils.isNotBlank(this.getSysSource())) {
            SysSourceEnum sysSourceEnum = SysSourceEnum.valueOfByCode(Integer.valueOf(this.getSysSource()));
            if (sysSourceEnum != null) {
                return sysSourceEnum.getDes();
            }
        }
        return sysSourceStr;
    }
}
