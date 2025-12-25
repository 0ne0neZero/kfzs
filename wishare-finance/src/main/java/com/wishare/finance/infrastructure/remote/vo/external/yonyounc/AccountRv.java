package com.wishare.finance.infrastructure.remote.vo.external.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 会计科目
 * @author: pgq
 * @since: 2022/12/6 19:20
 * @version: 1.0.0
 */
@ApiModel("会计科目")
@Getter
@Setter
public class AccountRv {

    /**
     * 科目编码
     */
    @ApiModelProperty("科目编码")
    private String acccode;

    /**
     * 科目名称
     */
    @ApiModelProperty("科目名称")
    private String accname;

    /**
     * 科目级次
     */
    @ApiModelProperty("科目级次")
    private String acclev;

    /**
     * 末级标志
     */
    @ApiModelProperty("末级标志")
    private String endflag;

    /**
     * 单元编码
     */
    @ApiModelProperty("单元编码")
    private String orgcode;

    /**
     * 单元名称
     */
    @ApiModelProperty("单元名称")
    private String orgname;

    /**
     * 辅助编码
     */
    @ApiModelProperty("辅助编码")
    private String fzcode;

    /**
     * 辅助名称
     */
    @ApiModelProperty("辅助名称")
    private String fzname;
}
