package com.wishare.contract.apps.remote.fo.message;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/12/7/12:01
 */
@ApiModel
@Getter
@Setter
@Accessors(chain = true)
public class ContractF {

    private List<String> ids;
}
