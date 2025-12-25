package com.wishare.finance.domains.voucher.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.voucher.fo.UpdateVoucherDetailF;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitDetailE;
import com.wishare.finance.domains.configure.businessunit.enums.BusinessDetailTypeEnum;
import com.wishare.finance.domains.configure.businessunit.repository.BusinessUnitDetailRepository;
import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.entity.Voucher;
import com.wishare.finance.domains.voucher.entity.VoucherBusinessDetail;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherInfoMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 凭证资源库
 *
 * @author dxclay
 * @since 2023-03-10
 */
@Service
public class VoucherInfoRepository extends ServiceImpl<VoucherInfoMapper, Voucher> {

    @Setter(onMethod_ = {@Autowired})
    private BusinessUnitDetailRepository businessUnitDetailRepository;

    /**
     * 根据查询条件获取分页详情
     *
     * @param searchPageF 查询条件
     * @return
     */
    public Page<Voucher> pageBySearch(PageF<SearchF<?>> searchPageF) {
        //元转分
        searchPageF.getConditions().getFields().forEach(e -> {
            if ("amount".equals(e.getName())) {
                e.setValue(new BigDecimal(e.getValue().toString()).multiply(new BigDecimal(100)));
            }
            if ("account_book_id".equals(e.getName())){
                e.setName("vi.account_book_id");
            }
        });
        List<Long> statutoryBodyIds = filterStatutoryBodyId(searchPageF);
        QueryWrapper<?> queryWrapper = RepositoryUtil.putLogicDeleted(searchPageF.getConditions().getQueryModel(),"vi").orderByDesc("gmt_create");
        if (CollectionUtils.isNotEmpty(statutoryBodyIds)) {
            // 如果有法定单位传进来，代表有权限
            if (!queryStatutoryBodyId(statutoryBodyIds, queryWrapper)) {
                return new Page<>();
            }
        }

        Page<Voucher> voucherPage = baseMapper.selectBySearch(RepositoryUtil.convertMPPage(searchPageF), queryWrapper);

        voucherPage.getRecords().forEach(
                t -> {
                    if (CollectionUtils.isNotEmpty(t.getCostCenters())) {
                        t.setCostCenterId(t.getCostCenters().get(0).getCostCenterId());
                        t.setCostCenterName(t.getCostCenters().get(0).getCostCenterName());
                    }
                    if (t.getEvenType() != null) {
                        t.setEvenValue(VoucherEventTypeEnum.valueOfByCode(t.getEvenType()).getValue());
                    }

                }
        );

        return voucherPage;
    }

    /**
     * 根据法定单位id查询业务单元单位id
     * @param statutoryBodyIds
     * @param queryWrapper
     */
    private boolean queryStatutoryBodyId(List<Long> statutoryBodyIds, QueryWrapper<?> queryWrapper) {
        if(EnvConst.YUANYANG.equals(EnvData.config)){
            LambdaQueryWrapper<BusinessUnitDetailE> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(BusinessUnitDetailE::getRelevanceId, statutoryBodyIds);
            wrapper.eq(BusinessUnitDetailE::getType, BusinessDetailTypeEnum.法定单位.getCode());
            wrapper.eq(BusinessUnitDetailE::getDeleted, DataDeletedEnum.NORMAL.getCode());
            List<BusinessUnitDetailE> detailES = businessUnitDetailRepository.list(wrapper);
            if(CollectionUtils.isEmpty(detailES)){
                return false;
            }
            queryWrapper.in("statutory_body_id", detailES.stream().filter(e -> Objects.nonNull(e.getBusinessUnitId())).map(BusinessUnitDetailE::getBusinessUnitId).collect(Collectors.toList()));
        }
        return true;
    }

    /**
     * 过滤statutoryBody_id
     * @param searchPageF
     * @return
     */
    private static List<Long> filterStatutoryBodyId(PageF<SearchF<?>> searchPageF) {
        List<Field> fieldsToRemove = new ArrayList<>();
        List<Long> statutoryBodyIds = new ArrayList<>();
        searchPageF.getConditions().getFields().forEach(e -> {
            if ("statutoryBody_id".equals(e.getName())) {
                fieldsToRemove.add(e);
                statutoryBodyIds.add(Long.valueOf(e.getValue().toString()));
            }
        });
        if(CollectionUtils.isNotEmpty(fieldsToRemove)){
            searchPageF.getConditions().getFields().removeAll(fieldsToRemove);
        }
        return statutoryBodyIds;
    }


