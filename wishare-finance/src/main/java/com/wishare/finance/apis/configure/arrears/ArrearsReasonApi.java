package com.wishare.finance.apis.configure.arrears;


import com.wishare.finance.apps.model.configure.arrearsCategory.fo.CreateArrearsReasonF;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsReasonBillV;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsReasonV;
import com.wishare.finance.apps.service.configure.arrears.ArrearsReasonAppService;
import com.wishare.finance.apps.service.spacePermission.SpacePermissionAppService;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;


@Api(tags = {"欠费原因配置接口"})
@RestController
@RequestMapping("/arrearsReason")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ArrearsReasonApi {

    private final ArrearsReasonAppService arrearsReasonAppService;

    private final SpacePermissionAppService spacePermissionAppService;


    @ApiOperation(value = "新增欠费原因", notes = "新增欠费原因")
    @PostMapping("/create")
    public boolean create(@RequestBody @Validated CreateArrearsReasonF createArrearsReasonF) {
        return arrearsReasonAppService.create(createArrearsReasonF);
    }


    @ApiOperation("分页查询历史欠费原因")
    @PostMapping("/query/page")
    public PageV<ArrearsReasonV> queryByPage(@RequestBody PageF<SearchF<?>> searchF) {
        return arrearsReasonAppService.queryByPage(searchF);
    }


    @ApiOperation("分页查询含有欠费原因的账单")
    @PostMapping("/queryPage/arrearsReasonBill")
    public PageV<ArrearsReasonBillV> queryPageArrearsReasonBill(@RequestBody PageF<SearchF<?>> queryF) {
        PageF<SearchF<?>> queryPermissionF = spacePermissionAppService.getSpacePermissionV(queryF,"b");
        if(Objects.isNull(queryPermissionF)){
            return PageV.of(queryF);
        }
        return arrearsReasonAppService.queryPageBill(queryF);
    }

    @ApiOperation("查询最新欠费原因")
    @PostMapping("/query/queryNewArrearsReason")
    public List<ArrearsReasonV> queryNewArrearsReason(@RequestBody List<Long> billId) {
        return arrearsReasonAppService.queryNewArrearsReason(billId);
    }
}
