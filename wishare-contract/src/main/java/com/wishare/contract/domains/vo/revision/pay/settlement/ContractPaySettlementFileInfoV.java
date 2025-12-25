package com.wishare.contract.domains.vo.revision.pay.settlement;

import com.wishare.contract.domains.vo.revision.attachment.AttachmentV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "结算单附件信息视图对象")
public class ContractPaySettlementFileInfoV {

    @ApiModelProperty(value = "合同扫描件")
    private List<AttachmentV> contractFiles;

    @ApiModelProperty(value = "合同数量确认单")
    private List<AttachmentV> quantityFiles;

    @ApiModelProperty(value = "合同结算表")
    private List<AttachmentV> settlementFiles;

    @ApiModelProperty(value = "其他附件")
    private List<AttachmentV> otherFiles;


}
