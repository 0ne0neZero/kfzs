package com.wishare.contract.apps.remote.vo.org;

import lombok.Data;

/**
 * 租户详细信息
 *
 * @author yancao
 */
@Data
public class TenantInfoRv {

    /**
     * 租户id
     */
    private String id;

    /**
     * 英文客户简称
     */
    private String englishName;

    /**
     * 租户名称
     */
    private String name;
}
