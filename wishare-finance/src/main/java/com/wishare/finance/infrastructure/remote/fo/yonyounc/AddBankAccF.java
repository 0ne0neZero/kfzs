package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 银行账户新增类
 * @author: pgq
 * @since: 2022/12/9 15:39
 * @version: 1.0.0
 */
@ApiModel("银行账户新增类")
@Valid
@Getter
@Setter
public class AddBankAccF {

    /**
     * 所属集团 默认集团：远洋亿家集团 G
     */
    @ApiModelProperty("所属集团 默认集团：远洋亿家集团 G")
    private String pkGroup = "G";

    /**
     * 所属业务单元
     */
    @ApiModelProperty("所属业务单元")
    private String pkOrg = "G";

    /**
     * 账户分类 0=个人，1=客户，2=公司，3=供应商 默认 2 公司
     */
    @ApiModelProperty("账户分类 "
        + "0=个人，1=客户，2=公司，3=供应商"
        + "默认 2 公司")
    private Integer accClass = 2;

    /**
     * 户名
     */
    @ApiModelProperty(value = "户名", required = true)
    private String accName;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号", required = true)
    private String accNum;

    /**
     * 开户日期 示例：2022-3-31 15:20:34
     */
    @ApiModelProperty("开户日期")
    private String accOpenDate;

    /**
     * 账户性质 0=公司，1=个人
     */
    @ApiModelProperty("账户性质 0=公司，1=个人")
    private Integer accountProperty;

    /**
     * 账户状态 0=正常，1=冻结，2=部分冻结，3=销户
     */
    @ApiModelProperty("账户状态 0=正常，1=冻结，2=部分冻结，3=销户")
    private Integer accState = 0;

    /**
     * 账户编码 与账号一致
     */
    @ApiModelProperty("账户编码")
    private String code;

    /**
     * 传递时间 默认当前系统时间
     */
    @ApiModelProperty("传递时间 默认当前系统时间")
    private String creationTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String creator;

    /**
     * 启用状态
     */
    @ApiModelProperty("启用状态")
    private Integer enableState = 2;

    /**
     * 启用时间 默认当前系统时间
     */
    @ApiModelProperty("启用时间")
    private String enableTime;

    /**
     * 启用人
     */
    @ApiModelProperty("启用人")
    private String enableUser;

    /**
     * 签约  Y N
     */
    @ApiModelProperty("签约 Y N 默认 N")
    private char isSigned = 'N';

    /**
     * 账户名称
     */
    @ApiModelProperty("账户名称")
    private String name;

    /**
     * 银行账户主键
     */
    @ApiModelProperty("银行账户主键")
    private String pkBankAccbas;

    /**
     * 开户银行
     */
    @ApiModelProperty("开户银行")
    private String pkBankDoc;

    /**
     * 银行类别
     */
    @ApiModelProperty("银行类别")
    private String pkBankType;

    /**
     * 开户单位
     */
    @ApiModelProperty("开户单位")
    private String financeOrg;

    /**
     * 核算归属组织
     */
    @ApiModelProperty("核算归属组织")
    private String controlOrg;

    /**
     * 收付属性 0=收入；1=支出；2=收支
     */
    @ApiModelProperty("收付属性 0=收入；1=支出；2=收支")
    private Integer arapProp;

    /**
     * 账户属性 0=基本; 1=临时; 2=一般; 3=专用;
     */
    @ApiModelProperty("账户属性 0=基本; 1=临时; 2=一般; 3=专用;")
    private Integer accAttribute;

    /**
     * 网银开通状态 0=未开通;1=开通查询;2=开通查询及支付;3=开通落地支付;
     */
    @ApiModelProperty("网银开通状态 0=未开通;1=开通查询;2=开通查询及支付;3=开通落地支付;")
    private Integer netQueryFlag;

    /**
     *
     */
    private List<BankAccSubF> bankaccsub;

}
