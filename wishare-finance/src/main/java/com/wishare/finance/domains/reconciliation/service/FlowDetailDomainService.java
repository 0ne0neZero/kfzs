package com.wishare.finance.domains.reconciliation.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.reconciliation.dto.FlowStatisticsDto;
import com.wishare.finance.apps.model.reconciliation.vo.*;
import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.domains.configure.organization.facade.CostCenterFacade;
import com.wishare.finance.domains.reconciliation.command.AddFlowDetailCommand;
import com.wishare.finance.domains.reconciliation.dto.FlowInvoiceDetailDto;
import com.wishare.finance.domains.reconciliation.entity.FlowClaimRecordE;
import com.wishare.finance.domains.reconciliation.entity.FlowDetailE;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimDetailStatusEnum;
import com.wishare.finance.domains.reconciliation.enums.FlowClaimStatusEnum;
import com.wishare.finance.domains.reconciliation.enums.FlowSourceEnum;
import com.wishare.finance.domains.reconciliation.enums.FlowTypeStatusEnum;
import com.wishare.finance.domains.reconciliation.facade.ReconciliationOrgFacade;
import com.wishare.finance.domains.reconciliation.repository.FlowDetailRepository;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.EnvConst;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.easyexcel.ExcelDataListener;
import com.wishare.finance.infrastructure.easyexcel.FlowDetailData;
import com.wishare.finance.infrastructure.remote.vo.org.OrgTenantRv;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.api.FileStorage;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 流水领域
 *
 * @author yancao
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FlowDetailDomainService {

    private final FlowDetailRepository flowDetailRepository;

    private final FileStorage fileStorage;
    private final CostCenterFacade costCenterFacade;
    private final ReconciliationOrgFacade reconciliationOrgFacade;
    /**
     * 新增流水明细
     *
     * @param command     command
     * @param englishName 客户简称
     * @return Boolean
     */
    public Boolean addDetail(AddFlowDetailCommand command, String englishName) {
        FlowDetailE flowDetailE = Global.mapperFacade.map(command, FlowDetailE.class);
        String serialNumber = flowDetailE.getSerialNumber();
        FlowDetailE existFlowDetailE = flowDetailRepository.queryBySerialNumber(serialNumber);
        if (existFlowDetailE != null) {
            throw BizException.throw400(ErrMsgEnum.FLOW_NUMBER_EXIST.getErrMsg());
        }
        flowDetailE.init(englishName);
        return flowDetailRepository.save(flowDetailE);
    }

    /**
     * 分页获取流水明细
     *
     * @param queryF queryF
     * @return IPage
     */
    public IPage<FlowDetailE> queryDetailPage(PageF<SearchF<FlowDetailE>> queryF) {
        if (!costCenterFacade.changeNodeIdSearch(queryF.getConditions(), "ba.cost_org_id")) {
            return new Page<>();
        }
        if (!costCenterFacade.changeNodeIdSearchByStatutoryBodyAccountId(queryF.getConditions(), "sba.statutory_body_id")) {
            return new Page<>();
        }
        QueryWrapper<FlowDetailE> queryWrapper = queryF.getConditions().getQueryModel();
//        queryWrapper.eq("fd.claim_status", FlowClaimStatusEnum.未认领.getCode());
        queryWrapper.eq("fd.deleted", 0);
        queryWrapper.groupBy("fd.id");
        if (EnvConst.ZHONGJIAO.equals(EnvData.config)){
            queryWrapper.orderByDesc("fd.pay_time");
        }else {
            queryWrapper.orderByDesc("fd.gmt_create");
        }
        Page<FlowDetailE> page = new Page<>(queryF.getPageNum(), queryF.getPageSize());
        return flowDetailRepository.queryDetailPage(page, queryWrapper);
//        return flowDetailRepository.page(page, queryWrapper);
    }

    /**
     * 查询流水明细
     *
     * @param queryF
     * @return
     */
    public IPage<FlowDetailComplexV> queryFlowDetailComplexPage(PageF<SearchF<FlowDetailE>> queryF) {
        if (!costCenterFacade.changeNodeIdSearchNew(queryF.getConditions(), "ba.cost_org_id", 2)) {
            return new Page<>();
        }
        if (!costCenterFacade.changeNodeIdSearchNew(queryF.getConditions(), "sba.statutory_body_id", 1)) {
            return new Page<>();
        }
        QueryWrapper<FlowDetailE> queryWrapper = queryF.getConditions().getQueryModel();
        queryWrapper.eq("fd.deleted", 0);
        queryWrapper.groupBy("fd.id");
        if (EnvConst.ZHONGJIAO.equals(EnvData.config)){
            queryWrapper.orderByDesc("fd.pay_time");
        }else {
            queryWrapper.orderByDesc("fd.gmt_create");
        }
        Page<FlowDetailE> page = new Page<>(queryF.getPageNum(), queryF.getPageSize());
        return flowDetailRepository.queryFlowDetailComplexPage(page, queryWrapper);
    }

    /**
     * 分页获取流水明细(汇款认领时)
     * @param queryF
     * @return
     */
    public IPage<FlowDetailE> remitQueryDetailPage(PageF<SearchF<FlowDetailE>> queryF) {
        if (!costCenterFacade.changeNodeIdSearch(queryF.getConditions(), "ba.cost_org_id")) {
            return new Page<>();
        }
        if (!costCenterFacade.changeNodeIdSearchByStatutoryBodyAccountId(queryF.getConditions(), "sba.statutory_body_id")) {
            return new Page<>();
        }
        QueryWrapper<FlowDetailE> queryWrapper = queryF.getConditions().getQueryModel();
        queryWrapper.eq("fd.claim_status", FlowClaimStatusEnum.未认领.getCode());
        queryWrapper.eq("fd.deleted", 0);
        queryWrapper.groupBy("fd.id");
        queryWrapper.orderByDesc("fd.gmt_create");
        Page<FlowDetailE> page = new Page<>(queryF.getPageNum(), queryF.getPageSize());
        return flowDetailRepository.queryDetailPage(page, queryWrapper);
//        return flowDetailRepository.page(page, queryWrapper);
    }

    public List<FlowDetailE> queryDetailList(Long id) {
        QueryWrapper<FlowDetailE> queryWrapper = new QueryWrapper<FlowDetailE>();
        queryWrapper.eq("sba.statutory_body_id",id);
        queryWrapper.eq("fd.claim_status", FlowClaimStatusEnum.未认领.getCode());
        queryWrapper.eq("fd.deleted", 0);
        queryWrapper.groupBy("fd.id");
        queryWrapper.orderByDesc("fd.gmt_create");
        return flowDetailRepository.queryDetailList(queryWrapper);
//        return flowDetailRepository.page(page, queryWrapper);
    }

    /**
     * 根据id集合获取流水明细
     *
     * @param flowIdList id集合
     * @return List
     */
    public List<FlowDetailE> getByIdList(List<Long> flowIdList) {
        return flowDetailRepository.listByIds(flowIdList);
    }

    /**
     * 导入流水明细
     *
     * @param file        导入文件
     * @param englishName 客户简称
     * @return FlowImportV
     */
    public FlowImportV importFlow(MultipartFile file, String englishName) {
        FlowImportV flowImportV = new FlowImportV(0, 0, null);
        try {
            //读取excel文件数据
            InputStream inputStream = file.getInputStream();
            ArrayList<List<Object>> failDataList = new ArrayList<>();
            EasyExcel.read(inputStream, FlowDetailData.class, new ExcelDataListener<FlowDetailData>(flowDetailDataList -> {
                //校验字段
                checkImportParams(flowDetailDataList, failDataList);
                //正常的数据进行入库
                List<FlowDetailE> saveFlowDetailList = Global.mapperFacade.mapAsList(flowDetailDataList, FlowDetailE.class);
                for (FlowDetailE flowDetailE : saveFlowDetailList) {
                    flowDetailE.init(englishName);
                    flowDetailE.setTransactionMode("线下");
                    flowDetailE.setOppositeAccount(StringUtils.deleteWhitespace(flowDetailE.getOppositeAccount()));
                    flowDetailE.setOurAccount(StringUtils.deleteWhitespace(flowDetailE.getOurAccount()));
                }
                flowDetailRepository.saveBatch(saveFlowDetailList);
                //更新成功导入的数据量
                flowImportV.setSuccessTotal(flowImportV.getSuccessTotal() + flowDetailDataList.size());
            }, failDataList::addAll)).sheet().headRowNumber(2).doRead();
            //失败数据导出到临时excel文件
            FileVo fileVo = writeErrorDataToExcel(file, FlowDetailData.class, failDataList, "流水错误数据");
            flowImportV.setFailTotal(flowImportV.getFailTotal() + failDataList.size());
            flowImportV.setExcelLinkUrl(fileVo == null ? null : fileVo.getFileKey());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flowImportV;
    }

    /**
     * 校验业务字段
     *
     * @param flowDetailDataList 读取的数据
     * @param failDataList       失败数据
     */
    private void checkImportParams(List<FlowDetailData> flowDetailDataList, ArrayList<List<Object>> failDataList) {
        ArrayList<FlowDetailData> tempList = new ArrayList<>(flowDetailDataList);
        Set<String> serialNumbers = flowDetailRepository.querySerialNumbers();
        for (FlowDetailData flowDetailData : tempList) {
//            if (StringUtils.isNotBlank(flowDetailData.getSerialNumber())){
//                if (strings.contains(flowDetailData.getSerialNumber())){
//                    flowDetailDataList.remove(flowDetailData);
//                    List<Object> failDataValue = getFieldsValue(flowDetailData);
//                    failDataValue.set(failDataValue.size() - 1, ErrMsgEnum.SERIAL_NUMBER_EXISTS.getErrMsg());
//                    failDataList.add(failDataValue);
//                    continue;
//                }
//                List<FlowDetailE> exitList = flowDetailRepository.list(new LambdaQueryWrapper<FlowDetailE>().eq(FlowDetailE::getSerialNumber, flowDetailData.getSerialNumber()));
//                if (CollectionUtils.isNotEmpty(exitList)){
//                    flowDetailDataList.remove(flowDetailData);
//                    List<Object> failDataValue = getFieldsValue(flowDetailData);
//                    failDataValue.set(failDataValue.size() - 1, ErrMsgEnum.SERIAL_NUMBER_EXISTS.getErrMsg());
//                    failDataList.add(failDataValue);
//                    continue;
//                }
//                strings.add(flowDetailData.getSerialNumber());
//            }
            if (StringUtils.isNotBlank(flowDetailData.getSerialNumber())) {
                if (serialNumbers.contains(flowDetailData.getSerialNumber())) {
                    // 已有该序列号
                    flowDetailDataList.remove(flowDetailData);
                    List<Object> failDataValue = getFieldsValue(flowDetailData);
                    failDataValue.set(failDataValue.size() - 1, ErrMsgEnum.SERIAL_NUMBER_EXISTS.getErrMsg());
                    failDataList.add(failDataValue);
                    continue;
                }
                serialNumbers.add(flowDetailData.getSerialNumber());
            }
            if (StringUtils.isEmpty(flowDetailData.getOppositeAccount())) {
                flowDetailDataList.remove(flowDetailData);
                List<Object> failDataValue = getFieldsValue(flowDetailData);
                failDataValue.set(failDataValue.size() - 1, ErrMsgEnum.FLOW_OPPOSITE_ACCOUNT_IS_EMPTY.getErrMsg());
                failDataList.add(failDataValue);
                continue;
            }
            if (StringUtils.isEmpty(flowDetailData.getOppositeName())) {
                flowDetailDataList.remove(flowDetailData);
                List<Object> failDataValue = getFieldsValue(flowDetailData);
                failDataValue.set(failDataValue.size() - 1, ErrMsgEnum.FLOW_OPPOSITE_NAME_IS_EMPTY.getErrMsg());
                failDataList.add(failDataValue);
                continue;
            }
            if (StringUtils.isEmpty(flowDetailData.getOppositeBank())) {
                flowDetailDataList.remove(flowDetailData);
                List<Object> failDataValue = getFieldsValue(flowDetailData);
                failDataValue.set(failDataValue.size() - 1, ErrMsgEnum.FLOW_OPPOSITE_BANK_IS_EMPTY.getErrMsg());
                failDataList.add(failDataValue);
                continue;
            }
            if (StringUtils.isEmpty(flowDetailData.getOurAccount())) {
                flowDetailDataList.remove(flowDetailData);
                List<Object> failDataValue = getFieldsValue(flowDetailData);
                failDataValue.set(failDataValue.size() - 1, ErrMsgEnum.FLOW_OUR_ACCOUNT_IS_EMPTY.getErrMsg());
                failDataList.add(failDataValue);
                continue;
            }
            if (StringUtils.isEmpty(flowDetailData.getOurName())) {
                flowDetailDataList.remove(flowDetailData);
                List<Object> failDataValue = getFieldsValue(flowDetailData);
                failDataValue.set(failDataValue.size() - 1, ErrMsgEnum.FLOW_OUR_NAME_IS_EMPTY.getErrMsg());
                failDataList.add(failDataValue);
                continue;
            }
            if (StringUtils.isEmpty(flowDetailData.getOurBank())) {
                flowDetailDataList.remove(flowDetailData);
                List<Object> failDataValue = getFieldsValue(flowDetailData);
                failDataValue.set(failDataValue.size() - 1, ErrMsgEnum.FLOW_OUR_BANK_IS_EMPTY.getErrMsg());
                failDataList.add(failDataValue);
                continue;
            }
            if (StringUtils.isEmpty(flowDetailData.getTradingPlatform())) {
                flowDetailDataList.remove(flowDetailData);
                List<Object> failDataValue = getFieldsValue(flowDetailData);
                failDataValue.set(failDataValue.size() - 1, ErrMsgEnum.FLOW_TRADING_PLATFORM_IS_EMPTY.getErrMsg());
                failDataList.add(failDataValue);
                continue;
            }
            if(StringUtils.isEmpty(flowDetailData.getType()) || Objects.isNull(FlowTypeStatusEnum.valueOfByName(flowDetailData.getType()))){
                flowDetailDataList.remove(flowDetailData);
                List<Object> failDataValue = getFieldsValue(flowDetailData);
                failDataValue.set(failDataValue.size() - 1, ErrMsgEnum.FLOW_TYPE_FAIL.getErrMsg());
                failDataList.add(failDataValue);
                continue;
            }
            if (flowDetailData.getSettleAmount() == null) {
                flowDetailDataList.remove(flowDetailData);
                List<Object> failDataValue = getFieldsValue(flowDetailData);
                failDataValue.set(failDataValue.size() - 1, ErrMsgEnum.FLOW_SETTLE_AMOUNT_IS_EMPTY.getErrMsg());
                failDataList.add(failDataValue);
            } else {
                //校验金额是否超过两位小数
                BigDecimal settleAmount = flowDetailData.getSettleAmount();
                String settleAmountStr = settleAmount.toString();
                int index = settleAmountStr.indexOf('.');
                if (index > 0 && (settleAmountStr.length() - 1 - index) > 2) {
                    flowDetailDataList.remove(flowDetailData);
                    List<Object> failDataValue = getFieldsValue(flowDetailData);
                    failDataValue.set(failDataValue.size() - 1, ErrMsgEnum.FLOW_SETTLE_AMOUNT_NOT_LEGAL.getErrMsg());
                    failDataList.add(failDataValue);
                }
            }

        }
    }



    /**
     * 反射获取对象值
     *
     * @param obj obj
     * @return List
     */
    public List<Object> getFieldsValue(Object obj) {
        ArrayList<Object> list = new ArrayList<>();
        Class<?> objClass = obj.getClass();
        //获取对象的所有属性(包括私有属性)
        Field[] fields = objClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            list.add(value);
        }
        return list;
    }

    /**
     * 导出错误数据到excel
     *
     * @param file          file
     * @param errorDataList 错误数据
     * @return FileVo
     */
    public FileVo writeErrorDataToExcel(MultipartFile file,
                                        Class<?> headClass,
                                        List<?> errorDataList,
                                        String sheetName) {
        if (errorDataList == null || errorDataList.size() <= 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(out).build();
        try {
            //新建ExcelWriter
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).head(headClass).build();
            excelWriter.write(errorDataList, writeSheet);
            excelWriter.finish();
            byte[] bytes = out.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            MultipartFile fileTo = new MockMultipartFile(file.getName(), file.getOriginalFilename(), ContentType.APPLICATION_OCTET_STREAM.toString(), inputStream);
            return fileStorage.tmpSave(fileTo);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            excelWriter.finish();
        }
        return null;
    }

    /**
     * 根据id集合获取流水总金额
     *
     * @param idList 流水id集合
     * @return Long
     */
    public FlowStatisticsDto statisticsAmount(List<Long> idList) {
        return flowDetailRepository.statisticsAmount(idList);
    }

    /**
     * 根据id集合获取流水总金额
     *
     * @return Long
     */
    public FlowStatisticsDto statisticsAmount2(PageF<SearchF<FlowDetailE>> form) {
        if (!costCenterFacade.changeNodeIdSearchNew(form.getConditions(), "ba.cost_org_id", 2)) {
            return new FlowStatisticsDto();
        }
        if (!costCenterFacade.changeNodeIdSearchNew(form.getConditions(), "sba.statutory_body_id", 1)) {
            return new FlowStatisticsDto();
        }
        QueryWrapper<?> queryWrapper = form.getConditions().getQueryModel();
        queryWrapper.eq("fd.deleted", DataDeletedEnum.NORMAL.getCode());
        return flowDetailRepository.statisticsAmount2(queryWrapper);
    }

    /**
     * 统计已勾选流水明细信息
     *
     */
    public FlowStatisticsClaimInfoV statisticsClaimInfo(List<Long> flowIdList) {
        List<FlowDetailE> flowDetailList = flowDetailRepository.listByIds(flowIdList);
        if (CollectionUtils.isEmpty(flowDetailList)){
            return new FlowStatisticsClaimInfoV();
        }
        if (flowDetailList.size()==1){
            FlowStatisticsClaimInfoV flowStatisticsClaimInfoV = Global.mapperFacade.map(flowDetailList.get(0), FlowStatisticsClaimInfoV.class);
            flowStatisticsClaimInfoV.setInAmount(flowDetailList.get(0).getType()==1?flowDetailList.get(0).getSettleAmount():0);
            flowStatisticsClaimInfoV.setOutAmount(flowDetailList.get(0).getType()==2?flowDetailList.get(0).getSettleAmount():0);
            flowStatisticsClaimInfoV.setTotalAmount(flowStatisticsClaimInfoV.getInAmount()-flowStatisticsClaimInfoV.getOutAmount());
            return flowStatisticsClaimInfoV;
        }
        FlowDetailE flowDetailE = flowDetailList.get(0);
        if (TenantUtil.bf2()) {
            for (FlowDetailE detailE : flowDetailList) {
                if (!flowDetailE.getOurAccount().equals(detailE.getOurAccount())){
                    throw BizException.throw402("多选的流水，需要本方账户相同");
                }
            }
        }


        Map<Integer, List<FlowDetailE>> group = flowDetailList.stream().collect(Collectors.groupingBy(FlowDetailE::getType));
        FlowStatisticsClaimInfoV flowStatisticsClaimInfoV = Global.mapperFacade.map(flowDetailE, FlowStatisticsClaimInfoV.class);
        if (group.get(1)!=null) {
            flowStatisticsClaimInfoV.setInAmount(group.get(1).stream().mapToLong(FlowDetailE::getSettleAmount).sum());
        }
        if (group.get(2)!=null) {
            flowStatisticsClaimInfoV.setOutAmount(group.get(2).stream().mapToLong(FlowDetailE::getSettleAmount).sum());
        }
        flowStatisticsClaimInfoV.setTotalAmount(flowStatisticsClaimInfoV.getInAmount()-flowStatisticsClaimInfoV.getOutAmount());
        return flowStatisticsClaimInfoV;
    }

    /**
     * 根据票据获取流水信息
     * @param invoiceIds
     * @param status
     * @return
     */
    public List<FlowInvoiceDetailDto> getListByInvoiceIds(List<Long> invoiceIds, FlowClaimDetailStatusEnum status) {
        return flowDetailRepository.getListByInvoiceIds(invoiceIds, status);
    }

    /**
     * 批量插入
     * @param list
     */
    public void batchInsert(List<FlowDetailE> list) {
        flowDetailRepository.saveBatch(list);
    }

    /**
     * 批量删除流水
     *
     * @param flowIdList 流水id集合
     * @return Boolean
     */
    public Boolean deleteBatch(List<Long> flowIdList) {
        List<FlowDetailE> flowDetailList = flowDetailRepository.listByIds(flowIdList);
        //同步的流水
        List<FlowDetailE> syncFlowList = flowDetailList.stream().filter(s -> s.getSyncData() == FlowSourceEnum.同步.getCode()).collect(Collectors.toList());
        ErrorAssertUtil.isFalseThrow300(CollectionUtils.isNotEmpty(syncFlowList), ErrorMessage.FLOW_SYNC_DELETE_FAIL);
        //已认领的流水
        List<FlowDetailE> claimedFlowList = flowDetailList.stream().filter(s -> s.getClaimStatus() == FlowClaimStatusEnum.已认领.getCode()).collect(Collectors.toList());
        ErrorAssertUtil.isFalseThrow300(CollectionUtils.isNotEmpty(claimedFlowList), ErrorMessage.FLOW_CLAIMED_DELETE_FAIL);
        List<FlowDetailE> approveList = flowDetailList.stream().filter(s -> s.getClaimStatus() == FlowClaimStatusEnum.报账审核中.getCode()).collect(Collectors.toList());
        ErrorAssertUtil.isFalseThrow300(CollectionUtils.isNotEmpty(approveList), ErrorMessage.FLOW_APPROVE_DELETE_FAIL);
        List<FlowDetailE> flowApproveList = flowDetailList.stream().filter(s -> s.getClaimStatus() == FlowClaimStatusEnum.认领审核中.getCode()).collect(Collectors.toList());
        ErrorAssertUtil.isFalseThrow300(CollectionUtils.isNotEmpty(flowApproveList), ErrorMessage.FLOW_APPROVE_DELETE_FAIL);
        return flowDetailRepository.removeBatchByIds(flowIdList);
    }

    /**
     * 统计批量删除流水信息
     *
     * @param flowIdList 流水id集合
     * @return Boolean
     */
    public Map<String,Object> groupInfoDeleteBatch(List<Long> flowIdList) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        List<FlowDetailE> flowDetailList = flowDetailRepository.listByIds(flowIdList);
        if (CollectionUtils.isNotEmpty(flowDetailList)){
            stringObjectHashMap.put("success",false);
            stringObjectHashMap.put("errorMessage","未查询到数据");
            return stringObjectHashMap;
        }
        //同步的流水
        List<FlowDetailE> syncFlowList = flowDetailList.stream().filter(s -> s.getSyncData() == FlowSourceEnum.同步.getCode()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(syncFlowList)){
            stringObjectHashMap.put("success",false);
            stringObjectHashMap.put("errorMessage",ErrorMessage.FLOW_SYNC_DELETE_FAIL.getErrMsg());
            return stringObjectHashMap;
        }
        //已认领的流水
        List<FlowDetailE> claimedFlowList = flowDetailList.stream().filter(s -> s.getClaimStatus() == FlowClaimStatusEnum.已认领.getCode()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(claimedFlowList)){
            stringObjectHashMap.put("success",false);
            stringObjectHashMap.put("errorMessage",ErrorMessage.FLOW_CLAIMED_DELETE_FAIL.getErrMsg());
            return stringObjectHashMap;
        }
        stringObjectHashMap.put("success",true);
        stringObjectHashMap.put("total",flowDetailList.size());
        long sum = flowDetailList.stream().mapToLong(FlowDetailE::getSettleAmount).sum();
        stringObjectHashMap.put("totalAmount",new BigDecimal(sum).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_UP));

        return stringObjectHashMap;
    }

    /**
     * 根据条件获取流水
     * @param names 法定单位名称
     * @param nameCompare 法定单位名称比较
     * @param fieldList 条件列表
     * @param flowType  流水类型
     * @param flowState  流水状态
     * @param flowStateCompare 流水状态比较
     */
    public List<FlowDetailE> listByConditions(List<String> names, Integer nameCompare, List<com.wishare.tools.starter.fo.search.Field> fieldList, int flowType, Integer flowState, Integer flowStateCompare) {
        if (9 == flowState) {
            if (2 == flowStateCompare) {
                return null;
            }
        } else {
            fieldList.add(new com.wishare.tools.starter.fo.search.Field("f.claim_status", flowState, flowStateCompare));
        }
        if (!CollectionUtils.isEmpty(names)) {
            fieldList.add(new com.wishare.tools.starter.fo.search.Field("f.our_name", names, nameCompare));
        }
        return flowDetailRepository.listByConditions(fieldList);
    }

    /**
     * 根据流水id列表获取流水认领id列表
     * @param flowIds
     * @return
     */
    public List<Long> getFlowDetailIdsByFlowIds(List<Long> flowIds) {
        return flowDetailRepository.getIdsByFlowIds(flowIds);
    }

    /**
     * 挂起流水
     * @param flowId
     * @return
     */
    public Boolean suspend(Long flowId) {
        // 校验
        FlowDetailE flowDetail = flowDetailRepository.getById(flowId);
        if (Objects.isNull(flowDetail)) {
            throw BizException.throw400(ErrorMessage.FLOW_DETAIL_NOT_EXIST.getErrMsg());
        }
        if (FlowClaimStatusEnum.未认领.getCode() != flowDetail.getClaimStatus()) {
            // 未认领才可以挂起
            throw BizException.throw400("流水未认领状态时才可以挂起");
        }
        LambdaUpdateWrapper<FlowDetailE> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(FlowDetailE::getId, flowId);
        updateWrapper.set(FlowDetailE::getClaimStatus, FlowClaimStatusEnum.已挂起.getCode());
        return flowDetailRepository.update(updateWrapper);
    }

    /**
     * 解除挂起
     * @param flowId
     * @return
     */
    public Boolean unsuspend(Long flowId) {
        // 校验
        FlowDetailE flowDetail = flowDetailRepository.getById(flowId);
        if (Objects.isNull(flowDetail)) {
            throw BizException.throw400(ErrorMessage.FLOW_DETAIL_NOT_EXIST.getErrMsg());
        }
        if (FlowClaimStatusEnum.已挂起.getCode() != flowDetail.getClaimStatus()) {
            // 挂起状态才可以解除挂起
            throw BizException.throw400("流水挂起状态时才可以解除挂起");
        }
        LambdaUpdateWrapper<FlowDetailE> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(FlowDetailE::getId, flowId);
        updateWrapper.set(FlowDetailE::getClaimStatus, FlowClaimStatusEnum.未认领.getCode());
        return flowDetailRepository.update(updateWrapper);
    }

    /**
     * 更新流水记录状态
     * @param flowIdList
     * @param claimStatus
     */
    public void updateClaimStatusByIdList(List<Long> flowIdList, Integer claimStatus) {
        flowDetailRepository.updateClaimStatusByIdList(flowIdList, claimStatus);
    }
    public FlowClaimRecordE getFlowClaimRecord(String flowId){
        return  flowDetailRepository.getFlowClaimRecord(flowId);
    }
    public Boolean importFlowClaim( List<FlowClaimDetailImportT> list, String  englishName) {
        List<FlowDetailE> saveFlowDetailList = Global.mapperFacade.mapAsList(list, FlowDetailE.class);
        for (FlowDetailE flowDetailE : saveFlowDetailList) {
            flowDetailE.init(englishName);
            flowDetailE.setTransactionMode("线下");
            flowDetailE.setOppositeAccount(StringUtils.deleteWhitespace(flowDetailE.getOppositeAccount()));
            flowDetailE.setOurAccount(StringUtils.deleteWhitespace(flowDetailE.getOurAccount()));
            flowDetailE.setClaimStatus(0);
//            BigDecimal multiply = new BigDecimal(String.valueOf(flowDetailE.getSettleAmount())).multiply(new BigDecimal("100"));
//            flowDetailE.setSettleAmount(Long.valueOf(String.valueOf(multiply)));
        }
        log.info("入库数据：" + JSON.toJSON(saveFlowDetailList)) ;
        flowDetailRepository.saveBatch(saveFlowDetailList);
        return true;
    }

    public Boolean syncFlowClaim( List<FlowDetailV> list){

        IdentityInfo identityInfo = new IdentityInfo();
        FlowDetailE tenantId = flowDetailRepository.getTenantId();
        if (null != tenantId) {
            identityInfo.setTenantId(tenantId.getTenantId());
        } else {
            identityInfo.setTenantId("13554968497211");
        }
        ThreadLocalUtil.set("IdentityInfo", identityInfo);

        List<FlowDetailE> saveFlowDetailList = Global.mapperFacade.mapAsList(list, FlowDetailE.class);
        Set<String> serialNumbers = flowDetailRepository.querySerialNumbers();
        Iterator<FlowDetailE> iterator = saveFlowDetailList.iterator();
        while (iterator.hasNext()){
            FlowDetailE next = iterator.next();
            if (serialNumbers.contains(next.getSerialNumber())){
                iterator.remove();
            }
        }
        if (saveFlowDetailList.size() > 0) {
            flowDetailRepository.saveBatch(saveFlowDetailList);
        }
        return true;
    }
}
