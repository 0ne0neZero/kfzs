package com.wishare.finance.infrastructure.remote.vo.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/5
 * @Description:
 */
@Getter
@Setter
@ApiModel("用友nc反参")
public class SendresultV {

    @ApiModelProperty("")
    private String billpk;

    @ApiModelProperty("")
    private String bdocid;

    @ApiModelProperty("")
    private String filename;

    @ApiModelProperty("")
    private Integer resultcode;

    @ApiModelProperty("")
    private String resultdescription;

    @ApiModelProperty("")
    private String content;

    @ApiModelProperty("凭证主键")
    /**
     * 凭证主键
     */
    private String pkVoucher;

    @ApiModelProperty("凭证号")
    /**
     * 凭证号
     */
    private String no;

    private List<InvaliddocV> invaliddoc;
}
