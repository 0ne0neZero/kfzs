package com.wishare.finance.infrastructure.conts;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @see com.wishare.finance.infrastructure.conts.EnvConst
 * @see com.wishare.finance.infrastructure.conts.EnvConstEnum
 * {@link com.wishare.finance.infrastructure.utils.TenantUtil}
 */
@Getter
@AllArgsConstructor
public enum EnvConstEnum {
    // 未来增加额外环境 比如
//    demo("demo","新环境","xxx",5),
    拈花湾(EnvConst.NIANHUAWAN,"拈花湾","receiptTenantNHW",6),//64

    临港(EnvConst.LINGANG,"临港","receiptTenantLG",5),//32

    慧享云(EnvConst.HUIXIANGYUN,"慧享云","receiptTenantHXY",4),//16

    方圆(EnvConst.FANGYUAN,"方圆","receiptTenantFY",3),//8

    远洋(EnvConst.YUANYANG,"远洋","receiptTenantYY",2),//4

    中交(EnvConst.ZHONGJIAO,"中交","receiptTenantZJ",1),//2

    未匹配到的环境("unKnown","查不到环境","receiptTenantUnknown",0) //1

    ;

    /** 环境-标签 */
    private final String tag;

    /** 租户-名称 */
    private final String tenantName;

    /** beanName-receipt  */
    private final String receiptBeanName;


    /** bitset位置 */
    private final int position;






    /**
     * 如果未查询到环境则返回null
     * @param tag
     * @return
     */
    public static String getReceiptBeanNameByTag(String tag){
        if(StringUtils.isEmpty(tag)){
            return null;
        }
        for (EnvConstEnum value : values()) {
            if(StringUtils.equalsIgnoreCase(value.tag,tag)){
                return value.receiptBeanName;
            }
        }
        return null;
    }


    /**
     * 如果未查询到环境则返回null
     * @param tag
     * @return
     */
    public static int getPosition(String tag){
        if(StringUtils.isEmpty(tag)){
            return EnvConstEnum.未匹配到的环境.position;
        }
        for (EnvConstEnum value : values()) {
            if(StringUtils.equals(value.tag,tag)){
                return value.position;
            }
        }
        return EnvConstEnum.未匹配到的环境.position;
    }





}