    /**
     * 统计凭证金额
     *
     * @param searchPageF 查询条件
     * @return
     */
    public long staticVoucherAmount(PageF<SearchF<?>> searchPageF) {

        searchPageF.getConditions().getFields().forEach(e -> {
            if ("account_book_id".equals(e.getName())){
                e.setName("vi.account_book_id");
            }
        });

        List<Long> statutoryBodyIds = filterStatutoryBodyId(searchPageF);
        QueryWrapper<?> queryWrapper = RepositoryUtil.putLogicDeleted(searchPageF.getConditions().getQueryModel(),"vi");
        if (CollectionUtils.isNotEmpty(statutoryBodyIds)) {
            if (!queryStatutoryBodyId(statutoryBodyIds, queryWrapper)) {
                return 0;
            }
        }
        Long amount = 0L;

        Optional<Field> first = searchPageF.getConditions().getFields().stream().filter(t -> t.getName().equals("vi.account_book_id")).findFirst();
        if (first.isPresent()) {
            Object accountBookId = first.get().getValue();
            String tableName = getVoucherBusinessDetailKey((Long) accountBookId);
            amount = baseMapper.staticVoucherAmountByTableName(queryWrapper, tableName);
        } else {
            amount = baseMapper.staticVoucherAmount(queryWrapper);
        }
        return Objects.isNull(amount) ? 0 : amount;
    }

    /**
     * @param businessId
     * @param businessType
     * @return
     */
    public List<Voucher> listByBusinessId(Long businessId, int businessType) {
//        return baseMapper.selectListByBusinessId(businessId, businessType);
        return new ArrayList<>(1);
    }

    public Integer deleteByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }

    public Integer deleteBusinessDetailByIds(List<Long> voucherIds, Long accountBookId) {
        return baseMapper.deleteBusinessDetailByIds(voucherIds,accountBookId);
    }

    public boolean selectVoucherInfoAndBusinessDetail(List<Long> ids) {
        List<Voucher> vouchers = baseMapper.selectBatchIds(ids);
        if (CollectionUtils.isNotEmpty(vouchers)) {
            StringBuilder stringBuilder = new StringBuilder("");
            for (Voucher voucher : vouchers) {
                if (!voucher.getState().equals(0) && !voucher.getState().equals(2) && !voucher.getState().equals(4)) {
                    stringBuilder.append("报账凭证编号:").append(voucher.getVoucherNo()).append("删除失败,失败原因:只支持待同步或同步失败或已作废状态凭证删除;");
                }
            }
            if (StringUtils.isNotBlank(stringBuilder.toString())) {
                throw BizException.throw400(stringBuilder.toString());
            }
        }
        vouchers = vouchers.stream().filter(v -> {
            return v.getDeleted() == 0;
        }).collect(Collectors.toList());

        int detailCount =0;
        for (Voucher voucher : vouchers) {
            Integer i = baseMapper.selectBusinessDetailByVouchIds(Collections.singletonList(voucher.getId()),
                    voucher.getAccountBookId());
            detailCount=detailCount+i;
        }

        boolean b1 = (vouchers != null && vouchers.size() > 0);
        if (b1 || detailCount > 0) {
            return true;
        }
        return false;
    }

    /**
     * 编辑凭证明细
     *
     * @param updateVoucherDetailF
     * @return
     */
    public Boolean updateVoucherDetail(UpdateVoucherDetailF updateVoucherDetailF) {
        Voucher voucher = baseMapper.selectById(updateVoucherDetailF.getId());
        //只有状态为待同步和同步失败的才可以进行编辑
        if (voucher.getState() != 0 && voucher.getState() != 2) {
            throw BizException.throw300(ErrorMessage.VOUCHER_STATE_IS_NOT_UPDATE.msg());
        }
        voucher.setDetails(updateVoucherDetailF.getDetails());
        int i = baseMapper.updateById(voucher);
        if (i < 1) {
            throw BizException.throw300(ErrorMessage.UPDATE_VOUCHER_DETAIL_FAIL.msg());
        }
        return true;
    }
    private String getVoucherBusinessDetailKey(Long accountBookId){
        return "voucher_business_detail_" +Math.abs(accountBookId.hashCode() % 512);
    }
}
