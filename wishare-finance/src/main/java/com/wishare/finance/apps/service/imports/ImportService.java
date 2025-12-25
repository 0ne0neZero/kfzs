package com.wishare.finance.apps.service.imports;

import com.alibaba.fastjson.JSON;
import com.wishare.component.imports.ImportStandardV;
import com.wishare.component.imports.factory.DataImportMultiFactory;
import com.wishare.component.imports.task.ImportTaskService;
import com.wishare.component.imports.task.beans.ContextInfo;
import com.wishare.finance.apps.enums.imports.ImportTaskEnum;
import com.wishare.finance.apps.template.BlueInvoiceImportT;
import com.wishare.finance.domains.conts.ImportConst;
import com.wishare.finance.infrastructure.conts.FinanceConst;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityShortV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.ThreadPoolManager;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wishare.finance.apps.enums.imports.ImportTaskEnum.蓝票补录导入;

@Service
@Slf4j
public class ImportService implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ImportTaskService importTaskService;

    @Setter(onMethod_ = {@Autowired})
    private SpaceClient client;

    @Setter(onMethod_ = {@Autowired})
    private ImportInvoiceService importInvoiceService;


    public ImportStandardV importTask(MultipartFile excelFile, ImportTaskEnum importTaskEnum) {
        String tenantId = tenantId();
        String userId = userId();
        String userName = userName();
        switch (importTaskEnum) {
            case 蓝票补录导入:
                importTaskService.createImportTask(null, null, FinanceConst.SERVER_NAME,
                        蓝票补录导入, excelFile, List.of(ContextInfo.MultiSheetInfo.builder()
                                .dataTClass(BlueInvoiceImportT.class.getName())
                                .headRowNum(2).sheetName(ImportConst.BLUE_INVOICE_IMPORT)
                                .standardImportService(ImportInvoiceService.class.getName()).build()));
            // 切换下方可以用于本地调试
//            importTaskService.executeTask(
//                    蓝票补录导入, excelFile, inputStream -> {
//                        Map<String, Object> customMap = new HashMap<>();
//                        customMap.put("tenantId", tenantId);
//                        customMap.put("userId", userId);
//                        customMap.put("userName", userName);
//                        //headRowNum对接真正开始解析excel数据的第几行
//                        return DataImportMultiFactory.converge(inputStream,
//                                customMap,
//                                sheets -> List.of(
//                                        new DataImportMultiFactory.MultiSheetInfo(BlueInvoiceImportT.class, 2, ImportConst.BLUE_INVOICE_IMPORT,
//                                                importInvoiceService)
//                                )
//                        );
//                    }, ThreadPoolManager.executorService);
            break;
            default:
                throw new IllegalArgumentException("导入任务不合法，请联系管理人员排查。");
        }
        return new ImportStandardV();
    }

}
