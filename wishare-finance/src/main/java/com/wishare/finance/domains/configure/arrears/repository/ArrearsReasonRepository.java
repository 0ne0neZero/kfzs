package com.wishare.finance.domains.configure.arrears.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsReasonBillV;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsReasonV;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.domains.bill.consts.enums.BillSharedingColumn;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.ReasonBillTotalDto;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.configure.arrears.entity.ArrearsReasonE;
import com.wishare.finance.domains.configure.arrears.repository.mapper.ArrearsReasonMapper;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.export.service.ExportService;
import com.wishare.finance.infrastructure.conts.*;
import com.wishare.finance.infrastructure.utils.page.PageQueryUtils;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ArrearsReasonRepository extends ServiceImpl<ArrearsReasonMapper, ArrearsReasonE> {

    @Autowired
    private ExportService exportService;

    @Autowired
    private SharedBillAppService sharedBillAppService;

    public IPage<ArrearsReasonBillV> queryPageBill(PageF<SearchF<?>> query) {
        QueryWrapper<?> wrapper = query.getConditions().getQueryModel();
//        wrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b." + BillSharedingColumn.应收账单.getColumnName());
        wrapper.orderByDesc("b.id");
        List<Field> fields = query.getConditions().getFields();

        // 导出场合
        IPage<ArrearsReasonBillV> queryPage;
        Object exportTaskIdObj = query.getConditions().getSpecialMap().get("exportTaskId");
        if (exportTaskIdObj != null) {
            Object totalObj = query.getConditions().getSpecialMap().get("total");
            Long exportTaskId = Long.parseLong(String.valueOf(exportTaskIdObj));
            long count = 0;
            if (totalObj == null) {
                count = baseMapper.queryPageBillCount(wrapper);
            } else {
                count = Long.parseLong(String.valueOf(totalObj));
            }
            // 导出条数超过指定条数，则通过临时表方式导出
            if (count > 1/*exportService.exportProperties().getTmpTableCount()*/) {
                String tblName = TableNames.RECEIVABLE_BILL;
                List<Field> supCpUnitIds = fields.stream().filter(s -> "b.sup_cp_unit_id".equals(s.getName())).collect(Collectors.toList());
                if (supCpUnitIds != null && supCpUnitIds.size() > 0) {
                    tblName = sharedBillAppService.getShareTableName(supCpUnitIds.get(0).getValue().toString(), tblName);
                }
                exportService.createTmpTbl(wrapper, tblName, exportTaskId, ExportTmpTblTypeEnum.OVERDUE_REASON_BILL);

                // 深分页查询优化
                long tid = (query.getPageNum() - 1) * query.getPageSize();
                queryPage = exportService.queryOverDueReasonBillByPageOnTempTbl(
                        Page.of(1, query.getPageSize(), false), tblName, exportTaskId, tid);
                queryPage.setTotal(count);
                return queryPage;
            }
        }
        IPage<ArrearsReasonBillV> result = baseMapper.queryPageBill(Page.of(query.getPageNum(), query.getPageSize(), query.isCount()), wrapper);
        return result;
    }

    public ReasonBillTotalDto batchReasonBillTotal(PageF<SearchF<?>> query) {
        QueryWrapper<?> wrapper = query.getConditions().getQueryModel();
//        wrapper.eq("b.bill_type", BillTypeEnum.应收账单.getCode());
        PageQueryUtils.validQueryContainsFieldAndValue(query, "b." + BillSharedingColumn.应收账单.getColumnName());
        return baseMapper.batchReasonBillTotal(wrapper);
    }

    public List<ArrearsReasonE> queryNewArrearsReason(List<Long> billId) {
        return baseMapper.queryNewArrearsReason(billId,ThreadLocalUtil.curIdentityInfo().getTenantId());
    }
}
