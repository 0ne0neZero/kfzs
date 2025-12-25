package com.wishare.finance.apps.model.bill.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author zyj
 * @since 2023-08-05
 */
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class BlockEntity {

    /**
     * 期区id
     */
    private String id;

    /**
     * 期区名称
     */
    private String name;
}
