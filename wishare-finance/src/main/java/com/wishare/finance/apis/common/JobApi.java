package com.wishare.finance.apis.common;

import com.alibaba.fastjson.JSONArray;
import com.wishare.finance.apps.pushbill.vo.dx.DxPaymentDetails;
import com.wishare.finance.apps.scheduler.mdm.Mdm11Handler;
import com.wishare.finance.apps.scheduler.mdm.Mdm73Handler;
import com.wishare.finance.apps.scheduler.mdm.Mdm97Handler;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author hhb
 * @describe
 * @date 2025/9/19 13:58
 */
@RestController
public class JobApi {
    @Autowired
    private Mdm97Handler mdm97Handler;
    @Autowired
    private Mdm73Handler mdm73Handler;
    @Autowired
    private OrgClient orgClient;


    @GetMapping("/queryDetailsOfPayments")
    public void runJob(@RequestParam("oig") String oig,@RequestParam("type") Integer type){
        List<String> dateList = Mdm11Handler.splitDatesByInterval("2021-09-15", 28);
        System.out.println("时间范围："+ JSONArray.toJSON(dateList));
        List<String> oidList = orgClient.getAllValidOidList();
        System.out.println("4A组织范围："+ JSONArray.toJSON(oidList));
        // 当前时间减10年
        LocalDate dateTenYearsAgo = LocalDate.now().minus(10, ChronoUnit.YEARS);
        String start = dateTenYearsAgo.toString();
        String end = LocalDate.now().toString();
        System.out.println("银行信息范围："+ start +" ～ "+end);
        if(type == 1){
            mdm97Handler.doSync(oig,dateList);
            mdm73Handler.doSync(oig,start,end);
        }else if (type == 2){
            mdm97Handler.doSync(oig,dateList);
        }else if (type == 3){
            mdm73Handler.doSync(oig,start,end);
        }else if (type == 4){
            for (String oid1 : oidList) {
                mdm97Handler.doSync(oid1,dateList);
                //mdm73Handler.doSync(oid1,start,end);
            }
        }else if (type == 5){
            for (String oid1 : oidList) {
                //mdm97Handler.doSync(oid1,dateList);
                mdm73Handler.doSync(oid1,start,end);
            }
        }else if (type == 6){
            for (String oid1 : oidList) {
                mdm97Handler.doSync(oid1,dateList);
                mdm73Handler.doSync(oid1,start,end);
            }
        }
    }
}
