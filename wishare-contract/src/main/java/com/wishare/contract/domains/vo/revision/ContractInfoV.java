package com.wishare.contract.domains.vo.revision;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/12/4/19:52
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractInfoV {


    @ApiModelProperty(value = "链接地址")
    private String linkAdress;


    @ApiModelProperty(value = "合同ID")
    private String id;

    @ApiModelProperty(value = "是否保存成功")
    private Boolean isSucess;

    @ApiModelProperty(value = "SSO链接地址")
    private String ssoLinkAdress;
}
