package com.wishare.contract.apps.remote.vo.org;

import com.wishare.starter.beans.Tree;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgTreeRv extends Tree<OrgTreeRv, Long> {

    private String orgName;

    private String tenantId;
}
