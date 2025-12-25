package com.wishare.contract.apps.fo.revision.pay.bill;

import com.wishare.contract.apps.remote.vo.config.CfgExternalDeportData;
import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("财务结算表单-其他附件")
public class ContractSettlementInvoiceOtherFileF {

    @ApiModelProperty("其他附件-业务事由")
    private String otherBusinessReasons;

    @ApiModelProperty("其他附件-附件列表")
    private List<AttachmentE> fileList;

    @ApiModelProperty(value = "部门List")
    private List<CfgExternalDeportData> departmentList;
    @ApiModelProperty(value = "推送部门code")
    private String externalDepartmentCode;
    //计税方式（1.一般计税，2.简单计税）
    private Integer calculationMethod;
}
