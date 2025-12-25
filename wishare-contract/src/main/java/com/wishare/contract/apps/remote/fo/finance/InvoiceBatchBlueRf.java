package com.wishare.contract.apps.remote.fo.finance;

import com.wishare.contract.domains.consts.ContractSetConst;
import com.wishare.starter.consts.Const;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 开具蓝票参数
 *
 * @author 永遇乐 yeoman <76164451@.qq.com>
 * @line --------------------------------
 * @date 2023/02/24
 */
@Getter
@Setter
public class InvoiceBatchBlueRf {
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 账单类型 0-无账单 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单
     */
    private Integer billType;
    /**
     * 购方名称
     */
    private String buyerName;
    /**
     * 购方税号
     */
    private String buyerTaxNum;
    /**
     * 购方银行开户行及账号
     */
    private String buyerAccount;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;
    /**
     * 购方电话
     */
    private String buyerTel;
    /**
     * 购方地址
     */
    private String buyerAddress;
    /**
     * 开票人
     */
    private String clerk;
    /**
     * 开票单元id
     */
    private String invRecUnitId;
    /**
     * 开票单元名称
     */
    private String invRecUnitName;
    /**
     * 发票来源：1.开具的发票 2.收入的发票
     */
    private Integer invSource;
    /**
     * 抬头类型：1个人，2企业
     */
    private Integer invoiceTitleType;
    /**
     * 推送方式：-1,不推送,0,邮箱;1,手机;2,站内信
     */
    private List<Integer> pushMode;
    /**
     * 销方地址
     */
    private String salerAddress;
    /**
     * 销方税号
     */
    private String salerTaxNum;
    /**
     * 销方电话
     */
    private String salerTel;
    /**
     * 系统来源：1 收费系统 2合同系统
     */
    private Integer sysSource;
    /**
     * 开票类型：
     * 1: 增值税普通发票
     * 2: 增值税专用发票
     * 3: 增值税电子发票
     * 4: 增值税电子专票
     * 5: 收据
     * 6：电子收据
     * 7：纸质收据
     * 8：全电普票
     */
    private Integer type;

    /**
     * 发票明细
     */
    private List<InvoiceBatchBlueDetailRf> invoiceBatchDetailBlueFList;

    public static InvoiceBatchBlueRf create(String buyerName, String buyerTaxNum, String buyerAccount,
                                            String buyerTel, String buyerAddress, String clerk, String invRecUnitId,
                                            String invRecUnitName, Integer invSource, List<Integer> pushMode,
                                            String salerAddress, String salerTaxNum, String salerTel, Integer type) {
        InvoiceBatchBlueRf f = new InvoiceBatchBlueRf();
        f.appId = ContractSetConst.CONTRACTAPPID;
        f.appName = ContractSetConst.CONTRACTAPPNAME;
        f.billType = Const.State._0;
        f.buyerName = buyerName;
        f.buyerAccount = buyerAccount;
        f.buyerTaxNum = buyerTaxNum;
        f.buyerTel = buyerTel;
        f.buyerAddress = buyerAddress;
        f.clerk = clerk;
        f.invRecUnitId = invRecUnitId;
        f.invRecUnitName = invRecUnitName;
        f.invSource = invSource;
        f.invoiceTitleType = Const.State._2;
        f.pushMode = pushMode;
        f.salerAddress = salerAddress;
        f.salerTaxNum = salerTaxNum;
        f.salerTel = salerTel;
        f.sysSource = Const.State._2;
        f.type = type;
        return f;
    }
}