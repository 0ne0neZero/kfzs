package com.wishare.finance.infrastructure.remote.vo.external.fangyuan;

import com.wishare.finance.infrastructure.remote.fo.external.fangyuan.FptxxF;
import com.wishare.finance.infrastructure.remote.fo.external.fangyuan.XmxxsF;
import lombok.Data;

import java.util.List;

/**
 * 方圆反参发票信息
 * @author dongpeng
 * @date 2023/7/4 20:30
 */
@Data
public class FptxxsResV {
    private FptxxF fptxx;
    private String nsrsbh;
    private List<XmxxsF> xmxxs;
}
