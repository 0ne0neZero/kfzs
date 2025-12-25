package com.wishare.finance.apps.model.invoice.invoice.fo;

import com.wishare.finance.domains.invoicereceipt.consts.enums.*;
import com.wishare.finance.infrastructure.remote.enums.NuonuoPushModeEnum;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request.InvoiceBuildingServiceInfoF;
import com.wishare.starter.exception.BizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/10/12
 * @Description:
 */
@Getter
@Setter
@ApiModel("批量开票")
public class InvoiceBatchF {

    @ApiModelProperty(value = "开票类型：1:蓝票;2:红票", required = true)
    @NotNull(message = "开票类型不能为空")
    private Integer invoiceType;


    /**
     * 建筑服务类-开票，入参
     * */
    private InvoiceBuildingServiceInfoF serviceInfoF;

    @ApiModelProperty(value = "开票类型：\n" +
            "     1: 增值税普通发票\n" +
            "     2: 增值税专用发票\n" +
            "     3: 增值税电子发票\n" +
            "     4: 增值税电子专票\n" +
            "     5: 收据\n" +
            "     6：电子收据\n" +
            "     7：纸质收据\n" +
            "     8：全电普票\n" +
            "     9：全电专票", required = true)
    @NotNull(message = "开票类型不能空")
    private Integer type;

    @ApiModelProperty(value = "购方名称", required = true)
    @NotBlank(message = "购方名称不能为空")
    private String buyerName;

    @ApiModelProperty(value = "购方税号")
    private String buyerTaxNum;

    @ApiModelProperty(value = "购方电话")
    private String buyerTel;

    @ApiModelProperty(value = "购方地址")
    private String buyerAddress;

    @ApiModelProperty("购方银行开户行及账号")
    private String buyerAccount;

    @ApiModelProperty("销方税号")
    private String salerTaxNum;

    @ApiModelProperty("销方电话")
    private String salerTel;

    @ApiModelProperty("销方地址")
    private String salerAddress;

    @ApiModelProperty("销方银行开户行及账号")
    private String salerAccount;

    @ApiModelProperty("机器编号")
    private String machineCode;

    @ApiModelProperty("分机号")
    private Long extensionNumber;

    @ApiModelProperty("终端号")
    private Long terminalNumber;

    @ApiModelProperty("终端代码")
    private String terminalCode;

    @ApiModelProperty("用户代码")
    private String userCode;

    @ApiModelProperty("备注信息")
    private String remark;

    @ApiModelProperty(value = "系统来源：1 收费系统 2合同系统", required = true)
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty(value = "发票来源：1.开具的发票 2.收入的发票", required = true)
    @NotNull(message = "发票来源不能为空")
    private Integer invSource;

    @ApiModelProperty(value = "是否推送业主手机：0,不推送,1,推送")
    private Integer isPushOwner = 0;

    @ApiModelProperty(value = "推送方式：-1,不推送,0,邮箱;1,手机;2,站内信")
    @NotNull(message = "推送方式不能为空")
    private List<Integer> pushMode;
    @ApiModelProperty(value = "购方手机（pushMode为1或2时，此项为必填）")
    private String buyerPhone;
    @ApiModelProperty(value = "推送邮箱（pushMode为0或2时，此项为必填）")
    private String email;

    @ApiModelProperty("冲红原因")
    private String redReason;

    @ApiModelProperty("红字信息表编号.专票冲红时此项必填，且必须在备注中注明“开具红字增值税专用发票信息表编号ZZZZZZZZZZZZZZZZ”字样，" +
            "其中“Z”为开具红字增值税专用发票所需要的长度为16位信息表编号")
    private String billInfoNo;

    @ApiModelProperty(value = "账单ids", required = true)
    private List<Long> billIds;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty("指定账单开票金额")
    private List<InvoiceBillAmount> invoiceBillAmounts;

    @ApiModelProperty("定额发票开票必传-指定收款明细开票金额")
    private List<InvoiceGatherDetailAmount> invoiceGatherDetailAmounts;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单")
    private Integer billType;

    @ApiModelProperty("价税合计（开票金额含税）")
    @NotNull(message = "价税合计不能为空")
    @Min(value = 1,message = "价税合计必须大于0")
    private Long priceTaxAmount;

    @ApiModelProperty(value = "抬头类型：1个人，2企业", required = true)
    @NotNull(message = "抬头类型不能为空")
    private Integer invoiceTitleType;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("发票号码")
    private String invoiceNo;

    @ApiModelProperty(value = "开票人")
    private String clerk;

    @ApiModelProperty(value = "收款人")
    private String payee;

    @ApiModelProperty("扩展字段：合同系统（合同名称）")
    private String extendFieldOne;

    @ApiModelProperty("回调地址")
    private String callBackUrl;

    @ApiModelProperty("收款单id")
    private Long gatherBillId;

    @ApiModelProperty("收款单id列表")
    private List<Long> gatherBillIds;

    @ApiModelProperty("定额发票开票必传-收款单明细id列表")
    private List<Long> gatherDetailBillIds;

    @ApiModelProperty(value = "收款单类型 0-收款单， 1-收款明细")
    private Integer gatherBillType;

    @ApiModelProperty("是否免税：0不免税，1免税， 默认不免税")
    private Integer freeTax;

    @ApiModelProperty("开票单元名称")
    private String invRecUnitName;


