package com.wishare.finance.infrastructure.remote.fo.space;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright @ 2022 慧享科技 Co. Ltd.
 * All right reserved.
 * <p>
 *
 * </p>
 *
 * @author:PengAn
 * @since:2022-10-11
 * @description:
 **/
@Data
@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpaceLastNodesF {
    @NotBlank
    @ApiModelProperty("项目id")
    private String communityId;

    @NotNull
    @ApiModelProperty("pids")
    private List<String> pids;


}
