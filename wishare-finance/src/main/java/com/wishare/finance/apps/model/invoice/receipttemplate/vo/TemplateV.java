package com.wishare.finance.apps.model.invoice.receipttemplate.vo;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: Linitly
 * @date: 2023/8/8 10:15
 * @descrption:
 * @see com.wishare.finance.apps.model.invoice.receipttemplate.vo.TemplateV
 * @see com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptTemplateE;
 */
@Data
@ApiModel(value = "票据模板")
public class TemplateV {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "模板名称")
    private String templateName;

    @ApiModelProperty(value = "模板类型")
    private Integer templateType;

    @ApiModelProperty(value = "模板样式")
    private Integer templateStyle;

    @ApiModelProperty(value = "启用电子签章：0:不启用;1:启用;")
    private Integer enableElectSign;

    @ApiModelProperty(value = "电子签章类型：1:系统默认;2:手动上传;")
    private Integer electSignType;

    @ApiModelProperty(value = "签章图片地址")
    private FileVo signPictureUrl;

    @ApiModelProperty(value = "是否启用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @ApiModelProperty(value = "是否删除:0未删除，1已删除")
    private Integer deleted;

    @ApiModelProperty(value = "创建人ID")
    private String creator;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人ID")
    private String operator;

    @ApiModelProperty(value = "修改人姓名")
    private String operatorName;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;
}