    @ApiModelProperty(value = "是否签章 0是 1否")
    private Integer signStatus;


    @ApiModelProperty("收款明细信息")
    private List<GatherInvoiceBatchF> gatherInvoiceBatchFList;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty(value = "任务编码,区分不同的任务")
    private String taskCode;

    @ApiModelProperty(value = "是否批量开票：Y是，N否")
    private String batch;

    @ApiModelProperty(value = "定额发票开票必传-发票收据主表ids和对应开票金额")
    private List<QuotaInvoiceBindF.InvoiceReceiptIdAndAmountF> invoiceReceiptIdAndAmount;

    @ApiModelProperty(value = "定额发票开票必传-开票金额 单位元")
    @Max(value = 1000000000, message = "账单金额格式不正确，允许最大值为1000000000")
    private Double totalInvoiceAmount;

    @ApiModelProperty(value = "定额发票开票必传-账单金额 单位元")
    @Max(value = 1000000000, message = "账单金额格式不正确，允许最大值为1000000000")
    private Double totalAmount;
    /**
     * 校检开票入参
     */
    public void checkParam() {
        //校检开票数量
        this.checkBillIdsNum();
        //校检该接口入参类型
        this.checkInvoiceType();
        //校检发票类型
        this.checkType();
        //校检发票抬头类型
        this.checkTitleType();
        //校检推送方式
        this.checkPushMode();
        //校检系统来源
        this.checkSysSource();
        //校检发票来源
        this.checkInvSource();
        //红票特殊校检
        if (this.getInvoiceType() == InvoiceTypeEnum.红票.getCode()) {
            if (StringUtils.isBlank(this.getInvoiceCode())) {
                throw BizException.throw400("冲红时必须填入发票代码");
            }
            if (StringUtils.isBlank(this.getInvoiceNo())) {
                throw BizException.throw400("冲红时必须填入发票号码");
            }
            if (this.type == InvoiceLineEnum.增值税电子专票.getCode()) {
                throw BizException.throw400("红字信息表编号.专票冲红时此项必填");
            }
        }
    }

    /**
     * 校检开票数量
     */
    private void checkBillIdsNum() {
        if (billIds != null && this.getBillIds().size() > 300) {
            throw BizException.throw400("最多只支持300个账单开票");
        }
    }

    /**
     * 校检该接口入参类型
     */
    private void checkInvoiceType() {
        if (this.getInvoiceType() != InvoiceTypeEnum.蓝票.getCode()) {
            throw BizException.throw400("该接口为开具蓝票接口");
        }
    }


    /**
     * 校检发票类型
     */
    public void checkType() {
        InvoiceLineEnum typeEnum = InvoiceLineEnum.valueOfByCode(this.getType());
        if (null == typeEnum) {
            throw BizException.throw400("发票类型异常");
        }
        switch (typeEnum) {
            case 增值税普通发票:
                if (StringUtils.isBlank(this.getInvoiceNo())) {
                    throw BizException.throw400("该票据类型，票据号码不能为空");
                }
                break;
            /*case 增值税电子发票:
                if (StringUtils.isBlank(this.getMachineCode())) {
                    throw BizException.throw400("该票据类型，电子发票机器编码不能为空");
                }*/
            case 增值税电子专票:
                throw BizException.throw400("暂不支持:" + InvoiceLineEnum.增值税专用发票.getDes());
            case 收据:
            case 电子收据:
            case 纸质收据:
                throw BizException.throw400("发票接口不支持开" + typeEnum.getDes());
        }
    }

    /**
     * 校检发票抬头类型
     */
    public void checkTitleType() {
        InvoiceTitleTypeEnum titleTypeEnum = InvoiceTitleTypeEnum.valueOfByCode(this.getInvoiceTitleType());
        if (null == titleTypeEnum) {
            throw BizException.throw400("发票抬头类型异常");
        }
    }

    /**
     * 校检推送方式
     */
    private void checkPushMode() {
        List<Integer> pushModeList = this.getPushMode();
        for (int i = 0; i < pushModeList.size(); i++) {
            NuonuoPushModeEnum nuonuoPushModeEnum = NuonuoPushModeEnum.valueOfByCode(pushModeList.get(i));
            switch (nuonuoPushModeEnum) {
                case 不推送:
                    break;
                case 手机:
                    if (StringUtils.isBlank(buyerPhone)) {
                        throw BizException.throw400("推送方式为手机,请检查购方手机号");
                    }
                    break;
                case 邮箱:
                    if (StringUtils.isBlank(email)) {
                        throw BizException.throw400("推送方式为邮箱,请检查邮箱号");
                    }
                    break;
            }
        }
    }

    /**
     * 校检发票来源
     */
    private void checkInvSource() {
        InvSourceEnum invSourceEnum = InvSourceEnum.valueOfByCode(this.getInvSource());
        if (null == invSourceEnum) {
            throw BizException.throw400("发票来源异常");
        }
    }

    /**
     * 校检系统来源
     */
    private void checkSysSource() {
        SysSourceEnum sysSourceEnum = SysSourceEnum.valueOfByCode(this.getSysSource());
        if (null == sysSourceEnum) {
            throw BizException.throw400("系统来源异常");
        }
        switch (sysSourceEnum) {
            case 合同系统:
                if (StringUtils.isBlank(this.getExtendFieldOne())) {
                    throw BizException.throw400("合同名称不能为空");
                }
                break;
        }
    }

}
