package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/4
 * @Description:
 */
@Getter
@Setter
@ApiModel("保存凭证头")
public class VoucherHeadF {

    @ApiModelProperty("凭证主键  如果没有那就是新增，有就是修改 可空")
    private String pk_voucher;

    @ApiModelProperty(value = "凭证类别 非空 （凭证类别）",required = true)
    @NotBlank(message = "凭证类别不能为空")
    private String pk_vouchertype;

    @ApiModelProperty("会计年度 非空")
    private String year;

    @ApiModelProperty(value = "来源系统 非空  只支持模块编号", required = true)
    @NotBlank(message = "来源系统不能为空")
    private String pk_system;

    @ApiModelProperty(value = "凭证类型值 0：正常凭证 3：数量调整凭证 不可空", required = true)
    @NotBlank(message = "凭证类型值不能为空")
    private String voucherkind;

    @ApiModelProperty(value = "核算账簿 非空 （账簿_财务核算账簿）", required = true)
    @NotBlank(message = "核算账簿不能为空")
    private String pk_accountingbook;

    @ApiModelProperty("作废标志 可空")
    private String discardflag;

    @ApiModelProperty("会计期间 非空")
    private String period;

    @ApiModelProperty("凭证号为空自动分配 非空：按凭证号处理")
    private String no;

    @ApiModelProperty("附单据数 可空")
    private String attachment;

    @ApiModelProperty(value = "制单日期 非空 例：2012-02-22 00:00:00",required = true)
    @NotBlank(message = "制单日期不能为空")
    private String prepareddate;

    @ApiModelProperty(value = "制单人 非空  （用户）",required = true)
    @NotBlank(message = "制单人不能为空")
    private String pk_prepared;

    @ApiModelProperty("出纳 （用户）")
    private String pk_casher;

    @ApiModelProperty("签字标志")
    private String signflag;

    @ApiModelProperty("审核人 （用户）")
    private String pk_checked;

    @ApiModelProperty("记账日期")
    private String tallydate;

    @ApiModelProperty("记账人 （用户）")
    private String pk_manager;

    @ApiModelProperty("")
    private String memo1;

    @ApiModelProperty("")
    private String memo2;

    @ApiModelProperty("")
    private String reserve1;

    @ApiModelProperty("")
    private String reserve2;

    @ApiModelProperty("")
    private String siscardflag;

    @ApiModelProperty(value = "所属组织 非空 （组织）",required = true)
    @NotBlank(message = "所属组织不能为空")
    private String pk_org;

    @ApiModelProperty("所属组织版本，可空 （组织版本）")
    private String pk_org_v;

    @ApiModelProperty("所集团  如果不输集团取当前登陆集团")
    private String pk_group;

    @Valid
    @ApiModelProperty("分录")
    private List<DetailsF> details;

    public VoucherHeadF create() {

        this.setPk_voucher("");                          // 新增不传
        this.setPk_vouchertype("01");                    // 等待提供了类型 01表示记账凭证  一般都只用这个
        this.setPk_system("GL");                         // 来源系统 默认GL
        this.setVoucherkind("0");                        // 凭证类型 写死
        this.setDiscardflag("N");
        this.setNo("");                                           //
        this.setAttachment("");
        this.setPk_prepared("liuyancui");                      // 等待nc提供  可能默认使用一个人，也可能需根据人员档案看
        this.setPk_casher("");
        this.setSignflag("Y");
        this.setPk_checked("");
        this.setTallydate(""); // 记账日期
        this.setPk_manager(""); // 记账人
        this.setMemo1("");
        this.setMemo2("");
        this.setReserve1("");
        this.setReserve2("");
        this.setSiscardflag("");
        this.setPk_org_v("");                            // 不用填
        this.setPk_group("G");                         // nc系统所建集团编码
        return this;
    }
}
