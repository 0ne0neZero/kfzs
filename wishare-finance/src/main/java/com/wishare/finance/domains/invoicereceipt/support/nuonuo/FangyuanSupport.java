package com.wishare.finance.domains.invoicereceipt.support.nuonuo;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.XmlUtil;
import com.alibaba.fastjson.JSON;
import com.wishare.finance.infrastructure.notify.SignatureUtil;
import com.wishare.finance.infrastructure.remote.fo.external.fangyuan.ElectroniceDetailF;
import com.wishare.finance.infrastructure.remote.fo.external.fangyuan.ElectroniceInfoF;
import com.wishare.finance.infrastructure.remote.fo.external.fangyuan.InterfaceInfoF;
import com.wishare.finance.infrastructure.remote.vo.external.fangyuan.InvoiceSuccessResV;
import com.wishare.finance.infrastructure.remote.vo.external.fangyuan.RedApplyQueryV;
import com.wishare.finance.infrastructure.remote.vo.external.fangyuan.ReturnElectroniceV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpEntity;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.wishare.finance.domains.invoicereceipt.consts.FangyuanConsts.*;


/**
 * @author dongpeng
 * @date 2023/6/20 11:06
 */
@Slf4j
@RefreshScope
@Service
public class FangyuanSupport implements ApiBase {


    @Value("${invoice.fangyuan.wsdUrl:demo}")
    private String wsdUrl;
    @Value("${invoice.fangyuan.wviUrl:demo}")
    private String wviUrl;
    /**
     * 电子发票请求开具发票接口(2.0)
     *
     * @param form
     * @return
     */
    public ReturnElectroniceV billingNew(ElectroniceInfoF form){
        String str = getElectroniceInfoXML(form);
        ReturnElectroniceV resV = sendReturnElectroniceV(str);
        if(!SUCCESS_CODE0.equals(resV.getReturnCode())){
            throw BizException.throw400(resV.getReturnMsg());
        }
        return resV;
    }

    /**
     * 方圆电子发票开票结果查询接口
     *
     * @param form
     * @return
     */
    public ReturnElectroniceV queryInvoiceResult(ElectroniceInfoF form){
        String str = getEliInfoXNL(form);
        return sendReturnElectroniceV(str);
    }

    /**
     * 方圆请求开具全电发票接口(2.0)
     *
     * @param form
     * @return
     */
    public InvoiceSuccessResV opeMplatformBillingNew(InterfaceInfoF form) {
        String str = getInvoiceDataReqFXML(form);
        InvoiceSuccessResV invoiceSuccessResV = sendInvoiceSuccessResV(str);
        if(!SUCCESS_CODE0000.equals(invoiceSuccessResV.getCode()) &&
                !SUCCESS_CODE00000.equals(invoiceSuccessResV.getCode())){
            throw BizException.throw400(invoiceSuccessResV.getMess());
        }
        return invoiceSuccessResV;
    }

    /**
     * 方圆全电发票详情查询接口
     *
     * @param form
     * @return
     */
    public InvoiceSuccessResV opeMplatformQueryInvoiceResult(InterfaceInfoF form) {
        String str = getInvoiceDataReqFXML(form);
        return sendInvoiceSuccessResV(str);
    }

    /**
     * 方圆红字确认单申请
     * @param form
     * @return 红字确认单申请号
     */
    public String electronInvoiceRedApply(InterfaceInfoF form) {
        String str = getInvoiceDataReqFXML(form);
        InvoiceSuccessResV invoiceSuccessResV = sendInvoiceSuccessResV(str);
        if(!SUCCESS_CODE0000.equals(invoiceSuccessResV.getCode()) &&
                !SUCCESS_CODE00000.equals(invoiceSuccessResV.getCode())){
            throw BizException.throw400(invoiceSuccessResV.getMess());
        }
        return form.getDataExchangeId();
    }

    /**
     * 方圆红字确认单查询
     * @param form
     */
    public RedApplyQueryV electronInvoiceRedQuery(InterfaceInfoF form) {
        String str = getInvoiceDataReqFXML(form);
        return sendInvoiceRedSuccessResV(str);
    }

    /**
     * 处理电子发票开票结果返回参数
     * @param str
     * @return
     */
    private ReturnElectroniceV sendReturnElectroniceV(String str) {
        HashMap<String, Object> map = getStringObjectHashMap(str,wsdUrl);
        ReturnElectroniceV resV = null;
        try {
            resV = SignatureUtil.mapToBean(map, ReturnElectroniceV.class);
        } catch (Exception e) {
            log.error("电子发票开票结果转换失败:", e);
        }
        return resV;

    }

