package com.wishare.finance.apps.model.invoice.receipttemplate.fo;

import com.wishare.finance.apps.pushbill.validation.AddGroup;
import com.wishare.finance.apps.pushbill.validation.UpdateGroup;
import com.wishare.finance.domains.invoicereceipt.consts.enums.ElectSignTypeEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.EnableElectSignEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.ReceiptTemplateStyleEnum;
import com.wishare.finance.domains.report.enums.InvoiceLineEnum;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author: Linitly
 * @date: 2023/8/7 20:43
 * @descrption:
 */
@Data
@ApiModel(value = "票据模板")
public class TemplateF {

    @ApiModelProperty(value = "id")
    @NotNull(message = "模板ID不能为空", groups = {UpdateGroup.class})
    private Long id;

    @ApiModelProperty(value = "模板名称")
    @NotBlank(message = "模板名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String templateName;

    @ApiModelProperty(value = "模板类型：6:电子收据;")
    @NotNull(message = "模板类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer templateType;

    @ApiModelProperty(value = "模板样式")
    @NotNull(message = "模板样式不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer templateStyle;

    @ApiModelProperty(value = "启用电子签章：0:不启用;1:启用;")
    @NotNull(message = "启用电子签章标识不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer enableElectSign;

    @ApiModelProperty(value = "电子签章类型：1:系统默认;2:手动上传;")
    private Integer electSignType;

    @ApiModelProperty(value = "签章图片地址")
    private FileVo signPictureUrl;

    public void check() {
        this.checkType();
        this.checkStyle();
        this.checkElectSign();
    }

    private void checkStyle() {
        ReceiptTemplateStyleEnum styleEnum = ReceiptTemplateStyleEnum.valueOfCode(this.templateStyle);
        if (Objects.isNull(styleEnum)) {
            throw BizException.throw400("模板样式异常");
        }
    }

    private void checkType() {
        InvoiceLineEnum invoiceLineEnum = InvoiceLineEnum.valueOfByCode(this.templateType);
        // 目前只支持电子收据，后续可能支持其他类型
        if (Objects.isNull(invoiceLineEnum) || invoiceLineEnum != InvoiceLineEnum.电子收据) {
            throw BizException.throw400("模板类型异常");
        }
    }

    /**
     * 校验电子签章信息是否传入
     */
    private void checkElectSign() {
        if (!EnableElectSignEnum.启用.getCode().equals(this.enableElectSign)) {
            return;
        }
        ElectSignTypeEnum signTypeEnum = ElectSignTypeEnum.valueOfCode(this.electSignType);
        if (Objects.isNull(signTypeEnum)) {
            throw BizException.throw400("电子签章类型异常");
        }
        if (!(ElectSignTypeEnum.手动上传 == signTypeEnum)) {
            return;
        }
        if (Objects.isNull(this.signPictureUrl)) {
            throw BizException.throw400("电子签章图片不能为空");
        }
    }
}
