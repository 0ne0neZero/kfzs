package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountReqV implements Serializable {

    @ApiModelProperty(value = "往来单位编号")
    private String wldwbh;

    @ApiModelProperty(value = "项目id")
    private String communityId;

    @ApiModelProperty(value = "单位code")
    private String unitCode;




}
