package com.wishare.finance.domains.bill.support;

import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.support.SerialNumberFactory;

public class BillSerialNumberFactory implements SerialNumberFactory {

    private static final BillSerialNumberFactory INSTANCE = new BillSerialNumberFactory();

    public static BillSerialNumberFactory getInstance(){
        return INSTANCE;
    }

    @Override
    public String serialNumber() {
        return IdentifierFactory.getInstance().serialNumber("bill","ZD", 20);
    }

}
