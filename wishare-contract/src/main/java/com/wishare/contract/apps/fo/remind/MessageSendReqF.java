package com.wishare.contract.apps.fo.remind;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class MessageSendReqF implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "成员ID列表（消息接收者，多个接收者用‘|’分隔，最多支持1000个）。特殊情况：指定为@all，则向该企业应用的全部成员发送")
    private String touser;

    @ApiModelProperty(value = "部门ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数")
    private String toparty;

    @ApiModelProperty(value = "标签ID列表，多个接收者用‘|’分隔，最多支持100个。当touser为@all时忽略本参数")
    private Integer totag;

    @ApiModelProperty(value = "消息类型", required = true)
    private String msgtype;

    @ApiModelProperty(value = "企业应用的id，整型。可在应用的设置页面查看", required = true)
    private String agentid;


    @ApiModelProperty(value = "text")
    private Map<String, String> text;

    @ApiModelProperty(value = "textcard")
    private Map<String, String> textcard;

}






