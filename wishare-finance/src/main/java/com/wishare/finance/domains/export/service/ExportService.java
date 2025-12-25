package com.wishare.finance.domains.export.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.component.export.ExportCreateTmpTable;
import com.wishare.component.export.ExportDataSourceService;
import com.wishare.component.export.ExportProperties;
import com.wishare.component.export.ExportQuery;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsReasonBillV;
import com.wishare.finance.domains.bill.dto.AdvanceBillGroupDto;
import com.wishare.finance.domains.bill.dto.ReceivableBillGroupDto;
import com.wishare.finance.domains.bill.entity.AdvanceBill;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.entity.TemporaryChargeBill;
import com.wishare.finance.domains.export.repository.mapper.ExportMapper;
import com.wishare.finance.infrastructure.conts.ExportTmpTblTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RefreshScope
public class ExportService {

    private final ExportDataSourceService exportDataSourceService;

    public ExportService(@Autowired(required = false) ExportDataSourceService exportDataSourceService) {
        this.exportDataSourceService = exportDataSourceService;
    }

    /**
     * 获取用于导出的配置信息
     */
    public ExportProperties exportProperties() {
        return exportDataSourceService.exportProperties();
    }

    /**
     * 创建账单临时表信息
     *
     * @param wrapper wrapper
     * @param tblName  tblName
     * @param taskId  taskId
     */
    public void createTmpTbl(QueryWrapper<?> wrapper, String tblName, Long taskId, ExportTmpTblTypeEnum tmpTblType) {
        ExportCreateTmpTable.of(exportDataSourceService).doCreate(tblName, taskId, (sqlSession -> {
            ExportMapper exportMapper = sqlSession.getMapper(ExportMapper.class);
            int result = exportMapper.createTmpTbl(wrapper, tblName, String.valueOf(taskId), tmpTblType.ordinal());
            log.info("create table result count: {}", result);
        }));
    }


    /**
     * 创建账单临时表信息
     *
     * @param wrapper wrapper
     * @param tblName  tblName
     * @param queryCondMd5  queryCondMd5
     */
    public void createTmpTbl(QueryWrapper<?> wrapper, String tblName, String queryCondMd5, ExportTmpTblTypeEnum tmpTblType) {
        ExportCreateTmpTable.of(exportDataSourceService).doCreate(tblName, queryCondMd5, (sqlSession -> {
            ExportMapper exportMapper = sqlSession.getMapper(ExportMapper.class);
            int result = exportMapper.createTmpTbl(wrapper, tblName, String.valueOf(queryCondMd5), tmpTblType.ordinal());
            log.info("create table result count: {}", result);
        }));
    }

    /**
     * 分页查询临时账单信息
     *
     * @param page    page
     * @param taskId  taskId
     * @return IPage
     */
    @Transactional
    public IPage<ReceivableBill> queryReceivableBillByPageOnTempTbl(Page<Object> page, String tblName, Long taskId, long uid) {
        return ExportQuery.<ReceivableBill>of(exportDataSourceService)
                .doQuery(page, tblName, taskId, (sqlSession)-> {
                    ExportMapper exportMapper = sqlSession.getMapper(ExportMapper.class);
                    return exportMapper.queryReceivableBill(page, tblName, String.valueOf(taskId), uid);
                });
    }

    /**
     * 分页查询临时账单信息
     *
     * @param page    page
     * @param taskId  taskId
     * @return IPage
     */
    @Transactional
    public IPage<TemporaryChargeBill> queryTemporaryChargeBillByPageOnTempTbl(Page<Object> page, String tblName, Long taskId, long uid) {
        return ExportQuery.<TemporaryChargeBill>of(exportDataSourceService)
                .doQuery(page, tblName, taskId, (sqlSession)-> {
                    ExportMapper exportMapper = sqlSession.getMapper(ExportMapper.class);
                    return exportMapper.queryTemporaryChargeBill(page, tblName, String.valueOf(taskId), uid);
                });
    }

    /**
     * 分组分页查询临时账单（审核）信息
     *
     * @param page    page
     * @param tblNameSuffix  tblNameSuffix
     * @return IPage
     */
    @Transactional
    public IPage<ReceivableBillGroupDto> queryReceivableBillGroupByPageOnTempTbl(Page<Object> page, String tblName, String tblNameSuffix, long uid) {
        return ExportQuery.<ReceivableBillGroupDto>of(exportDataSourceService)
                .doQuery(page, tblName, tblNameSuffix, (sqlSession)-> {
                    ExportMapper exportMapper = sqlSession.getMapper(ExportMapper.class);
                    return exportMapper.queryReceivableBillGroup(page, tblName, tblNameSuffix, uid);
                });
    }
    /**
     * 分页查询应收账单信息
     *
     * @param page    page
     * @param taskId  taskId
     * @return IPage
     */
    @Transactional
    public IPage<AdvanceBill> queryAdvanceBillByPageOnTempTbl(Page<Object> page, String tblName, Long taskId, long uid) {
        return ExportQuery.<AdvanceBill>of(exportDataSourceService)
                .doQuery(page, tblName, taskId, (sqlSession)-> {
                    ExportMapper exportMapper = sqlSession.getMapper(ExportMapper.class);
                    return exportMapper.queryAdvanceBill(page, tblName, String.valueOf(taskId), uid);
                });
    }

    /**
     * 分组分页查询应收账单（审核）信息
     *
     * @param page    page
     * @param taskId  taskId
     * @return IPage
     */
    @Transactional
    public IPage<AdvanceBillGroupDto> queryAdvanceBillGroupByPageOnTempTbl(Page<Object> page, String tblName, Long taskId, long uid) {
        return ExportQuery.<AdvanceBillGroupDto>of(exportDataSourceService)
                .doQuery(page, tblName, taskId, (sqlSession)-> {
                    ExportMapper exportMapper = sqlSession.getMapper(ExportMapper.class);
                    return exportMapper.queryAdvanceBillGroup(page, tblName, String.valueOf(taskId), uid);
                });
    }

    /**
     * 分组分页查询欠费原因账单信息
     *
     * @param page    page
     * @param taskId  taskId
     * @return IPage
     */
    @Transactional
    public IPage<ArrearsReasonBillV> queryOverDueReasonBillByPageOnTempTbl(Page<Object> page, String tblName, Long taskId, long uid) {
        return ExportQuery.<ArrearsReasonBillV>of(exportDataSourceService)
                .doQuery(page, tblName, taskId, (sqlSession)-> {
                    ExportMapper exportMapper = sqlSession.getMapper(ExportMapper.class);
                    return exportMapper.queryOverDueReasonBill(page, tblName, String.valueOf(taskId), uid);
                });
    }
}
