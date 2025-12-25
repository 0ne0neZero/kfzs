package com.wishare.finance.apps.pushbill.vo;

import com.wishare.finance.apps.model.reconciliation.vo.FlowDetailSimpleVo;
import com.wishare.finance.apps.model.reconciliation.vo.FlowRecordSimpleVo;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillZJ;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author longhuadmin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("all")
public class VoucherBillZJV2 extends VoucherBillZJV {

    private String recordSimpleStr;

    private String flowDetailSimpleStr;

    private List<FlowRecordSimpleVo> recordSimpleVoList;

    private List<FlowDetailSimpleVo> flowDetailSimpleVoList;

}
