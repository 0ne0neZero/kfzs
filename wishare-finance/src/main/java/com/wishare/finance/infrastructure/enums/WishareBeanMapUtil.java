package com.wishare.finance.infrastructure.enums;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.util.Collection;
import java.util.List;

public enum WishareBeanMapUtil {

    INSTANCE;

    private final MapperFactory mapperFactory;

    private final MapperFacade mapperFacade;

    WishareBeanMapUtil() {
        mapperFactory = new DefaultMapperFactory.Builder().build();
        mapperFacade = mapperFactory.getMapperFacade();
    }

    public <S, T> T map(S sourceData, Class<T> toClass) {
        if (null == sourceData) {
            return null;
        }
        return mapperFacade.map(sourceData, toClass);
    }

    public <S, T> void map(S sourceData, T toData) {
        if (sourceData == null || toData == null) {
            return;
        }
        mapperFacade.map(sourceData, toData);
    }

    public <S, T> List<T> mapAsList(Collection<S> sourceData, Class<T> toClass) {
        return mapperFacade.mapAsList(sourceData, toClass);
    }
}
