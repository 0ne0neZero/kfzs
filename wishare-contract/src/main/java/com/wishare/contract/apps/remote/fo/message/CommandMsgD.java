package com.wishare.contract.apps.remote.fo.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *  消息中心接收到的指令
 * </p>
 *
 * @author light
 * @since 2022/10/19
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CommandMsgD<T> {
    private MsgCommandEnum commandType;
    private T data;
}
