package com.wishare.finance.apps.scheduler.ncc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.configure.cashflow.entity.CashFlowE;
import com.wishare.finance.domains.configure.cashflow.service.CashFlowDomainService;
import com.wishare.finance.domains.configure.organization.entity.StatutoryBodyAccountE;
import com.wishare.finance.domains.configure.organization.service.StatutoryBodyAccountDomainService;
import com.wishare.finance.domains.configure.subject.dto.SubjectD;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.configure.subject.service.SubjectDomainService;
import com.wishare.finance.domains.configure.taxrate.entity.NccTaxRate;
import com.wishare.finance.domains.configure.taxrate.service.NccTaxRateDomainService;
import com.wishare.finance.domains.ncc.repository.NcTaskRepository;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddBankAccF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddBankDocF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddBankTypeF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddCustomerF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddProjectF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddSupplierF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.BankAccSubF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.ListAccount;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.AccountRv;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.CashFlowRv;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.RateRv;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.Result;
import com.wishare.starter.Global;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import com.xxl.job.core.handler.annotation.XxlJob;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @description: 定时向nc传输信息
 * @author: pgq
 * @since: 2023/1/31 11:36
 * @version: 1.0.0
 */
@Component
@Slf4j
public class NccInfosAddTask {

    @Autowired
    private ExternalClient externalClient;
    @Autowired
    private StatutoryBodyAccountDomainService statutoryBodyAccountDomainService;
    @Autowired
    private CashFlowDomainService cashFlowDomainService;
    @Autowired
    private NccTaxRateDomainService nccTaxRateDomainService;
    @Autowired
    private SubjectDomainService subjectDomainService;
    @Autowired
    private NcTaskRepository ncTaskRepository;
    @Value("${tenantId:108314314140208}")
    private String tenantId;

