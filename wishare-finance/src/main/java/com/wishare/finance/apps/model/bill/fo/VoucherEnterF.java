package com.wishare.finance.apps.model.bill.fo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wishare.finance.domains.voucher.entity.VoucherCostCenterOBV;
import com.wishare.finance.domains.voucher.entity.VoucherDetailOBV;
import com.wishare.finance.domains.voucher.support.ListVoucherCostCenterTypeHandler;
import com.wishare.finance.domains.voucher.support.ListVoucherDetailTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * 凭证信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/15
 */
@Getter
@Setter
@ApiModel("凭证信息")
public class VoucherEnterF {

    @ApiModelProperty(value = "凭证类别： 1记账凭证")
    private Integer voucherType;

    @ApiModelProperty(value = "账簿id")
    private Long accountBookId;

    @ApiModelProperty(value = "账簿编码")
    private String accountBookCode;

    @ApiModelProperty(value = "账簿名称")
    private String accountBookName;

    @ApiModelProperty(value = "推凭金额 单位：分")
    private Long amount;

    @ApiModelProperty(value = "推凭状态：0待同步，1成功，2失败，3同步中")
    private Integer state;

    @ApiModelProperty(value = "分录详情")
    private List<VoucherDetailOBV> details;

    @ApiModelProperty(value = "会计期间")
    private LocalDate fiscalPeriod;

    @ApiModelProperty(value = "会计年度")
    private Integer fiscalYear;

    @ApiModelProperty(value = "同步系统: 1用友NCC")
    private Integer syncSystem;

    @ApiModelProperty(value = "制单人名称")
    private String makerName;

}
