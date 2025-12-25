package com.wishare.finance.apps.model.bill.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: jinwh
 * @since: 2023年7月21日
 * @version: 1.0.0
 */
@Setter
@Getter
@ApiModel("冻结明细")
public class BillFreezeV {

    @ApiModelProperty(value = "id")
    @JSONField(name = "id")
    private Long id;

    @ApiModelProperty(value = "项目名称")
    @JSONField(name = "communityName")
    private String communityName;

    @ApiModelProperty(value = "项目id")
    @JSONField(name = "communityId")
    private String communityId;

    @ApiModelProperty(value = "房号")
    @JSONField(name = "roomName")
    private String roomName;

    @ApiModelProperty(value = "房号id")
    @JSONField(name = "roomId")
    private String roomId;

    @ApiModelProperty(value = "费项id")
    @JSONField(name = "chargeItemId")
    private String chargeItemId;

    @ApiModelProperty(value = "费项名称")
    @JSONField(name = "chargeItemName")
    private String chargeItemName;

    @ApiModelProperty(value = "冻结金额")
    @JSONField(name = "freezeAmount")
    private Long freezeAmount;

    @ApiModelProperty(value = "跳收状态：0-待审核,1-已完成,2-拒绝,3-处理中")
    @JSONField(name = "state")
    private Integer state;

    @ApiModelProperty(value = "跳收原因")
    @JSONField(name = "reason")
    private String reason;

    @ApiModelProperty(value = "文件集合")
    @JSONField(name = "fileList")
    private String  fileList;

    @ApiModelProperty(value = "操作人")
    @JSONField(name = "operatorName")
    private String operatorName;

    @ApiModelProperty(value = "冻结日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JSONField(name = "gmtCreate")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "冻结账单明细记录")
    private String jumpRecordsExtJson;

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("是否解冻:0表示解冻,1表示冻结")
    private Integer isRefreeze;

    /**
     * 冻结类型 1跳收 2代扣
     */
    @ApiModelProperty("冻结类型 1跳收 2代扣")
    private Integer freezeType;
}
