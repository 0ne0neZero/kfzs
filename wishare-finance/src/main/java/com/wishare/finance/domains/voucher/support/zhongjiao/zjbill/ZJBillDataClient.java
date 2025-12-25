package com.wishare.finance.domains.voucher.support.zhongjiao.zjbill;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.domains.voucher.support.fangyuan.FangYuanBillProperties;
import com.wishare.finance.domains.voucher.support.zhongjiao.ZJBillProperties;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.OrderDealResult;
import com.wishare.finance.infrastructure.remote.vo.zj.ZJSendresultV;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@AllArgsConstructor
@EnableConfigurationProperties(ZJBillProperties.class)
public class ZJBillDataClient {
    private final ZJBillProperties zjBillProperties;
    public  ZJSendresultV getPushOrder(String pushOrder,String billno ,String businessCode) {
        //http://10.35.58.87:6601
        String xChangeServletUrl = zjBillProperties.getUrl()+"/ESB/API/ChannelZJFW/YXDMC/PullFinanceOrderData";
        HttpRequest post = header(xChangeServletUrl);
        post.header("djnm", billno);
        post.header("lyxt", "CCCG-DMC");
        post.header("djbh", billno);
        post.header("businessCode", businessCode);
        post.body(pushOrder);
        log.info("中交请求信息 ：{}", post);
        log.info("报文信息:{}", JSON.toJSONString(pushOrder));
        HttpResponse execute = post.execute();
        String body = execute.body();
        log.info("ESB返回信息 ：{}", body);
        ZJSendresultV zjSendresultV = JSONObject.parseObject(body, ZJSendresultV.class);
        log.info("中交推单返回信息 ：{}", JSON.toJSONString(zjSendresultV));
        return zjSendresultV;
    }

    private HttpRequest header(String xChangeServletUrl) {
        HttpRequest post = HttpUtil.createPost(xChangeServletUrl);
        post.header(Header.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
        post.header(Header.ACCEPT, "*/*");
        post.contentType("application/json;charset=UTF-8");
        post.keepAlive(true);
        return post;
    }


    public String getOrderStatus(String orderStatusBody,String url) {
        String xChangeServletUrl = zjBillProperties.getUrl()+url;
        HttpRequest post = header(xChangeServletUrl);
        post.body(orderStatusBody);
        HttpResponse execute = post.execute();
        return execute.body();
    }

    public static void main(String[] args) {
        // 'HSDW':399E4F6F70AC485787A58FDEDD7CEDFF,'HSBM':null,'XZZZ':'1072467','XZBM':'1072467'
        String  pushOrder = "{\n" +
                "    \"context\": \"{'appInstanceCode':'10000','unitCode':'MDM','sourceSystem':'CCCG-DMC','businessCode':'DXJS','psData':[{'BILLDATA':[{'code':'DXJS','isMain':'true','data':[{'DJNM':'BJZJhxbz202406210012','DJBH':'BJZJhxbz202406210012','HSDW':null,'HSBM':null,'XZZZ':'1072598','XZBM':'101440049','XMID':'PJ2008000233','ZDR':'L10018794','DJRQ':'2024-06-21','LYXT':'CCCG-DMC','YWLX':'C3C70D06EA514287A01DAC7AB9DCEA0D','HTBH':'CT202311352805','ZQR':'BP00187934','JSDH':'ZJFW-CSYY-FW-2024-001-2405-02','SFQR':'1','JSKSRQ':null,'JSJZRQ':null,'JLQRRQ':null,'HSJEBB':100.00,'BHSJEBB':null,'SEBB':null,'LJHSJEBB':100.00,'JSFS':'1','BWBBH':null,'BZID':null,'DJZT':'1','DJFJZS':null,'BZSY':'对下结算单报账','DJZY':null,'FPYQ':null,'NWZBZ':null,'PZRQ':'2024-06-21'}]},{'code':'YXXX','isMain':'false','data':[]},{'code':'SPRZ','isMain':'false','data':null}]}]}\",\n" +
                "    \"requireCallback\": \"false\",\n" +
                "    \"taskType\": \"CCCCInterface\"\n" +
                "}";
//                getPushOrder(pushOrder,"BJZJhxbz202406210012",  "DXJS");
    }
}
