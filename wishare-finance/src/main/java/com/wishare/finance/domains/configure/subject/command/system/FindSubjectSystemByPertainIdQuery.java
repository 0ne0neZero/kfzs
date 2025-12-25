package com.wishare.finance.domains.configure.subject.command.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 根据法定单位id查询科目体系命令
 *
 * @author yancao
 */
@Getter
@Setter
@AllArgsConstructor
public class FindSubjectSystemByPertainIdQuery {

    /**
     * 法定单位id
     */
    private String pertainId;

    /**
     * 租户id
     */
    private String tenantId;


}
