package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/8
 * @Description:
 */
@Getter
@Setter
@ApiModel("保存凭证ufinterface")
public class UfinterfaceComF {

    private String account;

    private String billtype;

    private String businessunitcode;

    private String filename;

    private String groupcode;

    private String isexchange;

    private String orgcode;

    private String receiver;

    private String replace;

    private String roottag;

    private String sender;
}
