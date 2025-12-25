package com.wishare.contract.domains.vo.revision.log;

import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
/**
* <p>
* 合同改版动态记录表视图对象
* </p>
*
* @author chenglong
* @since 2023-07-12
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同改版动态记录表视图对象", description = "合同改版动态记录表视图对象")
public class RevisionLogV {

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;
    /**
    * 关联数据ID
    */
    @ApiModelProperty("关联数据ID")
    private String bizId;
    /**
    * 标题
    */
    @ApiModelProperty("标题")
    private String title;
    /**
    * 操作类型code
    */
    @ApiModelProperty("操作类型code")
    private String actionCode;
    /**
    * 操作类型
    */
    @ApiModelProperty("操作类型")
    private String action;
    /**
    * 内容
    */
    @ApiModelProperty("内容")
    private String content;
    /**
    * 租户id
    */
    @ApiModelProperty("租户id")
    private String tenantId;
    /**
    * 创建人
    */
    @ApiModelProperty("创建人")
    private String creator;
    /**
    * 创建人名称
    */
    @ApiModelProperty("创建人名称")
    private String creatorName;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * 操作人
    */
    @ApiModelProperty("操作人")
    private String operator;
    /**
    * 操作人名称
    */
    @ApiModelProperty("操作人名称")
    private String operatorName;
    /**
    * 操作时间
    */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

}
