package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/8
 * @Description:
 */
@Getter
@Setter
@ApiModel("保存凭证业务数据入参")
public class UfinterfaceF extends UfinterfaceComF {

    @Valid
    @ApiModelProperty("业务")
    private VoucherF voucher;
    
    public UfinterfaceF create() {
        this.setAccount("develop");                             // 等待提供
        this.setBilltype("vouchergl");                          // 传凭证就是这个类型写死
        this.setBusinessunitcode("develop");                    // 等待提供
        this.setFilename("");
        this.setGroupcode("");
        this.setIsexchange("");
        this.setOrgcode("");
        this.setReceiver("");                                   // 可以不用，默认空
        this.setReplace("");
        this.setRoottag("");
        this.setSender("default");
        return this;
    }

}
