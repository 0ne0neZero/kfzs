package com.wishare.finance.apis.bill;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xujian
 * @date 2022/10/20
 * @Description:
 */
@Api(tags = {"账单冲销"})
@Validated
@RestController
@RequestMapping("/reverse")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillReverseApi {
}
