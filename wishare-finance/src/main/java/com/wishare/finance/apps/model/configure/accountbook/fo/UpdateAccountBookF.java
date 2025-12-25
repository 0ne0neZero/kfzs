package com.wishare.finance.apps.model.configure.accountbook.fo;

import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.configure.accountbook.dto.AccountBookGroupDto;
import com.wishare.finance.domains.configure.accountbook.command.UpdateAccountBookCommand;
import com.wishare.starter.Global;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/8/18
 * @Description:
 */
@Getter
@Setter
@ApiModel("修改账簿入参")
public class UpdateAccountBookF {

    @ApiModelProperty(value = "账簿id", required = true)
    @NotNull(message = "账簿id不能为空")
    private Long id;
    @ApiModelProperty(value = "账簿编码", required = true)
    @NotBlank(message = "账簿编码不能为空")
    private String code;

    @ApiModelProperty(value = "账簿名称", required = true)
    @NotBlank(message = "账簿名称不能为空")
    private String name;

    @ApiModelProperty(value = "法定单位id", required = true)
    @NotNull(message = "法定单位id不能为空")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称",required = true)
    @NotBlank(message = "法定单位名称不能为空")
    private String statutoryBodyName;

    @ApiModelProperty(value = "账簿组合")
    private List<AccountBookGroupDto> accountBookGroupDtoList;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "凭证系统：1 用友ncc")
    private Integer voucherSys;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    @NotNull(message = "是否禁用不能为空")
    private Integer disabled;

    @ApiModelProperty(value = "映射数值")
    private List<String> mapValues;

    @ApiModelProperty(value = "账单系统来源")
    private List<Integer> sysSource;


    /**
     * 构建更新账簿command
     * @return
     */
    public UpdateAccountBookCommand updateAccountBookCommand() {
        UpdateAccountBookCommand updateAccountBookCommand = Global.mapperFacade.map(this, UpdateAccountBookCommand.class);
        List<AccountBookGroupDto> groupDtoList = Lists.newArrayList();
        for (AccountBookGroupDto accountBookGroupDto : this.accountBookGroupDtoList) {
            List<List<Long>> chargeItemIdPath = accountBookGroupDto.getChargeItemIdPath();
            List<List<Long>> costCenterIdPath = accountBookGroupDto.getCostCenterIdPath();
            List<List<Long>> statutoryBodyIdPath = accountBookGroupDto.getStatutoryBodyIdPath();
            accountBookGroupDto.setChargeItemIdPath(chargeItemIdPath);
            accountBookGroupDto.setCostCenterIdPath(costCenterIdPath);
            accountBookGroupDto.setStatutoryBodyIdPath(statutoryBodyIdPath);
            groupDtoList.add(accountBookGroupDto);
        }
        updateAccountBookCommand.setAccountBookGroupDtoList(groupDtoList);
        return updateAccountBookCommand;
    }
}
