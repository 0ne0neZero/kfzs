package com.wishare.finance.apps.model.voucher.fo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class CloseAccountF {


    @ApiModelProperty(value = "账簿id")
    @NotNull(message = "账簿id不能为空")
    private Long accountBookId;

    @ApiModelProperty(value = "账簿编码")
    private String accountBookCode;

    @ApiModelProperty(value = "账簿名称")
    private String accountBookName;

    @ApiModelProperty(value = "会计期间")
    @NotNull(message = "会计期间不能为空")
    private String accountingPeriod;

    @ApiModelProperty(value = "关账状态：0，未关账， 1，已关账")
    private Integer states;

    @ApiModelProperty(value = "原交账记录id")
    private Long rawId;

    @ApiModelProperty(value = "操作：0，反关账， 1，关账")
    private Integer operateStates;



}
