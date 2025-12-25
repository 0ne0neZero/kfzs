package com.wishare.finance.infrastructure.utils;


import com.wishare.finance.domains.bill.aggregate.data.EnvData;
import com.wishare.finance.infrastructure.configs.TenantConfigProperties;
import com.wishare.finance.infrastructure.conts.EnvConstEnum;
import com.wishare.starter.Global;
import org.apache.commons.lang3.StringUtils;


/**
 * 租户
 *
 * @see com.wishare.finance.infrastructure.conts.EnvConstEnum
 */
public class TenantUtil {


    /**
     * 获取当前的tag
     * @return
     */
    static public String curTag(){
        String tag = "";
        if (Global.ac.getEnvironment().getProperty("spring.profiles.active").equals("dev")) {
            tag = Global.ac.getBean(TenantConfigProperties.class).getNameTest();
        }
        if (StringUtils.isEmpty(tag)) {
            tag = EnvData.config;
        }
        return tag;
    }


    /**
     * 获取当前服务的租户租户tag beanName
     * 稳定运行的线上服务可以切换为调用
     *
     * @return
     */
    static public String curTagDevReceiptBeanName() {
        String receiptBeanName = null;
        //服务于研发环境(用来模拟其他环境)给予前端方便开发
        if (Global.ac.getEnvironment().getProperty("spring.profiles.active").equals("dev")) {
            receiptBeanName = EnvConstEnum.getReceiptBeanNameByTag(Global.ac.getBean(TenantConfigProperties.class).getNameTest());
        }
        if (StringUtils.isEmpty(receiptBeanName)) {
            receiptBeanName = curTagReceiptBeanName();
        }
        return receiptBeanName;
    }

    /**
     * 正常线上跑的接口可以切换当前方法
     *
     * @return
     */
    static public String curTagReceiptBeanName() {
        return EnvConstEnum.getReceiptBeanNameByTag(EnvData.config);

    }


    /**
     * 获取当前服务的租户租户tag beanName
     * 稳定运行的线上服务可以切换为调用
     *
     * @return
     * @see TenantUtil#curTagPosition()
     */
    static public int curTagDevPosition() {
        int position = 0;
        //服务于研发环境(用来模拟其他环境)给予前端方便开发
        if (Global.ac.getEnvironment().getProperty("spring.profiles.active").equals("dev")) {
            position = EnvConstEnum.getPosition(Global.ac.getBean(TenantConfigProperties.class).getNameTest());
        }
        if (position == 0) {
            position = curTagPosition();
        }
        return position;
    }

    /**
     * 正常线上跑的接口可以切换当前方法
     *
     * @return
     */
    static public int curTagPosition() {
        return EnvConstEnum.getPosition(EnvData.config);
    }


    //当前
    static public int curBitDev() {
        return (1 << curTagDevPosition());
    }

    //当前
    static public int curBit() {
        return (1 << curTagPosition());
    }

    //中交10
    static public int bit2() {
        return (1 << EnvConstEnum.中交.getPosition());
    }

    //远洋
    static public int bit4() {
        return (1 << EnvConstEnum.远洋.getPosition());
    }

    //方圆 1000
    static public int bit8() {
        return (1 << EnvConstEnum.方圆.getPosition());
    }

    //慧享云 10000
    static public int bit16() {
        return (1 << EnvConstEnum.慧享云.getPosition());
    }

    //临港
    static public int bit32() {
        return (1 << EnvConstEnum.临港.getPosition());
    }

    //拈花湾
    static public int bit64() {
        return (1 << EnvConstEnum.拈花湾.getPosition());
    }



    /** 多环境站位 数 start */





    //中交、慧享云
    static public int bit18() {
        return bit2() + bit16();
    }

    /**
     * 方圆、慧享云
     * @return
     */
    public static int bit24() {
        return bit8() + bit16();
    }

    /**
     * 方圆、中交
     * @return
     */
    public static int bit10() {
        return bit8() + bit2();
    }

    //中交、慧享云、临港
    static public int bit50() {
        return bit2() + bit16() + bit32();
    }


    //远洋、慧享云
    static public int bit20() {
        return bit4() + bit16();
    }

    //远洋、慧享云、临港
    static public int bit52() {
        return bit4() + bit16() +bit32();
    }





    /** 多环境站位 数 end */

    /**
     * belong flag 远洋、慧享云、临港
     *
     * @return true 当前环境命中
     */
    static public boolean bf52() {
        return (bit52() & curBitDev()) != 0;
    }


    /**
     * belong flag 中交、慧享云、临港
     *
     * @return true 当前环境命中
     */
    static public boolean bf50() {
        return (bit50() & curBitDev()) != 0;
    }



    /**
     * belong flag 远洋、慧享云
     *
     * @return true 当前环境命中
     */
    static public boolean bf20() {
        return (bit20() & curBitDev()) != 0;
    }


    //临港、远洋
    static public int bit36() {
        return bit32() + bit4();
    }

    //拈花湾、慧享云、远洋
    static public int bit84() {
        return bit64() + bit16() + bit4();
    }










    /**
     * belong flag 中交、慧享云
     *
     * @return true 当前环境命中
     */
    static public boolean bf18() {
        return (bit18() & curBitDev()) != 0;
    }

    /**
     * belong flag 方圆、慧享云
     *
     * @return true 当前环境命中
     */
    static public boolean bf24() {
        return (bit24() & curBitDev()) != 0;
    }

    /**
     * belong flag 方圆、中交
     *
     * @return true 当前环境命中
     */
    static public boolean bf10() {
        return (bit10() & curBitDev()) != 0;
    }

    /**
     * belong flag 慧享云
     *
     * @return true 当前环境命中
     */
    static public boolean bf16() {
        return (bit16() & curBitDev()) != 0;
    }

    /**
     * 中交
     * @return
     */
    static public boolean bf2() {
        return (bit2() & curBitDev()) != 0;
    }

    /**
     * 远洋
     * @return
     */
    static public boolean bf4() {
        return (bit4() & curBitDev()) != 0;
    }

    /**
     * 方圆
     * @return
     */
    static public boolean bf8() {
        return (bit8() & curBitDev()) != 0;
    }

    /**
     * 临港+远洋
     * @return
     */
    static public boolean bf36() {
        return (bit36() & curBitDev()) != 0;
    }

    /**
     * 拈花湾
     * @return
     */
    static public boolean bf64() {
        return (bit64() & curBitDev()) != 0;
    }

    /**
     * 拈花湾64、慧享云16、远洋4
     * @return
     */
    static public boolean bf84() {
        return (bit84() & curBitDev()) != 0;
    }


}
