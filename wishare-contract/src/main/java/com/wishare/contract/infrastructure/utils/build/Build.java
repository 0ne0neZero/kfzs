package com.wishare.contract.infrastructure.utils.build;

import java.io.Serializable;

/**
 * @author hxl97
 */
public interface Build<T> extends Serializable {
    T build();
}
