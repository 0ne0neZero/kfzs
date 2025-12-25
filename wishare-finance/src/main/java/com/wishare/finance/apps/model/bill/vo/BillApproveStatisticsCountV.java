package com.wishare.finance.apps.model.bill.vo;

import com.wishare.finance.domains.bill.dto.BillApproveTotalDto;
import com.wishare.finance.domains.bill.dto.BillApproveTotalNewDto;
import com.wishare.finance.infrastructure.remote.enums.OperateTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author fxl
 * @describe
 * @date 2023/11/11
 */
@Setter
@Getter
@ApiModel("账单审核合计信息（新）")
public class BillApproveStatisticsCountV {

    @ApiModelProperty("初始化审核数量")
    private Long  initializeCount = 0L;

    @ApiModelProperty("变更审核数量")
    private Long  changeCount = 0L;

    @ApiModelProperty("初始化应收审核数量")
    private Long  initializeReceivableCount = 0L;

    @ApiModelProperty("初始化预收审核数量")
    private Long  initializeAdvanceCount = 0L;

    @ApiModelProperty("初始化临时审核数量")
    private Long  initializeTempCount = 0L;

    @ApiModelProperty("变更应收审核数量")
    private Long  changeReceivableCount= 0L;

    @ApiModelProperty("变更预收审核数量")
    private Long  changeAdvanceCount = 0L;

    @ApiModelProperty("变更临时审核数量")
    private Long  changeTempCount = 0L;


    public void dealAdvanceTotal(List<BillApproveTotalDto> advanceBillApproveTotals) {
        if (CollectionUtils.isEmpty(advanceBillApproveTotals)) {
            return;
        }
        for (BillApproveTotalDto item : advanceBillApproveTotals) {
            Long total = item.getTotal();
            if (Objects.isNull(item.getApprovedState()) ||
                    OperateTypeEnum.生成审核.getCode().equals(item.getOperateType())) {
                this.initializeCount = this.initializeCount + total;
                this.initializeAdvanceCount = this.initializeAdvanceCount + total;
            }else {
                this.changeCount = this.changeCount + total;
                this.changeAdvanceCount = this.changeAdvanceCount + total;
            }
        }
    }


    /**
     * 处理审核统计数据
     * @param billApproveTotalNews
     * @param type 0：初始化审核 1：变更审核
     */
    public void dealReceivableBillApproveTotal(List<BillApproveTotalNewDto> billApproveTotalNews,Integer type) {
        if (CollectionUtils.isEmpty(billApproveTotalNews)) {
            return;
        }
        for (BillApproveTotalNewDto item : billApproveTotalNews) {
            Long total = item.getTotal();
            if (item.getIsInit() && 0==type) {
                this.initializeCount = this.initializeCount + total;
                if (item.getBillType() == 1) {
                    this.initializeReceivableCount = this.initializeReceivableCount + total;
                }else {
                    this.initializeTempCount = this.initializeTempCount + total;
                }
            }else if (!item.getIsInit() && 1==type){
                this.changeCount = this.changeCount + total;
                if (item.getBillType() == 1) {
                    this.changeReceivableCount = this.changeReceivableCount + total;
                }else {
                    this.changeTempCount = this.changeTempCount + total;
                }
            }
        }
    }

}
