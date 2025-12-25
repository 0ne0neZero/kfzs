package com.wishare.finance.apps.service.remind;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import com.wishare.finance.apps.model.reconciliation.fo.ListUserInfoF;
import com.wishare.finance.apps.model.reconciliation.fo.PhoneParamF;
import com.wishare.finance.apps.model.reconciliation.vo.PhoneThirdPartyIdV;
import com.wishare.finance.apps.model.reconciliation.vo.UserMobileV;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.PaymentApplicationFormZJ;
import com.wishare.finance.infrastructure.remote.clients.base.AuthClient;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.clients.base.UserClient;
import com.wishare.finance.infrastructure.remote.fo.space.CommunityIdF;
import com.wishare.finance.infrastructure.remote.vo.contract.FirstExamineMessageF;
import com.wishare.finance.infrastructure.remote.vo.space.CommunityOrgV;
import com.wishare.finance.infrastructure.remote.vo.space.UserInfoRawV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RemindMessageConfigServiceImpl
        implements RemindMessageConfigService, IOwlApiBase {

    @Autowired
    private UserClient userFeignClient;

    @Autowired
    private ExternalClient externalFeignClient;

    @Autowired
    private RemindMessageSendService remindMessageSendService;
    
    @Autowired
    private SpaceClient spaceClient;

    @Autowired
    private UserClient userClient;

    @Autowired
    private AuthClient authClient;

    @Value("${finance.remind.role:13656324198601}")
    private String roleId;



    /**
     * 消息推送
     */
    @Override
    public void send(FirstExamineMessageF message, Boolean flag, PaymentApplicationFormZJ paymentApplicationFormZJ) {
        try {
            List<String> userIds = getUserIds(paymentApplicationFormZJ,roleId);

            //查询员工手机号
            List<UserMobileV> userMobileVS = queryUserPhones(userIds.stream().collect(Collectors.toSet()));

            //查员工三方id
            List<PhoneThirdPartyIdV> phoneThirdPartyIdVS = queryUserThirdPartyIds(userMobileVS);
            //发PC消息
            remindMessageSendService.sendPCNoticeMessage(userIds, flag, message.getReason());

            if(CollectionUtils.isNotEmpty(phoneThirdPartyIdVS)){
                List<String> thirdPartyIds = phoneThirdPartyIdVS.stream().map(PhoneThirdPartyIdV::getThirdPartyId).collect(Collectors.toList());
                //发中建通消息
                remindMessageSendService.sendZJNoticeMessage(thirdPartyIds, flag, message.getReason(), paymentApplicationFormZJ);
            }
        } catch (Exception e) {
            log.error("消息推送失败,msg:{}", e.getMessage(), e);
        }
    }

    public List<String> getUserIds(PaymentApplicationFormZJ paymentApplicationFormZJ, String roleId) {
        //根据项目查询对应组织
        String communityId = paymentApplicationFormZJ.getCommunityId();
        CommunityIdF communityIdF = new CommunityIdF();
        communityIdF.setCommunityIds(Arrays.asList(communityId));
        List<CommunityOrgV> orgIdsByCommunityIds = spaceClient.getOrgIdsByCommunityIds(communityIdF);
        if(CollectionUtils.isEmpty(orgIdsByCommunityIds)){
            return Collections.emptyList();
        }
        List<String> orgIds = orgIdsByCommunityIds.get(0).getOrgIds();

        //根据组织和角色判断是否存在对应角色
        if(CollectionUtils.isEmpty(orgIds) || !authClient.getByRoleAndOrgs(orgIds, roleId)){
            return Collections.emptyList();
        }

        //根据角色查询对应员工
        List<UserInfoRawV> userInfoRawVS = userClient.listUserInfoByTenantIdAndRoleId(paymentApplicationFormZJ.getTenantId(), roleId, null, 100, null);
        if(CollectionUtils.isEmpty(userInfoRawVS)){
            return Collections.emptyList();
        }
        return userInfoRawVS.stream().map(UserInfoRawV::getId).collect(Collectors.toList());
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

}