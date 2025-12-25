package com.wishare.finance.apps.scheduler.mdm.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/6/12/10:44
 */

@Data
@ApiModel(value = "数据字典查询")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DictionaryF<T> {

    @ApiModelProperty(value = "实例编号 由财务云系统分配，内容同X-ECC-Current-Tenant")
    String appInstanceCode;
    @ApiModelProperty(value = "核算组织编号")
    String unitCode;
    @ApiModelProperty(value = "来源系统 由财务云系统分配")
    String sourceSystem;
    @ApiModelProperty(value = "")
    String dicCode;
    @ApiModelProperty(value = "业务单据数据 Json格式，见【入参数据项说明】")
    T whereCondition;
}
