package com.wishare.finance.apps.service.bill;

import cn.hutool.core.util.StrUtil;
import com.wishare.finance.apps.model.configure.chargeitem.fo.ReduceApprovalProcessF;
import com.wishare.finance.apps.model.configure.chargeitem.vo.ReduceApprovalProcessDTO;
import com.wishare.finance.apps.process.GenericProcessOperate;
import com.wishare.finance.apps.process.service.ReductionAppProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import static com.wishare.finance.apps.process.enums.BusinessProcessType.Reduction_FORM;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReductionApplicationFormService {

    @Lazy
    @Autowired
    private ReductionAppProcessService reductionAppProcessService;

    public ReduceApprovalProcessDTO startReductionByOA(ReduceApprovalProcessF f){
        ReduceApprovalProcessDTO dto = new ReduceApprovalProcessDTO();
        try {
            String str = reductionAppProcessService.createProcess(f, Reduction_FORM);
            String[] arr = StrUtil.splitToArray(str, ',', 2);
            dto.setProcessId(arr[0]);
            dto.setUrl(arr[1]);
            return dto;
        } catch (Exception e) {
            log.error("减免发起流程异常:{}",e);
            return dto;
        }
    }

}
