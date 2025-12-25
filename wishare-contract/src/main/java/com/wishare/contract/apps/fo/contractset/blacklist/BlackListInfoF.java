package com.wishare.contract.apps.fo.contractset.blacklist;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 黑名单信息
 *
 * @author long
 * @date 2023/8/1 15:33
 */
@Data
public class BlackListInfoF {
    /**
     * 示例:101077359
     */
    @ApiModelProperty("申请查询的单位4A编码")
    @NotBlank(message = "申请查询的单位4A编码不能为空")
    private String comCode;

    /**
     * 如所要查询的市场主体在主数据往来单位中已入库，则此参数必填。
     * 示例:BP00589799
     */
    @ApiModelProperty("往来单位主数据编码(BP码)")
    private String subjectInfoCode;

    /**
     * 如所要查询的市场主体暂未在主数据往来单位中入库，则此参数必输，后续会根据此参数查询是否黑名单或重点关注名单。
     * 示例:91440101671829338D
     */
    @ApiModelProperty("唯一识别码")
    @NotBlank(message = "唯一识别码不能为空")
    private String societyCreditCode;

    /**
     * 如所要查询的市场主体暂未在主数据往来单位中入库，则此参数必输，后续会根据此参数查询改头换面信息。
     * 示例:130525199203112744
     */
    @ApiModelProperty("法人代表人身份证")
    @NotBlank(message = "法人代表人身份证不能为空")
    private String legalIdNumber;

    /**
     * 示例: SCMA
     */
    @ApiModelProperty("来源系统")
    @NotBlank(message = "来源系统不行为空")
    private String sourceSystem;

    /**
     * 1 招投标 2 新合同签约 3 合同补充协议签约 4 合同结算 5 合同付款
     */
    @ApiModelProperty("环节编码")
    private String stepCode;
}
