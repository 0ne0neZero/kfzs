package com.wishare.finance.infrastructure.remote.fo.msg;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xujian
 * @date 2022/10/25
 * @Description:
 */
@Getter
@Setter
public class MsgSmsRf {

    /**
     * 手机号码
     */
    private String mobileNum;

    /**
     * 模板变量 格式：["param1","param2", ...]
     */
    private String[] params;

    /**
     * 模板id
     */
    private Long templateId;
}
