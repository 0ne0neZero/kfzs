package com.wishare.finance.domains.mdm.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * @author longhuadmin
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = "mdm63_lock", autoResultMap = true)
public class Mdm63LockE {

    /**
     * 应收应付明细ID
     **/
    @TableId
    private Long id;

    /**
     * id
     **/
    private String ftId;

    /**
     * voucherBillId
     **/
    private Long voucherBillId;

    /**
     * voucherBillNo
     **/
    private String voucherBillNo;

    /**
     * 删除标识
     **/
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
}
