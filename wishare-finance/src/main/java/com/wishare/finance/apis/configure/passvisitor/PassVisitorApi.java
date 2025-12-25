package com.wishare.finance.apis.configure.passvisitor;

import com.wishare.finance.apps.model.configure.chargeitem.fo.VisitorApprovalProcessF;
import com.wishare.finance.apps.process.vo.ProcessCreateV;
import com.wishare.finance.apps.service.VisitorApplicationFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"访客接口"})
@RestController
@RequestMapping("/pass")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PassVisitorApi {
    private final VisitorApplicationFormService visitorApplicationFormService;


    @ApiOperation("交建通-访客邀约发起OA审批流")
    @PostMapping("/visitor/initOaApprovalProcess")
    public ProcessCreateV initOaApprovalProcess(@RequestBody VisitorApprovalProcessF dto) {
        return visitorApplicationFormService.initOaApprovalProcess(dto);
    }
}
