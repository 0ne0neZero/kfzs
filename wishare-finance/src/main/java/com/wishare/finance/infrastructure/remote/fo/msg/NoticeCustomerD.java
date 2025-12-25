package com.wishare.finance.infrastructure.remote.fo.msg;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * C端站内信
 */
@Data
public class NoticeCustomerD {
    @ApiModelProperty("消息卡片编码")
    private String cardCode;
    @ApiModelProperty("通知卡片类型")
    @NotNull(message = "通知卡片类型不可为空")
    private Integer noticeCardType;
    @ApiModelProperty("通知卡片类型名称")
    @NotBlank(message = "通知卡片类型名称不可为空")
    private String noticeCardTypeName;
    @ApiModelProperty("标题")
    @NotBlank(message = "标题不可为空")
    private String title = "站内信";
    @ApiModelProperty(value = "图标",notes = "图标为空时，使用默认图标")
    private String icon = "";
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
    private List<NoticeCustomerItemDisplayD> displayItems;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "业务系统发起时间不可为空")
    private LocalDateTime noticeTime = LocalDateTime.now();
    @ApiModelProperty("拓展信息")
    private String ext;
    @ApiModelProperty("用户列表")
    @NotEmpty(message = "被通知的用户列表不可为空")
    private List<String> userId;
    @ApiModelProperty("租户ID")
    private String tenantId;
}
