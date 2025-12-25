package com.wishare.contract.apps.remote.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.starter.beans.Tree;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author hhb
 * @describe
 * @date 2025/11/4 0:06
 */
@Getter
@Setter
@ApiModel
@Accessors(chain = true)
public class TreeMenuV extends Tree<TreeMenuV,Long> {
    @ApiModelProperty("菜单名称")
    private String name;
    @ApiModelProperty("菜单层级:一级菜单为0,二级为1...")
    private Integer level;
    @ApiModelProperty("菜单类型:1文件夹 2页面 3按钮(无二级页面) 4按钮(有二级页面)")
    private Integer type;
    @ApiModelProperty("菜单排序")
    private Integer sort;
    @ApiModelProperty("菜单功能链接，如果是原生页面，当菜单类型为“页面”时，该字段为页面code，需要与前端保持一致")
    private String url;
    @ApiModelProperty(value = "是否是列表页面/菜单：0否  1是")
    private Integer list;
    @ApiModelProperty("图标url")
    private FileVo iconUrl;
    @ApiModelProperty("链接跳转类型:0后台嵌入（即和当前原生菜单一样打开） 1页面重定向（页面地址更新为配置的地址） 2 新窗口（新开窗口打开）")
    private Integer urlJumpType;
    @ApiModelProperty("是否禁用：0否 1是，默认1")
    private Integer disabled;
    @ApiModelProperty("操作人名称")
    private String operatorName;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

    @ApiModelProperty("表单代码")
    private String formCode;

    @ApiModelProperty("是否可见：0否，1是")
    private Byte invisible = 1;
}