    /**
     * 处理全电发票开票结果返回参数
     * @param str
     * @return
     */
    private InvoiceSuccessResV sendInvoiceSuccessResV(String str) {
        HashMap<String, Object> map = getStringObjectHashMap(str,wviUrl);
        Object obj = map.get("return");
        return JSON.parseObject(obj.toString(), InvoiceSuccessResV.class);
    }

    /**
     * 处理全电红字确认单结果返回参数
     * @param str
     * @return
     */
    private RedApplyQueryV sendInvoiceRedSuccessResV(String str) {
        HashMap<String, Object> map = getStringObjectHashMap(str,wviUrl);
        Object obj = map.get("return");
        return JSON.parseObject(obj.toString(), RedApplyQueryV.class);
    }


    private HashMap<String, Object> getStringObjectHashMap(String str,String url) {
        String result = doPostSoap(str,url);
        Map<String, Object> resMap = XmlUtil.xmlToMap(result);
        HashMap<String, Object> map = new HashMap<>();
        for (Entry<?, ?> entry : resMap.entrySet()) {
            extracted(entry.getKey().toString(), entry.getValue(), map);
        }
        return map;
    }

    private HashMap<String,Object> extracted(String key, Object value,HashMap<String,Object> map) {
        if (null != value) {
            if(value instanceof Map){
                Entry<?, ?> entry;
                for (Object obj : ((Map<?,?>) value).entrySet()) {
                    entry = (Entry<?, ?>) obj;
                    extracted(entry.getKey().toString(), entry.getValue(),map);
                }
            }{
                map.put(key,value);
            }
        }
        return map;
    }


