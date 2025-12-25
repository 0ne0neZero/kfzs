package com.wishare.finance.apps.model.bill.fo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <p>
 * 账单跳收明细表，管理账单跳收历史信息记录
 * </p>
 *
 * @author jinwh
 * @since 2023-07-25
 */
@Getter
@Setter
@Accessors(chain = true)
public class BillFreezeF {

    @ApiModelProperty(value = "账单ids")
    @NotEmpty(message = "账单id不能为空")
    private List<Long> billIds;

    @ApiModelProperty("上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty(value = "项目id")
    @JsonIgnore
    private String communityId;

    @ApiModelProperty("文件key集合")
    @JSONField(name = "fileVos")
    private List<FileVo> fileVos;

    @ApiModelProperty("跳收原因")
    @JSONField(name = "reason")
    private String reason;

    @ApiModelProperty("冻结类型 1跳收 2代扣")
    private Integer freezeType;


}
