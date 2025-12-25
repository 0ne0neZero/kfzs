package com.wishare.finance.domains.bill.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.model.bill.vo.GatherDetailV;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.invoicereceipt.dto.GatherDetailInfo;
import com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core.PushZJBusinessBill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  收款明细Mapper 接口
 * </p>
 *
 * @author dxclay
 * @since 2022-12-19
 */
@Mapper
public interface GatherDetailMapper extends BaseMapper<GatherDetail> {

    /**
     * 批量更新账单详情的账单推凭状态
     * @param concatIds
     * @param state
     */
    void batchUpdateDetailInferenceSate(@Param("list") List<Long> concatIds, @Param("state") int state);

    /**
     * 根据id和推凭状态查询账单
     * @param queryWrapper
     * @return
     */
    List<BillInferenceV> listInferenceInfoByIdAndInfer(@Param(Constants.WRAPPER) QueryWrapper<GatherDetail> queryWrapper);

    int updateHandRecState(@Param("billId") Long billId, @Param("code") int code, @Param("supCpUnitId") String supCpUnitId);

    @InterceptorIgnore(tenantLine = "on")
    List<GatherDetail> queryByGatherBillIdIgnore(@Param("id") Long id, @Param("supCpUnitId") String supCpUnitId);

    List<GatherDetail> listGatherBillByRecIdAndSupCpUnitId(@Param("id") Long id, @Param("supCpUnitId") String supCpUnitId);

    List<GatherDetail> listGatherBillByRecIdAndSupCpUnitIds(@Param("ids") List<Long> recBillIds, @Param("supCpUnitId") String supCpUnitId);

    /**
     * todo 高明-资金收款单改动-税率税额等
     **/
    List<PushZJBusinessBill> getFundReceiptsBillZJList(@Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
                                                       @Param("gatherBillTableName")String gatherBillTableName,
                                                       @Param("gatherDetailTableName")String gatherDetailTableName,
                                                       @Param("receivableBillTableName")String receivableBillTableName);

    /**
     * 根据roomIds修改成本中心
     * @param roomIds
     * @param supCpUnitId
     * @param costCenterId
     * @param costCenterName
     */
    void updateCostMsgByRoomIds(@Param("list") List<String> roomIds,@Param("billType") Integer billType,
            @Param("supCpUnitId") String supCpUnitId, @Param("costCenterId") Long costCenterId,
            @Param("costCenterName") String costCenterName,@Param("gatherDetailName")String gatherDetailName,@Param("receivableBillName")String receivableBillName);


    /**
     * 根据项目ids+账单类型 修改成本中心信息 存在于临时、应收
     *
     * @param billType           账单类型
     * @param supCpUnitId        项目id
     * @param costCenterId       成本中心id
     * @param costCenterName     成本中心名称
     * @param gatherDetailName   分表名
     * @param receivableBillName 分表名
     */
    void updateCostMsgBySupCpUnitId(@Param("billType") Integer billType,
            @Param("supCpUnitId") String supCpUnitId, @Param("costCenterId") Long costCenterId,
            @Param("costCenterName") String costCenterName,@Param("gatherDetailName")String gatherDetailName,@Param("receivableBillName")String receivableBillName);



    /**
     * 根据roomIds修改成本中心
     * @param roomIds
     * @param supCpUnitId
     * @param costCenterId
     * @param costCenterName
     */
    void updateCostMsgByRoomIdsAdv(@Param("list") List<String> roomIds,
            @Param("supCpUnitId") String supCpUnitId, @Param("costCenterId") Long costCenterId,
            @Param("costCenterName") String costCenterName,@Param("gatherDetailName")String gatherDetailName);


    /**
     * 根据项目id 修改成本中心
     *
     * @param supCpUnitId      项目id
     * @param costCenterId     成本中心id
     * @param costCenterName   成本中心名称
     * @param gatherDetailName 分表名
     */
    void updateCostMsgBysupCpUnitIdAdv(
            @Param("supCpUnitId") String supCpUnitId, @Param("costCenterId") Long costCenterId,
            @Param("costCenterName") String costCenterName,@Param("gatherDetailName")String gatherDetailName);

    Long statisticsGather(@Param("ew") QueryWrapper<?> queryModel,@Param("gatherBillName")String gatherBillName,@Param("gatherDetailName")String gatherDetailName);

    List<GatherDetail> getAllDetailByGatherBillIds(@Param("gatherBillIds") List<Long> gatherBillIds, @Param("supCpUnitId")String supCpUnitId);

    List<GatherDetail> getAllDetailByIds(@Param("ids") List<Long> ids, @Param("supCpUnitId")String supCpUnitId);

    Page<GatherDetailV> queryPageGatherDetail(Page<Object> of, @Param("ew") QueryWrapper<?> queryPayModel);
}
