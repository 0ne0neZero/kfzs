package com.wishare.contract.domains.vo.contractset;

import com.wishare.component.tree.interfaces.enums.RadioEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractOrgPermissionV {

    private RadioEnum radio;

    private Set<String> orgIds;

}
