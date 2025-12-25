package com.wishare.finance.apps.model.invoice.invoicebook.fo;

import cn.hutool.core.util.NumberUtil;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.invoicereceipt.command.invociebook.AddInvoiceBookCommand;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceBookStateEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceTypeEnum;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.UidHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author xujian
 * @date 2022/8/31
 * @Description:
 */
@Getter
@Setter
@ApiModel("新增票本入参")
public class AddInvoiceBookF {

    @ApiModelProperty("票本编号")
    private String invoiceBookNumber;

    @ApiModelProperty(value = "票据类型:1增值税普通发票，2增值税专用发票，3纸质收据", required = true)
    @NotNull(message = "票据类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "购入组织id", required = true)
    @NotNull(message = "购入组织id不能为空")
    private Long buyOrgId;

    @ApiModelProperty(value = "购入组织名称", required = true)
    @NotBlank(message = "购入组织名称不能为空")
    private String buyOrgName;

    @ApiModelProperty(value = "购入数量", required = true)
    @NotNull(message = "购入数量不能为空")
    @Min(value = 0)
    private Long buyQuantity;

    @ApiModelProperty(value = "购入日期", required = true)
    @NotNull(message = "购入日期不能为空")
    private LocalDate buyDate;

    @ApiModelProperty("票面金额(单位：分)")
    private Long denomination;

    @ApiModelProperty(value = "发票代码")
    private String invoiceCode;

    @ApiModelProperty(value = "票本起始号码", required = true)
    @NotNull(message = "票本起始号码不能为空")
    private String invoiceStartNumber;

    @ApiModelProperty(value = "票本结束号码", required = true)
    @NotNull(message = "票本结束号码不能为空")
    private String invoiceEndNumber;

    /**
     * 参数校检
     */
    public void check() {
        checkInvoiceCode();
    }

    /**
     * 校检发票代码
     */
    private void checkInvoiceCode() {
        switch (InvoiceLineEnum.valueOfByCode(this.getType())) {
            case 增值税普通发票:
            case 增值税专用发票:
                if (StringUtils.isBlank(this.getInvoiceCode())) {
                    throw BizException.throw400("发票代码不能为空");
                }
                break;
            case 纸质收据:
                break;
        }
    }

    public AddInvoiceBookCommand getAddInvoiceBookCommand(String englishName) {
        AddInvoiceBookCommand command = Global.mapperFacade.map(this, AddInvoiceBookCommand.class);
        command.setId(IdentifierFactory.getInstance().generateLongIdentifier("invoice_book_id"));
        String convertInvoiceBookNumber = StringUtils.isBlank(this.getInvoiceBookNumber()) ? convertInvoiceBookNumber(englishName) : this.getInvoiceBookNumber();
        command.setInvoiceBookNumber(convertInvoiceBookNumber);
        command.setState(InvoiceBookStateEnum.未领用.getCode());
        checkInvoiceBookEndNumber(NumberUtil.parseLong(this.getInvoiceStartNumber()), this.buyQuantity, NumberUtil.parseLong(this.getInvoiceEndNumber()));
        return command;
    }

    /**
     * 校检票本结束号码
     *
     * @param bookStartNumber
     * @param buyQuantity
     * @param bookEndNumber   票本起始号码+票据数目-1
     */
    private void checkInvoiceBookEndNumber(Long bookStartNumber, Long buyQuantity, Long bookEndNumber) {
        if (bookEndNumber != (bookStartNumber + buyQuantity - 1)) {
            throw BizException.throw400("发票结束号码异常");
        }
    }


    /**
     * 构建票本编号
     *
     * @return
     */
    private String convertInvoiceBookNumber(String englishName) {
        //String invoiceBookNumber = UidHelper.nextIdByDTStr(, 6);
        //String InvoiceBookNumber = englishName +  + invoiceBookNumber;
        return IdentifierFactory.getInstance().serialNumber("InvoiceBookNumber", "PB", 20);
    }



}
