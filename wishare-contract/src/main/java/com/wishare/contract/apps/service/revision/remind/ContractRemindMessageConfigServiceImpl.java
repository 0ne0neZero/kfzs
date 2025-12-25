package com.wishare.contract.apps.service.revision.remind;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.apps.fo.remind.CommunityIdF;
import com.wishare.contract.apps.fo.remind.ContractRemindMessageConfigF;
import com.wishare.contract.apps.fo.remind.ListUserInfoF;
import com.wishare.contract.apps.fo.remind.PhoneParamF;
import com.wishare.contract.apps.fo.remind.RemindRuleDetailF;
import com.wishare.contract.apps.fo.remind.UserOrgRoleF;
import com.wishare.contract.apps.remote.clients.ExternalFeignClient;
import com.wishare.contract.apps.remote.clients.SpaceFeignClient;
import com.wishare.contract.apps.remote.clients.UserFeignClient;
import com.wishare.contract.apps.remote.fo.message.FirstExamineMessageF;
import com.wishare.contract.domains.entity.revision.remind.ContractRemindMessageConfigE;
import com.wishare.contract.domains.enums.DeletedEnum;
import com.wishare.contract.domains.enums.revision.RemindMessageTypeEnum;
import com.wishare.contract.domains.enums.revision.RemindTargetTypeEnum;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomePlanConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.contract.domains.mapper.revision.remind.ContractRemindMessageConfigMapper;
import com.wishare.contract.domains.vo.revision.remind.CommunityOrgV;
import com.wishare.contract.domains.vo.revision.remind.ContractAggregationV;
import com.wishare.contract.domains.vo.revision.remind.ContractAndPlanInfoV;
import com.wishare.contract.domains.vo.revision.remind.ContractRemindMessageConfigV;
import com.wishare.contract.domains.vo.revision.remind.PhoneThirdPartyIdV;
import com.wishare.contract.domains.vo.revision.remind.RemindRuleDetailV;
import com.wishare.contract.domains.vo.revision.remind.TargetInfoV;
import com.wishare.contract.domains.vo.revision.remind.UserMobileV;
import com.wishare.contract.domains.vo.revision.remind.UserOrgRoleV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.UidHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ContractRemindMessageConfigServiceImpl
        extends ServiceImpl<ContractRemindMessageConfigMapper, ContractRemindMessageConfigE>
        implements ContractRemindMessageConfigService, IOwlApiBase {

    @Autowired
    private ContractIncomeConcludeMapper contractIncomeConcludeMapper;

    @Autowired
    private ContractPayConcludeMapper contractPayConcludeMapper;

    @Autowired
    private ContractIncomePlanConcludeMapper contractIncomePlanConcludeMapper;

    @Autowired
    private SpaceFeignClient spaceFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private ExternalFeignClient externalFeignClient;

    @Autowired
    private ContractRemindMessageSendService contractRemindMessageSendService;

    /**
     * 保存提醒与预警消息配置
     *
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ContractRemindMessageConfigF param) {
        //将当前数据进行逻辑删除
        this.update(Wrappers.<ContractRemindMessageConfigE>lambdaUpdate()
                .set(ContractRemindMessageConfigE::getDeleted, DeletedEnum.ONE));
        //重新组装数据
        List<ContractRemindMessageConfigE> remindMessageConfigs = new ArrayList<>(
                convertRemindMessageConfigs(RemindMessageTypeEnum.EXPIRE, param.getExpiredConfigs()));
        remindMessageConfigs.addAll(convertRemindMessageConfigs(RemindMessageTypeEnum.REMIND, param.getIncomeConfigs()));
        remindMessageConfigs.addAll(convertRemindMessageConfigs(RemindMessageTypeEnum.OVERDUE, param.getOverdueConfigs()));
        if (CollUtil.isEmpty(remindMessageConfigs)) {
            return;
        }
        //保存数据
        this.saveBatch(remindMessageConfigs);
    }

    /**
     * 查询当前生效的消息配置
     *
     * @return
     */
    @Override
    public ContractRemindMessageConfigV detail() {
        ContractRemindMessageConfigV result = new ContractRemindMessageConfigV();
        //查询当前生效的全部配置
        List<ContractRemindMessageConfigE> remindMessageConfigs = this.list(Wrappers.<ContractRemindMessageConfigE>lambdaQuery()
                .eq(ContractRemindMessageConfigE::getDeleted, DeletedEnum.ZERO)
                .orderByAsc(ContractRemindMessageConfigE::getId));
        if (CollUtil.isEmpty(remindMessageConfigs)) {
            log.info("当前没有有效的的消息配置");
            return result;
        }
        //按照消息类型分组
        Map<RemindMessageTypeEnum, List<ContractRemindMessageConfigE>> remindMessageMap = remindMessageConfigs.stream()
                .collect(Collectors.groupingBy(ContractRemindMessageConfigE::getMessageType));
        //遍历每个组组装RemindRuleDetailV列表
        remindMessageMap.forEach((messageType, configs) -> {
            List<RemindRuleDetailV> ruleDetails = configs.stream().map(config -> {
                RemindRuleDetailV ruleDetail = new RemindRuleDetailV();
                ruleDetail.setId(config.getId());
                ruleDetail.setRemindDays(config.getRemindDays());
                ruleDetail.setTargetType(config.getTargetType());
                ruleDetail.setTargetInfos(StrUtil.isBlank(config.getTargetInfo()) ? null
                        : JSON.parseArray(config.getTargetInfo(), TargetInfoV.class));
                return ruleDetail;
            }).collect(Collectors.toList());
            //根据组类型设置ruleDetails
            if (RemindMessageTypeEnum.EXPIRE.equals(messageType)) {
                result.setExpiredConfigs(ruleDetails);
            } else if (RemindMessageTypeEnum.REMIND.equals(messageType)) {
                result.setIncomeConfigs(ruleDetails);
            } else if (RemindMessageTypeEnum.OVERDUE.equals(messageType)) {
                result.setOverdueConfigs(ruleDetails);
            }
        });
        return result;
    }

    /**
     * 消息推送
     */
    @Override
    public void send() {
        List<ContractRemindMessageConfigE> remindMessageConfigs = this.list(Wrappers.<ContractRemindMessageConfigE>lambdaQuery()
                .eq(ContractRemindMessageConfigE::getDeleted, DeletedEnum.ZERO));
        if (CollUtil.isEmpty(remindMessageConfigs)) {
            log.info("当前没有有效的的消息配置,不进行消息推送");
            return;
        }
        //开始进行数据聚合
        ContractAggregationV contractAggregationV = aggregation(remindMessageConfigs);
        //开始进行发送信息的聚合
        Map<String, ContractAndPlanInfoV> sendMap = new HashMap<>();
        assembleExpireSendMap(contractAggregationV, sendMap);
        assembleRemindSendMap(contractAggregationV, sendMap);
        assembleOverdueSendMap(contractAggregationV, sendMap);
        //从sendMap中将values信息转为合同编号+计划id为key的map
        Map<String, ContractAndPlanInfoV> planInfoMap = new HashMap<>();
        sendMap.forEach((key, planInfo) -> {
            String newKey = key.substring(key.indexOf("@") + 1);
            planInfoMap.put(newKey, planInfo);
        });
        //从sendMap中将keys信息转为合同编号+计划id为key，人员id为value的map
        Map<String, Set<String>> contractUserIdMap = sendMap.keySet().stream()
                .collect(Collectors.groupingBy(key -> key.substring(key.indexOf("@") + 1),
                        Collectors.mapping(key -> key.substring(0, key.indexOf("@")), Collectors.toSet())));
        //汇总全部的userId
        Set<String> allUserIds = sendMap.keySet().stream()
                .map(key -> key.substring(0, key.indexOf("@"))).collect(Collectors.toSet());
        //查询员工手机号
        List<UserMobileV> userMobileVS = queryUserPhones(allUserIds);
        //转为map
        Map<String, UserMobileV> userIdPhoneMap = userMobileVS.stream()
                .collect(Collectors.toMap(UserMobileV::getId, Function.identity(), (v1, v2) -> v1));
        //查员工三方id
        List<PhoneThirdPartyIdV> phoneThirdPartyIdVS = queryUserThirdPartyIds(userMobileVS);
        //转为map
        Map<String, PhoneThirdPartyIdV> phoneThirdPartyIdVMap = phoneThirdPartyIdVS.stream()
                .collect(Collectors.toMap(PhoneThirdPartyIdV::getPhone, Function.identity(), (v1, v2) -> v1));

        planInfoMap.forEach((key, planInfo) -> {
            //发PC消息
            Set<String> userIds = contractUserIdMap.get(key);
            contractRemindMessageSendService.sendPCNoticeMessage(userIds, planInfo);

            //userIds换phones
            List<String> phones = userIds.stream()
                    .filter(userIdPhoneMap::containsKey)
                    .map(id -> userIdPhoneMap.get(id).getMobileNum())
                    .collect(Collectors.toList());
            //phones换三方id
            Set<String> thirdPartyIds = phones.stream()
                    .filter(phoneThirdPartyIdVMap::containsKey)
                    .map(phone -> phoneThirdPartyIdVMap.get(phone).getThirdPartyId())
                    .collect(Collectors.toSet());
            //发中建通消息
            contractRemindMessageSendService.sendZJNoticeMessage(thirdPartyIds, planInfo);
        });
    }


    /**
     * 消息推送
     */
    @Override
    public void send(FirstExamineMessageF message, Boolean flag) {

        //查询员工手机号
        List<UserMobileV> userMobileVS = queryUserPhones(Set.of(message.getUserId()));

        //查员工三方id
        List<PhoneThirdPartyIdV> phoneThirdPartyIdVS = queryUserThirdPartyIds(userMobileVS);
        //发PC消息
        contractRemindMessageSendService.sendPCNoticeMessage(message.getUserId(), flag, message.getReason());

        if(CollectionUtils.isNotEmpty(phoneThirdPartyIdVS)){
            String thirdPartyId = phoneThirdPartyIdVS.get(0).getThirdPartyId();
            //发中建通消息
            contractRemindMessageSendService.sendZJNoticeMessage(thirdPartyId, flag, message.getReason());
        }
    }

    /**
     * 根据userId查询手机号
     *
     * @param userIds
     * @return
     */
    private List<UserMobileV> queryUserPhones(Set<String> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        //查用户手机号
        ListUserInfoF listUserInfoF = new ListUserInfoF();
        listUserInfoF.setUserIds(new ArrayList<>(userIds));
        listUserInfoF.setGatewaySymbol("saas");
        List<UserMobileV> userMobileVS;
        try {
            log.info("listUserInfoBy入参:{}", JSON.toJSONString(listUserInfoF));
            userMobileVS = userFeignClient.listUserInfoBy(listUserInfoF);
            log.info("listUserInfoBy出参:{}", JSON.toJSONString(userMobileVS));
        } catch (Exception e) {
            log.error("listUserInfoBy获取员工手机号失败,msg:{}", e.getMessage(), e);
            throw new BizException(500, "listUserInfoBy获取员工手机号失败", e);
        }
        return userMobileVS;
    }

    /**
     * 根据手机号查询员工三方id
     *
     * @param userMobileVS
     * @return
     */
    private List<PhoneThirdPartyIdV> queryUserThirdPartyIds(List<UserMobileV> userMobileVS) {
        if (CollUtil.isEmpty(userMobileVS)) {
            return new ArrayList<>();
        }
        //根据手机号查三方id
        List<String> phones = userMobileVS.stream().map(UserMobileV::getMobileNum).distinct().collect(Collectors.toList());
        PhoneParamF phoneParamF = new PhoneParamF();
        phoneParamF.setPhones(phones);
        List<PhoneThirdPartyIdV> infos;
        try {
            infos = externalFeignClient.getUserThirdPartyIdByPhone(phoneParamF);
        } catch (Exception e) {
            log.error("根据手机号查询用户三方id失败,msg:{}", e.getMessage(), e);
            throw new BizException(500, "根据手机号查询用户三方id失败", e);
        }
        return infos;
    }

    /**
     * 数据聚合函数
     *
     * @param remindMessageConfigs
     * @return
     */
    private ContractAggregationV aggregation(List<ContractRemindMessageConfigE> remindMessageConfigs) {
        ContractAggregationV contractAggregationV = new ContractAggregationV();
        //初始化contractAggregationV全部的集合，防止后续可能出现的空指针
        contractAggregationV.initData();
        //收集：满足"合同到期预警"配置的收入/支出合同数据
        collectExpireContract(remindMessageConfigs, contractAggregationV);
        //收集：满足"收款提醒"配置的合同与计划数据
        collectRemindContract(remindMessageConfigs, contractAggregationV);
        //收集：满足"收款逾期预警"配置的合同与计划数据
        collectOverdueContract(remindMessageConfigs, contractAggregationV);
        //收集：项目所属的组织信息
        collectCommunityOrg(contractAggregationV);
        //收集：员工组织和角色信息
        collectUserOrgRoles(contractAggregationV);
        //返回
        return contractAggregationV;
    }

    /**
     * 合同到期预警-发送消息聚合
     *
     * @param contractAggregationV
     * @param sendMap
     */
    private void assembleExpireSendMap(ContractAggregationV contractAggregationV, Map<String, ContractAndPlanInfoV> sendMap) {
        if (CollUtil.isEmpty(contractAggregationV.getExpireConfigs())) {
            return;
        }
        //将配置信息按照提醒天数为key进行分组遍历
        Map<Integer, List<ContractRemindMessageConfigE>> expireConfigMap = contractAggregationV.getExpireConfigs().stream()
                .collect(Collectors.groupingBy(ContractRemindMessageConfigE::getRemindDays));
        for (Integer days : expireConfigMap.keySet()) {
            //获取合同
            List<ContractAndPlanInfoV> planInfos = contractAggregationV.getExpireList().stream()
                    .filter(info -> info.getContractGmtExpireEnd().minusDays(days).equals(LocalDate.now()))
                    .collect(Collectors.toList());
            if (CollUtil.isEmpty(planInfos)) {
                continue;
            }

            List<ContractRemindMessageConfigE> configs = expireConfigMap.get(days);
            //经办人
            handler(configs, planInfos, sendMap, RemindMessageTypeEnum.EXPIRE);
            //人员
            person(configs, planInfos, sendMap, RemindMessageTypeEnum.EXPIRE);
            //角色
            role(contractAggregationV, configs, planInfos, sendMap, RemindMessageTypeEnum.EXPIRE);
        }
    }

    /**
     * 收款到期提醒-发送消息聚合
     *
     * @param contractAggregationV
     * @param sendMap
     */
    private void assembleRemindSendMap(ContractAggregationV contractAggregationV, Map<String, ContractAndPlanInfoV> sendMap) {
        if (CollUtil.isEmpty(contractAggregationV.getRemindConfigs())) {
            return;
        }
        Map<Integer, List<ContractRemindMessageConfigE>> expireConfigMap = contractAggregationV.getRemindConfigs().stream()
                .collect(Collectors.groupingBy(ContractRemindMessageConfigE::getRemindDays));

        for (Integer days : expireConfigMap.keySet()) {
            //获取合同
            List<ContractAndPlanInfoV> planInfos = contractAggregationV.getRemindList().stream()
                    .filter(info -> info.getPlannedCollectionTime().minusDays(days).equals(LocalDate.now()))
                    .collect(Collectors.toList());
            if (CollUtil.isEmpty(planInfos)) {
                continue;
            }

            List<ContractRemindMessageConfigE> configs = expireConfigMap.get(days);
            //经办人
            handler(configs, planInfos, sendMap, RemindMessageTypeEnum.REMIND);
            //人员
            person(configs, planInfos, sendMap, RemindMessageTypeEnum.REMIND);
            //角色
            role(contractAggregationV, configs, planInfos, sendMap, RemindMessageTypeEnum.REMIND);
        }
    }

    /**
     * 收款逾期预警-发送消息聚合
     *
     * @param contractAggregationV
     * @param sendMap
     */
    private void assembleOverdueSendMap(ContractAggregationV contractAggregationV, Map<String, ContractAndPlanInfoV> sendMap) {
        if (CollUtil.isEmpty(contractAggregationV.getOverdueConfigs())) {
            return;
        }
        Map<Integer, List<ContractRemindMessageConfigE>> expireConfigMap = contractAggregationV.getOverdueConfigs().stream()
                .collect(Collectors.groupingBy(ContractRemindMessageConfigE::getRemindDays));

        for (Integer days : expireConfigMap.keySet()) {
            //获取合同
            List<ContractAndPlanInfoV> planInfos = contractAggregationV.getOverdueList().stream()
                    .filter(info -> LocalDate.now().minusDays(days).equals(info.getPlannedCollectionTime()))
                    .collect(Collectors.toList());
            if (CollUtil.isEmpty(planInfos)) {
                continue;
            }

            List<ContractRemindMessageConfigE> configs = expireConfigMap.get(days);
            //经办人
            handler(configs, planInfos, sendMap, RemindMessageTypeEnum.OVERDUE);
            //人员
            person(configs, planInfos, sendMap, RemindMessageTypeEnum.OVERDUE);
            //角色
            role(contractAggregationV, configs, planInfos, sendMap, RemindMessageTypeEnum.OVERDUE);
        }
    }

    /**
     * 收集经办人的发送信息
     *
     * @param configs
     * @param planInfos
     * @param sendMap
     */
    private void handler(List<ContractRemindMessageConfigE> configs,
                         List<ContractAndPlanInfoV> planInfos,
                         Map<String, ContractAndPlanInfoV> sendMap,
                         RemindMessageTypeEnum messageTypeEnum) {
        List<ContractRemindMessageConfigE> handlerConfigs = configs.stream()
                .filter(config -> RemindTargetTypeEnum.HANDLER.equals(config.getTargetType()))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(handlerConfigs)) {
            return;
        }
        planInfos.forEach(planInfo -> sendMap.put(planInfo.getHandlerId() + "@"
                + planInfo.getContractId() + "@"
                + planInfo.getPlanId() + "@"
                + messageTypeEnum.name(), planInfo));
    }

    /**
     * 收集人员的发送信息
     *
     * @param configs
     * @param planInfos
     * @param sendMap
     */
    private void person(List<ContractRemindMessageConfigE> configs,
                        List<ContractAndPlanInfoV> planInfos,
                        Map<String, ContractAndPlanInfoV> sendMap,
                        RemindMessageTypeEnum messageTypeEnum) {
        Set<String> userIds = configs.stream()
                .filter(config -> RemindTargetTypeEnum.PERSON.equals(config.getTargetType())
                        && StrUtil.isNotBlank(config.getTargetInfo()))
                .map(config -> JSON.parseArray(config.getTargetInfo(), TargetInfoV.class))
                .flatMap(Collection::stream)
                .map(TargetInfoV::getTargetId)
                .collect(Collectors.toSet());
        if (CollUtil.isEmpty(userIds)) {
            return;
        }
        planInfos.forEach(planInfo -> userIds.forEach(userId -> sendMap.put(userId + "@"
                + planInfo.getContractId() + "@"
                + planInfo.getPlanId() + "@"
                + messageTypeEnum.name(), planInfo)));
    }

    /**
     * 收集角色的发送信息
     *
     * @param contractAggregationV
     * @param configs
     * @param planInfos
     * @param sendMap
     */
    private void role(ContractAggregationV contractAggregationV,
                      List<ContractRemindMessageConfigE> configs,
                      List<ContractAndPlanInfoV> planInfos,
                      Map<String, ContractAndPlanInfoV> sendMap,
                      RemindMessageTypeEnum messageTypeEnum) {
        Set<String> roleIds = configs.stream()
                .filter(config -> RemindTargetTypeEnum.ROLE.equals(config.getTargetType())
                        && StrUtil.isNotBlank(config.getTargetInfo()))
                .map(config -> JSON.parseArray(config.getTargetInfo(), TargetInfoV.class))
                .flatMap(Collection::stream)
                .map(TargetInfoV::getTargetId)
                .collect(Collectors.toSet());
        if (CollUtil.isEmpty(roleIds)) {
            return;
        }
        for (ContractAndPlanInfoV planInfo : planInfos) {
            //获取到该合同的所属项目关联的组织id
            List<String> orgIds = contractAggregationV.getCommunityOrgMap().get(planInfo.getCommunityId()).getOrgIds();
            for (UserOrgRoleV userOrgRole : contractAggregationV.getUserOrgRoles()) {
                //计算每个员工的角色范围是否与roleIds有交集
                List<String> retainRoles = roleIds.stream().filter(userOrgRole.getRoleIds()::contains).collect(Collectors.toList());
                //计算每个员工的组织范围是否与orgIds有交集
                List<String> retainOrgs = orgIds.stream().filter(userOrgRole.getOrgIds()::contains).collect(Collectors.toList());
                //若角色和组织均有交集
                if (CollUtil.isNotEmpty(retainRoles) && CollUtil.isNotEmpty(retainOrgs)) {
                    sendMap.put(userOrgRole.getUserId() + "@"
                            + planInfo.getContractId() + "@"
                            + planInfo.getPlanId() + "@"
                            + messageTypeEnum.name(), planInfo);
                }
            }
        }
    }

    /**
     * 合同到期预警-相关数据收集
     *
     * @param remindMessageConfigs
     * @param contractAggregationV
     */
    private void collectExpireContract(List<ContractRemindMessageConfigE> remindMessageConfigs, ContractAggregationV contractAggregationV) {
        //过滤得到"合同到期预警"配置
        List<ContractRemindMessageConfigE> expireConfigs = remindMessageConfigs.stream()
                .filter(config -> RemindMessageTypeEnum.EXPIRE.equals(config.getMessageType()))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(expireConfigs)) {
            log.info("不存在合同到期预警相关消息配置");
            return;
        }
        //记录"合同到期预警"配置
        contractAggregationV.setExpireConfigs(expireConfigs);
        //收集"合同到期预警"中配置的提醒天数列表
        List<Integer> expireDays = expireConfigs.stream()
                .map(ContractRemindMessageConfigE::getRemindDays)
                .distinct()
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(expireDays)) {
            log.info("合同到期预警相关消息配置中,没有提醒天数相关配置");
            return;
        }

        //查询满足"合同到期预警"配置的收入/支出合同，并汇总
        List<ContractAndPlanInfoV> infos = Stream.of(
                Optional.ofNullable(contractIncomeConcludeMapper.selectIncomeExpireContract(expireDays)).orElseGet(ArrayList::new),
                Optional.ofNullable(contractPayConcludeMapper.selectPayExpireContract(expireDays)).orElseGet(ArrayList::new)
        ).flatMap(Collection::stream).collect(Collectors.toList());
        if (CollUtil.isEmpty(infos)) {
            log.info("没有满足合同到期预警天数的收入和支出合同");
            return;
        }
        //记录合同信息
        contractAggregationV.setExpireList(infos);
        //记录角色id和项目id
        collectRoleIdsAndCommunityIds(expireConfigs, infos, contractAggregationV, RemindMessageTypeEnum.EXPIRE);
    }

    /**
     * 收款提醒-相关数据收集
     *
     * @param remindMessageConfigs
     * @param contractAggregationV
     */
    private void collectRemindContract(List<ContractRemindMessageConfigE> remindMessageConfigs, ContractAggregationV contractAggregationV) {
        //过滤得到"收款提醒"配置信息
        List<ContractRemindMessageConfigE> remindConfigs = remindMessageConfigs.stream()
                .filter(config -> RemindMessageTypeEnum.REMIND.equals(config.getMessageType()))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(remindConfigs)) {
            log.info("不存在收款提醒相关消息配置");
            return;
        }
        //记录"收款提醒"配置信息
        contractAggregationV.setRemindConfigs(remindConfigs);
        //收集收款提醒配置的提醒天数
        List<Integer> remindDays = remindConfigs.stream()
                .map(ContractRemindMessageConfigE::getRemindDays)
                .distinct()
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(remindDays)) {
            log.info("收款提醒相关消息配置中,没有提醒天数相关配置");
            return;
        }
        List<ContractAndPlanInfoV> infos = contractIncomePlanConcludeMapper.selectRemindContractPlan(remindDays);
        contractAggregationV.setRemindList(CollUtil.isEmpty(infos) ? new ArrayList<>() : infos);
        //记录角色id和项目id
        collectRoleIdsAndCommunityIds(remindConfigs, infos, contractAggregationV, RemindMessageTypeEnum.REMIND);
    }

    private void collectOverdueContract(List<ContractRemindMessageConfigE> remindMessageConfigs, ContractAggregationV contractAggregationV) {
        //过滤得到"收款逾期"的配置
        List<ContractRemindMessageConfigE> overdueConfigs = remindMessageConfigs.stream()
                .filter(config -> RemindMessageTypeEnum.OVERDUE.equals(config.getMessageType()))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(overdueConfigs)) {
            log.info("不存在收款逾期预警相关消息配置");
            return;
        }
        //记录收款逾期预警相关配置
        contractAggregationV.setOverdueConfigs(overdueConfigs);
        //收集收款逾期预警中配置的天数
        List<Integer> overdueDays = overdueConfigs.stream()
                .map(ContractRemindMessageConfigE::getRemindDays)
                .distinct()
                .collect(Collectors.toList());
        List<ContractAndPlanInfoV> infos = contractIncomePlanConcludeMapper.selectOverdueContractPlan(overdueDays);
        contractAggregationV.setOverdueList(CollUtil.isEmpty(infos) ? new ArrayList<>() : infos);
        //记录角色id和项目id
        collectRoleIdsAndCommunityIds(overdueConfigs, infos, contractAggregationV, RemindMessageTypeEnum.OVERDUE);
    }

    /**
     * 项目所属的组织信息
     *
     * @param contractAggregationV
     */
    private void collectCommunityOrg(ContractAggregationV contractAggregationV) {
        if (CollUtil.isEmpty(contractAggregationV.getCommunityIds())) {
            log.info("聚合数据中所属项目的列表为空");
            return;
        }
        List<CommunityOrgV> communityOrgs;
        try {
            CommunityIdF communityIdF = new CommunityIdF(new ArrayList<>(contractAggregationV.getCommunityIds()));
            log.info("getOrgIdsByCommunityIds入参:{}", JSON.toJSONString(communityIdF));
            communityOrgs = spaceFeignClient.getOrgIdsByCommunityIds(communityIdF);
            log.info("getOrgIdsByCommunityIds出参:{}", JSON.toJSONString(communityOrgs));
        } catch (Exception e) {
            log.error("getOrgIdsByCommunityIds查询组织失败,msg:{}", e.getMessage(), e);
            throw new BizException(500, "getOrgIdsByCommunityIds查询组织失败", e);
        }
        if (CollUtil.isEmpty(communityOrgs)) {
            log.info("查询到组织信息为空");
            contractAggregationV.setOrgIds(new HashSet<>());
            contractAggregationV.setCommunityOrgMap(new HashMap<>());
            return;
        }
        Set<String> orgIds = communityOrgs.stream()
                .map(CommunityOrgV::getOrgIds)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        contractAggregationV.setOrgIds(orgIds);
        Map<String, CommunityOrgV> communityOrgMap = communityOrgs.stream()
                .collect(Collectors.toMap(CommunityOrgV::getCommunityId, Function.identity(), (v1, v2) -> v1));
        contractAggregationV.setCommunityOrgMap(communityOrgMap);
    }

    /**
     * 员工组织和角色信息
     *
     * @param contractAggregationV
     */
    private void collectUserOrgRoles(ContractAggregationV contractAggregationV) {
        if (CollUtil.isEmpty(contractAggregationV.getRoleIds()) || CollUtil.isEmpty(contractAggregationV.getOrgIds())) {
            log.info("聚合数据中角色id或组织id为空");
            return;
        }
        List<UserOrgRoleV> userOrgRoles;
        try {
            UserOrgRoleF userOrgRoleF = new UserOrgRoleF(contractAggregationV.getRoleIds(), contractAggregationV.getOrgIds());
            log.info("getUserOrgRole入参:{}", JSON.toJSONString(userOrgRoleF));
            userOrgRoles = userFeignClient.getUserOrgRole(userOrgRoleF);
            log.info("getUserOrgRole出参:{}", JSON.toJSONString(userOrgRoles));
        } catch (Exception e) {
            log.error("getUserOrgRole查询组织和角色信息失败,msg:{}", e.getMessage(), e);
            throw new BizException(500, "getUserOrgRole查询组织和角色信息失败", e);
        }
        if (CollUtil.isEmpty(userOrgRoles)) {
            log.info("查询得到的组织和角色信息为空");
            contractAggregationV.setUserOrgRoles(new ArrayList<>());
        }
        contractAggregationV.setUserOrgRoles(userOrgRoles);
    }

    /**
     * 收集配置的角色信息以及合同的所属项目信息
     *
     * @param configs
     * @param infos
     * @param contractAggregationV
     * @param messageTypeEnum
     */
    private void collectRoleIdsAndCommunityIds(List<ContractRemindMessageConfigE> configs,
                                               List<ContractAndPlanInfoV> infos,
                                               ContractAggregationV contractAggregationV,
                                               RemindMessageTypeEnum messageTypeEnum) {
        //再次过滤出"角色目标"的配置
        List<ContractRemindMessageConfigE> roleConfigs = configs.stream()
                .filter(config -> RemindTargetTypeEnum.ROLE.equals(config.getTargetType())
                        && StrUtil.isNotBlank(config.getTargetInfo()))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(roleConfigs)) {
            log.info("{}-不存在有效的角色类配置", messageTypeEnum.getDesc());
            return;
        }
        //获取到对应的提醒天数列表
        List<Integer> roleDays = roleConfigs.stream()
                .map(ContractRemindMessageConfigE::getRemindDays)
                .distinct()
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(roleDays)) {
            log.info("{}-角色类配置不存在天数配置", messageTypeEnum.getDesc());
            return;
        }

        //解析配置的角色id集合，设置到contractAggregationV中
        Set<String> roleIds = roleConfigs.stream()
                .map(config -> JSON.parseArray(config.getTargetInfo(), TargetInfoV.class))
                .flatMap(Collection::stream)
                .map(TargetInfoV::getTargetId)
                .collect(Collectors.toSet());
        if (CollUtil.isEmpty(contractAggregationV.getRoleIds())) {
            contractAggregationV.setRoleIds(new HashSet<>());
        }
        contractAggregationV.getRoleIds().addAll(roleIds);
        //遍历合同，若合同满足天数条件，记录合同的所属项目
        for (ContractAndPlanInfoV info : infos) {
            //根据messageTypeEnum计算实际的天数
            int days = 0;
            if (RemindMessageTypeEnum.EXPIRE.equals(messageTypeEnum)) {
                days = Math.toIntExact(ChronoUnit.DAYS.between(LocalDate.now(), info.getContractGmtExpireEnd()));
            } else if (RemindMessageTypeEnum.REMIND.equals(messageTypeEnum)) {
                days = Math.toIntExact(ChronoUnit.DAYS.between(LocalDate.now(), info.getPlannedCollectionTime()));
            } else if (RemindMessageTypeEnum.OVERDUE.equals(messageTypeEnum)) {
                days = Math.toIntExact(ChronoUnit.DAYS.between(info.getPlannedCollectionTime(), LocalDate.now()));
            }
            //若角色类提醒天数不包含该合同实际计算的天数，说明该合同不符合角色类配置的通知
            if (!roleDays.contains(days)) {
                continue;
            }
            //若包含，说明该合同符合角色类配置的通知，记录该合同的所属项目
            if (CollUtil.isEmpty(contractAggregationV.getCommunityIds())) {
                contractAggregationV.setCommunityIds(new HashSet<>());
            }
            contractAggregationV.getCommunityIds().add(info.getCommunityId());
        }
    }

    /**
     * 组装ContractRemindMessageConfigE实体
     *
     * @param messageTypeEnum
     * @param details
     * @return
     */
    private List<ContractRemindMessageConfigE> convertRemindMessageConfigs(RemindMessageTypeEnum messageTypeEnum, List<RemindRuleDetailF> details) {
        if (CollUtil.isEmpty(details)) {
            return new ArrayList<>();
        }
        if (details.size() > 10) {
            throw BizException.throw400("每类预警规则最多配置10条");
        }
        return details.stream().filter(this::checkFilter).map(detail -> {
            ContractRemindMessageConfigE messageConfigE = new ContractRemindMessageConfigE();
            messageConfigE.setId(UidHelper.nextId("contract_remind_message_config"));
            messageConfigE.setRemindDays(detail.getRemindDays());
            messageConfigE.setMessageType(messageTypeEnum);
            messageConfigE.setTargetType(detail.getTargetType());
            messageConfigE.setTargetInfo(RemindTargetTypeEnum.HANDLER.equals(detail.getTargetType()) ? null
                    : JSON.toJSONString(detail.getTargetInfos()));
            messageConfigE.setCreator(this.userId());
            messageConfigE.setCreatorName(this.userName());
            messageConfigE.setOperator(userId());
            messageConfigE.setOperatorName(this.userName());
            return messageConfigE;
        }).collect(Collectors.toList());
    }

    /**
     * 数据校验过滤
     *
     * @param detail
     * @return
     */
    private boolean checkFilter(RemindRuleDetailF detail) {
        if (Objects.isNull(detail)) {
            return false;
        }
        if (Objects.isNull(detail.getTargetType())) {
            return false;
        }
        if (detail.getRemindDays() < 0 || detail.getRemindDays() > 999) {
            return false;
        }
        if ((RemindTargetTypeEnum.PERSON.equals(detail.getTargetType())
                || RemindTargetTypeEnum.ROLE.equals(detail.getTargetType()))
                && CollUtil.isEmpty(detail.getTargetInfos())) {
            return false;
        }
        return true;
    }

}