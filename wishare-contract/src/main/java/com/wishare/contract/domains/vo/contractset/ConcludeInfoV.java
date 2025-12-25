package com.wishare.contract.domains.vo.contractset;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wishare.starter.beans.Tree;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
* <p>
* 合同订立信息表
* </p>
*
* @author wangrui
* @since 2022-09-09
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConcludeInfoV {

    @ApiModelProperty("合同id")
    private Long id;
    @ApiModelProperty("合同名称")
    private String name;
    @ApiModelProperty("公司id 来源 org_tenant.id")
    private String tenantId;
    @ApiModelProperty(value = "项目id 来源 成本中心",required = true)
    private String communityId;
    @ApiModelProperty("合同编号")
    private String contractNo;
    @ApiModelProperty("父级id")
    private Long pid;

}
