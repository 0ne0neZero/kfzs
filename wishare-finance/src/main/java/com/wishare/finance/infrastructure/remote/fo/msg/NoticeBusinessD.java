package com.wishare.finance.infrastructure.remote.fo.msg;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.tools.starter.vo.FileVo;
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

/**
 * @author fxl
 * @describe
 * @date 2024/1/11
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "B端用户移动端提醒信息保存传输参数", description = "B端用户移动端提醒信息保存传输参数")
public class NoticeBusinessD {

    @ApiModelProperty("业务id")
    private String businessId;
    @ApiModelProperty("消息卡片编码")
    private String cardCode ;
    @ApiModelProperty("通知卡片类型")
    @NotNull(message = "通知卡片类型不可为空")
    private Integer noticeCardType;
    @ApiModelProperty("通知卡片类型名称")
    @NotBlank(message = "通知卡片类型名称不可为空")
    private String noticeCardTypeName;
    @ApiModelProperty("标题")
    @NotBlank(message = "标题不可为空")
    private String title;
    @ApiModelProperty("图标为空时，使用默认图标，由前端根据UI确定")
    private String icon;
    @ApiModelProperty("目录")
    @NotBlank(message = "目录不可为空")
    private String category = "业务通知";
    @ApiModelProperty("文本内容")
    private String content;
    @ApiModelProperty("模块编码")
    private String modelCode;
    @ApiModelProperty("模块编码名称")
    private String modelCodeName;
    @ApiModelProperty("子数据内容展示")
    private List<NoticeBusinessItemDisplayD> displayItems;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "业务系统发起时间不可为空")
    private LocalDateTime noticeTime;
    @ApiModelProperty("链接图片的URL")
    private FileVo linkImage;
    @ApiModelProperty("拓展信息")
    private String ext;
    @ApiModelProperty("用户列表")
    @NotEmpty(message = "被通知的用户列表不可为空")
    private List<String> userId;

    @ApiModelProperty("任务消息-自定义内容对象")
    private TaskMsgDO taskMsg;
    @ApiModelProperty("过期时间")
    private Long overTime;
    @ApiModelProperty("是否弹窗消息,0:否;1:是")
    private String wbsMessage;
    @ApiModelProperty("是否签收，0否，1是")
    private Integer isSignFor;
}
