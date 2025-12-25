package com.wishare.finance.infrastructure.remote.fo.zj;

import lombok.Data;

/**
 * @author longhuadmin
 */
@Data
public class FinancialDocumentStatusListQueryBody {

    private String djbh;

    private String djnm;

    private String lyxt;

    private Integer queryType;

    private String businessCode;

}
