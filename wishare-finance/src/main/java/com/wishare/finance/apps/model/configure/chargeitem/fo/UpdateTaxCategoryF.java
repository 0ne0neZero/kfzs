package com.wishare.finance.apps.model.configure.chargeitem.fo;

import com.wishare.finance.domains.configure.chargeitem.command.tax.UpdateTaxCategoryCommand;
import com.wishare.starter.beans.IdentityInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("修改税种信息")
public class UpdateTaxCategoryF {

    @ApiModelProperty("税种id")
    @NotNull(message = "税种id不能为空")
    private Long id;

    @ApiModelProperty("税种编码")
    private String code;

    @ApiModelProperty("税种名称")
    private String name;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    /**
     * 修改税种信息
     *
     * @return
     */
    public UpdateTaxCategoryCommand update(IdentityInfo identityInfo) {
        UpdateTaxCategoryCommand updateTaxCategoryCommand = new UpdateTaxCategoryCommand();
        updateTaxCategoryCommand.setId(this.id);
        updateTaxCategoryCommand.setCode(this.getCode());
        updateTaxCategoryCommand.setName(this.getName());
        updateTaxCategoryCommand.setDisabled(this.disabled);
        updateTaxCategoryCommand.setOperator(identityInfo.getUserId());
        updateTaxCategoryCommand.setOperatorName(identityInfo.getUserName());
        updateTaxCategoryCommand.setGmtModify(LocalDateTime.now());
        return updateTaxCategoryCommand;
    }
}
