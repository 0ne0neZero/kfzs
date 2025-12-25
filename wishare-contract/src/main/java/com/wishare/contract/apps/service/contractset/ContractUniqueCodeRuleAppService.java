package com.wishare.contract.apps.service.contractset;

import com.wishare.contract.apps.remote.clients.ConfigFeignClient;
import com.wishare.contract.apps.remote.vo.config.DictionaryCode;
import com.wishare.contract.domains.enums.revision.ContractAreaEnum;
import com.wishare.contract.domains.enums.revision.ContractIncomeManageTypeEnum;
import com.wishare.contract.domains.enums.revision.ContractPayManageTypeEnum;
import com.wishare.contract.domains.enums.revision.DictionaryCodeEnum;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeMapper;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeMapper;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeV;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2024/4/28/11:07
 */
@Service
@Slf4j
public class ContractUniqueCodeRuleAppService {

    private static final String UNIT_NAME = "ZJFW-";

    private static final String AREA_NAME = "总部";

    private static final String PROJECT_NAME = "中交服务";

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ConfigFeignClient configFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeMapper contractPayConcludeMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeConcludeMapper contractIncomeConcludeMapper;

    public String genUniqueCode(Integer isBackDate, String departName, String regionName, String conmanagetype, String communityName, Integer type, String conperformprovincesname){
        LocalDateTime localDateTime =  LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        String strTime = localDateTime.format(formatter);
        String codeSum = "";
        String conmanage = "";
        String count = "";

        if(type == 1){
            conmanage = getPinYinHeadChar(ContractIncomeManageTypeEnum.parseName(Integer.parseInt(conmanagetype))).substring(0,2);
        }else{
            conmanage = getPinYinHeadChar(ContractPayManageTypeEnum.parseName(Integer.parseInt(conmanagetype))).substring(0,2);
        }

        //总部拼装规则
        if(ContractAreaEnum.总部.getCode().equals(isBackDate)){
            String depart = getPinYinHeadChar(departName).substring(0,2);

            if(type == 1){
                List<ContractIncomeConcludeV> s = contractIncomeConcludeMapper.queryContractCount(1,strTime,null);
                if(ObjectUtils.isNotEmpty(s)){
                    int b = s.size() + 1;
                    if(b < 10){
                        count = count + "00" + b;
                    }
                    if(b >= 10 && b < 99){
                        count = count + "0" + b;
                    }
                    if(b > 99){
                        count = count +  b;
                    }
                }else{
                    count = "001";
                }
            }else{
                List<ContractPayConcludeV> s = contractPayConcludeMapper.queryContractCount(1,strTime,null);
                if(ObjectUtils.isNotEmpty(s)){
                    int b = s.size() + 1;
                    if(b < 10){
                        count = count + "00" + b;
                    }
                    if(b >= 10 && b < 99){
                        count = count + "0" + b;
                    }
                    if(b > 99){
                        count = count +  b;
                    }
                }else{
                    count = "001";
                }
            }

            codeSum =   UNIT_NAME + depart + "-" + conmanage + "-" + strTime + "-" + count;
        }

        //事业部，区域公司
        if(!ContractAreaEnum.总部.getCode().equals(isBackDate) && communityName.contains(AREA_NAME)){
            String region = getPinYinHeadChar(regionName).substring(0,4);

            if(type == 1){
                List<ContractIncomeConcludeV> s = contractIncomeConcludeMapper.queryContractCount(2,strTime,1);
                if(ObjectUtils.isNotEmpty(s)){
                    int b = s.size() + 1;
                    if(b < 10){
                        count = count + "00" + b;
                    }
                    if(b >= 10 && b < 99){
                        count = count + "0" + b;
                    }
                    if(b > 99){
                        count = count +  b;
                    }
                }else{
                    count = "001";
                }
            }else{
                List<ContractPayConcludeV> s = contractPayConcludeMapper.queryContractCount(2,strTime,1);
                if(ObjectUtils.isNotEmpty(s)){
                    int b = s.size() + 1;
                    if(b < 10){
                        count = count + "00" + b;
                    }
                    if(b >= 10 && b < 99){
                        count = count + "0" + b;
                    }
                    if(b > 99){
                        count = count +  b;
                    }
                }else{
                    count = "001";
                }
            }

            codeSum =   UNIT_NAME + region + "-" + conmanage + "-" + strTime + "-" + count;
        }

        //事业部，区域公司所属项目
        if(!ContractAreaEnum.总部.getCode().equals(isBackDate) && !communityName.contains(AREA_NAME)){
            String region = getPinYinHeadChar(regionName).substring(0,4);
            String community = "";
            String conperformprovinces = "";
            //-- 币种
            if (StringUtils.isNotBlank(conperformprovincesname)) {
                List<DictionaryCode> value = configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.中交地区映射.getCode(), conperformprovincesname);
                if (CollectionUtils.isNotEmpty(value)) {
                    conperformprovinces = value.get(0).getName();
                }
            }

            if(communityName.contains(PROJECT_NAME)){
                community = getPinYinHeadChar(communityName).substring(4,8);
            }else {
                community = getPinYinHeadChar(communityName).substring(0,4);
            }

            if(type == 1){
                List<ContractIncomeConcludeV> s = contractIncomeConcludeMapper.queryContractCount(2,strTime,2);
                if(ObjectUtils.isNotEmpty(s)){
                    int b = s.size() + 1;
                    if(b < 10){
                        count = count + "00" + b;
                    }
                    if(b >= 10 && b < 99){
                        count = count + "0" + b;
                    }
                    if(b > 99){
                        count = count +  b;
                    }
                }else{
                    count = "001";
                }
            }else{
                List<ContractPayConcludeV> s = contractPayConcludeMapper.queryContractCount(2,strTime,2);
                if(ObjectUtils.isNotEmpty(s)){
                    int b = s.size() + 1;
                    if(b < 10){
                        count = count + "00" + b;
                    }
                    if(b >= 10 && b < 99){
                        count = count + "0" + b;
                    }
                    if(b > 99){
                        count = count +  b;
                    }
                }else{
                    count = "001";
                }
            }

            codeSum =   UNIT_NAME + region + "-" + conperformprovinces + "-" + community + "-" + conmanage  + "-" + count;
        }


        return codeSum;
    }


    public static void main(String[] args) {
        String community = "天津中交雅郡润园";
        if(community.contains(PROJECT_NAME)){
            community = getPinYinHeadChar(community).substring(4,8);
        }else {
            community = getPinYinHeadChar(community).substring(0,4);
        }

        System.out.println(community);
    }


    public static String getPinYinHeadChar(String str) {
        if (str == null || str.trim().equals("")) {
            return "";
        }
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            // 提取字符的首字母
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert.toUpperCase();
    }
}
