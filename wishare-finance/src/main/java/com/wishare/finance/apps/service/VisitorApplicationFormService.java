package com.wishare.finance.apps.service;

import com.wishare.finance.apps.model.configure.chargeitem.fo.VisitorApprovalProcessF;
import com.wishare.finance.apps.process.service.VisitorAppProcessService;
import com.wishare.finance.apps.process.vo.ProcessCreateV;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.wishare.finance.apps.process.enums.BusinessProcessType.VISITOR_FORM;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VisitorApplicationFormService {
    private final VisitorAppProcessService visitorAppProcessService;
    public ProcessCreateV initOaApprovalProcess(VisitorApprovalProcessF dto){
        return visitorAppProcessService.createProcessVistor(dto, VISITOR_FORM);
    }
}
