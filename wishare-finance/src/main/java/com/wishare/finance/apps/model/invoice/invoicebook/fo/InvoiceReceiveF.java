package com.wishare.finance.apps.model.invoice.invoicebook.fo;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.domains.invoicereceipt.command.invociebook.AddInvoiceReceiveCommand;
import com.wishare.starter.Global;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujian
 * @date 2022/9/20
 * @Description: 申请领用审核
 */
@Getter
@Setter
@ApiModel("票本领用")
public class InvoiceReceiveF {

    @ApiModelProperty(value = "票本id", required = true)
    @NotNull(message = "票本id不能为空")
    private Long invoiceBookId;

    @ApiModelProperty(value = "领用组织id", required = true)
    @NotNull(message = "领用组织id不能为空")
    private Long receiveOrgId;

    @ApiModelProperty(value = "领用组织名称", required = true)
    @NotNull(message = "领用组织名称不能为空")
    private String receiveOrgName;

    @ApiModelProperty(value = "领用日期", required = true)
    @NotNull(message = "领用日期不能为空")
    private LocalDateTime receiveTime;

    @ApiModelProperty(value = "发票起始号码", required = true)
    @NotNull(message = "发票起始号码不能为空")
    private Long invoiceStartNumber;

    @ApiModelProperty(value = "发票结束号码", required = true)
    @NotNull(message = "发票结束号码不能为空")
    private Long invoiceEndNumber;

    @ApiModelProperty(value = "领用数目", required = true)
    @NotNull(message = "领用数目不能为空")
    private Long receiveNumber;

    @ApiModelProperty(value = "领用项目信息",required = true)
    @NotNull(message = "领用项目信息为空")
    @Valid
    private List<ReceiveCommunityF> communityFS;


    /**
     * 构造票本领用command
     *
     * @return
     */
    public AddInvoiceReceiveCommand getInvoiceReceive() {
        AddInvoiceReceiveCommand command = Global.mapperFacade.map(this, AddInvoiceReceiveCommand.class);
        command.setReceiveInvoiceBookId(this.invoiceBookId);
        command.setCommunity(JSON.toJSONString(communityFS));
        return command;
    }
}
