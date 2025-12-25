package com.wishare.contract.apps.service.revision.export;

import com.alibaba.excel.EasyExcel;
import com.wishare.component.imports.DataImportContext;
import com.wishare.component.imports.IStandardImport;
import com.wishare.component.imports.ImportStandardV;
import com.wishare.component.imports.builder.DataImportBuilder;
import com.wishare.component.imports.extension.easyexcel.handle.HeadModifyHandler;
import com.wishare.component.imports.factory.DataImportFactory;
import com.wishare.component.imports.task.ImportTaskService;
import com.wishare.contract.apps.service.contractset.ContractConcludeAppService;
import com.wishare.contract.domains.consts.ContractNatureEnum;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.enums.revision.*;
import com.wishare.contract.domains.enums.revision.log.LogActionTypeEnum;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.contract.domains.service.revision.log.RevisionLogService;
import com.wishare.contract.domains.task.ImportTaskEnum;
import com.wishare.contract.domains.vo.revision.export.IncomeContractImport;
import com.wishare.contract.domains.vo.revision.export.PayContractImport;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.tools.starter.service.FileStorageService;
import com.wishare.tools.starter.vo.FileVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.time.LocalDate;
import java.util.*;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/10  15:48
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class IncomeExportService implements IStandardImport<IncomeContractImport>, IOwlApiBase {

    private final ImportTaskService importTaskService;

    private final FileStorageService fileStorageService;

    private final ContractConcludeAppService contractConcludeAppService;

    private final ContractIncomeConcludeMapper contractIncomeConcludeMapper;

    private final RevisionLogService logService;

    public ImportStandardV customerImport(MultipartFile excelFile) {
        importTaskService.executeTask(
                ImportTaskEnum.INCOME_CONTRACT_IMPORT, excelFile, inputStream -> {
                    DataImportBuilder<IncomeContractImport> importBuilder = DataImportFactory.create();
                    Map<String, Object> customMap = new HashMap<>();
                    return importBuilder.createFrom(inputStream, IncomeContractImport.class, 1)
                            .with(customMap)
                            .standard(this);
                });
        return new ImportStandardV();
    }

    @Override
    public void execute(IncomeContractImport next, DataImportContext<IncomeContractImport> dataImportContext) {

    }

    @Override
    public List<ErrorRow<IncomeContractImport>> save(List<IncomeContractImport> dataList, DataImportContext<IncomeContractImport> dataImportContext) {

        for (IncomeContractImport map : dataList) {

            ContractIncomeConcludeE add = new ContractIncomeConcludeE();

            add.setName(map.getName())
                    .setContractNature(VirtuallyTypeEnum.parseCode(map.getContractNature()))
                    .setCategoryName(map.getCategoryName())
                    .setContractType(ContractTypeEnum.parseCode(map.getContractType()))
                    .setPartyAName(map.getPartyAName())
                    .setPartyBName(map.getPartyBName())
                    .setOrgName(map.getOrgName())
                    .setDepartName(map.getDepartName());

            try {
                add.setContractAmount(new BigDecimal(map.getContractAmount()));
                add.setSignDate(LocalDate.parse(map.getSignDate()));
                add.setGmtExpireStart(LocalDate.parse(map.getGmtExpireStart()));
                add.setGmtExpireEnd(LocalDate.parse(map.getGmtExpireEnd()));
            } catch (Exception e) {
                log.info("支出合同导入部时分字段处理异常");
            }

            String code = contractConcludeAppService.revContractCode(tenantId(), null, RevTypeEnum.收入合同.getCode());

            add.setContractNo(code)
                    .setTenantId(tenantId())
                    .setReviewStatus(ReviewStatusEnum.待提交.getCode());

            try {
                contractIncomeConcludeMapper.insert(add);

                logService.insertOneLog(add.getId(), add.getName(), LogActionTypeEnum.EXCEL导入新增.getCode());
            } catch (Exception e) {
                log.info("支出合同Excel数据导入新增异常" + e);
            }

        }

        return new ArrayList<>();
    }

    @Override
    public List<ErrorColumn> isSatisfied(IncomeContractImport next, DataImportContext<IncomeContractImport> dataImportContext) {

        Map<String, Object> customMap = (Map<String, Object>) dataImportContext.getCustomMap();
        ArrayList<ErrorColumn> errorColumns = new ArrayList<>();

        if (StringUtils.isBlank(next.getName())) {
            errorColumns.add(new ErrorColumn(1, "合同名称不可为空"));
        }
        if (Objects.isNull(ContractTypeEnum.parseCode(next.getContractType()))) {
            errorColumns.add(new ErrorColumn(1, "合同分类属性错误"));
        }

        return errorColumns;
    }

    private static final String modelName = "收入合同列表数据导入模板.xlsx";

    private static final String materialImportModel = "excel/template/" + modelName;

    public FileVo download() {
        try {
            Path easy_excel_temp = Files.createTempFile("EASY_EXCEL_TEMP", ".xlsx");
            File file = easy_excel_temp.toFile();
            ClassPathResource resource = new ClassPathResource(materialImportModel);
            IncomeContractImport importD = new IncomeContractImport();

            Map<Integer, String[]> nature = new HashMap<>();
            nature.put(1, new String[]{"非虚拟合同", "虚拟合同"});
            Map<Integer, String[]> type = new HashMap<>();
            nature.put(3, new String[]{"普通合同", "框架合同", "补充协议"});
            EasyExcel.write(file, IncomeContractImport.class)
                    .withTemplate(resource.getInputStream())
                    .registerWriteHandler(new HeadModifyHandler(nature, 1))
                    .registerWriteHandler(new HeadModifyHandler(type, 1))
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
