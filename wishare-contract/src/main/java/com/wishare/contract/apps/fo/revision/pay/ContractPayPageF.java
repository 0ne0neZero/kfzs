package com.wishare.contract.apps.fo.revision.pay;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
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
import java.time.LocalDateTime;
import java.util.List;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/6  11:05
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同信息表PayPageF", description = "支出合同信息表PayPageF")
public class ContractPayPageF {

    /**
     * name
     */
    @ApiModelProperty("合同名称")
    @Length(message = "合同名称不可超过 128 个字符",max = 128)
    private String name;
    /**
     * chooseType
     */
    @ApiModelProperty("选择操作业务类型")
    private Integer chooseType;


}
