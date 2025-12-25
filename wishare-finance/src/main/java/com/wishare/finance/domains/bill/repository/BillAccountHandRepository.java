package com.wishare.finance.domains.bill.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.bill.consts.enums.*;
import com.wishare.finance.domains.bill.dto.AccountHandCountDto;
import com.wishare.finance.domains.bill.dto.AccountHandTotalDto;
import com.wishare.finance.domains.bill.entity.BillAccountHand;
import com.wishare.finance.domains.bill.repository.mapper.BillAccountHandMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.finance.infrastructure.utils.WrapperUtils;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 交账信息表 服务实现类
 * </p>
 *
 * @author dxclay
 * @since 2023-01-11
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillAccountHandRepository extends ServiceImpl<BillAccountHandMapper, BillAccountHand> {

    /**
     * 根据searchF 分页查询信息
     * @param queryF 查询参数
     * @return 分页列表
     */
    public Page<BillAccountHand> getPageBySearch(PageF<SearchF<?>> queryF){
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "bah." + BillSharedingColumn.收款明细.getColumnName());
        QueryWrapper<?> queryModel = queryF.getConditions().getQueryModel();
        queryModel.and(
                QueryWrapper -> QueryWrapper.gt("pay_amount", 0).or()
                        .eq("on_account",BillOnAccountEnum.已挂账.getCode())
        ).ne("refund_state",BillRefundStateEnum.已退款.getCode())
         .ne("carried_state",BillCarriedStateEnum.已结转.getCode())
         .eq("approved_state",BillApproveStateEnum.已审核.getCode());
        WrapperUtils.logWrapper(queryModel);
        return baseMapper.queryPageBySearch(Page.of(queryF.getPageNum(), queryF.getPageSize()),
            RepositoryUtil.putLogicDeleted(queryModel));
    }

    /**
     * 查询交账合计数据
     * @param queryF 查询参数
     * @return 合计数据
     */
    public AccountHandTotalDto getTotal(PageF<SearchF<?>> queryF){
        PageQueryUtils.validQueryContainsFieldAndValue(queryF, "bah." + BillSharedingColumn.交账信息.getColumnName());
        return baseMapper.queryTotal(RepositoryUtil.putLogicDeleted(queryF.getConditions().getQueryModel()));
    }

    /**
     * 根据账单id和账单类型查询交账明细
     * @param billId
     * @param billType
     * @return
     */
    public BillAccountHand getByBillIdAndType(Long billId, Integer billType, String supCpUnitId){
        return getOne(new QueryWrapper<BillAccountHand>().eq("bill_id", billId)
            .eq("bill_type", billType).eq("sup_cp_unit_id", supCpUnitId));
    }

    /**
     * 根据账单id和账单类型和上级收费单元id查询交账明细
     * @param billId
     * @param billType
     * @param supCpUnitId
     * @return
     */
    public BillAccountHand getBillIdAndTypeAndSupCpUnitId(Long billId, Integer billType,String supCpUnitId){
        return getOne(new LambdaQueryWrapper<BillAccountHand>().eq(BillAccountHand::getBillId, billId).eq(BillAccountHand::getBillType, billType).eq(StringUtils.isNotBlank(supCpUnitId), BillAccountHand::getSupCpUnitId,supCpUnitId));
    }

    /**
     * 根据账单id列表和账单类型查询交账明细
     * @param billIds
     * @param billType
     * @return
     */
    public List<BillAccountHand> getListByBillIdsAndTypes(List<Long> billIds, Integer billType,String supCpUnitId){
        return list(new QueryWrapper<BillAccountHand>().in("bill_id", billIds)
            .eq("bill_type", billType).eq("sup_cp_unit_id", supCpUnitId));
    }


    /**
     * 批量交账
     * @param queryWrapper 查询条件
     * @return
     */
    public int updateHandAccount(QueryWrapper<?> queryWrapper) {
        return baseMapper.updateOfHandAccount(RepositoryUtil.putLogicDeleted((queryWrapper)));
    }

    /**
     * 应收账单交账
     * @param queryWrapper 查询条件
     * @return
     */
    public int updateRecBillOfHandAccount(QueryWrapper<?> queryWrapper, String supCpUnitId, String receivableBillName, String billAccountHandName){
        if(StringUtils.isBlank(supCpUnitId)) {
            throw new RuntimeException("更新应收账单必须传入参数:上级收费单元 supCpUnitId !");
        }
        return baseMapper.updateRecBillOfHandAccount(RepositoryUtil.putLogicDeleted(queryWrapper), supCpUnitId, receivableBillName, billAccountHandName);
    }

    /**
     * 预收账单交账
     * @param queryWrapper 查询条件
     * @return
     */
    public int updateAdvBillOfHandAccount(QueryWrapper<?> queryWrapper, String billAccountHandName){
        return baseMapper.updateAdvBillOfHandAccount(RepositoryUtil.putLogicDeleted(queryWrapper), billAccountHandName);
    }

    /**
     * 查询可交账数量
     * @param queryWrapper
     * @return
     */
    public List<AccountHandCountDto> getHandAccountCount(QueryWrapper<?> queryWrapper){
        return baseMapper.queryHandAccountCount(RepositoryUtil.putLogicDeleted(queryWrapper));
    }

    /**
     * 分页查询交账账单id列表
     * @param queryWrapper 查询条件
     * @return 账单id列表
     */
    public Page<String> getHandAccountBillIdPage(Page<String> page, QueryWrapper<?> queryWrapper) {
        return baseMapper.getHandAccountBillIdPage(page, RepositoryUtil.putLogicDeleted((queryWrapper)));
    }

    /**
     * 更新交账状态
     * @param billId 账单id
     * @param billType 账单类型
     * @param accountHandedState 账单状态
     * @return
     */
    public boolean updateHandState(Long billId, BillTypeEnum billType, BillAccountHandedStateEnum accountHandedState, String supCpUnitId){
        return update(new UpdateWrapper<BillAccountHand>()
                .set("account_handed", accountHandedState.getCode())
                .eq("bill_id", billId)
                .eq("bill_type", billType.getCode())
                .eq("sup_cp_unit_id", supCpUnitId));
    }

    public List<BillAccountHand> queryNoInvoiceInfoList(String supCpUnitId){
        return baseMapper.queryNoInvoiceInfoList(supCpUnitId);
    }

    /**
     * 获取全部交账信息表的项目，目前一定会走全表，后续再优化
     * @return
     */
    public List<String> queryAllCommunityIdList() {
        return baseMapper.queryAllCommunityIdList();
    }
}
