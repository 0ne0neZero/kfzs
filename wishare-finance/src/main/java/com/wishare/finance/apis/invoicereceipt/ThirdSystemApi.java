package com.wishare.finance.apis.invoicereceipt;

import com.wishare.finance.apps.model.invoice.invoice.fo.LingshuitongLoginF;
import com.wishare.finance.apps.service.invoicereceipt.ThirdSystemAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xujian
 * @date 2022/12/1
 * @Description:
 */
@Api(tags = {"第三方系统"})
@RestController
@RequestMapping("/thirdSystem")
@RequiredArgsConstructor
public class ThirdSystemApi {

    private final ThirdSystemAppService thirdSystemAppService;

    @PostMapping("/lingshuitonglogin")
    @ApiOperation(value = "灵税通进项发票系统登录", notes = "灵税通进项发票系统登录")
    public String lingshuitonglogin(@Validated @RequestBody LingshuitongLoginF form) {
        return thirdSystemAppService.lingshuitonglogin(form);
    }

}
