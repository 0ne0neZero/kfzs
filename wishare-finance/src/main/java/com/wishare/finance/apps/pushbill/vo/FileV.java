package com.wishare.finance.apps.pushbill.vo;

import com.wishare.finance.infrastructure.remote.vo.contract.ZJFileVo;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("文件上传信息")
public class FileV {
    @ApiModelProperty(value = "中交返回文件信息")
    private ZJFileVo zjFileVo;
    @ApiModelProperty(value = "惠享云返回文件信息")
    private FileVo fileVo;

    @ApiModelProperty(value = "报账单编号")
    private String billNo;

}
