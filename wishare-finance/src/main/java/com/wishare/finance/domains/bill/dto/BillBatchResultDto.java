package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 批量操作结果信息
 *
 * @Author dxclay
 * @Date 2022/8/29
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("批量操作结果信息")
public class BillBatchResultDto {

    @ApiModelProperty("成功数量")
    private int successCount = 0;

    @ApiModelProperty(value = "失败数量")
    private int failCount = 0;

    public BillBatchResultDto() {
    }

    public BillBatchResultDto(int successCount, int failCount) {
        this.successCount = successCount;
        this.failCount = failCount;
    }

    /**
     * 成功
     */
    public void success(){
        this.successCount += 1;
    }

    /**
     * 失败
     */
    public void fail(){
        this.failCount += 1;
    }

}
