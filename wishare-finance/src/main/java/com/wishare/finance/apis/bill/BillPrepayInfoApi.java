package com.wishare.finance.apis.bill;

import com.wishare.finance.apps.model.bill.fo.BillPrepayByMchNoUpdateF;
import com.wishare.finance.apps.model.bill.fo.BillPrepayInfoAddF;
import com.wishare.finance.apps.model.bill.fo.BillPrepayInfoQueryF;
import com.wishare.finance.apps.model.bill.fo.BillPrepayInfoUpdateF;
import com.wishare.finance.apps.model.bill.vo.BillPrepayInfoV;
import com.wishare.finance.apps.service.bill.prepay.BillPrepayInfoAppService;
import com.wishare.finance.infrastructure.support.mutiltable.MutilTableParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillPrepayInfoApi
 * @date 2023.11.08  10:56
 * @description:账单预支付信息 api
 */
@Api(tags = {"账单预支付信息接口"})
@Validated
@RestController
@RequestMapping("/billPrepay")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillPrepayInfoApi {

    private final BillPrepayInfoAppService billPrepayInfoAppService;

    @PostMapping("/list")
    @ApiOperation(value = "查询账单预支付信息", notes = "查询账单预支付信息")
    public List<BillPrepayInfoV> queryPrepayList(@Validated @RequestBody BillPrepayInfoQueryF billPrepayInfoQueryF){
        MutilTableParam.supCpUnitId.set(billPrepayInfoQueryF.getSupCpUnitId());
        return billPrepayInfoAppService.queryPrepayList(billPrepayInfoQueryF);
    }

    @PostMapping("/add")
    @ApiOperation(value = "账单预支付", notes = "账单预支付")
    public Boolean billPrepay(@Validated @RequestBody BillPrepayInfoAddF billPrepayInfoAddF){
        return billPrepayInfoAppService.billPrepay(billPrepayInfoAddF);
    }

    @PostMapping("/update")
    @ApiOperation(value = "账单预支付信息更新", notes = "账单预支付信息更新")
    public Boolean releasePaymentOpr(@Validated @RequestBody BillPrepayInfoUpdateF billPrepayInfoUpdateF){
        return billPrepayInfoAppService.releasePaymentOpr(billPrepayInfoUpdateF);
    }

    @PostMapping("/updateByMchNo")
    @ApiOperation(value = "更新账单列表的预支付状态", notes = "更新账单列表的预支付状态")
    public Boolean updateByMchNo(@Validated @RequestBody BillPrepayByMchNoUpdateF billPrepayInfoUpdateF) {
        return billPrepayInfoAppService.updateByMchNo(billPrepayInfoUpdateF);
    }

    @PostMapping("/getByMchNo")
    @ApiOperation(value = "更新订单号查询预支付信息", notes = "更新订单号查询预支付信息")
    public List<BillPrepayInfoV> getByMchNo(@Validated @RequestParam @NotBlank(message = "商户订单号不能为空")String mchOrderNo,
                                      @RequestParam Integer payState) {
        return billPrepayInfoAppService.getByMchNo(mchOrderNo,payState);
    }

}
