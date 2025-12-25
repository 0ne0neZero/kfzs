package com.wishare.finance.apps.model.configure.accountbook.dto;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/8/19
 * @Description:
 */
@Getter
@Setter
public class AccountbookDTO {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("凭证系统")
    private Integer voucherSys;

    @ApiModelProperty("账簿编码")
    private String code;

    @ApiModelProperty("账簿名称")
    private String name;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称")
    private String statutoryBodyName;

    @ApiModelProperty("法定单位id路径")
    private List<Long> statutoryBodyIdPath;

    @ApiModelProperty("账簿组合")
    private List<AccountBookGroupDto> accountBookGroupDtoList;

    @ApiModelProperty(value = "映射数值")
    private List<String> mapValues;

    @ApiModelProperty(value = "账单系统来源")
    private List<Integer> sysSource;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("备注")
    private String remark;

    /**
     * 根据账簿实体
     *
     * @param accountBookE
     * @param statutoryBodyIdPath
     * @return
     */
    public AccountbookDTO handleAccountBookE(AccountBookE accountBookE, List<Long> statutoryBodyIdPath) {
        AccountbookDTO accountbookDTO = new AccountbookDTO();
        accountbookDTO.setId(accountBookE.getId());
        accountbookDTO.setStatutoryBodyId(accountBookE.getStatutoryBodyId());
        accountbookDTO.setStatutoryBodyName(accountBookE.getStatutoryBodyName());
        accountbookDTO.setStatutoryBodyIdPath(statutoryBodyIdPath);
        accountbookDTO.setCode(accountBookE.getCode());
        accountbookDTO.setName(accountBookE.getName());
        accountbookDTO.setVoucherSys(accountBookE.getVoucherSys());
        accountbookDTO.setAccountBookGroupDtoList(this.getAccountBookGroupDtoList());
        accountbookDTO.setMapValues(JSON.parseArray(accountBookE.getMapValues(),String.class));
        accountbookDTO.setSysSource(accountBookE.getSysSource());
        accountbookDTO.setDisabled(accountBookE.getDisabled());
        accountbookDTO.setRemark(accountBookE.getRemark());
        return accountbookDTO;
    }
}
