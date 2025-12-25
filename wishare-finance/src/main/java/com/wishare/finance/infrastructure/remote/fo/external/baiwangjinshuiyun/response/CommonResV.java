package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.response;

import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Service;

/**
 * 返回响应信息
 * @author dongpeng
 * @date 2023/10/25 20:24
 */
@Data
@Getter
public class CommonResV {

    /**
     * 返回代码
     */
    private String code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 描述
     */
    private String renson;

    /**
     * 返回具体信息对象
     */
    private String data;
}
