package com.wishare.finance.domains.configure.subject.command.subject;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 同步科目体系科目信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/13
 */
@Getter
@Setter
@ApiModel("同步科目体系科目信息")
public class SyncSystemSubjectCommand {

    @ApiModelProperty(value = "科目体系编码", required = true)
    @NotBlank(message = "科目体系编码不能为空")
    private String code;

    @ApiModelProperty(value = "科目体系名称", required = true)
    @NotBlank(message = "科目体系名称不能为空")
    private String name;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("科目列表")
    private List<SyncSubjectCommand> subjects;


}
