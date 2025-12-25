package com.wishare.finance.apis.fangyuan;

import com.wishare.finance.apps.model.fangyuan.vo.ParkRenewInfoPageV;
import com.wishare.finance.apps.service.fangyuan.ParkRenewInfoAppService;
import com.wishare.finance.domains.fangyuan.entity.ParkRenewInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Api(tags = {"方圆停车场信息API"})
@Validated
@RestController
@RequestMapping("/parkRenewInfo")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ParkRenewInfoApi {

    private final ParkRenewInfoAppService parkRenewInfoAppService;

    @PostMapping("/add")
    @ApiOperation(value = "新增停车场信息", notes = "新增停车场信息")
    public Boolean addParkRenewInfo(@Validated @RequestBody ParkRenewInfo parkRenewInfo) {
        return parkRenewInfoAppService.addParkRenewInfo(parkRenewInfo);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新停车场信息", notes = "更新停车场信息")
    public Boolean updateParkRenewInfo(@Validated @RequestBody ParkRenewInfo parkRenewInfo) {
        return parkRenewInfoAppService.updateParkRenewInfo(parkRenewInfo);
    }

    @PostMapping("/page")
    @ApiOperation(value = "分页查询停车场信息", notes = "分页查询停车场信息")
    public PageV<ParkRenewInfoPageV> getParkRenewInfoList(@Validated @RequestBody PageF<SearchF<?>> pageF){
        return parkRenewInfoAppService.getParkRenewInfoList(pageF);
    }

    @PostMapping("/getByBillId/{billId}")
    @ApiOperation(value = "查询停车场信息", notes = "查询停车场信息")
    public ParkRenewInfo getParkRenewInfo(@PathVariable("billId") @ApiParam("账单id") @NotNull(message = "账单id不能为空") Long billId) {
        return parkRenewInfoAppService.getParkRenewInfo(billId);
    }


}
