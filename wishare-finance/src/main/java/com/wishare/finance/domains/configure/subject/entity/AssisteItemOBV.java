package com.wishare.finance.domains.configure.subject.entity;

import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 辅助核算项
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/10
 */
@Getter
@Setter
@ApiModel("辅助核算项信息")
public class AssisteItemOBV {

    @ApiModelProperty(value = "辅助核算类型： 1部门，2业务单元，3收支项目， 4业务类型， 5客商")
    @NotNull(message = "辅助核算类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "辅助核算编码")
    @NotBlank(message = "辅助核算编码不能为空")
    private String code;

    @ApiModelProperty(value = "辅助核算名称")
    @NotBlank(message = "辅助核算名称不能为空")
    private String name;

    @ApiModelProperty(value = "辅助核算项编码")
    @NotBlank(message = "辅助核算项编码不能为空")
    private String ascCode;

    @ApiModelProperty(value = "辅助核算项名称")
    @NotBlank(message = "辅助核算项名称不能为空")
    private String ascName;


    public AssisteItemOBV() {
    }

    public AssisteItemOBV(AssisteItemTypeEnum assisteItemType, String code, String name) {
        this.type = assisteItemType.getCode();
        this.ascCode = assisteItemType.getAscCode();
        this.ascName = assisteItemType.getValue();
        this.code = code;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof AssisteItemOBV)) return false;

        AssisteItemOBV that = (AssisteItemOBV) o;

        return new EqualsBuilder().append(getCode(), that.getCode()).append(getAscCode(), that.getAscCode()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getCode()).append(getAscCode()).toHashCode();
    }
}
