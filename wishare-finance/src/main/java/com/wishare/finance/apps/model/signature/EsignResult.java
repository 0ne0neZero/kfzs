package com.wishare.finance.apps.model.signature;

import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @author xujian
 * @date 2022/12/27
 * @Description:
 */
@Getter
@Setter
@ApiModel(value = "查询签署结果")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EsignResult {

    //1 - 签署中 2 - 已完成（所有签署方完成签署） 3 - 已撤销（发起方撤销签署任务）5 - 已过期（签署截止日到期后触发） 7 - 已拒签（签署方拒绝签署）
    @ApiModelProperty(value = "0 -- 正在异步处理pdf，还未发起签署 1 - 签署中 2 - 已完成（所有签署方完成签署） 3 - 已撤销（发起方撤销签署任务）5 - 已过期（签署截止日到期后触发） 7 - 已拒签（签署方拒绝签署）,9:法定单位不可用",example = "2")
    private String status;

    @ApiModelProperty(value = "签署文件结果url", example = "https://saasdev.wisharetec.com/files/wishare-external/ESignTreasureAppService/6085856a-89fb-4711-83fd-a68453d80b2e/20231010/1696925391355105.pdf")
    private String reusltUrl;

    @ApiModelProperty(value = "异常信息")
    private String msg;

    @ApiModelProperty(value = "签署文件结果url集合")
    private List<String> resultUrls;

    @ApiModelProperty(value = "签署文件集合")
    private List<FileVo> fileVos;

    @ApiModelProperty(value = "签署文件")
    private FileVo fileVo;



}
