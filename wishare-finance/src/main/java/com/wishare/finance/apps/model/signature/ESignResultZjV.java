package com.wishare.finance.apps.model.signature;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * 功能解释
 *
 * @author 龙江锋
 * @date 2023/10/10 10:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ESignResultZjV {

    @ApiModelProperty(value = " 1 - 签署中(作废中) 2 - 已完成（所有签署方完成签署")
    private String status;

    @ApiModelProperty(value = "签署文件结果url集合")
    private List<String> resultUrls;

    @ApiModelProperty(value = "签署文件集合")
    private List<FileVo> fileVos;


}
