package com.wishare.finance.domains.bill.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.BillDetailDto;
import com.wishare.finance.domains.bill.dto.BillSettleDto;
import com.wishare.finance.domains.bill.entity.GatherBill;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import com.wishare.finance.domains.bill.entity.PayableBill;
import com.wishare.finance.domains.bill.repository.PayableBillRepository;
import com.wishare.finance.infrastructure.easyexcel.ExcelUtil;
import com.wishare.finance.infrastructure.easyexcel.ExportPayableBillData;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 应付账单领域
 *
 * @author yancao
 */
@Service
public class PayableBillDomainService extends BillDomainServiceImpl<PayableBillRepository, PayableBill> {

    /**
     * 导出收款单
     *
     * @param queryF  查询参数
     * @param response response
     */
    public void export(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        String fileName = "应付单";
        OutputStream outputStream;
        ExcelWriter excelWriter;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName+".xlsx", StandardCharsets.UTF_8));
            outputStream = response.getOutputStream();
            excelWriter = ExcelUtil.getExportExcelWriter(outputStream);
            WriteSheet writeSheet = EasyExcel.writerSheet(fileName).build();
            WriteTable writeTable = EasyExcel.writerTable(0).head(ExportPayableBillData.class).needHead(true).build();
            //分页查询数据
            int pageNumber = 1;
            long totalPage = pageNumber;
            int pageSize = 1000;
            List<ExportPayableBillData> resultList;
            queryF.setPageSize(pageSize);
            while(pageNumber <= totalPage){
                queryF.setPageNum(pageNumber);
                IPage<PayableBill> payableBillPage = billRepository.queryBillByPage(queryF);
                resultList = Global.mapperFacade.mapAsList(payableBillPage.getRecords(), ExportPayableBillData.class);
                if(CollectionUtils.isEmpty(resultList)){
                    break;
                }
                //写数据
                excelWriter.write(resultList, writeSheet,writeTable);
                totalPage = payableBillPage.getPages();
                pageNumber++;
            }
            //关闭writer的输出流
            excelWriter.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public BillDetailDto getDetailById(Long bid) {
//        PayableBill bill = billRepository.getById(bid);
//        if (Objects.nonNull(bill)) {
//            BillDetailDto billDetailDto = new BillDetailDto();
//            billDetailDto.setBill(bill);
//            billDetailDto.setBillAdjustDtos(adjustRepository.listByBillId(bid));
//            billDetailDto.setApproves(approveRepository.listByBillId(bid));
//            if (BillTypeEnum.预收账单.equalsByCode(bill.getType())) {
//                billDetailDto.setBillSettleDtos(handleSettle(gatherDetailRepository.queryByGatherBillId(bid)));
//            } else {
//                List<GatherDetail> gatherDetails = gatherDetailRepository.listByRecBillId(bid);
//                Map<Long, List<GatherBill>> gatherBillMap = new HashedMap<>();
//                if (CollectionUtils.isNotEmpty(gatherDetails)) {
//                    List<Long> gatherBillIds = gatherDetails.stream().map(GatherDetail::getGatherBillId).collect(Collectors.toList());
//                    List<GatherBill> gatherBillList = gatherBillRepository.getGatherBill(gatherBillIds);
//                    gatherBillMap = gatherBillList.stream().collect(Collectors.groupingBy(GatherBill::getId));
//                }
//                List<BillSettleDto> billSettleDtos = handleSettle(gatherDetails);
//                if (CollectionUtils.isNotEmpty(billSettleDtos)) {
//                    for (BillSettleDto billSettleDto : billSettleDtos) {
//                        if (gatherBillMap != null && !gatherBillMap.isEmpty()) {
//                            GatherBill gatherBill = gatherBillMap.get(billSettleDto.getGatherBillId()).get(0);
//                            billSettleDto.setTradeNo(gatherBill.getTradeNo());
//                            billSettleDto.setDiscounts(JSON.toJSONString(gatherBill.getDiscounts()));
//                            billSettleDto.setRemark(gatherBill.getRemark());
//                        }
//
//                    }
//                }
//                billDetailDto.setBillSettleDtos(billSettleDtos);
//            }
//            billDetailDto.setBillRefundDtos(refundRepository.listByBillId(bid));
//            billDetailDto.setBillCarryoverDtos(carryoverRepository.listByCarriedBillId(bid));
//            return billDetailDto;
//        }
//        return null;
//    }
}
