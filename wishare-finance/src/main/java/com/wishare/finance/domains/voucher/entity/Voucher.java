package com.wishare.finance.domains.voucher.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBookDateTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherMakeTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherStateEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherTypeEnum;
import com.wishare.finance.domains.voucher.strategy.core.VoucherData;
import com.wishare.finance.domains.voucher.support.ListVoucherCostCenterTypeHandler;
import com.wishare.finance.domains.voucher.support.ListVoucherDetailTypeHandler;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.conts.VoucherSysEnum;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 凭证明细
 *
 * @author dxclay
 * @since 2023-03-10
 */
@Getter
@Setter
@ApiModel(value = "凭证信息")
@TableName(value = TableNames.VOUCHER_INFO, autoResultMap = true)
public class Voucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "凭证id")
    private Long id;

    @ApiModelProperty(value = "报账凭证编号")
    private String voucherNo;

    @ApiModelProperty(value = "同步系统凭证编号")
    private String syncSystemVoucherNo;

    @ApiModelProperty(value = "录制方式：0自动录制，1手动录制")
    private Integer madeType;

    @ApiModelProperty(value = "凭证类别： 1记账凭证")
    private Integer voucherType;

    @ApiModelProperty(value = "账簿id")
    private Long accountBookId;

    @ApiModelProperty(value = "账簿编码")
    private String accountBookCode;

    @ApiModelProperty(value = "账簿名称")
    private String accountBookName;

    @ApiModelProperty(value = "业务单元id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "业务单元编码")
    private String statutoryBodyCode;

    @ApiModelProperty(value = "业务单元名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "成本中心 [ {\"costCenterId\": 成本中心id, \"costCenterName\": \"成本中心名称\"}]")
    @TableField(typeHandler = ListVoucherCostCenterTypeHandler.class)
    private List<VoucherCostCenterOBV> costCenters;

    @ApiModelProperty(value = "推凭金额 单位：分")
    private Long amount;

    /**
     * {@linkplain VoucherStateEnum}
     */
    @ApiModelProperty(value = "推凭状态：0待同步，1成功，2失败，3同步中，4作废")
    private Integer state;

    @ApiModelProperty(value = "分录详情")
    @TableField(typeHandler = ListVoucherDetailTypeHandler.class)
    private List<VoucherDetailOBV> details;

    @ApiModelProperty(value = "业务单据列表")
    @TableField(exist = false)
    private List<VoucherBusinessDetail> businessBills;

    @ApiModelProperty(value = "会计期间")
    private LocalDate fiscalPeriod;

    @ApiModelProperty(value = "会计年度")
    private Integer fiscalYear;

    @ApiModelProperty(value = "记账日期")
    private LocalDate bookkeepingDate;

    @ApiModelProperty(value = "推凭记录id")
    private Long voucherRuleRecordId;

    @ApiModelProperty(value = "触发事件code")
    private Integer evenType;

    @ApiModelProperty(value = "触发事件")
    private String evenValue;

    @ApiModelProperty(value = "同步系统: 1用友NCC")
    private Integer syncSystem;

    @ApiModelProperty(value = "同步时间")
    private LocalDateTime syncTime;

    @ApiModelProperty(value = "制单人id")
    private String makerId;

    @ApiModelProperty(value = "制单人名称")
    private String makerName;

    /**
     * {@linkplain VoucherSourceEnum}
     */
    @ApiModelProperty(value = "凭证来源：0 系统生成，1 BPM推送")
    private Integer voucherSource;


    /**
     * {@linkplain SysSourceEnum}
     */
    private Integer sysSource;

    /**
     * 成本中心id
     */
    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    /**
     * 成本中心名称
     */
        @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;


    @ApiModelProperty(value = "推凭失败原因")
    private String errorReason;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 支付方式
     */
    @TableField(exist = false)
    private String payChannel;
    /**
     * 支付方式
     */
    private String payChannelValue;
    /**
     * 初始化参数
     */
    public void init() {
        if (Objects.isNull(voucherNo)) {
            voucherNo = IdentifierFactory.getInstance().serialNumber("voucher_no", "PZ", 20);
        }

        if (Objects.isNull(id)) {
            id = IdentifierFactory.getInstance().generateLongIdentifier(TableNames.VOUCHER_INFO);
        }

        if (Objects.isNull(fiscalYear) && Objects.nonNull(fiscalPeriod)){
            fiscalYear = fiscalPeriod.getYear();
        }
    }

    /**
     * 创建凭证信息
     * @param eventTypeEnum 触发事件类型
     * @param book 账簿
     * @param template 凭证模板
     * @return
     */
    public static Voucher create(VoucherEventTypeEnum eventTypeEnum, VoucherBook book, VoucherTemplate template) {
        Voucher voucher = new Voucher();
        voucher.setMadeType(VoucherMakeTypeEnum.自动.getCode());
        voucher.setVoucherType(VoucherTypeEnum.记账凭证.getCode());
        voucher.setState(VoucherStateEnum.待同步.getCode());
        voucher.setFiscalPeriod(LocalDate.now());
        voucher.setSyncSystem(VoucherSysEnum.用友Ncc系统.getCode());
        voucher.setMakerId(VoucherData.makerId);
        voucher.setMakerName(VoucherData.makerName);
        voucher.setEvenType(eventTypeEnum.getCode());
        voucher.setEvenValue(eventTypeEnum.getValue());
        voucher.setAccountBookId(book.getBookId());
        voucher.setAccountBookCode(book.getBookCode());
        voucher.setAccountBookName(book.getBookName());
        voucher.setFiscalPeriod(LocalDate.now());
        if (VoucherBookDateTypeEnum.凭证生成日期.equalsByCode(template.getBookDateType())) {
            voucher.setBookkeepingDate(LocalDate.now());
        }
        return voucher;
    }

    public String getCostCenterIdByCostCenters() {
        if (CollectionUtils.isNotEmpty(costCenters)) {
            // 目前凭证里面只会有一个成本中心
            VoucherCostCenterOBV costCenterOBV = costCenters.get(0);
            return String.valueOf(costCenterOBV.getCostCenterId());
        }
        return "";
    }

}
