package com.wishare.finance.apps.model.reconciliation.fo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOrgRoleF implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<String> roleIds;

    private Set<String> orgIds;

}
