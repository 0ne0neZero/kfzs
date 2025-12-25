package com.wishare.finance.infrastructure.utils;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.wishare.starter.Global;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.Type;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.hutool.core.date.DatePattern.NORM_MONTH_PATTERN;


public class MapperFacadeUtil {

    public static MapperFacade getMoneyMapperFacade(){
        BigDecimal HUNDRED = new BigDecimal(100);

        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.getConverterFactory().registerConverter(new BidirectionalConverter<Long, BigDecimal>(){
            // 元和分互转
            @Override
            public BigDecimal convertTo(Long source, Type<BigDecimal> destinationType, MappingContext mappingContext) {
                return new BigDecimal(source).divide(HUNDRED, 2, RoundingMode.HALF_UP);
            }

            @Override
            public Long convertFrom(BigDecimal source, Type<Long> destinationType, MappingContext mappingContext) {
                return source.multiply(HUNDRED).longValue();
            }
        });

        mapperFactory.getConverterFactory().registerConverter(new BidirectionalConverter<LocalDate, String>(){

            @Override
            public String convertTo(LocalDate source, Type<String> destinationType, MappingContext mappingContext) {
                return LocalDateTimeUtil.format(source, NORM_MONTH_PATTERN);
            }

            @Override
            public LocalDate convertFrom(String source, Type<LocalDate> destinationType, MappingContext mappingContext) {
                return LocalDateTimeUtil.parseDate(source, NORM_MONTH_PATTERN);
            }
        });

        mapperFactory.getConverterFactory().registerConverter(new BidirectionalConverter<BigDecimal, String>(){

            @Override
            public String convertTo(BigDecimal source, Type<String> destinationType, MappingContext mappingContext) {
                return source.stripTrailingZeros().toPlainString()+"%";
            }

            @Override
            public BigDecimal convertFrom(String source, Type<BigDecimal> destinationType, MappingContext mappingContext) {
                return new BigDecimal(source.replace("%",""));
            }
        });
        return mapperFactory.getMapperFacade();
    }

    public static MapperFacade getMoneyMapperFacade(Class calzzA, Class calzzB, Map<String, String> fieldMap){
        BigDecimal HUNDRED = new BigDecimal(100);

        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        mapperFactory.getConverterFactory().registerConverter(new BidirectionalConverter<Long, BigDecimal>(){
            // 元和分互转
            @Override
            public BigDecimal convertTo(Long source, Type<BigDecimal> destinationType, MappingContext mappingContext) {
                return new BigDecimal(source).divide(HUNDRED, 2, RoundingMode.HALF_UP);
            }

            @Override
            public Long convertFrom(BigDecimal source, Type<Long> destinationType, MappingContext mappingContext) {
                return source.multiply(HUNDRED).longValue();
            }
        });
        mapperFactory.getConverterFactory().registerConverter("unitPriceConverter" , new BidirectionalConverter<BigDecimal, BigDecimal>(){
            // 单价互转
            @Override
            public BigDecimal convertTo(BigDecimal source, Type<BigDecimal> destinationType, MappingContext mappingContext) {
                return source.divide(HUNDRED, 8, RoundingMode.HALF_UP).stripTrailingZeros();
            }

            @Override
            public BigDecimal convertFrom(BigDecimal source, Type<BigDecimal> destinationType, MappingContext mappingContext) {
                return source.multiply(HUNDRED);
            }
        });
        ClassMapBuilder classMapBuilder = mapperFactory.classMap(calzzA, calzzB);
        List<Field> fieldsA = new ArrayList<>();
        while (Object.class != calzzA){
            fieldsA.addAll(Arrays.asList(calzzA.getDeclaredFields()));
            calzzA = calzzA.getSuperclass();
        }
        List<String> fieldsNameA = fieldsA.stream().map(Field::getName).collect(Collectors.toList());
        List<Field> fieldsB = new ArrayList<>();
        while (Object.class != calzzB){
            fieldsB.addAll(Arrays.asList(calzzB.getDeclaredFields()));
            calzzB = calzzB.getSuperclass();
        }
        List<String> fieldsNameB = fieldsB.stream().map(Field::getName).collect(Collectors.toList());
        ClassMapBuilder field = null;
        for(Map.Entry<String, String> entry : fieldMap.entrySet()){
            if(fieldsNameA.contains(entry.getKey()) && fieldsNameB.contains(entry.getValue())){
                field = classMapBuilder.field(entry.getKey(), entry.getValue());
            }
        }
        if(field != null){
            if(fieldsNameA.contains("unitPrice") && fieldsNameB.contains("unitPrice")){
                classMapBuilder.fieldMap("unitPrice", "unitPrice").converter("unitPriceConverter").add().byDefault().register();
            } else {
                field.byDefault().register();
            }
        }
        return mapperFactory.getMapperFacade();
    }

    public static MapperFacade defaultMapperFacade() {
        return Global.mapperFacade;
    }
}