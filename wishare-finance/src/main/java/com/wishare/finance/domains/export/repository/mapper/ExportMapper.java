package com.wishare.finance.domains.export.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsReasonBillV;
import com.wishare.finance.domains.bill.dto.AdvanceBillGroupDto;
import com.wishare.finance.domains.bill.dto.ReceivableBillGroupDto;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.entity.TemporaryChargeBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ExportMapper {

    /**
     * 删除账单临时表
     *
     * @param tmpTblName
     * @return
     */
    int dropTmpTbl(String tmpTblName);

    /**
     * 创建账单临时表
     *
     * @param wrapper wrapper
     * @param tblName  tblName
     * @param tblNameSuffix  tblNameSuffix
     * @param tmpTblType  tmpTblType
     */
    int createTmpTbl(@Param(Constants.WRAPPER) QueryWrapper<?> wrapper, String tblName, String tblNameSuffix, int tmpTblType);

    /**
     * 分页查询应收账单信息
     *
     * @param page    page
     * @param tblName  tblName
     * @param tblNameSuffix  tblNameSuffix
     * @param tid  tid
     * @return IPage
     */
    Page<ReceivableBill> queryReceivableBill(Page<Object> page, String tblName, String tblNameSuffix, long tid);

    Integer queryReceivableBillCount(Page<Object> page, String tblName, String tblNameSuffix, long tid);

    /**
     * 分页查询临时账单信息
     *
     * @param page    page
     * @param tblName  tblName
     * @param tblNameSuffix  tblNameSuffix
     * @param tid  tid
     * @return IPage
     */
    Page<TemporaryChargeBill> queryTemporaryChargeBill(Page<Object> page, String tblName, String tblNameSuffix, long tid);

    /**
     * 分组分页查询应收账单（审核）列表
     *
     * @param page    page
     * @param tblName  tblName
     * @param tblNameSuffix  tblNameSuffix
     * @param tid  tid
     * @return IPage
     */
    Page<ReceivableBillGroupDto> queryReceivableBillGroup(Page<Object> page, String tblName, String tblNameSuffix, long tid);

    /**
     * 分页查询预收账单列表
     *
     * @param page    page
     * @param tblName  tblName
     * @param tblNameSuffix  tblNameSuffix
     * @param tid  tid
     * @return IPage
     */
    Page<AdvanceBill> queryAdvanceBill(Page<Object> page, String tblName, String tblNameSuffix, long tid);

    /**
     * 分组分页查询预收账单（审核）列表
     *
     * @param page    page
     * @param tblName  tblName
     * @param tblNameSuffix  tblNameSuffix
     * @param tid  tid
     * @return IPage
     */
    Page<AdvanceBillGroupDto> queryAdvanceBillGroup(Page<Object> page, String tblName, String tblNameSuffix, long tid);

    /**
     * 分组分页查询欠费原因账单
     *
     * @param page    page
     * @param tblName  tblName
     * @param tblNameSuffix  tblNameSuffix
     * @param tid  tid
     * @return IPage
     */
    Page<ArrearsReasonBillV> queryOverDueReasonBill(Page<Object> page, String tblName, String tblNameSuffix, long tid);

}
