package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @description: 供应商档案新增类
 * @author: pgq
 * @since: 2022/12/3 10:29
 * @version: 1.0.0
 */
@ApiModel("供应商档案新增类")
@Valid
@Getter
@Setter
@ToString
public class AddSupplierF {

    /**
     * 所属业务单元
     */
    @ApiModelProperty("所属业务单元")
    private String pkOrg = "G";

    /**
     * 人员编码
     */
    @ApiModelProperty("人员编码")
    private String code;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 启用状态 1=未启用; 2=已启用; 3=已停用;
     */
    @ApiModelProperty("启用状态"
        + "1=未启用;\n"
        + "2=已启用;\n"
        + "3=已停用;"
        + "默认2")
    private Integer enableState = 2;

    /**
     * 所属集团 默认集团：远洋亿家集团 G
     */
    @ApiModelProperty("所属集团 默认集团：远洋亿家集团 G")
    private String pkGroup = "G";

    /**
     * 供应商类型 0=外部单位；1=内部单位 默认 0
     */
    @ApiModelProperty("供应商类型 0=外部单位；1=内部单位 默认 0")
    private Integer supProp = 0;

    /**
     * 供应商基本分类 供应商基本分类目录（A=集团内， B=关联方，C=集团外，D=集团外-商业专用）
     */
    @ApiModelProperty("供应商基本分类 供应商基本分类目录（A=集团内， B=关联方，C=集团外，D=集团外-商业专用")
    private String pkSupplierClass;

    /**
     * 纳税人登记号
     */
    @ApiModelProperty("纳税人登记号 ")
    private String taxPayerId;

    /**
     * 创建人 默认收费系统
     */
    @ApiModelProperty("创建人 默认收费系统")
    private String creator;

    /**
     * 创建时间 示例 2022-01-01 12:10:55
     */
    @ApiModelProperty("创建时间")
    private String creationTime;
}
