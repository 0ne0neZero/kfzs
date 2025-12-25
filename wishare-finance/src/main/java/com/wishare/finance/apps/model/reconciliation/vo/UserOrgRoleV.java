package com.wishare.finance.apps.model.reconciliation.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class UserOrgRoleV implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private Set<String> roleIds;

    private Set<String> orgIds;

}
