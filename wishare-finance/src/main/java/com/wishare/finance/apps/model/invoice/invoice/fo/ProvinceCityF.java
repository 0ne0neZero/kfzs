package com.wishare.finance.apps.model.invoice.invoice.fo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author szh
 * @date 2024/5/9 16:22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProvinceCityF {

    /**
     * 省市区全城
     */
    private String fullName;

    /**
     * 省市区code
     */
    private List<String> provinceCityCode;
}
