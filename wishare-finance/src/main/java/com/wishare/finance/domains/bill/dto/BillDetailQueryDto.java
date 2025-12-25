package com.wishare.finance.domains.bill.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class BillDetailQueryDto {

    private Long billId;

    /**
     * 账单类型（1:应收账单，2:预收账单，3:临时收费账单，4：应付账单）
     */
    private Integer billType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BillDetailQueryDto queryDto = (BillDetailQueryDto) o;
        return Objects.equals(billId, queryDto.billId) && Objects.equals(billType, queryDto.billType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(billId, billType);
    }
}
