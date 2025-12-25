package com.wishare.contract.apps.remote.vo;

import com.wishare.component.tree.interfaces.IPermissionViewConstraint;
import com.wishare.component.tree.interfaces.enums.CheckedListEnum;
import com.wishare.component.tree.interfaces.enums.RadioEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserAccountPermissionV implements IPermissionViewConstraint<Long> {

    @ApiModelProperty("全部和指定和无权限 三选一")
    private RadioEnum radio;

    @ApiModelProperty("当前XX和下级XX以及特定XX必选其一")
    private List<CheckedListEnum> checkedList = new ArrayList<>();

    @ApiModelProperty("选中的值")
    private List<Long> casValue = new ArrayList<>();

    @ApiModelProperty("选中的值全路径")
    private List<Long[]> allPath = new ArrayList<>();

}
