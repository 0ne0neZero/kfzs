package com.wishare.finance.infrastructure.remote.vo.contract;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("中交文件响应")
public class ZJFileVo  {

    private String fileId;

    private String fileKey;

    private String name;

    private String suffix;

    private Long size;

    private Integer type;

    private String billNo;
}
