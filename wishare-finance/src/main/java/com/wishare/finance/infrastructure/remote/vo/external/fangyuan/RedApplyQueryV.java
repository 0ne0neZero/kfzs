package com.wishare.finance.infrastructure.remote.vo.external.fangyuan;

import lombok.Data;

import java.util.List;

/**
 * 方圆红字确认返回参数
 * @author dongpeng
 * @date 2023/7/4 20:34
 */
@Data
public class RedApplyQueryV {
    private String code;
    private DataInfoRedV data;
    private String data2;
    private String data3;
    private String mess;

}


