package com.wishare.contract.apps.vo.contractset;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("合同范本树返回信息")
public class ContractTemplateTreeV extends Tree<ContractTemplateTreeV, Long> {

    @ApiModelProperty("合同范本名称")
    private String name;

    @ApiModelProperty("合同分类id")
    private Long categoryId;

    @ApiModelProperty("合同分类id路径")
    private String categoryPath;

    @ApiModelProperty("合同分类名称路径")
    private String categoryPathName;

    @ApiModelProperty("合同范本id路径")
    private String path;

    @ApiModelProperty("父合同范本id")
    private Long parentId;

    @ApiModelProperty("范本版本")
    private String version;

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

    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty("是否删除：0未删除，1已删除")
    private Integer deleted;

    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("创建人id")
    private String creator;

    @ApiModelProperty("创建人名称")
    private String creatorName;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("操作人id")
    private String operator;

    @ApiModelProperty("操作人名称")
    private String operatorName;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

    @ApiModelProperty("文件名")
    private String fileName;
}
