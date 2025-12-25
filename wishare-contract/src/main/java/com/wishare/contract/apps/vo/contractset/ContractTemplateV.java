package com.wishare.contract.apps.vo.contractset;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("合同范本返回信息")
public class ContractTemplateV {

    @ApiModelProperty("合同范本id")
    private Long id;

    @ApiModelProperty("合同范本名称")
    private String name;

    @ApiModelProperty("合同分类id")
    private Long categoryId;

    @ApiModelProperty("父合同范本id")
    private Long parentId;

    @ApiModelProperty("范本版本")
    private BigDecimal version;

    @ApiModelProperty("引用次数")
    private Integer useCount;

    @ApiModelProperty("引用状态：0未被引用  1被引用")
    private Integer useStatus;

    @ApiModelProperty("文件url")
    private String fileUrl;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态：0启用，1草稿，2禁用")
    private Integer status;

    @ApiModelProperty("是否删除：0未删除，1已删除")
    private Integer deleted;

    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("创建人id")
    private String creator;

    @ApiModelProperty("创建人名称")
    private String creatorName;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("操作人id")
    private String operator;

    @ApiModelProperty("操作人名称")
    private String operatorName;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModify;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("合同范本id路径")
    private String path;

    @ApiModelProperty("合同分类路径名")
    private String categoryPathName;

}
