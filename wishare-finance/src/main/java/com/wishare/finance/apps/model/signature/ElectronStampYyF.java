package com.wishare.finance.apps.model.signature;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("电子签章入参")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ElectronStampYyF {
    @ApiModelProperty(value = "公司名")
    private String companyName;
    @ApiModelProperty(value = "印章类型 COMMON-SEAL 公章 SFZYZ 收费专用章 ZFZ 作废章")
    @NotBlank(message = "印章类型不能为空")
    private String sealType;
    @ApiModelProperty(value = "盖章位置所在唯一关键字")
    @NotBlank(message = "盖章位置所在唯一关键字不能为空")
    private String keyword;
    @ApiModelProperty(value = "组织机构证件号")
    @NotBlank(message = "组织机构证件号不能为空")
    private String orgIDCardNum;
    @ApiModelProperty(value = "文件集合")
    @NotNull(message = "文件不能为空")
    private List<FileVo> fileVos;
}
