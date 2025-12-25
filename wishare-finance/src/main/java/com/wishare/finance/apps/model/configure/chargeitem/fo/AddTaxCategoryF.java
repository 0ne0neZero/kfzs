package com.wishare.finance.apps.model.configure.chargeitem.fo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wishare.finance.domains.configure.chargeitem.command.tax.AddTaxCategoryCommand;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxCategoryE;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.helpers.UidHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("新增税种信息")
public class AddTaxCategoryF {

    @ApiModelProperty("税种编码")
    private String code;

    @ApiModelProperty("税种名称")
    private String name;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("父税种id,默认根id为0")
    private Long parentId;

    @ApiModelProperty("税种id路径")
    private String path;

    /**
     * 构造新增税种command
     *
     * @param taxCategoryEParent
     * @param identityInfo
     * @return
     */
    public AddTaxCategoryCommand getAddTaxCategoryCommand(TaxCategoryE taxCategoryEParent, IdentityInfo identityInfo) {
        AddTaxCategoryCommand addTaxCategoryCommand = Global.mapperFacade.map(this, AddTaxCategoryCommand.class);
        Long taxCategoryId = IdentifierFactory.getInstance().generateLongIdentifier("taxCategoryId");
        addTaxCategoryCommand.setId(taxCategoryId);
        addTaxCategoryCommand.setPath(handlePath(taxCategoryId, taxCategoryEParent));
        addTaxCategoryCommand.setParentId(null == parentId ? 0 : parentId);
        addTaxCategoryCommand.setDisabled(this.disabled);
        addTaxCategoryCommand.setDeleted(DataDeletedEnum.NORMAL.getCode());
        addTaxCategoryCommand.setTenantId(identityInfo.getTenantId());
        addTaxCategoryCommand.setCreator(identityInfo.getUserId());
        addTaxCategoryCommand.setCreatorName(identityInfo.getUserName());
        addTaxCategoryCommand.setGmtCreate(LocalDateTime.now());
        addTaxCategoryCommand.setOperator(identityInfo.getUserId());
        addTaxCategoryCommand.setOperatorName(identityInfo.getUserName());
        addTaxCategoryCommand.setGmtModify(LocalDateTime.now());
        return addTaxCategoryCommand;
    }

    /**
     * 处理路径
     *
     * @param taxCategoryId
     * @param taxCategoryEParent
     * @return
     */
    private String handlePath(Long taxCategoryId, TaxCategoryE taxCategoryEParent) {
        if (this.parentId == null || this.getParentId() == 0L) {
            return JSON.toJSONString(Lists.newArrayList(taxCategoryId));
        } else {
            List<Long> paths = JSON.parseArray(taxCategoryEParent.getPath(), Long.class);
            paths.add(taxCategoryId);
            return JSON.toJSONString(paths);
        }
    }


}
