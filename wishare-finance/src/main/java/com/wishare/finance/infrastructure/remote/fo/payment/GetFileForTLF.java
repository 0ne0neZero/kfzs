package com.wishare.finance.infrastructure.remote.fo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zyj
 * @date 2023/08/07
 * @Description:
 */
@Getter
@Setter
@ApiModel("获取通联对账文件")
public class GetFileForTLF {

    @ApiModelProperty("文件路径")
    private String filePath;

    @ApiModelProperty("模糊匹配名称")
    private String queryName;
}
