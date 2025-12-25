package com.wishare.finance.apps.model.signature;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author xujian
 * @date 2022/12/27
 * @Description:
 */
@Getter
@Setter
@ApiModel(value = "e签宝签章需要的信息")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ESignF {

    @ApiModelProperty(value = "需要签章的文件")
    private FileVo fileVo;

    @ApiModelProperty(value = "盖章位置所在唯一关键字", example = "异议")
    private String keyword;

    @ApiModelProperty(value = "组织机构证件号",example = "91440101231251085G")
    private String orgIDCardNum;

    /**
     * 当需要盖的章组织不是方圆股份时
     */
    @ApiModelProperty(value = "授权组织机构证件号")
    private String authOrgIDCardNum;

    @ApiModelProperty(value = "授权组织机构名称")
    private String authOrgName;


    @ApiModelProperty(value = "组织机构证件类型 CRED_ORG_USCC - 统一社会信用代码 CRED_ORG_REGCODE - 工商注册号", example = "CRED_ORG_USCC")
    private String orgIDCardType;

    @ApiModelProperty(value = "印章类型PUBLIC - 公章CONTRACT - 合同专用章FINANCE - 财务专用章PERSONNEL - 人事专用章LEGAL_PERSON - 法定代表人章COMMON - 其他", example = "PERSONNEL")
    private String sealType;


}
