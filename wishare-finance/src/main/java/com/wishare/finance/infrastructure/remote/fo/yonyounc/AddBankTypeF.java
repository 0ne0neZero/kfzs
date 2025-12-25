package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 新增银行类型类
 * @author: pgq
 * @since: 2022/12/6 10:13
 * @version: 1.0.0
 */
@ApiModel("银行类型新增类")
@Valid
@Getter
@Setter
public class AddBankTypeF {

    /**
     * 银行类别编码
     */
    @ApiModelProperty("银行类别编码")
    private String code;

    /**
     * 银行类别名称
     */
    @ApiModelProperty("银行类别名称")
    private String name;

    /**
     * 助记码
     */
    @ApiModelProperty("助记码")
    private String mnecode;

    /**
     * 所属集团 默认集团：远洋亿家集团 G
     */
    @ApiModelProperty("所属集团 默认集团：远洋亿家集团 G")
    private String pkGroup = "G";
//
//    /**
//     * 所属业务单元
//     */
//    @ApiModelProperty("所属业务单元")
//    @JSONField(name = "pk_org")
//    @NotBlank(message = "所属业务单元不能为空")
//    @Size(max = 20, message = "所属业务单元最大长度为20")
//    private String pkOrg;
}
