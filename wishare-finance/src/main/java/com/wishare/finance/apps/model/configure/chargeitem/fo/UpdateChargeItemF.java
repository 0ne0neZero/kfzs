package com.wishare.finance.apps.model.configure.chargeitem.fo;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.domains.configure.chargeitem.command.chargeitem.ChargeItemBusinessCommand;
import com.wishare.finance.domains.configure.chargeitem.command.chargeitem.UpdateChargeItemCommand;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 更新费项入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("更新费项数据")
public class UpdateChargeItemF {

    @ApiModelProperty(value = "费项id", required = true)
    @NotNull(message = "费项id不能为空")
    private Long id;

    @ApiModelProperty(value = "父费项id")
    private Long parentId;

    @Size(max = 20,message = "费项编码长度不能超过20位")
    @ApiModelProperty("费项编码")
    private String code;

    @ApiModelProperty("费项名称")
    private String name;

    @ApiModelProperty(value = "费项类型")
    private Integer type;

    @ApiModelProperty(value = "费项属性 1收入,2支出，3代收代付及其他")
    private Integer attribute;

    @ApiModelProperty(value = "是否末级：0否,1是")
    private Integer lastLevel;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("是否启用暂估收人:0未启用，1启用")
    private Integer estimated;

    @ApiModelProperty("税率")
    private String taxRate;

    @ApiModelProperty("税率id")
    private Long taxRateId;

    @ApiModelProperty(value = "业务标识")
    private String businessFlag;

    @ApiModelProperty(value = "分成费项编码id")
    private String shareChargeId;

    @ApiModelProperty(value = "是否校验唯一性,1:关闭唯一性校验,其他:开启唯一性校验")
    private Integer isUnique;

    @ApiModelProperty(value = "是否为违约金 0 否 1 是")
    private Integer isOverdue;

    @ApiModelProperty(value = "业务板块编码")
    private String businessSegmentCode;

    @ApiModelProperty(value = "业务板块名称")
    private String businessSegmentName;

    @ApiModelProperty(value = "关联的税率信息")
    private List<TaxRateInfoF> taxRateInfos;

    @ApiModelProperty(value = "业务类型数据信息")
    private List<ChargeItemBusinessCommand> businessCommands;
    public UpdateChargeItemCommand getUpdateChargeItemCommand(IdentityInfo identityInfo){
        UpdateChargeItemCommand command = Global.mapperFacade.map(this, UpdateChargeItemCommand.class);
        command.setOperator(identityInfo.getUserId());
        command.setOperatorName(identityInfo.getUserName());
        command.setGmtModify(LocalDateTime.now());
        command.setTenantId(identityInfo.getTenantId());
        if (CollectionUtils.isNotEmpty(taxRateInfos)) {
            command.setTaxRateInfo(JSON.toJSONString(taxRateInfos));
        }
        return command;
    }
}
