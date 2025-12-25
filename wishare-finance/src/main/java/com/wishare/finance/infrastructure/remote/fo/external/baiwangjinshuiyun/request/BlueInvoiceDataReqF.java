package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 蓝票发票主体信息
 * @author dongpeng
 * @date 2023/10/25 20:19
 */
@Data
@ApiModel(value = "发票主体信息")
public class BlueInvoiceDataReqF {

    @ApiModelProperty(value = "单据编号(全局唯一)", required = true)
    private String djbh;

    @ApiModelProperty(value = "开票终端代码", required = true)
    private String kpzddm;

    @ApiModelProperty(value = "发票类型代码（01:增值税专用发票,02:普通发票）", required = true)
    private String fplxdm;

    @ApiModelProperty(value = "开票类型（0:蓝字发票）", required = true)
    private String kplx;

    @ApiModelProperty(value = "特定约束类型代码（见附录8.2，默认00）")
    private String tdyslxdm;

    @ApiModelProperty(value = "征收方式(0:普通征税,1:减按征税,2:差额征税,3:全额征收)", required = true)
    private String zsfs;

    @ApiModelProperty(value = "清单标识(0:非清单,1:清单(商品明细超8条默认清单票))")
    private String qdbz;

    @ApiModelProperty(value = "销方地址电话(地址和电话之间加空格)")
    private String xfdzdh;

    @ApiModelProperty(value = "销方银行账户(银行和账号之间加空格)")
    private String xfyhzh;

    @ApiModelProperty(value = "购货单位名称", required = true)
    private String khmc;

    @ApiModelProperty(value = "购货单位税号(发票类型代码为01时必填)")
    private String khsh;

    @ApiModelProperty(value = "购货单位地址电话(地址和电话之间加空格)")
    private String khdzdh;

    @ApiModelProperty(value = "购货单位银行账号(银行和账号之间加空格)")
    private String khyhzh;

    @ApiModelProperty(value = "购货单位手机号(电子发票填写)")
    private String gmfMobile;

    @ApiModelProperty(value = "购货单位邮箱(电子发票填写)")
    private String gmfEmail;

    @ApiModelProperty(value = "购货单位抄送邮箱(电子发票填写，抄送邮箱1)")
    private String gmfEmail1;

    @ApiModelProperty(value = "购货单位抄送邮箱(电子发票填写，抄送邮箱2)")
    private String gmfEmail2;

    @ApiModelProperty(value = "购货单位抄送邮箱(电子发票填写，抄送邮箱3)")
    private String gmfEmail3;

    @ApiModelProperty(value = "含税金额（价税合计 小数点2位）", required = true)
    private BigDecimal hsje;

    @ApiModelProperty(value = "合计金额（小数点2位）")
    private BigDecimal hjje;

    @ApiModelProperty(value = "合计税额（小数点2位）")
    private BigDecimal hjse;

    @ApiModelProperty(value = "扣除额（征收方式为2时必填 小数点2位）")
    private BigDecimal kce;

    @ApiModelProperty(value = "发票理由（小规模开具3%税率时必填\n" +
            "1:开具发票为2022年3月31日前发生纳税义务的业务\n" +
            "2:前期已开具相应征收率发票，发生销售折让、中止或者退回等情形需要开具红字发票，或者开票有误需要重新开具\n" +
            "3:因为实际经营业务需要，放弃享受免征增值税政策）")
    private String kjly;

    @ApiModelProperty(value = "发票代码（纸质发票必填）")
    private String fpdm;

    @ApiModelProperty(value = "发票号码（纸质发票必填）")
    private String fphm;

    @ApiModelProperty(value = "是否为纸质发票（Y是 N不是(默认)）")
    private String sfwzzfp;

    @ApiModelProperty(value = "纸票票种代码")
    private String zppzdm;

    @ApiModelProperty(value = "含税标志")
    private String hsbz;

    @ApiModelProperty(value = "备注")
    private String bz;

    @ApiModelProperty(value = "公司代码", required = true)
    private String gsdm;

    @ApiModelProperty(value = "部门代码")
    private String bmdm;

    @ApiModelProperty(value = "用户代码", required = true)
    private String yhdm;

    @ApiModelProperty(value = "单据日期")
    private String djrq;

    @ApiModelProperty(value = "数据类型（百旺金穗云开户时提供 ）", required = true)
    private String sjlx;

    @ApiModelProperty(value = "数据来源")
    private String sjly;

    @ApiModelProperty(value = "开票人")
    private String kpr;

    @ApiModelProperty(value = "收款人（传值后会显示在备注里）")
    private String skr;

    @ApiModelProperty(value = "复核人（传值后会显示在备注里）")
    private String fhr;

    @ApiModelProperty(value = "制单人")
    private String zdr;

    @ApiModelProperty(value = "扩展1")
    private String kz1;

    @ApiModelProperty(value = "扩展2")
    private String kz2;

    @ApiModelProperty(value = "扩展3")
    private String kz3;

    @ApiModelProperty(value = "购买方自然人标识(N:非自然人标识(默认)\n" +
            "Y:自然人标识)")
    private String gmfzrrbs;

    @ApiModelProperty(value = "事业单位(非自然人购方名称少于4个字的传0)")
    private String spflxConfirm;

    @ApiModelProperty(value = "大额发票开具提醒(Y 确认开具\n" +
            "开具金额较大，请确认是否开具)")
    private String sxedDefptxgz;

    @ApiModelProperty(value = "是否展示购买方银行账号(N:不展示\n" +
            "Y:展示)")
    private String sfzsgmfyhzh;

    @ApiModelProperty(value = "是否展示销售方银行账户(N:不展示\n" +
            "Y:展示)")
    private String sfzsxsfyhzh;

    @ApiModelProperty(value = "商品明细信息",required = true)
    private List<BlueInvoiceDetailF> mxxx;

    @ApiModelProperty(value = "建筑服务信息")
    private InvoiceBuildingServiceInfoF jzfw;

    @ApiModelProperty(value = "不动产经营租赁服务")
    private InvoiceRealEstateLeaseInfoF bdcjyzlfw;

}
