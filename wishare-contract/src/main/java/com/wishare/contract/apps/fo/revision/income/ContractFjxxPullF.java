package com.wishare.contract.apps.fo.revision.income;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同订立信息拓展表附件信息实体
 * </p>
 *
 * @author chenglong
 * @since 2023-09-22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractFjxxPullF {

    @ApiModelProperty("fileId")
    private String fileId;

    @ApiModelProperty("附件名称")
    private String fileName;

    @ApiModelProperty("附件业务类型")
    private String busitype;



}
