package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @description: 客户新增类
 * @author: pgq
 * @since: 2022/12/3 10:29
 * @version: 1.0.0
 */
@ApiModel("客户新增类")
@Getter
@Setter
@ToString
public class AddCustomerF {

    /**
     * 所属业务单元
     */
    @ApiModelProperty("所属业务单元")
    private String pkOrg = "G";

    /**
     * 人员编码
     */
    @ApiModelProperty(value = "人员编码", required = true)
    private String code;

    /**
     * 收费系统传客户名称
     */
    @ApiModelProperty(value = "收费系统传客户名称", required = true)
    private String name;

    /**
     * 所属集团 默认集团：远洋亿家集团 G
     */
    @ApiModelProperty("所属集团 默认集团：远洋亿家集团 G")
    private String pkGroup = "G";

    /**
     * 客户基本信息主键 收费系统不传
     */
    @ApiModelProperty("客户基本信息主键 收费系统不传")
    private Integer pkCustomer;

    /**
     * 客户类型 0=外部单位; 1=内部单位
     */
    @ApiModelProperty("客户类型 0=外部单位; 1=内部单位 默认0")
    private Integer custProp = 0;

    /**
     * 客户基本分类 供应商基本分类目录（A=集团内， B=关联方，C=集团外，D=集团外-商业专用）
     */
    @ApiModelProperty(value = "客户基本分类 供应商基本分类目录（A=集团内， B=关联方，C=集团外，D=集团外-商业专用）", required = true)
    private String pkCustClass;

    /**
     * 纳税人登记号
     */
    @ApiModelProperty(value = "纳税人登记号 没有的时候填空", required = true)
    private String taxPayerId;

}
