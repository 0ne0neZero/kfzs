package com.wishare.finance.domains.voucher.strategy.assisteitem;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.strategy.MerchantAssisteItemStrategy;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBillCustomerTypeEnum;
import com.wishare.finance.domains.voucher.entity.yuanyang.YyNccCustomerRelE;
import com.wishare.finance.domains.voucher.facade.VoucherFacade;
import com.wishare.finance.domains.voucher.repository.yuanyang.YyNccCustomerRelRepository;
import com.wishare.finance.domains.voucher.repository.yuanyang.mapper.YyNccCustomerRelMapper;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.domains.voucher.strategy.core.VoucherData;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.clients.base.UserClient;
import com.wishare.finance.infrastructure.remote.fo.external.mdmmb.MdmMbQueryRF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddCustomerF;
import com.wishare.finance.infrastructure.remote.vo.external.mdmmb.MdmMbCommunityRV;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.Result;
import com.wishare.finance.infrastructure.remote.vo.space.ArchivesEnterpriseBaseV;
import com.wishare.finance.infrastructure.remote.vo.space.ArchivesEnterpriseDetailV;
import com.wishare.finance.infrastructure.remote.vo.user.EnterpriseBaseInfoRV;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 客商辅助核算策略
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/11
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MerchantVoucherAssisteItemStrategy implements VoucherAssisteItemStrategy {

    private final VoucherFacade voucherFacade;
    private final ExternalClient externalClient;
    private final SpaceClient spaceClient;
    private final OrgClient orgClient;
    private final YyNccCustomerRelMapper yyNccCustomerRelMapper;
    private final YyNccCustomerRelRepository yyNccCustomerRelRepository;
    private final MerchantAssisteItemStrategy merchantAssisteItemStrategy;
    private final UserClient userClient;


    private static final String LOG_PRE = "开发商获取客商信息";
    @Override
    public AssisteItemTypeEnum type() {
        return AssisteItemTypeEnum.客商;
    }

    @Override
    public AssisteItemOBV getByBus(VoucherBusinessBill businessBill) {

        // 收费对象 开发商 11282667941001 -> 业主1156760069630001
        if (Objects.equals(VoucherBillCustomerTypeEnum.开发商.getCode(),businessBill.getOriginalPayerType()) &&
                Objects.equals(VoucherBillCustomerTypeEnum.业主.getCode(),businessBill.getPayerType())){
            // 是 业主,取默认业主
            if (businessBill.getOriginalAmount() >=0){
                String customerId = String.valueOf(VoucherData.defaultMerchantId);
                return voucherFacade.getAssisteItem(customerId, type());
            }else {
                // 是开发商
                return developers(businessBill.getOriginalPayerId(),businessBill);

            }
        }

        return noChargeObjectGetItem(businessBill);
    }

    private AssisteItemOBV noChargeObjectGetItem(VoucherBusinessBill businessBill) {
        String customerId;
        if (Objects.isNull(businessBill.getCustomerType()) || VoucherBillCustomerTypeEnum.业主.equalsByCode(businessBill.getCustomerType())){
            customerId = String.valueOf(VoucherData.defaultMerchantId);
        }else if (VoucherBillCustomerTypeEnum.临时客商.equalsByCode(businessBill.getCustomerType())){
            customerId = String.valueOf(VoucherData.defaultTempCustomerId);
        }else if (VoucherBillCustomerTypeEnum.开发商.equalsByCode(businessBill.getCustomerType())){
            return developers(businessBill.getCustomerId(), businessBill);
        }
        else {
            customerId = String.valueOf(VoucherData.defaultMerchantId);
        }
        return voucherFacade.getAssisteItem(customerId, type());
    }


    private EnterpriseBaseInfoRV getDevelopers(String customerId){
        try {
            EnterpriseBaseInfoRV enterpriseInfo = userClient.getEnterpriseInfo(Long.parseLong(customerId), null);
            log.info(LOG_PRE+"userClient.getEnterpriseInfo:{},出参{}", customerId,JSONObject.toJSONString(enterpriseInfo));
            if (ObjectUtil.isNotNull(enterpriseInfo)){
                String creditCode = enterpriseInfo.getCreditCode();
                if (StrUtil.isNotBlank(creditCode)){
                    return enterpriseInfo;
                }
            }
            // 补偿调用
            //  先去  select * from wishare_space.archives_enterprise_base where id =入参cus_id
            ArchivesEnterpriseDetailV detail = spaceClient.getEnterpriseDetail(Long.valueOf(customerId));
            log.info(LOG_PRE+"spaceClient.getEnterpriseDetail:{},出参{}", customerId,JSONObject.toJSONString(detail));
            if (ObjectUtil.isNull(detail) || ObjectUtil.isNull(detail.getArchivesEnterpriseBaseV())){
                return null;
            }

            String creditCode = detail.getArchivesEnterpriseBaseV().getCreditCode();
            String name = detail.getArchivesEnterpriseBaseV().getName();
            EnterpriseBaseInfoRV rv = new EnterpriseBaseInfoRV();
            rv.setCreditCode(creditCode);
            rv.setName(name);
            return rv;
        } catch (Exception e) {
            log.error(LOG_PRE+"获取开发商信息出现异常,异常信息:{}",e.getMessage());
        }
        return null;
    }
    /** 获取开发商信息
     * @param customerId 开发商id
     * @return
     */
    private AssisteItemOBV developers(String customerId, VoucherBusinessBill businessBill) {
        if (StrUtil.isBlank(customerId)){
            log.info(LOG_PRE+"开发商开始查询-客户id为空{}", JSONObject.toJSONString(businessBill));
            return merchantAssisteItemStrategy.toAssisteItem(type(), null, "");
        }
        EnterpriseBaseInfoRV infoRV = getDevelopers(customerId);

        if (ObjectUtil.isNotNull(infoRV)){
            String creditCode = infoRV.getCreditCode();
            String name = infoRV.getName();

            // select customer_name,ncc_customer_code from wishare_finance.yy_ncc_customer_rel where credit_code =
            YyNccCustomerRelE nccCus = yyNccCustomerRelMapper.selectOne(Wrappers.<YyNccCustomerRelE>lambdaQuery()
                    .eq(YyNccCustomerRelE::getCreditCode, creditCode)
                    .isNotNull(YyNccCustomerRelE::getNccCustomerCode).last("limit 1"));


            if (ObjectUtil.isNull(nccCus) || StrUtil.isBlank(nccCus.getNccCustomerCode())){
                // 新增 NccCustomer
                String nccCode =generateCodeAndDatabaseCheck();
                addConstomer(nccCode, name, creditCode);
                log.info(LOG_PRE+"存在统一信用代码不存在NCC-入参:{}。新增NCC客户",creditCode);
                return merchantAssisteItemStrategy.toAssisteItem(type(), nccCode, name);
            }else {
                return merchantAssisteItemStrategy.toAssisteItem(type(), nccCus.getNccCustomerCode(), nccCus.getCustomerName());
            }
        }
        else {
//                select businessId from wishare_external.mdm_mb_user_resident where mdmId = 入参cus_id
            MdmMbQueryRF queryRF = new MdmMbQueryRF();
            queryRF.setTableName("mdm_mb_user_resident");
            queryRF.setRemoteSystemId(12004355279901L);
            queryRF.setMdmId(customerId);
            log.info(LOG_PRE+"不存在统一信用代码，查询sn入参:{}", JSONObject.toJSONString(queryRF));
            List<MdmMbCommunityRV> list = externalClient.queryCodeInfo(queryRF);
            log.info(LOG_PRE+"不存在统一信用代码，查询sn入参:{},出参:{}", JSONObject.toJSONString(queryRF),JSONObject.toJSONString(list));

            Set<String> snSet = list.stream().map(MdmMbCommunityRV::getBusinessId).filter(StrUtil::isNotBlank).collect(Collectors.toSet());

            //select creditCode from wishare_org.merchant where sn = businessId
            Set<String > creditCodeSet = orgClient.getCreditCodeBySn(snSet);
            log.info(LOG_PRE+"不存在统一信用代码，查询creditCodeSet入参:{},出参:{}",snSet,creditCodeSet);

            if (CollUtil.isEmpty(creditCodeSet)){
                return merchantAssisteItemStrategy.toAssisteItem(type(), null, "");
            }
            // select customer_name,ncc_customer_code from wishare_finance.yy_ncc_customer_rel where credit_code =
            List<YyNccCustomerRelE> nncCusList = yyNccCustomerRelMapper.selectList(Wrappers.<YyNccCustomerRelE>lambdaQuery()
                    .in(YyNccCustomerRelE::getCreditCode, creditCodeSet).isNotNull(YyNccCustomerRelE::getNccCustomerCode));
            log.info(LOG_PRE+"不存在统一信用代码，根据信用代码查询ncc。入参:{},出参:{}",creditCodeSet,JSONObject.toJSONString(nncCusList));

            if (CollUtil.isNotEmpty(nncCusList) ){
                YyNccCustomerRelE a = nncCusList.get(0);
                return merchantAssisteItemStrategy.toAssisteItem(type(), a.getNccCustomerCode(), a.getCustomerName());

            }else {
                // 最终还是没找到
                return merchantAssisteItemStrategy.toAssisteItem(type(), null, "");

            }

        }
    }


    public static void main(String[] args) {
        System.out.println(RandomUtil.randomNumbers(9)+"A");
    }
    private String generateCodeAndDatabaseCheck(){

        String code = RandomUtil.randomNumbers(9)+"A";
        while (true){
            Long l = yyNccCustomerRelMapper.selectCount(Wrappers.<YyNccCustomerRelE>lambdaQuery().eq(YyNccCustomerRelE::getNccCustomerCode, code));
            if (l==0){
                return code;
            }else {
                code = RandomUtil.randomNumbers(9)+"A";
            }
        }

    }

    private void addConstomer(String nccCode,String nccCustomerName,String creditCode) {
        AddCustomerF addCustomerF = new AddCustomerF();
        addCustomerF.setName(nccCustomerName);
        addCustomerF.setCode(nccCode);
        addCustomerF.setPkCustClass("C");
        addCustomerF.setTaxPayerId(creditCode);
        log.info("不存在映射编码，开始请求ncc新增客户:{}", JSON.toJSONString(addCustomerF));
        Result result = externalClient.addCustomer(addCustomerF);
        // 返回示例：{"nccCode":"001","msg":"客户更新成功，客户ID为：1001A21000000000LJMP"}
        log.info("新增NCC客户返回结果:{}", JSON.toJSONString(result));
        if (Objects.nonNull(result) && "001".equals(result.getCode())) {

            YyNccCustomerRelE yyNccCustomerRelE = new YyNccCustomerRelE();
            yyNccCustomerRelE.setCustomerName(nccCustomerName);
            yyNccCustomerRelE.setNccCustomerCode(nccCode);
            yyNccCustomerRelE.setCreditCode(creditCode);
            yyNccCustomerRelMapper.insert(yyNccCustomerRelE);

        }
    }

}
