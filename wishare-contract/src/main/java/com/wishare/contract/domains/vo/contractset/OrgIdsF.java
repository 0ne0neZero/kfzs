package com.wishare.contract.domains.vo.contractset;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author hhb
 * @describe
 * @date 2025/11/15 15:13
 */
@Getter
@Setter
@Accessors(chain = true)
public class OrgIdsF {

    @ApiModelProperty("组织ID 主键")
    @NotNull(message = "组织ID不能为空！")
    private List<Long> orgIds;
}
