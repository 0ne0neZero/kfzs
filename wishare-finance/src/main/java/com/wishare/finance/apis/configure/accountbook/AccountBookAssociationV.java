package com.wishare.finance.apis.configure.accountbook;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 账号账簿对应表类
 */
@Data
public class AccountBookAssociationV {

    /**
     * 主键id
     */
    private Long id;

    @ApiModelProperty(value = "userId用户id", required = true)
    @NotNull(message = "userId用户id")
    private Long userId;

    @ApiModelProperty(value = "账簿id字符串,英文逗号拼接", required = true)
    private String accountBookIds;

    /**
     * 0-全部,1-指定,2-无
     */
    @ApiModelProperty(value = "类型0-全部,1-指定,2-无", required = true)
    @NotNull(message = "association 不能空")
    private Integer association;

}

