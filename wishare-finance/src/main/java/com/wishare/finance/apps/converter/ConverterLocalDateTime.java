package com.wishare.finance.apps.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DateUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.time.LocalTime.MIDNIGHT;

@Slf4j
public class ConverterLocalDateTime implements Converter<LocalDateTime> {

    private DateTimeFormatter _formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter _SLASH_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    @Override
    public Class<?> supportJavaTypeKey() {
        return LocalDateTime.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.NUMBER;
    }

    /*@Override
    public LocalDateTime convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if (cellData.getType().equals(CellDataTypeEnum.NUMBER)) {
            LocalDate localDate = LocalDate.of(1900, 1, 1);
            //excel 有些奇怪的bug, 导致日期数差2
            localDate = localDate.plusDays(cellData.getNumberValue().longValue() - 2);
            return localDate.atStartOfDay();
        }
        if (cellData.getStringValue() == null) {
            return null;
        }

        return convert(cellData.getStringValue());
    }
*/
    @Override
    public LocalDateTime convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
                                           GlobalConfiguration globalConfiguration) {

        if (CellDataTypeEnum.STRING.equals(cellData.getType())) {
            return convert(cellData.getStringValue());
        }
        if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
            return DateUtil.getLocalDateTime(cellData.getNumberValue().doubleValue(),
                    globalConfiguration.getUse1904windowing());
        } else {
            return DateUtil.getLocalDateTime(cellData.getNumberValue().doubleValue(),
                    contentProperty.getDateTimeFormatProperty().getUse1904windowing());
        }
    }

    /*@Override
    public WriteCellData<?> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return new WriteCellData(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
*/
    @Override
    public WriteCellData<?> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        if (Objects.isNull(value)){
            return new WriteCellData<>();
        }
        LocalTime localTime = value.toLocalTime();
        if (localTime.compareTo(MIDNIGHT)==0){
            return new WriteCellData(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        return new WriteCellData(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public LocalDateTime convert(String source) {
        source = source.trim();
        if ("".equals(source)) {
            return null;
        }
        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            // yyyy-MM
            return LocalDateTime.parse(source + "-01 00:00:00", _formatter);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            // yyyy-MM-dd
            return LocalDateTime.parse(source + " 00:00:00", _formatter);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            // yyyy-MM-dd HH:mm
            return LocalDateTime.parse(source + ":00", _formatter);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            // yyyy-MM-dd HH:mm:ss
            return LocalDateTime.parse(source, _formatter);
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            // yyyy-MM-dd T HH:mm:ss
            return LocalDateTime.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd w HH:mm:ss"));
        } else if (source.matches("^\\d{4}/\\d{1,2}$")) {
            // yyyy-MM
            return LocalDateTime.parse(source + "/01 00:00:00", _SLASH_FORMATTER);
        }else if (source.matches("^\\d{4}/\\d{1,2}/\\d{1,2}$")) {
            // yyyy-MM-dd
            return LocalDateTime.parse(source + " 00:00:00", _SLASH_FORMATTER);
        } else if (source.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            // yyyy-MM-dd HH:mm
            return LocalDateTime.parse(source + ":00", _SLASH_FORMATTER);
        } else if (source.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            // yyyy-MM-dd HH:mm:ss
            return LocalDateTime.parse(source, _SLASH_FORMATTER);
        } else {
            throw new IllegalArgumentException("Invalid datetime value '" + source + "'");
        }
    }


}
