package com.wishare.contract.apps.remote.fo.image;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 安全校验节点
 *
 * @author 龙江锋
 * @date 2023/8/8 13:39
 */
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