    @XxlJob("addBankAccount") // cron = "8 8 1 * * ?")
    public void addBankAccount() {


        log.info("---------------------- start ----------------------");
        AddBankTypeF bankType = new AddBankTypeF();
        bankType.setCode("4444");
        bankType.setName("慧享银行杭州西溪支行");
        bankType.setMnecode("HXB");
        Result res = externalClient.addBankType(bankType);
        log.info("param : {}, res : {}", JSON.toJSONString(bankType), JSON.toJSONString(res));
        log.info("---------------------- middle ----------------------");
        AddBankDocF bankDoc = new AddBankDocF();
        bankDoc.setCode("4444");
        bankDoc.setBankTypeCode("444");
        bankDoc.setName("慧享银行杭州西溪支行");
        bankDoc.setShortname("慧享西溪支行");
        bankDoc.setPkCountry("中国");
        Result docRes = externalClient.addBankDoc(bankDoc);
        log.info("param : {}, res : {}", JSON.toJSONString(bankDoc), JSON.toJSONString(res));
        log.info("---------------------- end ----------------------");
        PageF<StatutoryBodyAccountE> page = new PageF<>();
        page.setPageNum(1);
        page.setPageSize(1000);
        PageV<StatutoryBodyAccountE> result;
        do {
            result = statutoryBodyAccountDomainService.queryPageWithoutTenantId(page);

            if (!CollectionUtils.isEmpty(result.getRecords())) {
                List<StatutoryBodyAccountE> records = result.getRecords();
                records.stream()
                    .filter(r -> StringUtils.isNotBlank(r.getBankAccount()) && r.getBankAccount().length() < 40)
                    .forEach(r -> {
                    AddBankAccF bankAcc = new AddBankAccF();
                    bankAcc.setAccName(r.getName());
                    bankAcc.setAccNum(r.getBankAccount());
                    bankAcc.setAccOpenDate(r.getGmtCreate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    bankAcc.setAccountProperty(0);
                    bankAcc.setAccState(0);
                    bankAcc.setPkBankType("444");
                    bankAcc.setPkBankDoc("4444");
                    bankAcc.setCode(r.getBankAccount());
                    bankAcc.setFinanceOrg("010010010A");
                    bankAcc.setControlOrg("010010010A");
                    bankAcc.setArapProp(map2Prop(r.getRecPayType()));
                    bankAcc.setAccAttribute(map2Attribute(r.getType()));
                    bankAcc.setNetQueryFlag(0);
                    bankAcc.setBankaccsub(new ArrayList<>());

                    BankAccSubF bankAccSub = new BankAccSubF();
                    bankAcc.getBankaccsub().add(bankAccSub);

                    bankAccSub.setAccName(r.getName());
                    bankAccSub.setAccNum(r.getBankAccount());
                    bankAccSub.setCode(r.getBankAccount());
                    bankAccSub.setPkCurrtype("CNY");
                    bankAccSub.setAccType(0);
                    bankAccSub.setOverDraftmny(0.0);
                    bankAccSub.setOverDraftType("2");
                    bankAccSub.setPayArea("0");

                    Result accRes = externalClient.addBankAcc(bankAcc);
                    log.info("----------------------- param : {} ,  res : {}", JSON.toJSONString(bankAcc), JSON.toJSONString(res));
                });
            } else {
                result.setLast(true);
            }

            page.setPageNum(page.getPageNum() + 1);
        } while (!result.isLast());
//        yonyouNcAppService.addBankType();
//        yonyouNcAppService.addBankDoc();
//        yonyouNcAppService.addBankAcc();
    }

    /**
     * 隐射账户类型
     * @param type
     * @return
     */
    private Integer map2Attribute(Integer type) {
        switch (type) {
            case 1: return 0;
            case 2: return 2;
            case 3: return 3;
            default: return 0;
        }
    }

    /**
     * 映射收支类型
     * @param recPayType
     * @return
     */
    private Integer map2Prop(Integer recPayType) {
        switch (recPayType) {
            case 1: return 2;
            case 2: return 0;
            case 3: return 1;
            default: return 2;
        }
    }


//    @Scheduled(cron = "8 38 2 * * ?")
    @XxlJob("addSupplierAndCustomer")
    public void addSupplierAndCustomer() {
        Page<JSONObject> page = new Page<>(1, 1000);
        do {
            page = ncTaskRepository.pageSupplierAndCustomer(page);
            log.info(" maps ---------------- : {}", JSON.toJSONString(page));
            if (!CollectionUtils.isEmpty(page.getRecords())) {
                page.getRecords().forEach(jsonObject -> {
                    if (1 == jsonObject.getInteger("type")) {
                        addConstomer(jsonObject);
                    } else if (2 == jsonObject.getInteger("type")) {
                        addSupplier(jsonObject);
                    } else {
                        addConstomer(jsonObject);
                        addSupplier(jsonObject);
                    }
                });
            }
        } while (page.hasNext());
    }

    private void addConstomer(JSONObject jsonObject) {
        AddCustomerF customer = new AddCustomerF();
        customer.setCode(jsonObject.getString("code"));
        customer.setCustProp(0);
        customer.setPkCustClass("B");
        customer.setName(jsonObject.getString("name"));
        customer.setTaxPayerId(jsonObject.getString("creditCode"));
        Result result = externalClient.addCustomer(customer);
        log.info("新增客户 ------------ param : {},  result : {}", JSON.toJSONString(customer), JSON.toJSONString(result));
    }

    private void addSupplier(JSONObject jsonObject) {
        AddSupplierF supplier = new AddSupplierF();
        supplier.setCode(jsonObject.getString("code"));
        supplier.setSupProp(0);
        supplier.setPkSupplierClass("B");
        supplier.setName(jsonObject.getString("name"));
        supplier.setTaxPayerId(jsonObject.getString("creditCode"));
        Result result = externalClient.addSupplier(supplier);
        log.info("新增供应商 ------------ param : {},  result : {}", JSON.toJSONString(supplier), JSON.toJSONString(result));
    }


//    @Scheduled(cron = "8 38 1 * * ?")
    @XxlJob("addOrgFinance")
    public void addOrgFinance() {
        Page<JSONObject> page = new Page<>(1, 1000);
        do {
            page = ncTaskRepository.pageOrgFinance(page);
            log.info(" maps ---------------- : {}", JSON.toJSONString(page));
            if (!CollectionUtils.isEmpty(page.getRecords())) {
                page.getRecords().forEach(jsonObject -> {
                    AddProjectF project = new AddProjectF();
                    project.setProjectCode(jsonObject.getString("id"));
                    project.setProjectName(jsonObject.getString("nameCn"));
                    project.setPkProjectClass("HZ");
                    project.setDef1("99");
                    project.setDef2("99");
                    project.setDef3("99");
                    project.setDef4("99");
                    project.setDef5("99");
                    project.setDef6("99");
                    project.setDef7("99");
                    project.setPkEps("05");
                    project.setPlanStartDate(timeTranf(jsonObject.getString("gmtCreate")));
                    project.setPlanFinishDate(timeTranf(jsonObject.getString("gmtCreate")));
                    Result result = externalClient.addProject(project);
                    log.info("新增项目（成本中心） ------------ param : {},  result : {}", JSON.toJSONString(project), JSON.toJSONString(result));
                });
            }
        } while (page.hasNext());
    }

//    @Scheduled(cron = "8 18 11 * * ?")
//    @XxlJob("listCashFlow")
    public void listCashFlow() {
        addTenant();
        //
        //List<CashFlowRv> list = externalClient.listCashFlow();
        //List<CashFlowE> cashFlows = new ArrayList<>();
        //for (CashFlowRv cashFlowRv : list) {
        //    CashFlowE cashFlow = Global.mapperFacade.map(cashFlowRv, CashFlowE.class);
        //    cashFlow.setFCode(cashFlowRv.getFcode());
        //    cashFlow.setFName(cashFlowRv.getFname());
        //    cashFlow.setAllName(cashFlowRv.getFname());
        //    cashFlow.setItemType(cashFlowRv.getItemtype());
        //    cashFlows.add(cashFlow);
        //}
        //cashFlowDomainService.batchInsert(cashFlows);
        //
        //Map<String, CashFlowE> cashFlowEMap = cashFlows.stream()
        //    .collect(Collectors.toMap(CashFlowE::getCode, Function.identity()));
        //
        //cashFlows.forEach(item -> {
        //    while (StringUtils.isNotBlank(item.getFCode())) {
        //        item.setAllName(cashFlowEMap.get(item.getFCode()).getAllName() + item.getAllName());
        //    }
        //});
        //cashFlowDomainService.batchInsert(cashFlows);
        //
    }

//    @Scheduled(cron = "8 28 11 * * ?")
    @XxlJob("listRate")
    public void listRate() {
        addTenant();

        List<RateRv> list = externalClient.listRate();
        List<NccTaxRate> rates = new ArrayList<>();
        for (RateRv rateRv : list) {
            NccTaxRate rate = Global.mapperFacade.map(rateRv, NccTaxRate.class);
            rates.add(rate);
        }
        nccTaxRateDomainService.batchInsert(rates);

    }

    @XxlJob("batchUpdateSubAcc")
    public void batchUpdateSubAcc() {
        addTenant();

//        List<AccountRv> list = externalClient.listAccount();
//        List<AccountRv> accounts = new ArrayList<>();
//        Map<String, List<AccountRv>> map = accounts.parallelStream()
//            .collect(Collectors.groupingBy(AccountRv::getAcccode));
//        List<SubjectE> subjectES = subjectDomainService.listByCodes(
//            new ArrayList<>(map.keySet()));
//        subjectES.forEach(item -> {
//            List<String> collect = map.get(item.getSubjectCode()).stream().map(AccountRv::getFzcode)
//                .collect(Collectors.toList());
//            item.setAuxiliaryCount(JSON.toJSONString(collect));
//        });
        int current = 1;
        boolean isLast = false;
        do {
            SearchF<SubjectE> searchF = new SearchF<>();
            searchF.setFields(new ArrayList<>());
            PageF<SearchF<SubjectE>> searchFPageF = new PageF<>();
            searchFPageF.setConditions(searchF);
            searchFPageF.setPageNum(current);
            searchFPageF.setPageSize(100);
            Page<SubjectD> page = subjectDomainService.pageForNcTask(searchFPageF);
            if (!CollectionUtils.isEmpty(page.getRecords())){
                List<String> codeList = page.getRecords().stream().map(SubjectD::getSubjectCode)
                    .collect(Collectors.toList());
                List<AccountRv> accountRvs = externalClient.listAccountByCodes(new ListAccount(codeList));
                Map<String, List<AccountRv>> collect = accountRvs.parallelStream()
                    .collect(Collectors.groupingBy(AccountRv::getAcccode));
                List<SubjectE> subjectES = Global.mapperFacade.mapAsList(page.getRecords(), SubjectE.class);
                subjectES.forEach(item -> {
                    if (collect.containsKey(item.getSubjectCode())) {
                        List<String> list = collect.get(item.getSubjectCode()).stream()
                            .map(AccountRv::getFzcode).collect(Collectors.toList());
                        item.setAuxiliaryCount(JSON.toJSONString(list));
                    }
                });

                subjectDomainService.batchUpdate(subjectES);
            }
            isLast = !page.hasNext();
            current++;
        } while (!isLast);
    }



    private void addTenant() {

        // 加入租户隔离
        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(tenantId);
        ThreadLocalUtil.set("IdentityInfo", identityInfo);
    }


    private String timeTranf(String time) {
        return time.replaceAll("T", " ");

//        return time.substring(0, time.indexOf("T"));
    }
}
