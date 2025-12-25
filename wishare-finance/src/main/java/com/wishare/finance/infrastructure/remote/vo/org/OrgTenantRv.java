package com.wishare.finance.infrastructure.remote.vo.org;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujian
 * @date 2022/8/31
 * @Description: 根据id获取租户详情反参
 */
@Getter
@Setter
public class OrgTenantRv {

    @ApiModelProperty("租户id")
    private String id;
    @ApiModelProperty("租户名称")
    private String name;
    @ApiModelProperty("客户简称")
    private String shortName;
    @ApiModelProperty("客户英文简称")
    private String englishName;

}
