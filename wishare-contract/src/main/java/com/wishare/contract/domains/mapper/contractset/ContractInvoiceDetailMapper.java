package com.wishare.contract.domains.mapper.contractset;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wishare.contract.apps.remote.vo.InvoiceInfoRv;
import com.wishare.contract.domains.entity.contractset.ContractInvoiceDetailE;
import com.wishare.contract.domains.vo.contractset.InvoiceDetailPlanV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 合同开票明细表
 * </p>
 *
 * @author ljx
 * @since 2022-09-26
 */
@Mapper
public interface ContractInvoiceDetailMapper extends BaseMapper<ContractInvoiceDetailE> {

    List<InvoiceDetailPlanV> selectByCollectionPlanId(@Param("contractId") Long contractId,
                                                      @Param("collectionPlanId") Long collectionPlanId,
                                                      @Param("tenantId") String tenantId);

    void deleteByCollectionPlanId(Long collectionPlanId);

    void deleteByContractId(Long contractId);

    /**
     * 更新发票相关信息
     *
     * @param invoiceInfoRv
     */
    void updateInvoiceInfo(InvoiceInfoRv invoiceInfoRv);

    Long selectCountCurrentDate();

    @Select({"SELECT id,collectionPlanId,invoiceId,invoiceStatus,tenantId FROM contract_invoice_detail WHERE invoiceStatus=1 AND invoiceId IS NOT NULL" +
            " AND invoiceApplyTime > #{yesterday} ORDER BY invoiceApplyTime LIMIT #{limit}"})
    List<ContractInvoiceDetailE> selectInvoiceStatus1ByYesterday(@Param("yesterday") LocalDateTime yesterday, @Param("limit") int limit);

    @Update({"UPDATE contract_invoice_detail SET invoiceStatus=0,gmtModify=NOW() WHERE id=#{id}"})
    void updateInvoiceStateSuccess(@Param("id") Long id);

    @Update({"UPDATE contract_invoice_detail SET invoiceStatus=2,failReason=#{failReason},gmtModify=NOW() WHERE id=#{id}"})
    void updateInvoiceStateFail(@Param("id") Long id, @Param("failReason") String failReason);
}
