package com.wishare.finance.infrastructure.remote.fo.xxljob;

/**
 * 更新执行器
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/5
 */
public class UpdateXxlJobGroupRf {

    /**
     * 执行器id
     */
    private Long id;

    /**
     * 应用名称（服务名）
     */
    private String appname;
    /**
     * 执行器名称
     */
    private String title;
    /**
     * 注册方式 0： 自动注册， 1手动录入
     */
    private String addressType;
    /**
     * 机器地址
     */
    private String addressList;

    public String getAppname() {
        return appname;
    }

    public UpdateXxlJobGroupRf setAppname(String appname) {
        this.appname = appname;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public UpdateXxlJobGroupRf setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAddressType() {
        return addressType;
    }

    public UpdateXxlJobGroupRf setAddressType(String addressType) {
        this.addressType = addressType;
        return this;
    }

    public String getAddressList() {
        return addressList;
    }

    public UpdateXxlJobGroupRf setAddressList(String addressList) {
        this.addressList = addressList;
        return this;
    }
}
