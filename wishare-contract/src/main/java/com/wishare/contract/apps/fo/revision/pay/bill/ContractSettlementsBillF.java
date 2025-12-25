package com.wishare.contract.apps.fo.revision.pay.bill;

import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillCalculateSaveF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillDetailsSaveF;
import com.wishare.contract.apps.fo.revision.invoice.ContractSettlementsBillItemSaveF;
import com.wishare.contract.apps.remote.fo.image.ZJFileVo;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/17/14:36
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同结算单开票信息表", description = "合同结算单开票信息表")
public class ContractSettlementsBillF {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("结算单id")
    private String settlementId;

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("票据类型")
    private Integer billType;

    @ApiModelProperty("发票代码")
    private String billCode;

    @ApiModelProperty("发票号码")
    private String billNum;

    @ApiModelProperty("收票/开票类型 收款 0 付款 1 ")
    private String type;

    @ApiModelProperty("收票/开票金额")
    private BigDecimal amount;

    @ApiModelProperty("抬头")
    private String title;

    @ApiModelProperty("纳税人识别号")
    private String creditCode;

    @ApiModelProperty("收票时间")
    private LocalDate collectTime;

    /**
     * 业务类型ID
     */
    @ApiModelProperty("业务类型ID")
    @Length(message = "业务类型ID不可超过 40 个字符",max = 40)
    private List<String> bussTypeCode;
    /**
     * 业务类型名称
     */
    @ApiModelProperty("业务类型名称")
    @Length(message = "业务类型名称不可超过 50 个字符",max = 50)
    private String bussTypeName;
    /**
     * 变动ID
     */
    @ApiModelProperty("变动ID")
    @Length(message = "变动ID不可超过 40 个字符",max = 40)
    private String changeId;
    /**
     * 变动名称
     */
    @ApiModelProperty("变动名称")
    @Length(message = "变动名称不可超过 50 个字符",max = 50)
    private String changeName;
    /**
     * 款项
     */
    @ApiModelProperty("款项")
    @Length(message = "款项不可超过 40 个字符",max = 40)
    private List<String> itemCode;
    /**
     * 款项名称
     */
    @ApiModelProperty("款项名称")
    @Length(message = "款项名称不可超过 40 个字符",max = 40)
    private String itemName;
    /**
     * 核销应收应付信息{\"id\":\"\",\"orgName\":\"\"}
     */
    @ApiModelProperty("核销应收应付信息{\"id\":\"\",\"orgName\":\"\"}")
    private String writeOffInfo;

    @ApiModelProperty("收票计量明细表(前端传数组)")
    private List<ContractSettlementsBillCalculateSaveF> contractSettlementsBillCalculateSaveFList;

    @ApiModelProperty("收票明细表(前端传数组)")
    private List<ContractSettlementsBillDetailsSaveF> contractSettlementsBillDetailsSaveFList;

    @ApiModelProperty("附件(前端传数组)")
    private List<ZJFileVo> attachment;

    @ApiModelProperty("备注")
    private String remark;
}
