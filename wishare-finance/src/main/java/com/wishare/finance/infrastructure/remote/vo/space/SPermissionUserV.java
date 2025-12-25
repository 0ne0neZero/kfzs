package com.wishare.finance.infrastructure.remote.vo.space;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * Copyright @ 2023 慧享科技 Co. Ltd.
 * All right reserved.
 * <p>
 *
 * </p>
 *
 * @author:PengAn
 * @since:2023-02-13
 * @description:
 **/
@Data
public class SPermissionUserV {

    @ApiModelProperty("权限范围，0全部，1指定 2无权限")
    private Byte scope;

    @ApiModelProperty("项目id集合")
    private Set<String> communityIds;

}
