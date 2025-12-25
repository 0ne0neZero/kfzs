package com.wishare.finance.infrastructure.remote.fo.msg;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "C端用户移动端提醒信息保存传输参数", description = "C端用户移动端提醒信息保存传输参数")

public class NoticeConsumerD {

    @ApiModelProperty("通知卡片类型")
    @NotNull(message = "通知卡片类型不可为空")
    private Integer noticeCardType;
    @ApiModelProperty("通知卡片类型名称")
    @NotBlank(message = "通知卡片类型名称不可为空")
    private String noticeCardTypeName;
    @ApiModelProperty("标题")
    @NotBlank(message = "标题不可为空")
    private String title;
    @ApiModelProperty("图标")
    @NotBlank(message = "图标不可为空")
    private String icon;
    @ApiModelProperty("目录")
    @NotBlank(message = "目录不可为空")
    private String category;
    @ApiModelProperty("模块编码")
    @NotBlank(message = "模块编码不可为空")
    private String modelCode;
    @ApiModelProperty("模块编码名称")
    @NotBlank(message = "模块编码名称不可为空")
    private String modelCodeName;
    @ApiModelProperty("文本内容")
    private String content;
    @ApiModelProperty("子数据内容展示")
    private List<NoticeConsumerItemDisplayD> displayItems;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "业务系统发起时间不可为空")
    private LocalDateTime noticeTime;
    @ApiModelProperty("用户列表")
    @NotEmpty(message = "被通知的用户列表不可为空")
    private List<String> userId;
}
