package com.wishare.finance.domains.message;

import java.util.Set;

public interface VoucherBillRuleMessageSendService {


    void send2PC(Set<String> userIds, String title);

    void send2JJ(Set<String> user4ACodes, String title);

}



