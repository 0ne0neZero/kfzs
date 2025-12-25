package com.wishare.contract.domains.vo.revision.income;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.contract.apps.fo.revision.income.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/6/28  10:45
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同订立信息表视图对象detailV", description = "收入合同订立信息表视图对象detailV")
public class ContractIncomeConcludeDetailFjxxV {

    /**
     * fjxx
     */
    @ApiModelProperty("(附件信息)")
    private List<ContractFjxxF> records;

    private int pageNum;

    private int pageSize;

    private long total;


}