    /**
     * 全电发票开具发票
     * 根据拼接 xml 字符串
     * @param form
     * @return
     */
    public  String getInvoiceDataReqFXML(InterfaceInfoF form) {
        //开始拼接请求报文
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tax=\"http://tax.soa.csg.cn\">");
        stringBuilder.append("<soapenv:Header/>\n");
        stringBuilder.append("<soapenv:Body>\n");
        stringBuilder.append("<tax:dataInterface>\n");
        stringBuilder.append("<parameter>\n");
        stringBuilder.append("<![CDATA[\n");
        stringBuilder.append("<interface>\n");
        stringBuilder.append("<version>"+ form.getVersion() +"</version>\n");
        stringBuilder.append("<interfaceCode>"+ form.getInterfaceCode() +"</interfaceCode>\n");
        stringBuilder.append("<taxpayerId>"+ form.getTaxpayerId() +"</taxpayerId>\n");
        stringBuilder.append("<passWord>"+ form.getPassWord() +"</passWord>\n");
        String fjh = "";
        if (StringUtils.isNotBlank(form.getFjh())) {
            fjh = form.getFjh();
        }
        stringBuilder.append("<fjh>"+ fjh +"</fjh>\n");
        stringBuilder.append("<jqbh>"+ form.getJqbh() +"</jqbh>\n");
        stringBuilder.append("<dataExchangeId>"+ form.getDataExchangeId() +"</dataExchangeId>\n");
        stringBuilder.append("<zipCode>"+ form.getZipCode() +"</zipCode>\n");
        stringBuilder.append("<encryptCode>"+ form.getEncryptCode() +"</encryptCode>\n");
        stringBuilder.append("<content>"+ form.getContent() +"</content>\n");
        stringBuilder.append("</interface>\n");
        stringBuilder.append("]]>\n");
        stringBuilder.append("</parameter>\n");
        stringBuilder.append("</tax:dataInterface>\n");
        stringBuilder.append("</soapenv:Body>\n");
        stringBuilder.append("</soapenv:Envelope>\n");
        log.info("全电发票拼接后的参数 data:{} ,content:{}",stringBuilder, Base64.decodeStr(form.getContent()));
        return stringBuilder.toString();
    }

    /**
     * 电子发票开票结果查询
     * 根据拼接 xml 字符串
     * @param form
     * @return
     */
    public  String getEliInfoXNL(ElectroniceInfoF form) {
        log.info("电子发票查询开始拼接请求报文");
        //开始拼接请求报文
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:web=\"http://webservice.companyInterface.dzfp.fp.aisinogd.com\" " +
                "xmlns:pojo=\"http://pojo.hessian.companyInterface.dzfp.fp.aisinogd.com\">\n");
        stringBuilder.append("<soapenv:Header/>\n");
        stringBuilder.append("<soapenv:Body>\n");
        stringBuilder.append("<web:queryEliData>\n");
        stringBuilder.append("<web:ElectroniceInfo>\n");
        stringBuilder.append("<pojo:FPQQLSH>"+form.getFPQQLSH()+"</pojo:FPQQLSH>\n");
        stringBuilder.append("<pojo:DDH>"+form.getDDH()+"</pojo:DDH>\n");
        stringBuilder.append("<pojo:KP_NSRSBH>"+form.getKP_NSRSBH()+"</pojo:KP_NSRSBH>\n");
        stringBuilder.append("<pojo:XHF_NSRSBH>"+form.getXHF_NSRSBH()+"</pojo:XHF_NSRSBH>\n");
        stringBuilder.append("</web:ElectroniceInfo>\n");
        stringBuilder.append("</web:queryEliData>\n");
        stringBuilder.append("</soapenv:Body>\n");
        stringBuilder.append("</soapenv:Envelope>\n");
        log.info("电子发票查询拼接后的参数"+stringBuilder.toString());
        return stringBuilder.toString();
    }


    /**
     * 电子发票开具发票
     * 根据拼接 xml 字符串
     * @param form
     * @return
     */
    public  String getElectroniceInfoXML(ElectroniceInfoF form) {
        log.info("电子发票开具开始拼接请求报文");
        //开始拼接请求报文
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:web=\"http://webservice.companyInterface.dzfp.fp.aisinogd.com\" " +
                "xmlns:pojo=\"http://pojo.hessian.companyInterface.dzfp.fp.aisinogd.com\">\n");
        stringBuilder.append("<soapenv:Header/>\n");
        stringBuilder.append("<soapenv:Body>\n");
        stringBuilder.append("<web:sendToInvEli>\n");
        stringBuilder.append("<web:ElectroniceInfo>\n");
        stringBuilder.append("<pojo:FPQQLSH>"+form.getFPQQLSH()+"</pojo:FPQQLSH>\n");
        stringBuilder.append("<pojo:DDH>"+form.getDDH()+"</pojo:DDH>\n");
        stringBuilder.append("<pojo:GHF_SJ>"+form.getGHF_SJ()+"</pojo:GHF_SJ>\n");
        stringBuilder.append("<pojo:KP_NSRSBH>"+form.getKP_NSRSBH()+"</pojo:KP_NSRSBH>\n");
        stringBuilder.append("<pojo:KP_NSRMC>"+form.getKP_NSRMC()+"</pojo:KP_NSRMC>\n");
        stringBuilder.append("<pojo:NSRDZDAH>"+form.getNSRDZDAH()+"</pojo:NSRDZDAH>\n");
        stringBuilder.append("<pojo:SWJG_DM>"+form.getSWJG_DM()+"</pojo:SWJG_DM>\n");
        stringBuilder.append("<pojo:DKBZ>"+form.getDKBZ()+"</pojo:DKBZ>\n");
        stringBuilder.append("<pojo:KPXM>"+form.getKPXM()+"</pojo:KPXM>\n");
        stringBuilder.append("<pojo:XHF_NSRSBH>"+form.getXHF_NSRSBH()+"</pojo:XHF_NSRSBH>\n");
        stringBuilder.append("<pojo:XHF_MC>"+form.getXHF_MC()+"</pojo:XHF_MC>\n");
        stringBuilder.append("<pojo:GHF_DZ>"+form.getGHF_DZ()+"</pojo:GHF_DZ>\n");
        stringBuilder.append("<pojo:XHF_DZ>"+form.getXHF_DZ()+"</pojo:XHF_DZ>\n");
        stringBuilder.append("<pojo:GHF_NSRSBH>"+form.getGHF_NSRSBH()+"</pojo:GHF_NSRSBH>\n");
        stringBuilder.append("<pojo:GHF_GDDH>"+form.getGHF_GDDH()+"</pojo:GHF_GDDH>\n");
        stringBuilder.append("<pojo:GHF_EMAIL>"+form.getGHF_EMAIL()+"</pojo:GHF_EMAIL>\n");
