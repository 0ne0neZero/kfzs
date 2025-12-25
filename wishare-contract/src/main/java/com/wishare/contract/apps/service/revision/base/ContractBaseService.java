package com.wishare.contract.apps.service.revision.base;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.contract.apps.fo.revision.ContractNoGenerateF;
import com.wishare.contract.apps.fo.revision.income.*;
import com.wishare.contract.apps.remote.clients.ConfigFeignClient;
import com.wishare.contract.apps.remote.clients.OrgFeignClient;
import com.wishare.contract.apps.remote.clients.SpaceFeignClient;
import com.wishare.contract.apps.remote.vo.OrgInfoRv;
import com.wishare.contract.apps.remote.vo.SpaceCommunityRv;
import com.wishare.contract.apps.remote.vo.config.AreaInfoListV;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.domains.entity.contractset.ContractCategoryE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeExpandE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeExpandE;
import com.wishare.contract.domains.entity.revision.pay.fund.ContractPayFundE;
import com.wishare.contract.domains.enums.revision.*;
import com.wishare.contract.domains.mapper.contractset.ContractCategoryMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeExpandMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeExpandMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeService;
import com.wishare.contract.domains.vo.revision.ContractNoInfoV;
import com.wishare.contract.domains.vo.revision.ContractPaymentVO;
import com.wishare.owl.exception.OwlBizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ContractBaseService {

//    @Autowired
//    private StringRedisTemplate redisTemplate;

    @Autowired
    private ContractIncomeConcludeService contractIncomeConcludeService;
    @Autowired
    private ContractIncomeConcludeMapper contractIncomeConcludeMapper;
    @Autowired
    private ContractIncomeConcludeExpandMapper contractIncomeConcludeExpandMapper;

    @Autowired
    private ContractPayConcludeService contractPayConcludeService;
    @Autowired
    private ContractPayConcludeMapper contractPayConcludeMapper;
    @Autowired
    private ContractPayConcludeExpandMapper contractPayConcludeExpandMapper;

    @Autowired
    private SpaceFeignClient spaceFeignClient;
    @Autowired
    private OrgFeignClient orgFeignClient;
    @Autowired
    private ConfigFeignClient configFeignClient;


    private static final String INCOME_SUPPLY_PREFIX = "INCOME-SUPPLY-";
    private static final String INCOME_GENERATE_CONTRACT_NO_PREFIX = "INCOME-GROUP-";
    private static final String PAY_SUPPLY_PREFIX = "PAY-SUPPLY-";
    private static final String PAY_GENERATE_CONTRACT_NO_PREFIX = "PAY-GROUP-";
    private static Pattern contractNoPattern = Pattern.compile("(?<=-)(?!(20\\d{2}))(\\d{3,4})(?=-|$)");
    private static Pattern contractNoPatternSupply = Pattern.compile("(?<=补)\\d+");


    /**
     * 收入合同-处理合同编号
     * 只有发起流程生成编码
     * 修改合同时组织或部门或项目无变化的时候保留原后缀
     **/
    public ContractNoInfoV generateIncomeContractNo(ContractIncomeConcludeE contractIncomeConcludeE, ContractIncomeConcludeE oldConcludeE){
        log.info("收入合同生成合同编号开始：{}",JSONArray.toJSON(contractIncomeConcludeE));
        // 若合同是修改合同,则不需要进行后续逻辑处理 合同编号直接返回去
        if (Objects.nonNull(contractIncomeConcludeE) &&
                Objects.nonNull(contractIncomeConcludeE.getContractType()) &&
                contractIncomeConcludeE.getContractType().equals(ContractTypeEnum.修改合同.getCode())){
            return ContractNoInfoV.builder().code(1).contractNo(contractIncomeConcludeE.getContractNo()).build();
        }

        // 若合同是补充合同
        if (Objects.nonNull(contractIncomeConcludeE) &&
                contractIncomeConcludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())){
            // 若之前的合同也是补充合同
            if (Objects.nonNull(oldConcludeE) &&
                    oldConcludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())){
                // 若父id保持一致，则不变
                if (StringUtils.equals(oldConcludeE.getPid(), contractIncomeConcludeE.getPid())){
                    return ContractNoInfoV.builder().code(1).contractNo(oldConcludeE.getContractNo()).build();
                } else {
                    return handleContractNoOnSupplyIncomeContract(contractIncomeConcludeE);
                }
            } else {
                // 如果发起流程的时候,补充协议已有编号,那么就是通过编辑生成过了,不处理了
                if (Objects.isNull(oldConcludeE) &&
                        StringUtils.isNotBlank(contractIncomeConcludeE.getContractNo())){
                    return ContractNoInfoV.builder().code(1).contractNo(contractIncomeConcludeE.getContractNo()).build();
                }
                // 若之前的合同不是补充合同现在是 或者 没有之前的合同[发起流程]
                return handleContractNoOnSupplyIncomeContract(contractIncomeConcludeE);
            }
        }

        if (null == oldConcludeE && StringUtils.isNotBlank(contractIncomeConcludeE.getContractNo())) {
            return ContractNoInfoV.builder().code(1).contractNo(contractIncomeConcludeE.getContractNo()).build();
        }
        QueryWrapper<ContractIncomeConcludeExpandE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ContractIncomeConcludeExpandE.CONTRACTID, contractIncomeConcludeE.getId());
        ContractIncomeConcludeExpandE concludeExpandE = contractIncomeConcludeExpandMapper.selectOne(queryWrapper);
        if (Objects.isNull(concludeExpandE)){
            return ContractNoInfoV.builder().code(0).failReason("合同数据异常,请检查").build();
        }
        String curContractNo = contractIncomeConcludeE.getContractNo();
        // 合同所属区域  0总部 2华北 3华南 4华东 5华西 6华中?
        Integer isBackDate = contractIncomeConcludeE.getIsBackDate();
        if (Objects.isNull(isBackDate)){
            return ContractNoInfoV.builder().code(0).failReason("请维护所属区域").build();
        }
        if (isBackDate.equals(ContractAreaEnum.区域公司.getCode()) || isBackDate.equals(ContractAreaEnum.商写事业部.getCode())){
            return ContractNoInfoV.builder().code(0).failReason("所选区域无法生成合同编号").build();
        }
        String regionAbbrCode = ContractAreaEnum.parseAbbrCode(isBackDate);

        // 项目id及项目简称编码
        SpaceCommunityRv communityRv = spaceFeignClient.getById(contractIncomeConcludeE.getCommunityId());
        int scope = judgeScope(regionAbbrCode, communityRv.getName());

        // 支付类型简称
        String incomeTypeAbbr = contractIncomeConcludeE.getNoPaySign() == 1 ? "WZF" : "SHR";

        // 编写合同年份
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        Date now = new Date();
        Date startTime = getYearFirst(year);
        Date endTime = getEndTimeOfCurrentYear(now);

        String serialStr = "";
        String supplySerialStr = "";
        StringBuilder sb = new StringBuilder();
        // 固定 单位简称
        sb.append("ZJFW");
        /**
         * 1 集团: 选择合同所属区域为[总部]，没有简称编码
         *     单位简称-收支类型简称-部门简称-合同类别简称-编写合同年份-合同序号
         **/
        if (scope == 1){
            String departId = contractIncomeConcludeE.getDepartId();
            OrgInfoRv orgInfoRv = orgFeignClient.getByOrgId(Long.parseLong(departId));
            sb.append("-").append(incomeTypeAbbr);
            if (StringUtils.isBlank(orgInfoRv.getAbbrCode())){
                return ContractNoInfoV.builder().code(0).failReason("请维护部门简称").build();
            }
            sb.append("-").append(orgInfoRv.getAbbrCode());
            if (StringUtils.isBlank(concludeExpandE.getConmanagetype())){
                return ContractNoInfoV.builder().code(0).failReason("合同类别不存在,请维护").build();
            }
            String contractManageTypeAbbrCode = null;
            if(ContractBusinessLineEnum.物管.getCode().equals(contractIncomeConcludeE.getContractBusinessLine())){
                log.info("物管合同查询合同管理类别：{}",concludeExpandE.getConmanagetype());
                contractManageTypeAbbrCode = ContractIncomeManageTypeEnum.parseAbbrCode(Integer.parseInt(concludeExpandE.getConmanagetype()));
            }else if (ContractBusinessLineEnum.建管.getCode().equals(contractIncomeConcludeE.getContractBusinessLine())){
                contractManageTypeAbbrCode = "JG";
                log.info("建管合同查询合同管理类别指定JG");
            }else{
                log.info("物管合同查询合同管理类别最后判断：{}",concludeExpandE.getConmanagetype());
                contractManageTypeAbbrCode = ContractIncomeManageTypeEnum.parseAbbrCode(Integer.parseInt(concludeExpandE.getConmanagetype()));
            }
            if (StringUtils.isBlank(contractManageTypeAbbrCode)){
                return ContractNoInfoV.builder().code(0).failReason("合同类别数据异常,请维护").build();
            }
            sb.append("-").append(contractManageTypeAbbrCode);
            sb.append("-").append(year);
            String redisKey = INCOME_GENERATE_CONTRACT_NO_PREFIX + year + departId;
            if (null != oldConcludeE && contractIncomeConcludeE.getDepartId().equals(oldConcludeE.getDepartId())) {
                serialStr = matcherSerial(curContractNo);
            }
            if (StringUtils.isBlank(serialStr)) {
                // 总部的序号按照 年份+部门 递增
                // 非补充协议合同编号
                List<String> contractNos = contractIncomeConcludeMapper.queryContractNosByRangeTimeAndCertainPropertyNotSupply(
                        departId, null, null, startTime, endTime, year);
                contractNos.remove(curContractNo);
                int maxSerialNumber = getMaxSerialNumberByDb(contractNos, "-");
                serialStr = calculateCertainSerialNumber(redisKey, maxSerialNumber, 3);
                /*String contractNum = contractIncomeConcludeMapper.getContractNoNum(sb.toString());
                if(StringUtils.isNotEmpty(contractNum)){
                    serialStr = String.format("%03d", Integer.parseInt(contractNum)+1);
                }else{
                    serialStr = "001";
                }*/
            }
            sb.append("-").append(serialStr);
            // 补充协议合同编号
            if (!contractIncomeConcludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())){
                return ContractNoInfoV.builder().code(1).contractNo(sb.toString()).redisKey(redisKey).build();
            }
            String supplyRedisKey = INCOME_SUPPLY_PREFIX + year + departId;
            if (null != oldConcludeE && contractIncomeConcludeE.getDepartId().equals(oldConcludeE.getDepartId())) {
                supplySerialStr = matcherSupplySerial(curContractNo);
            }
            if (StringUtils.isBlank(supplySerialStr)) {
                List<String> supplyContractNos = contractIncomeConcludeMapper.queryContractNosByRangeTimeAndCertainPropertySupply(
                        departId, null, null, startTime,endTime, year);
                supplyContractNos.remove(curContractNo);
                int maxSupplyNumber = getMaxSerialNumberByDb(supplyContractNos,"补");
                supplySerialStr = calculateCertainSerialNumber(supplyRedisKey, maxSupplyNumber, 2);
            }
            sb.append("-补").append(supplySerialStr);
            return ContractNoInfoV.builder().code(1).contractNo(sb.toString()).redisKey(redisKey).supplyRedisKey(supplyRedisKey).build();
        }
        /**
         * 区域
         * 单位简称-区域公司简称-收支类型简称-合同类别简称-编写合同年份-合同序号
         **/
        else if (scope == 2){
            sb.append("-").append(regionAbbrCode);
            sb.append("-").append(incomeTypeAbbr);

            if (StringUtils.isBlank(concludeExpandE.getConmanagetype())){
                return ContractNoInfoV.builder().code(0).failReason("合同类别不存在,请维护").build();
            }
            String contractManageTypeAbbrCode = null;
            if(ContractBusinessLineEnum.物管.getCode().equals(contractIncomeConcludeE.getContractBusinessLine())){
                log.info("物管合同查询合同管理类别：{}",concludeExpandE.getConmanagetype());
                contractManageTypeAbbrCode = ContractIncomeManageTypeEnum.parseAbbrCode(Integer.parseInt(concludeExpandE.getConmanagetype()));
            }else if (ContractBusinessLineEnum.建管.getCode().equals(contractIncomeConcludeE.getContractBusinessLine())){
                contractManageTypeAbbrCode = "JG";
                log.info("建管合同查询合同管理类别指定JG");
            }else{
                log.info("物管合同查询合同管理类别最后判断：{}",concludeExpandE.getConmanagetype());
                contractManageTypeAbbrCode = ContractIncomeManageTypeEnum.parseAbbrCode(Integer.parseInt(concludeExpandE.getConmanagetype()));
            }
            if (StringUtils.isBlank(contractManageTypeAbbrCode)){
                return ContractNoInfoV.builder().code(0).failReason("合同类别数据异常,请维护").build();
            }
            sb.append("-").append(contractManageTypeAbbrCode);
            sb.append("-").append(year);
            String redisKey = INCOME_GENERATE_CONTRACT_NO_PREFIX + year + isBackDate;
            if (null != oldConcludeE && contractIncomeConcludeE.getIsBackDate().equals(oldConcludeE.getIsBackDate())) {
                serialStr = matcherSerial(curContractNo);
            }
            if (StringUtils.isBlank(serialStr)) {
                // 区域的序号维度是: 年份+区域(对应字段isBackDate)
                // 非补充协议合同编号
                List<String> contractNos = contractIncomeConcludeMapper.queryContractNosByRangeTimeAndCertainPropertyNotSupply(
                        null,isBackDate,null,startTime,endTime, year);
                contractNos.remove(curContractNo);
                int maxSerialNumber = getMaxSerialNumberByDb(contractNos,"-");
                serialStr = calculateCertainSerialNumber(redisKey, maxSerialNumber, 3);
                /*String contractNum = contractIncomeConcludeMapper.getContractNoNum(sb.toString());
                if(StringUtils.isNotEmpty(contractNum)){
                    serialStr = String.format("%03d", Integer.parseInt(contractNum)+1);
                }else{
                    serialStr = "001";
                }*/
            }
            sb.append("-").append(serialStr);
            // 补充协议合同编号
            if (!contractIncomeConcludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())){
                return ContractNoInfoV.builder().code(1).contractNo(sb.toString()).redisKey(redisKey).build();
            }
            String supplyRedisKey = INCOME_SUPPLY_PREFIX + year + isBackDate;
            if (null != oldConcludeE && contractIncomeConcludeE.getIsBackDate().equals(oldConcludeE.getIsBackDate())) {
                supplySerialStr = matcherSupplySerial(curContractNo);
            }
            if (StringUtils.isBlank(supplySerialStr)) {
                List<String> supplyContractNos = contractIncomeConcludeMapper.queryContractNosByRangeTimeAndCertainPropertySupply(
                        null,isBackDate, null,startTime,endTime, year);
                supplyContractNos.remove(curContractNo);
                int maxSupplyNumber = getMaxSerialNumberByDb(supplyContractNos,"补");
                supplySerialStr = calculateCertainSerialNumber(supplyRedisKey, maxSupplyNumber, 2);
            }
            sb.append("-补").append(supplySerialStr);
            return ContractNoInfoV.builder().code(1).contractNo(sb.toString()).redisKey(redisKey).supplyRedisKey(supplyRedisKey).build();
        }
        else if (scope == 3){
            sb.append("-").append(regionAbbrCode);

            if (StringUtils.isBlank(communityRv.getProvince())){
                return ContractNoInfoV.builder().code(0).failReason("请维护项目所在省份信息").build();
            }
            AreaInfoListV provinceInfo = configFeignClient.getProvinceAreaByName(communityRv.getProvince());
            if (Objects.isNull(provinceInfo)){
                return ContractNoInfoV.builder().code(0).failReason("省份信息有误,请检查").build();
            }
            if (StringUtils.isBlank(provinceInfo.getAbbrCode())){
                return ContractNoInfoV.builder().code(0).failReason("请维护省份简称").build();
            }
            sb.append("-").append(provinceInfo.getAbbrCode());
            if (StringUtils.isBlank(communityRv.getProjectNameNumber())){
                return ContractNoInfoV.builder().code(0).failReason("请维护项目简称编号").build();
            }
            sb.append("-").append(communityRv.getProjectNameNumber());
            sb.append("-").append(incomeTypeAbbr);

            if (StringUtils.isBlank(concludeExpandE.getConmanagetype())){
                return ContractNoInfoV.builder().code(0).failReason("合同类别不存在,请维护").build();
            }
            String contractManageTypeAbbrCode = null;
            if(ContractBusinessLineEnum.物管.getCode().equals(contractIncomeConcludeE.getContractBusinessLine())){
                log.info("物管合同查询合同管理类别：{}",concludeExpandE.getConmanagetype());
                contractManageTypeAbbrCode = ContractIncomeManageTypeEnum.parseAbbrCode(Integer.parseInt(concludeExpandE.getConmanagetype()));
            }else if (ContractBusinessLineEnum.建管.getCode().equals(contractIncomeConcludeE.getContractBusinessLine())){
                contractManageTypeAbbrCode = "JG";
                log.info("建管合同查询合同管理类别指定JG");
            }else{
                log.info("物管合同查询合同管理类别最后判断：{}",concludeExpandE.getConmanagetype());
                contractManageTypeAbbrCode = ContractIncomeManageTypeEnum.parseAbbrCode(Integer.parseInt(concludeExpandE.getConmanagetype()));
            }
            if (StringUtils.isBlank(contractManageTypeAbbrCode)){
                return ContractNoInfoV.builder().code(0).failReason("合同类别数据异常,请维护").build();
            }
            sb.append("-").append(contractManageTypeAbbrCode);
            sb.append("-").append(year);
            String redisKey = INCOME_GENERATE_CONTRACT_NO_PREFIX + year + contractIncomeConcludeE.getCommunityId();
            if (null != oldConcludeE && contractIncomeConcludeE.getCommunityId().equals(oldConcludeE.getCommunityId())) {
                serialStr = matcherSerial(curContractNo);
            }
            if (StringUtils.isBlank(serialStr)) {
                // 项目的序号维度是: 年份+项目
                // 非补充协议合同编号
                /*List<String> contractNos = contractIncomeConcludeMapper.queryContractNosByRangeTimeAndCertainPropertyNotSupply(
                        null,null,contractIncomeConcludeE.getCommunityId(),startTime,endTime, year);
                contractNos.remove(curContractNo);
                int maxSerialNumber = getMaxSerialNumberByDb(contractNos,"-");
                serialStr = calculateCertainSerialNumber(redisKey, maxSerialNumber, 3);*/
                String contractNum = contractIncomeConcludeMapper.getContractNoNum(contractIncomeConcludeE.getCommunityId(),"-"+year);
                if(StringUtils.isNotEmpty(contractNum)){
                    serialStr = String.format("%03d", Integer.parseInt(contractNum)+1);
                }else{
                    serialStr = "001";
                }
            }
            sb.append("-").append(serialStr);
            // 补充协议合同编号
            if (!contractIncomeConcludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())){
                return ContractNoInfoV.builder().code(1).contractNo(sb.toString()).redisKey(redisKey).build();
            }
            String supplyRedisKey = INCOME_SUPPLY_PREFIX + year + contractIncomeConcludeE.getCommunityId();
            if (null != oldConcludeE && contractIncomeConcludeE.getCommunityId().equals(oldConcludeE.getCommunityId())) {
                supplySerialStr = matcherSupplySerial(curContractNo);
            }
            if (StringUtils.isBlank(supplySerialStr)) {
                List<String> supplyContractNos = contractIncomeConcludeMapper.queryContractNosByRangeTimeAndCertainPropertySupply(
                        null,null, contractIncomeConcludeE.getCommunityId(),startTime,endTime, year);
                supplyContractNos.remove(curContractNo);
                int maxSupplyNumber = getMaxSerialNumberByDb(supplyContractNos,"补");
                supplySerialStr = calculateCertainSerialNumber(supplyRedisKey, maxSupplyNumber, 2);
            }
            sb.append("-补").append(supplySerialStr);
            return ContractNoInfoV.builder().code(1).contractNo(sb.toString()).redisKey(redisKey).supplyRedisKey(supplyRedisKey).build();
        } else {
            return ContractNoInfoV.builder().code(0).failReason("合同数据有误,无法判定归属").build();
        }
    }

    /**
     * 截取合同编码中的自增编码
     * @param curContractNo
     * @return
     */
    private static String matcherSerial(String curContractNo) {
        if (StringUtils.isNotBlank(curContractNo)) {
            Matcher matcher = contractNoPattern.matcher(curContractNo);
            return matcher.find() ? matcher.group() : null;
        }
        return null;
    }

    /**
     * 截取合同编码中补充协议的自增编码
     * @param curContractNo
     * @return
     */
    private static String matcherSupplySerial(String curContractNo) {
        if (StringUtils.isNotBlank(curContractNo)) {
            Matcher matcher = contractNoPatternSupply.matcher(curContractNo);
            return matcher.find() ? matcher.group() : null;
        }
        return null;
    }

    /**
     * 支出合同-处理合同编号
     * 只有发起流程生成编码
     * 修改合同时组织或部门或项目无变化的时候保留原后缀
     *
     * todo 修改、补充V2
     **/
    public ContractNoInfoV generatePayContractNo(ContractPayConcludeE contractPayConcludeE, ContractPayConcludeE oldConcludeE){
        log.info("合同生成合同编号：{}", JSONArray.toJSON(contractPayConcludeE));
        // 若合同是修改合同,则不需要进行后续逻辑处理 合同编号直接返回去
        if (Objects.nonNull(contractPayConcludeE) &&
                Objects.nonNull(contractPayConcludeE.getContractType()) &&
                contractPayConcludeE.getContractType().equals(ContractTypeEnum.修改合同.getCode())){
            return ContractNoInfoV.builder().code(1).contractNo(contractPayConcludeE.getContractNo()).build();
        }

        // 若合同是补充合同
        if (Objects.nonNull(contractPayConcludeE) &&
                contractPayConcludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())){
            // 若之前的合同也是补充合同
            if (Objects.nonNull(oldConcludeE) &&
                    oldConcludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())){
                // 若父id保持一致，则不变
                if (StringUtils.equals(oldConcludeE.getPid(), contractPayConcludeE.getPid())){
                    return ContractNoInfoV.builder().code(1).contractNo(oldConcludeE.getContractNo()).build();
                } else {
                    return handleContractNoOnSupplyPayContract(contractPayConcludeE);
                }
            } else {
                // 如果发起流程的时候,补充协议已有编号,那么就是通过编辑生成过了,不处理了
                if (Objects.isNull(oldConcludeE) &&
                        StringUtils.isNotBlank(contractPayConcludeE.getContractNo())){
                    return ContractNoInfoV.builder().code(1).contractNo(contractPayConcludeE.getContractNo()).build();
                }
                // 若之前的合同不是补充合同现在是 或者 没有之前的合同[发起流程]
                return handleContractNoOnSupplyPayContract(contractPayConcludeE);
            }
        }

        if (null == oldConcludeE && StringUtils.isNotBlank(contractPayConcludeE.getContractNo())) {
            return ContractNoInfoV.builder().code(1).contractNo(contractPayConcludeE.getContractNo()).build();
        }
        QueryWrapper<ContractPayConcludeExpandE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ContractPayConcludeExpandE.CONTRACTID, contractPayConcludeE.getId());
        ContractPayConcludeExpandE concludeExpandE = contractPayConcludeExpandMapper.selectOne(queryWrapper);
        if (Objects.isNull(concludeExpandE)){
            return ContractNoInfoV.builder().code(0).failReason("合同数据异常,请检查").build();
        }

        // 合同所属区域  0总部 2华北 3华南 4华东 5华西 6华中?
        Integer isBackDate = contractPayConcludeE.getIsBackDate();
        if (Objects.isNull(isBackDate)){
            return ContractNoInfoV.builder().code(0).failReason("请维护所属区域").build();
        }
        if (isBackDate.equals(ContractAreaEnum.区域公司.getCode()) || isBackDate.equals(ContractAreaEnum.商写事业部.getCode())){
            return ContractNoInfoV.builder().code(0).failReason("所选区域无法生成合同编号").build();
        }
        String regionAbbrCode = ContractAreaEnum.parseAbbrCode(isBackDate);

        // 项目id及项目简称编码
        SpaceCommunityRv communityRv = spaceFeignClient.getById(contractPayConcludeE.getCommunityId());
        int scope = judgeScope(regionAbbrCode, communityRv.getName());

        // 支付类型简称
        String incomeTypeAbbr = contractPayConcludeE.getNoPaySign() == 1 ? "WZF" : "CHB";

        // 编写合同年份
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        Date now = new Date();
        Date startTime = getYearFirst(year);
        Date endTime = getEndTimeOfCurrentYear(now);

        String curContractNo = contractPayConcludeE.getContractNo();
        String serialStr = "";
        String supplySerialStr = "";
        StringBuilder sb = new StringBuilder();
        // 固定 单位简称
        sb.append("ZJFW");
        /**
         * 1 集团: 选择合同所属区域为[总部]，没有简称编码
         *     单位简称-收支类型简称-部门简称-合同类别简称-编写合同年份-合同序号
         **/
        if (scope == 1){
            String departId = contractPayConcludeE.getDepartId();
            OrgInfoRv orgInfoRv = orgFeignClient.getByOrgId(Long.parseLong(departId));
            sb.append("-").append(incomeTypeAbbr);
            if (StringUtils.isBlank(orgInfoRv.getAbbrCode())){
                return ContractNoInfoV.builder().code(0).failReason("请维护部门简称").build();
            }
            sb.append("-").append(orgInfoRv.getAbbrCode());
            if (StringUtils.isBlank(concludeExpandE.getConmanagetype())){
                return ContractNoInfoV.builder().code(0).failReason("合同类别不存在,请维护").build();
            }
            String contractManageTypeAbbrCode = null;
            if(ContractBusinessLineEnum.物管.getCode().equals(contractPayConcludeE.getContractBusinessLine())){
                log.info("物管合同查询合同管理类别：{}",concludeExpandE.getConmanagetype());
                contractManageTypeAbbrCode = ContractPayManageTypeEnum.parseAbbrCode(Integer.parseInt(concludeExpandE.getConmanagetype()));
            }else if (ContractBusinessLineEnum.建管.getCode().equals(contractPayConcludeE.getContractBusinessLine())){
                contractManageTypeAbbrCode = "JG";
                log.info("建管合同查询合同管理类别指定JG");
            }else if (ContractBusinessLineEnum.商管.getCode().equals(contractPayConcludeE.getContractBusinessLine())){
                contractManageTypeAbbrCode = "SG";
                log.info("商管合同查询合同管理类别指定SG");
            }else{
                log.info("物管合同查询合同管理类别最后判断：{}",concludeExpandE.getConmanagetype());
                contractManageTypeAbbrCode = ContractPayManageTypeEnum.parseAbbrCode(Integer.parseInt(concludeExpandE.getConmanagetype()));
            }
            if (StringUtils.isBlank(contractManageTypeAbbrCode)){
                return ContractNoInfoV.builder().code(0).failReason("合同类别数据异常,请维护").build();
            }
            sb.append("-").append(contractManageTypeAbbrCode);

            sb.append("-").append(year);
            String redisKey = PAY_GENERATE_CONTRACT_NO_PREFIX + year + departId;
            if (null != oldConcludeE && contractPayConcludeE.getDepartId().equals(oldConcludeE.getDepartId())) {
                serialStr = matcherSerial(curContractNo);
            }
            if (StringUtils.isBlank(serialStr)) {
                // 总部的序号按照 年份+部门 递增
                // 非补充协议合同编号
                List<String> contractNos = contractPayConcludeMapper.queryContractNosByRangeTimeAndCertainPropertyNotSupply(
                        departId,null, null, startTime,endTime, year);
                contractNos.remove(curContractNo);
                int maxSerialNumber = getMaxSerialNumberByDb(contractNos,"-");
                serialStr = calculateCertainSerialNumber(redisKey, maxSerialNumber, 3);

                /*String contractNum = contractPayConcludeMapper.getContractNoNum(sb.toString());
                if(StringUtils.isNotEmpty(contractNum)){
                    serialStr = String.format("%03d", Integer.parseInt(contractNum)+1);
                }else{
                    serialStr = "001";
                }*/

            }

            sb.append("-").append(serialStr);
            // 补充协议合同编号
            if (!contractPayConcludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())){
                return ContractNoInfoV.builder().code(1).contractNo(sb.toString()).redisKey(redisKey).build();
            }
            String supplyRedisKey = PAY_SUPPLY_PREFIX + year + departId;
            if (null != oldConcludeE && contractPayConcludeE.getDepartId().equals(oldConcludeE.getDepartId())) {
                supplySerialStr = matcherSupplySerial(curContractNo);
            }
            if (StringUtils.isBlank(supplySerialStr)) {
                List<String> supplyContractNos = contractPayConcludeMapper.queryContractNosByRangeTimeAndCertainPropertySupply(
                        departId, null, null, startTime,endTime, year);
                supplyContractNos.remove(curContractNo);
                int maxSupplyNumber = getMaxSerialNumberByDb(supplyContractNos,"补");
                supplySerialStr = calculateCertainSerialNumber(supplyRedisKey, maxSupplyNumber, 2);
            }

            sb.append("-补").append(supplySerialStr);
            return ContractNoInfoV.builder().code(1).contractNo(sb.toString()).redisKey(redisKey).supplyRedisKey(supplyRedisKey).build();
        }
        /**
         * 区域
         * 单位简称-区域公司简称-收支类型简称-合同类别简称-编写合同年份-合同序号
         **/
        else if (scope == 2){
            sb.append("-").append(regionAbbrCode);
            sb.append("-").append(incomeTypeAbbr);

            if (StringUtils.isBlank(concludeExpandE.getConmanagetype())){
                return ContractNoInfoV.builder().code(0).failReason("合同类别不存在,请维护").build();
            }
            String contractManageTypeAbbrCode = null;
            if(ContractBusinessLineEnum.物管.getCode().equals(contractPayConcludeE.getContractBusinessLine())){
                log.info("物管合同查询合同管理类别：{}",concludeExpandE.getConmanagetype());
                contractManageTypeAbbrCode = ContractPayManageTypeEnum.parseAbbrCode(Integer.parseInt(concludeExpandE.getConmanagetype()));
            }else if (ContractBusinessLineEnum.建管.getCode().equals(contractPayConcludeE.getContractBusinessLine())){
                contractManageTypeAbbrCode = "JG";
                log.info("建管合同查询合同管理类别指定JG");
            }else if (ContractBusinessLineEnum.商管.getCode().equals(contractPayConcludeE.getContractBusinessLine())){
                contractManageTypeAbbrCode = "SG";
                log.info("商管合同查询合同管理类别指定SG");
            }else{
                log.info("物管合同查询合同管理类别最后判断：{}",concludeExpandE.getConmanagetype());
                contractManageTypeAbbrCode = ContractPayManageTypeEnum.parseAbbrCode(Integer.parseInt(concludeExpandE.getConmanagetype()));
            }

            if (StringUtils.isBlank(contractManageTypeAbbrCode)){
                return ContractNoInfoV.builder().code(0).failReason("合同类别数据异常,请维护").build();
            }
            sb.append("-").append(contractManageTypeAbbrCode);
            sb.append("-").append(year);
            String redisKey = PAY_GENERATE_CONTRACT_NO_PREFIX + year + isBackDate;
            if (null != oldConcludeE && contractPayConcludeE.getIsBackDate().equals(oldConcludeE.getIsBackDate())) {
                serialStr = matcherSerial(curContractNo);
            }
            if (StringUtils.isBlank(serialStr)) {
                // 区域的序号维度是: 年份+区域(对应字段isBackDate)
                // 非补充协议合同编号
                List<String> contractNos = contractPayConcludeMapper.queryContractNosByRangeTimeAndCertainPropertyNotSupply(
                        null,isBackDate,null,startTime,endTime, year);
                contractNos.remove(curContractNo);
                int maxSerialNumber = getMaxSerialNumberByDb(contractNos,"-");
                serialStr = calculateCertainSerialNumber(redisKey, maxSerialNumber, 3);
                /*String contractNum = contractPayConcludeMapper.getContractNoNum(sb.toString());
                if(StringUtils.isNotEmpty(contractNum)){
                    serialStr = String.format("%03d", Integer.parseInt(contractNum)+1);
                }else{
                    serialStr = "001";
                }*/
            }

            sb.append("-").append(serialStr);
            // 补充协议合同编号
            if (!contractPayConcludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())){
                return ContractNoInfoV.builder().code(1).contractNo(sb.toString()).redisKey(redisKey).build();
            }
            String supplyRedisKey = PAY_SUPPLY_PREFIX + year + isBackDate;
            if (null != oldConcludeE && contractPayConcludeE.getIsBackDate().equals(oldConcludeE.getIsBackDate())) {
                supplySerialStr = matcherSupplySerial(curContractNo);
            }
            if (StringUtils.isBlank(supplySerialStr)) {
                List<String> supplyContractNos = contractPayConcludeMapper.queryContractNosByRangeTimeAndCertainPropertySupply(
                        null,isBackDate, null,startTime,endTime, year);
                supplyContractNos.remove(curContractNo);
                int maxSupplyNumber = getMaxSerialNumberByDb(supplyContractNos,"补");
                supplySerialStr = calculateCertainSerialNumber(supplyRedisKey, maxSupplyNumber, 2);
            }
            sb.append("-补").append(supplySerialStr);
            return ContractNoInfoV.builder().code(1).contractNo(sb.toString()).redisKey(redisKey).supplyRedisKey(supplyRedisKey).build();
        }
        else if (scope == 3){
            sb.append("-").append(regionAbbrCode);


            if (StringUtils.isBlank(communityRv.getProvince())){
                return ContractNoInfoV.builder().code(0).failReason("请维护项目所在省份信息").build();
            }
            AreaInfoListV provinceInfo = configFeignClient.getProvinceAreaByName(communityRv.getProvince());
            if (Objects.isNull(provinceInfo)){
                return ContractNoInfoV.builder().code(0).failReason("省份信息有误,请检查").build();
            }
            if (StringUtils.isBlank(provinceInfo.getAbbrCode())){
                return ContractNoInfoV.builder().code(0).failReason("请维护省份简称").build();
            }
            sb.append("-").append(provinceInfo.getAbbrCode());
            if (StringUtils.isBlank(communityRv.getProjectNameNumber())){
                return ContractNoInfoV.builder().code(0).failReason("请维护项目简称编号").build();
            }
            sb.append("-").append(communityRv.getProjectNameNumber());
            sb.append("-").append(incomeTypeAbbr);

            if (StringUtils.isBlank(concludeExpandE.getConmanagetype())){
                return ContractNoInfoV.builder().code(0).failReason("合同类别不存在,请维护").build();
            }
            String contractManageTypeAbbrCode = null;
            if(ContractBusinessLineEnum.物管.getCode().equals(contractPayConcludeE.getContractBusinessLine())){
                log.info("物管合同查询合同管理类别：{}",concludeExpandE.getConmanagetype());
                contractManageTypeAbbrCode = ContractPayManageTypeEnum.parseAbbrCode(Integer.parseInt(concludeExpandE.getConmanagetype()));
            }else if (ContractBusinessLineEnum.建管.getCode().equals(contractPayConcludeE.getContractBusinessLine())){
                contractManageTypeAbbrCode = "JG";
                log.info("建管合同查询合同管理类别指定JG");
            }else if (ContractBusinessLineEnum.商管.getCode().equals(contractPayConcludeE.getContractBusinessLine())){
                contractManageTypeAbbrCode = "SG";
                log.info("商管合同查询合同管理类别指定SG");
            }else{
                log.info("物管合同查询合同管理类别最后判断：{}",concludeExpandE.getConmanagetype());
                contractManageTypeAbbrCode = ContractPayManageTypeEnum.parseAbbrCode(Integer.parseInt(concludeExpandE.getConmanagetype()));
            }
            if (StringUtils.isBlank(contractManageTypeAbbrCode)){
                return ContractNoInfoV.builder().code(0).failReason("合同类别数据异常,请维护").build();
            }
            sb.append("-").append(contractManageTypeAbbrCode);
            sb.append("-").append(year);
            String redisKey = PAY_GENERATE_CONTRACT_NO_PREFIX + year + contractPayConcludeE.getCommunityId();
            if (null != oldConcludeE && contractPayConcludeE.getCommunityId().equals(oldConcludeE.getCommunityId())) {
                serialStr = matcherSerial(curContractNo);
            }
           if (StringUtils.isBlank(serialStr)) {
                // 项目的序号维度是: 年份+项目
                // 非补充协议合同编号
                /*List<String> contractNos = contractPayConcludeMapper.queryContractNosByRangeTimeAndCertainPropertyNotSupply(
                        null,null,contractPayConcludeE.getCommunityId(),startTime,endTime, year);
                contractNos.remove(curContractNo);
                int maxSerialNumber = getMaxSerialNumberByDb(contractNos,"-");
                serialStr = calculateCertainSerialNumber(redisKey, maxSerialNumber, 3);*/
               String contractNum = contractPayConcludeMapper.getContractNoNum(contractPayConcludeE.getCommunityId(),"-"+year);
               if(StringUtils.isNotEmpty(contractNum)){
                   serialStr = String.format("%03d", Integer.parseInt(contractNum)+1);
               }else{
                   serialStr = "001";
               }
            }
            sb.append("-").append(serialStr);
            // 补充协议合同编号
            if (!contractPayConcludeE.getContractType().equals(ContractTypeEnum.补充协议.getCode())){
                return ContractNoInfoV.builder().code(1).contractNo(sb.toString()).redisKey(redisKey).build();
            }
            String supplyRedisKey = PAY_SUPPLY_PREFIX + year + contractPayConcludeE.getCommunityId();
            if (null != oldConcludeE && contractPayConcludeE.getCommunityId().equals(oldConcludeE.getCommunityId())) {
                supplySerialStr = matcherSupplySerial(curContractNo);
            }
            if (StringUtils.isBlank(supplySerialStr)) {
                List<String> supplyContractNos = contractPayConcludeMapper.queryContractNosByRangeTimeAndCertainPropertySupply(
                        null,null, contractPayConcludeE.getCommunityId(),startTime,endTime, year);
                supplyContractNos.remove(curContractNo);
                int maxSupplyNumber = getMaxSerialNumberByDb(supplyContractNos,"补");
                supplySerialStr = calculateCertainSerialNumber(supplyRedisKey, maxSupplyNumber, 2);
            }
            sb.append("-补").append(supplySerialStr);
            return ContractNoInfoV.builder().code(1).contractNo(sb.toString()).redisKey(redisKey).supplyRedisKey(supplyRedisKey).build();
        } else {
            return ContractNoInfoV.builder().code(0).failReason("合同数据有误,无法判定归属").build();
        }
    }

    private ContractNoInfoV handleContractNoOnSupplyPayContract(ContractPayConcludeE contractPayConcludeE) {
        ContractPayConcludeE curParentPayConclude = contractPayConcludeMapper.selectById(contractPayConcludeE.getPid());
        if (Objects.isNull(curParentPayConclude)) {
            return ContractNoInfoV.builder().code(0).failReason("找不到对应的补充协议父合同").build();
        }
        String supplyRedisKey = PAY_SUPPLY_PREFIX + contractPayConcludeE.getPid();
        List<String> supplyContractNos = contractPayConcludeMapper.queryContractNosByPid(contractPayConcludeE.getPid());
        int maxSupplyNumber = getMaxSerialNumberByDb(supplyContractNos, "补");
        String supplySerialStr = calculateCertainSerialNumber(supplyRedisKey, maxSupplyNumber, 2);
        return ContractNoInfoV.builder().code(1).contractNo(curParentPayConclude.getContractNo() + "-补" + supplySerialStr).supplyRedisKey(supplyRedisKey).build();
    }

    private ContractNoInfoV handleContractNoOnSupplyIncomeContract(ContractIncomeConcludeE contractPayConcludeE) {
        ContractIncomeConcludeE curParentIncomeConclude = contractIncomeConcludeMapper.selectById(contractPayConcludeE.getPid());
        if (Objects.isNull(curParentIncomeConclude)) {
            return ContractNoInfoV.builder().code(0).failReason("找不到对应的补充协议父合同").build();
        }
        String supplyRedisKey = INCOME_SUPPLY_PREFIX + contractPayConcludeE.getPid();
        List<String> supplyContractNos = contractIncomeConcludeMapper.queryContractNosByPid(contractPayConcludeE.getPid());
        int maxSupplyNumber = getMaxSerialNumberByDb(supplyContractNos, "补");
        String supplySerialStr = calculateCertainSerialNumber(supplyRedisKey, maxSupplyNumber, 2);
        return ContractNoInfoV.builder().code(1).contractNo(curParentIncomeConclude.getContractNo() + "-补" + supplySerialStr).supplyRedisKey(supplyRedisKey).build();
    }

    /**
     * 回退计数器
     **/
    public void callBackRedisCounter(ContractNoInfoV contractNoInfoV){
//        if (StringUtils.isNotBlank(contractNoInfoV.getRedisKey())){
//            redisTemplate.opsForValue().decrement(contractNoInfoV.getRedisKey());
//        }
//        if (StringUtils.isNotBlank(contractNoInfoV.getSupplyRedisKey())){
//            redisTemplate.opsForValue().decrement(contractNoInfoV.getSupplyRedisKey());
//        }
    }


    /**
     * 指定redisKey，走计数器比较生成序号
     * 若已有比较大的序号，重置计数器
     **/
    private String calculateCertainSerialNumber(String redisKey,Integer maxSerialNumber,int digit){
//        Long increment = redisTemplate.opsForValue().increment(redisKey,1L);
//        if (increment <= maxSerialNumber){
//            redisTemplate.opsForValue().set(redisKey, String.valueOf(maxSerialNumber));
//            increment = redisTemplate.opsForValue().increment(redisKey,1L);
//        }
        // 小于三位补0
        String serialStr;
        Integer increment = maxSerialNumber + 1;
        if (increment < Math.pow(10, digit)){
            serialStr = String.format("%0"+digit+"d", increment);
        } else {
            serialStr = increment.toString();
        }
        return serialStr;
    }

    /**
     * 指定数据(db数据)和数据分隔符,获取最大的序号
     **/
    private int getMaxSerialNumberByDb(List<String> values,String splitUnit){
        int maxNumber = 0;
        if (CollectionUtils.isNotEmpty(values)){
            for (String curContractNo : values) {
                if (StringUtils.isBlank(curContractNo) || !curContractNo.contains(splitUnit)){
                    continue;
                }
                String[] split = curContractNo.split(splitUnit);
                String serialNumber = split[split.length - 1].trim();
                if (serialNumber.matches("[0-9]+")){
                    try {
                        int curNum = Integer.parseInt(serialNumber);
                        maxNumber = Math.max(curNum, maxNumber);
                    } catch (NumberFormatException e){
                        log.warn("contractNo is not int number, contractNo: {}", serialNumber);
                    }
                }
            }
        }
        return maxNumber;
    }

    /**
     * 判定项目属于什么范围
     * 1 集团: 选择合同所属区域为[总部]，没有简称编码
     *          单位简称-收支类型简称-部门简称-合同类别简称-编写合同年份-合同序号
     * 2 区域: 选择合同所属区域非总部(有简称编码),且项目名称包含[区域]
     *          单位简称-区域公司简称-收支类型简称-合同类别简称-编写合同年份-合同序号
     * 3 项目: 选择合同所属区域非总部(有简称编码),且项目名称不包含[区域]
     *          单位简称-区域公司简称-项目所在地简称(省份)-项目简称编号-收支类型简称-合同类别简称-编写合同年份-合同序号
     **/
    public ContractNoInfoV generateContractNo(ContractNoGenerateF contractNoGenerateF) {
        int scope = judgeScope(contractNoGenerateF.getRegionAbbrCode(), contractNoGenerateF.getCommunityName());
        // 收支类型简称
        String incomeTypeAbbr;
        if (contractNoGenerateF.getNoPaySign() == 1){
            incomeTypeAbbr = "WZF";
        } else {
            incomeTypeAbbr = contractNoGenerateF.getIncomeType() == 1 ? "SHR": "CHB";
        }
        // 编写合同年份
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        StringBuilder sb = new StringBuilder();
        // 固定 单位简称
        sb.append("ZJFW");
        switch(scope) {
            case 1:
                sb.append("-").append(incomeTypeAbbr);
                if (StringUtils.isBlank(contractNoGenerateF.getDepartAbbrCode())){
                    return ContractNoInfoV.builder().code(0).failReason("请维护部门简称").build();
                }
                sb.append("-").append(contractNoGenerateF.getDepartAbbrCode());
                sb.append("-").append(contractNoGenerateF.getCategoryAbbrCode());
                sb.append("-").append(year);
                sb.append("-").append("待定序号");
                break;
            case 2:
                if (StringUtils.isBlank(contractNoGenerateF.getRegionAbbrCode())){
                    return ContractNoInfoV.builder().code(0).failReason("请维护所属区域简称").build();
                }
                sb.append("-").append(contractNoGenerateF.getRegionAbbrCode());
                sb.append("-").append(incomeTypeAbbr);
                sb.append("-").append(contractNoGenerateF.getCategoryAbbrCode());
                sb.append("-").append(year);
                sb.append("-").append("待定序号");
                break;
            case 3:
                if (StringUtils.isBlank(contractNoGenerateF.getRegionAbbrCode())){
                    return ContractNoInfoV.builder().code(0).failReason("请维护所属区域简称").build();
                }
                sb.append("-").append(contractNoGenerateF.getRegionAbbrCode());
                if (StringUtils.isBlank(contractNoGenerateF.getProvinceAbbrCode())){
                    return ContractNoInfoV.builder().code(0).failReason("请维护省份简称").build();
                }
                sb.append("-").append(contractNoGenerateF.getProvinceAbbrCode());
                if (StringUtils.isBlank(contractNoGenerateF.getProjectNameNumber())){
                    return ContractNoInfoV.builder().code(0).failReason("请维护项目简称编号").build();
                }
                sb.append("-").append(contractNoGenerateF.getProjectNameNumber());
                sb.append("-").append(incomeTypeAbbr);
                sb.append("-").append(contractNoGenerateF.getCategoryAbbrCode());
                sb.append("-").append(year);
                sb.append("-").append("待定序号");
                break;
            default:
                throw new IllegalArgumentException("项目分类归属异常");
        }

        if (Objects.nonNull(contractNoGenerateF.getSigningMethod()) && contractNoGenerateF.getSigningMethod() == 1){
            //Long supplementalNumber = redisTemplate.opsForValue().increment("tmp-supplement",1L);
            //sb.append("-").append("补").append(supplementalNumber);
        }
        return ContractNoInfoV.builder().code(1).contractNo(sb.toString()).build();
    }

    private int judgeScope(String regionAbbrCode, String communityName) {
        if (StringUtils.isBlank(regionAbbrCode)){
            return 1;
        } else {
            if (communityName.contains("区域")){
                return 2;
            } else {
                return 3;
            }
        }
    }

    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    private Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 获取某年最后一天日期
     * @return Date
     */
    private Date getEndTimeOfCurrentYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), Calendar.DECEMBER,31);
        setMaxTimeOfDay(calendar);
        return calendar.getTime();
    }

    /**
     * 设置当天的结束时间
     * @param calendar
     */
    private void setMaxTimeOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    //根据合同id批量获取收款信息
    public List<ContractPaymentVO> getContractPaymentList(List<String> ids) {
        List<ContractPaymentVO> resultList = new ArrayList<>();
        //收入合同扩展信息
        QueryWrapper<ContractIncomeConcludeExpandE> incomeQueryWrapper = new QueryWrapper<>();
        incomeQueryWrapper.in(ContractPayFundE.CONTRACT_ID, ids)
                .eq(ContractIncomeConcludeExpandE.DELETED,0);
        List<ContractIncomeConcludeExpandE> incomeExpandList = contractIncomeConcludeExpandMapper.selectList(incomeQueryWrapper);
        if(CollectionUtils.isNotEmpty(incomeExpandList)){
            for(ContractIncomeConcludeExpandE payExpand : incomeExpandList){
                extracted(payExpand.getContractId(),1, payExpand.getFkdwxx(), resultList);
            }
        }

        //支出合同扩展信息
        QueryWrapper<ContractPayConcludeExpandE> payQueryWrapper = new QueryWrapper<>();
        payQueryWrapper.in(ContractPayFundE.CONTRACT_ID, ids)
                .eq(ContractPayConcludeExpandE.DELETED,0);
        List<ContractPayConcludeExpandE> payExpandList = contractPayConcludeExpandMapper.selectList(payQueryWrapper);
        if(CollectionUtils.isNotEmpty(payExpandList)){
            for(ContractPayConcludeExpandE payExpand : payExpandList){
                extracted(payExpand.getContractId(),2, payExpand.getSkdwxx(), resultList);
            }
        }
        return resultList;
    }

    private static void extracted(String contractId, Integer contractType, String fkdwxxOrskdwxx, List<ContractPaymentVO> resultList) {
        ContractPaymentVO paymentVO = new ContractPaymentVO();
        paymentVO.setContractId(contractId);
        paymentVO.setContractType(contractType);
        //合同支付信息:收入方信息
        if(StringUtils.isNotEmpty(fkdwxxOrskdwxx)){
            //收款人ID
            StringJoiner payeeId = new StringJoiner(",");
            StringJoiner payeeName = new StringJoiner(",");
            //收入方ID
            StringJoiner incomeId = new StringJoiner(",");
            StringJoiner incomeName = new StringJoiner(",");
            try {
                //收入方信息
                List<ContractSrfxxF> fkdxxOrsrfxx = JSONObject.parseArray(fkdwxxOrskdwxx,ContractSrfxxF.class);
                if(CollectionUtils.isNotEmpty(fkdxxOrsrfxx)) {
                    fkdxxOrsrfxx.forEach(srf ->{
                        if (contractType == 1) {
                            // 收入合同取支出方人信息, 实际付款人优先
                            if (StringUtils.isNotEmpty(srf.getDrawee())) {
                                incomeId.add(srf.getDraweeid());
                                incomeName.add(srf.getDrawee());
                            }
                            //实际付款人信息
                            if (StringUtils.isNotEmpty(srf.getTruedrawee())) {
                                payeeId.add(srf.getTruedraweeid());
                                payeeName.add(srf.getTruedrawee());

                            }
                        } else if (contractType == 2) {
                            //支出合同取收入方信息,实际收款人优先
                            if (StringUtils.isNotEmpty(srf.getPayee())) {
                                incomeId.add(srf.getPayeeid());
                                incomeName.add(srf.getPayee());
                            }
                            //实际收款人信息
                            if (StringUtils.isNotEmpty(srf.getTruepayee())) {
                                payeeId.add(srf.getTruepayeeid());
                                payeeName.add(srf.getTruepayee());
                            }
                        }
                    });
                    paymentVO.setPayeeId(payeeId.toString());
                    paymentVO.setPayeeName(payeeName.toString());
                    paymentVO.setIncomeId(incomeId.toString());
                    paymentVO.setIncomeName(incomeName.toString());
                }
            } catch (BeansException e) {
            }
        }
        resultList.add(paymentVO);
    }

}
