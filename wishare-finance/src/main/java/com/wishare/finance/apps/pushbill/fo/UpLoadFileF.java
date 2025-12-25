package com.wishare.finance.apps.pushbill.fo;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
public class UpLoadFileF {
    @ApiModelProperty(value = "报账单id")
    @NotNull(message = "报账单id不能为空")
    private Long voucherBillId;

    @ApiModelProperty(value = "报账单号")
    @NotNull(message = "报账单号不能为空")
    private String voucherBillNo;

    @ApiModelProperty("附件信息")
    @NotNull(message = "附件信息不能为空")
    private List<FileVo> files;

    /**
     * 是否上传到财务云， 默认没有 1标识已经上传  0 标识没有上传
     */
    private Integer uploadFlag;

    @ApiModelProperty(value = "业务事由")
    private String receiptRemark;
    @ApiModelProperty(value = "推送部门code")
    private String externalDepartmentCode;
    //计税方式（1.一般计税，2.简单计税）
    private Integer calculationMethod;
}
