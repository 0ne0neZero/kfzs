package com.wishare.finance.apps.model.configure.accountbook.fo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.businessunit.command.businessunit.CreateBusinessUnitCommand;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @description: 账簿（账套）信息
 * @author: pgq
 * @since: 2022/12/6 19:20
 * @version: 1.0.0
 */
@ApiModel("账簿（账套）信息")
@Getter
@Setter
public class ExternalAccountingbookF {

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String code;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;


    @ApiModelProperty("法人组织代码")
    private String orgcode;


    @ApiModelProperty("法人组织名称")
    private String orgname;

    public AccountBookE getAccountBookECommand(IdentityInfo identityInfo){
        AccountBookE command = Global.mapperFacade.map(this, AccountBookE.class);
        command.setStatutoryBodyId(0l);
        command.setStatutoryBodyIdPath("[0]");
        command.setStatutoryBodyName("");
        command.setVoucherSys(1);
        command.setIsGeneralLedger(0);
        //设置创建人数据
        LocalDateTime now = LocalDateTime.now();
        command.setCreator(identityInfo.getUserId());
        command.setCreatorName(identityInfo.getUserName());
        command.setGmtCreate(now);
        command.setOperator(identityInfo.getUserId());
        command.setOperatorName(identityInfo.getUserName());
        command.setGmtModify(now);
        command.setTenantId(identityInfo.getTenantId());
        command.setId(IdentifierFactory.getInstance().generateLongIdentifier("businessUnit"));
        return command;
    }

}
