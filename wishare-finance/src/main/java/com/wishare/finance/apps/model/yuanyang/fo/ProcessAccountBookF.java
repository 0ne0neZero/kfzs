package com.wishare.finance.apps.model.yuanyang.fo;

import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

/**
 * @author luzhonghe
 * @version 1.0
 * @since 2023/8/24
 */
@Getter
@Setter
@ApiModel("账套信息")
public class ProcessAccountBookF {

    /**
     * 账簿编码
     */
    @NotBlank(message="账簿编码不能为空")
    @Length(max= 64,message="编码长度不能超过64")
    @ApiModelProperty(value = "账簿编码", required = true)
    private String accountBookCode;
    /**
     * 账簿名称
     */
    @NotBlank(message="账簿名称不能为空")
    @Length(max= 100,message="编码长度不能超过100")
    @ApiModelProperty(value = "账簿名称", required = true)
    private String accountBookName;

    @ApiModelProperty(value = "账套标识id")
    private String accountDetailId;

    @ApiModelProperty(value = "支付id")
    private List<String> payIds;

    @ApiModelProperty(value = "记账人")
    private String bookkeeper;

    @ApiModelProperty(value = "模板序号，默认为1，从1开始递增")
    private Integer templateNum = 1;

    @Valid
    @ApiModelProperty(value = "费用明细")
    private List<ProcessChargeDetailF> chargeDetails;

    @Valid
    @ApiModelProperty(value = "发票明细")
    private List<ReimbursementInvoiceF> invoices;

    @Valid
    @ApiModelProperty(value = "收款业务收款方信息")
    private List<ProcessPayeeF> payees;

    @Valid
    @ApiModelProperty(value = "收款业务付款方信息")
    private List<ProcessPayerF> payers;

    @ApiModelProperty(value = "支付方银行信息")
    private List<ProcessBankPayInfoF> payBankInfos;

    @ApiModelProperty(value = "对公收款方信息")
    private List<ProcessBankPublicF> publicPayees;

    @ApiModelProperty(value = "对私收款方信息")
    private List<ProcessBankPrivateF> privatePayees;

    @ApiModelProperty(value = "工资子表 信息")
    private List<WageTableF> wageTableInfo;

    @ApiModelProperty(value = "社保子表 信息")
    private List<SocialSecurityTableInfoF> socialSecurityTableInfo;

    public void init() {
        if (Objects.isNull(accountDetailId)) {
            accountDetailId = IdentifierFactory.generateNSUUID();
        }
    }

}
