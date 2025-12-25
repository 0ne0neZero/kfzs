package com.wishare.finance.infrastructure.remote.vo.zj;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;



@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MDM16V {
    Long id;
    @ApiModelProperty(value = "ID")
    String idExt;

    @ApiModelProperty(value = "编号")
    String code;

    @ApiModelProperty(value = "中文简体名称")
    String nameChs;

    @ApiModelProperty(value = "中文繁体名称")
    String nameCht;

    @ApiModelProperty(value = "英文名称")
    String nameEn;

    @ApiModelProperty(value = "是否明细 1 是 0 否")
    String pnThrInfoIsDetail;

    @ApiModelProperty(value = "层级")
    String pnThrInfoLayer;

    @ApiModelProperty(value = "父节点字段")
    String pnThrInfoParentElement;

    @ApiModelProperty(value = "顺序号")
    String pnThrInfoSequence;

    @ApiModelProperty(value = "是否禁用 0 禁用 1 非禁用")
    String stateIsenabled;

    @ApiModelProperty(value = "创建时间 UTC")
    String createdtime;

    @ApiModelProperty(value = "最后修改时间 UTC")
    String lastModifiedTime;
}
