package com.wishare.finance.apps.model.configure.chargeitem.vo;

import com.wishare.starter.beans.Tree;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 永遇乐 yeoman<76164451@.qq.com>
 * @line --------------------------------
 * @date 2022/06/07
 */
@Getter
@Setter
public class TreeDataV extends Tree<TreeDataV,String> {
    private String name;

    public TreeDataV(String id, String pid, String name) {
        this.setId(id);
        this.setPid(pid);
        this.name = name;
    }
}
