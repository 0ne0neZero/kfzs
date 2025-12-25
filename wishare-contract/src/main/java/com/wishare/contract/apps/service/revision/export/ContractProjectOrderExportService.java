package com.wishare.contract.apps.service.revision.export;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.wishare.component.imports.DataImportContext;
import com.wishare.component.imports.IStandardImport;
import com.wishare.component.imports.ImportStandardV;
import com.wishare.component.imports.builder.DataImportBuilder;
import com.wishare.component.imports.extension.easyexcel.handle.HeadModifyHandler;
import com.wishare.component.imports.factory.DataImportFactory;
import com.wishare.component.imports.task.ImportTaskService;
import com.wishare.contract.apps.service.revision.projectInitiation.ContractProjectInitiationAppService;
import com.wishare.contract.apps.service.revision.projectInitiation.ContractProjectOrderAppService;
import com.wishare.contract.domains.entity.revision.projectInitiation.ContractProjectInitiationE;
import com.wishare.contract.domains.entity.revision.projectInitiation.ContractProjectOrderE;
import com.wishare.contract.domains.enums.revision.ContractProjectOrderPlatformEnum;
import com.wishare.contract.domains.task.ImportTaskEnum;
import com.wishare.contract.domains.vo.revision.export.ContractProjectOrderImport;
import com.wishare.contract.domains.vo.revision.projectInitiation.ContractProjectOrderInfoV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.tools.starter.service.FileStorageService;
import com.wishare.tools.starter.vo.FileVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/10  15:48
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ContractProjectOrderExportService implements IStandardImport<ContractProjectOrderImport>, IOwlApiBase {

    private final ImportTaskService importTaskService;

    private final FileStorageService fileStorageService;

    private final ContractProjectOrderAppService contractProjectOrderAppService;

    private final ContractProjectInitiationAppService contractProjectInitiationAppService;

    public ImportStandardV orderImport(MultipartFile excelFile, String projectInitiationId) {
        importTaskService.executeTask(
                ImportTaskEnum.CONTRACT_PROJECT_ORDER_IMPORT, excelFile, inputStream -> {
                    DataImportBuilder<ContractProjectOrderImport> importBuilder = DataImportFactory.create();
                    Map<String, Object> customMap = new HashMap<>();
                    customMap.put("projectInitiationId", projectInitiationId);
                    return importBuilder.createFrom(inputStream, ContractProjectOrderImport.class, 1)
                            .with(customMap)
                            .standard(this);
                });
        return new ImportStandardV();
    }

    @Override
    public void execute(ContractProjectOrderImport next, DataImportContext<ContractProjectOrderImport> dataImportContext) {

    }

    @Override
    public List<ErrorRow<ContractProjectOrderImport>> save(List<ContractProjectOrderImport> dataList, DataImportContext<ContractProjectOrderImport> dataImportContext) {
        log.info("ContractProjectOrderExportService.save() called with parameters => 【dataList = {}】,【dataImportContext = {}】", JSON.toJSONString(dataList), JSON.toJSONString(dataImportContext));
        Map<String, List<ContractProjectOrderImport>> dataMap = dataList.stream().collect(Collectors.groupingBy(ContractProjectOrderImport::getOrderNumber));
        String projectInitiationId = dataImportContext.getCustomMap().get("projectInitiationId").toString();

        List<ContractProjectOrderE> contractProjectOrderES = new ArrayList<>();
        BigDecimal allOrderAmountWithoutTax = BigDecimal.ZERO;
        for (Map.Entry<String, List<ContractProjectOrderImport>> e : dataMap.entrySet()) {
            BigDecimal orderAmount = BigDecimal.ZERO;
            BigDecimal orderAmountWithoutTax = BigDecimal.ZERO;
            Integer goodsCount = 0;
            ContractProjectOrderE contractProjectOrderE = new ContractProjectOrderE()
                    .setProjectInitiationId(projectInitiationId);
            List<ContractProjectOrderInfoV> contractProjectOrderInfoVS = new ArrayList<>();
            for (ContractProjectOrderImport i : e.getValue()) {
                contractProjectOrderE.setOrderNumber(i.getOrderNumber())
                        .setPlatform(ContractProjectOrderPlatformEnum.parseCode(i.getPlatform()))
                        .setOrderCreateTime(i.getOrderCreateTime())
                        .setOrderAccount(i.getOrderAccount());

                orderAmount = NumberUtil.add(orderAmount, i.getAmount());
                orderAmountWithoutTax = NumberUtil.add(orderAmountWithoutTax, i.getAmountWithoutTax());
                goodsCount += i.getQuantity();

                ContractProjectOrderInfoV contractProjectOrderInfoV = new ContractProjectOrderInfoV();
                BeanUtils.copyProperties(i, contractProjectOrderInfoV);
                contractProjectOrderInfoV.setOrderStatus(1);
                contractProjectOrderInfoVS.add(contractProjectOrderInfoV);
            };
            allOrderAmountWithoutTax = NumberUtil.add(allOrderAmountWithoutTax, orderAmountWithoutTax);
            contractProjectOrderE.setOrderAmount(orderAmount)
                    .setOrderAmountWithoutTax(orderAmountWithoutTax)
                    .setGoodsCount(goodsCount)
                    .setBpmReviewStatus(2)
                    .setGoodsInfo(JSON.toJSONString(contractProjectOrderInfoVS))
                    .setTenantId(tenantId());
            contractProjectOrderES.add(contractProjectOrderE);
        }
        ContractProjectInitiationE initiationE = contractProjectInitiationAppService.getById(projectInitiationId);

        BigDecimal newOrderTotalAmount = NumberUtil.add(initiationE.getOrderTotalAmount(), allOrderAmountWithoutTax);
        BigDecimal remainingAmountWithoutTax = NumberUtil.sub(initiationE.getAmountWithoutTax(), newOrderTotalAmount);

        // 判断立项可用金额是否大于订单导入总金
        if (remainingAmountWithoutTax.compareTo(BigDecimal.ZERO) >= 0) {
            boolean b = contractProjectOrderAppService.saveBatch(contractProjectOrderES);

            initiationE.setOrderTotalAmount(newOrderTotalAmount)
                    .setRemainingAmountWithoutTax(remainingAmountWithoutTax);

            contractProjectInitiationAppService.updateById(initiationE);
            log.info("ContractProjectOrderExportService.save returned:{}", b);
        } else {
           return dataList.stream().map(e -> {
                ErrorRow<ContractProjectOrderImport> errorRow = new ErrorRow<>(e,
                        Collections.singletonList(new ErrorColumn(1, "采购订单金额超出立项剩余可用金额。")));
                return errorRow;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ErrorColumn> isSatisfied(ContractProjectOrderImport next, DataImportContext<ContractProjectOrderImport> dataImportContext) {
        ArrayList<ErrorColumn> errorColumns = new ArrayList<>();
        if (StringUtils.isBlank(next.getPlatform())) {
            errorColumns.add(new ErrorColumn(1, "采购平台不可为空"));
        }
        if (Objects.isNull(ContractProjectOrderPlatformEnum.parseCode(next.getPlatform()))) {
            errorColumns.add(new ErrorColumn(1, "采购平台填写错误"));
        }
        if (StringUtils.isBlank(next.getOrderNumber())) {
            errorColumns.add(new ErrorColumn(1, "订单号不可为空"));
        }
        if (StringUtils.isBlank(next.getName())) {
            errorColumns.add(new ErrorColumn(1, "商品名称不可为空"));
        }
        if (StringUtils.isBlank(next.getUnit())) {
            errorColumns.add(new ErrorColumn(1, "规格\\型号不可为空"));
        }
        if (Objects.isNull(next.getUnitPrice())) {
            errorColumns.add(new ErrorColumn(1, "单价不可为空"));
        }
        if (Objects.isNull(next.getQuantity())) {
            errorColumns.add(new ErrorColumn(1, "数量不可为空"));
        }
        if (Objects.isNull(next.getAmount())) {
            errorColumns.add(new ErrorColumn(1, "总金额（含税）不可为空"));
        }
        if (Objects.isNull(next.getAmountWithoutTax())) {
            errorColumns.add(new ErrorColumn(1, "总金额（不含税）不可为空"));
        }
        if (StringUtils.isBlank(next.getOrderAccount())) {
            errorColumns.add(new ErrorColumn(1, "下单人不可为空"));
        }
        if (Objects.isNull(next.getOrderCreateTime())) {
            errorColumns.add(new ErrorColumn(1, "下单时间不可为空"));
        }

        return errorColumns;
    }

    private static final String modelName = "立项订单数据导入模板.xlsx";

    private static final String materialImportModel = "excel/template/" + modelName;

    public FileVo orderImportTemplateDownload() {
        try {
            Path easy_excel_temp = Files.createTempFile("EASY_EXCEL_TEMP", ".xlsx");
            File file = easy_excel_temp.toFile();
            ClassPathResource resource = new ClassPathResource(materialImportModel);
            ContractProjectOrderImport importD = new ContractProjectOrderImport();

            Map<Integer, String[]> platform = new HashMap<>();
            platform.put(0, new String[]{"交心易购", "其他线上平台", "线下采购"});
            EasyExcel.write(file, ContractProjectOrderImport.class)
                    .withTemplate(resource.getInputStream())
                    .registerWriteHandler(new HeadModifyHandler(platform, 1))
                    .sheet()
                    .needHead(false)
                    .doWrite(List.of(importD));


            byte[] data;
            try (FileInputStream fis = new FileInputStream(file);
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                int len;
                byte[] buffer = new byte[1024];
                while ((len = fis.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                data = byteArrayOutputStream.toByteArray();
                FileVo fileVo = fileStorageService.tmpSave(data, modelName, tenantId());
                fileVo.setName(modelName);
                return fileVo;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