//        stringBuilder.append("<pojo:FPZL_DM></pojo:FPZL_DM>\n");// 购货方税号
        stringBuilder.append("<pojo:XHF_DH>"+form.getXHF_DH()+"</pojo:XHF_DH>\n");
        stringBuilder.append("<pojo:XHF_YHZH>"+form.getXHF_YHZH()+"</pojo:XHF_YHZH>\n");
        stringBuilder.append("<pojo:GHF_MC>"+form.getGHF_MC()+"</pojo:GHF_MC>\n");
        stringBuilder.append("<pojo:GHF_YHZH>"+form.getGHF_YHZH()+"</pojo:GHF_YHZH>\n");
        stringBuilder.append("<pojo:KPR>"+form.getKPR()+"</pojo:KPR>\n");
        stringBuilder.append("<pojo:SKR>"+form.getSKR()+"</pojo:SKR>\n");
        stringBuilder.append("<pojo:FHR>"+form.getFHR()+"</pojo:FHR>\n");
        stringBuilder.append("<pojo:KPLX>"+form.getKPLX()+"</pojo:KPLX>\n");
        stringBuilder.append("<pojo:YFP_DM>"+form.getYFP_DM()+"</pojo:YFP_DM>\n");
        stringBuilder.append("<pojo:YFP_HM>"+form.getYFP_HM()+"</pojo:YFP_HM>\n");
        stringBuilder.append("<pojo:CZDM>"+form.getCZDM()+"</pojo:CZDM>\n");
        stringBuilder.append("<pojo:CHYY>"+form.getCHYY()+"</pojo:CHYY>\n");
        stringBuilder.append("<pojo:KPHJJE>"+form.getKPHJJE()+"</pojo:KPHJJE>\n");
        stringBuilder.append("<pojo:HJBHSJE>"+form.getHJBHSJE()+"</pojo:HJBHSJE>\n");
        stringBuilder.append("<pojo:HJSE>"+form.getHJSE()+"</pojo:HJSE>\n");
        stringBuilder.append("<pojo:BMB_BBH>"+form.getBMB_BBH()+"</pojo:BMB_BBH>\n");
        stringBuilder.append("<pojo:BZ>"+form.getBZ()+"</pojo:BZ>\n");

        stringBuilder.append("<pojo:details>\n");
        List<ElectroniceDetailF> details = form.getDetails();
        details.forEach(record -> {
            stringBuilder.append("<pojo:ElectroniceDetail>\n");
            stringBuilder.append("<pojo:GGXH>"+record.getGGXH()+"</pojo:GGXH>\n");
            stringBuilder.append("<pojo:SL>"+record.getSL()+"</pojo:SL>\n");
            stringBuilder.append("<pojo:SE>"+record.getSE()+"</pojo:SE>\n");
            stringBuilder.append("<pojo:LSLBS>"+record.getLSLBS()+"</pojo:LSLBS>\n");
            stringBuilder.append("<pojo:DW>"+record.getDW()+"</pojo:DW>\n");
            stringBuilder.append("<pojo:SPBM>"+record.getSPBM()+"</pojo:SPBM>\n");
            stringBuilder.append("<pojo:KCE>"+record.getKCE()+"</pojo:KCE>\n");
            stringBuilder.append("<pojo:YHZCBS>"+record.getYHZCBS()+"</pojo:YHZCBS>\n");
            stringBuilder.append("<pojo:XMMC>"+record.getXMMC()+"</pojo:XMMC>\n");
            stringBuilder.append("<pojo:XMDJ>"+record.getXMDJ()+"</pojo:XMDJ>\n");
            stringBuilder.append("<pojo:ZZSTSGL>"+record.getZZSTSGL()+"</pojo:ZZSTSGL>\n");
            stringBuilder.append("<pojo:HSBZ>"+record.getHSBZ()+"</pojo:HSBZ>\n");
            stringBuilder.append("<pojo:XMJE>"+record.getXMJE()+"</pojo:XMJE>\n");
            stringBuilder.append("<pojo:XMBM>"+record.getXMBM()+"</pojo:XMBM>\n");
            stringBuilder.append("<pojo:ZXBM>"+record.getZXBM()+"</pojo:ZXBM>\n");
            stringBuilder.append("<pojo:XMSL>"+record.getXMSL()+"</pojo:XMSL>\n");
            stringBuilder.append("</pojo:ElectroniceDetail>\n");
        });
        stringBuilder.append("</pojo:details>\n");

        stringBuilder.append("</web:ElectroniceInfo>\n");
        stringBuilder.append("</web:sendToInvEli>\n");
        stringBuilder.append("</soapenv:Body>\n");
        stringBuilder.append("</soapenv:Envelope>\n");
        log.info("电子发票开具拼接后的参数"+stringBuilder);
        return stringBuilder.toString();
    }

    /**
     * HTTPClient 调用 WebService
     * @param soap
     * @return
     */
    public String doPostSoap(String soap,String url) {
        //请求体
        String retStr = "";
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
            httpPost.setHeader("SOAPAction", "");
            StringEntity data = new StringEntity(soap,
                    StandardCharsets.UTF_8);
            httpPost.setEntity(data);
            CloseableHttpResponse response = closeableHttpClient
                    .execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                // 打印响应内容
                retStr = EntityUtils.toString(httpEntity, "UTF-8");
            }
            log.info("方圆返回参数：{}",retStr);
            // 释放资源
            closeableHttpClient.close();
        } catch (Exception e) {
            log.info("方圆开票接口失败：", e);
        }
        return retStr;
    }

}
