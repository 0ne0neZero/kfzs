package com.wishare.finance.apis.bill;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"账单审核"})
@Validated
@RestController
@RequestMapping("/approve")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillApproveApi {

}
