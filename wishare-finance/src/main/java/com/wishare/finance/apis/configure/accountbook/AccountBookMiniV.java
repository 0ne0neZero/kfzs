package com.wishare.finance.apis.configure.accountbook;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 账号账簿对应表类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountBookMiniV {

    /**
     * 主键id
     */
    @ApiModelProperty(value = "账簿id", required = true)
    private Long accountBookId;
    @ApiModelProperty(value = "账簿名字", required = true)
    private String accountBookName;


}

