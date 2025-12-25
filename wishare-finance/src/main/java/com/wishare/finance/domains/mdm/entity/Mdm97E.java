package com.wishare.finance.domains.mdm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author longhuadmin
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("mdm97")
public class Mdm97E {

    private String id;

    /**
     * 银行账户ID
     **/
    private String parentId;

    /**
     * 授权单位ID
     **/
    private String authorizedUnit;

    /**
     * 状态	1,未启用;2,启用;3,停用
     **/
    private String startedStatus;

    /**
     * 最后更新时间
     **/
    private Date lastModifiedTime;

    private String tenantId;
}
