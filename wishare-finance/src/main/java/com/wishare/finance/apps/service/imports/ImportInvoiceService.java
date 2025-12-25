package com.wishare.finance.apps.service.imports;

import com.wishare.component.imports.DataImportContext;
import com.wishare.component.imports.IStandardImport;
import com.wishare.component.imports.IValidator;
import com.wishare.finance.apps.template.BlueInvoiceImportT;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceImportService;
import com.wishare.owl.enhance.IOwlApiBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportInvoiceService implements IStandardImport<BlueInvoiceImportT>, IOwlApiBase {

    private final ImportPublicService importPublicService;
    private final InvoiceImportService invoiceImportService;

    @Override
    public void execute(BlueInvoiceImportT next, DataImportContext<BlueInvoiceImportT> dataImportContext) {

    }

    @Override
    public List<ErrorRow<BlueInvoiceImportT>> save(List<BlueInvoiceImportT> dataList,
                                                   DataImportContext<BlueInvoiceImportT> dataImportContext) {
        List<BlueInvoiceImportT> errorList = invoiceImportService
                .saveSuccessData((List<BlueInvoiceImportT>) importPublicService.rowNumberSetDefaultValue(dataList));
        return importPublicService.saveAfterHandler(dataList, errorList);
    }

    @Override
    public List<ErrorColumn> isSatisfied(BlueInvoiceImportT next, DataImportContext<BlueInvoiceImportT> dataImportContext) {
        String errorMessage = invoiceImportService.validRecordChoiceInfo(next, null);
        if (StringUtils.hasText(errorMessage)) {
            log.error("发票补录导入异常：" + errorMessage);
            return List.of(new IValidator.ErrorColumn(errorMessage));
        }
        return null;
    }


}
