package com.wishare.finance.infrastructure.remote.fo.external.fangyuan;

import lombok.Data;

import java.util.List;

/**
 * @author dongpeng
 * @date 2023/7/4 20:42
 * 发票开具及确认单请求体
 */
@Data
public class InvoiceDataReqF {
    private FptxxF fptxx;
    private List<XmxxsF> xmxxs;
    private List<RealPropertyRentInfoF> bdcjyzlfwxxs;

}
