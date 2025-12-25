package com.wishare.contract.apps.service.revision.bond;

import com.wishare.contract.domains.service.revision.attachment.AttachmentService;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.bond.CollectBondRelationBillE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.bond.CollectBondRelationBillPageF;
import com.wishare.contract.domains.service.revision.bond.CollectBondRelationBillService;
import com.wishare.contract.domains.vo.revision.bond.CollectBondRelationBillV;
import com.wishare.contract.apps.fo.revision.bond.CollectBondRelationBillF;
import com.wishare.contract.apps.fo.revision.bond.CollectBondRelationBillListF;
import com.wishare.contract.apps.fo.revision.bond.CollectBondRelationBillSaveF;
import com.wishare.contract.apps.fo.revision.bond.CollectBondRelationBillUpdateF;
import com.wishare.contract.domains.vo.revision.bond.CollectBondRelationBillListV;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 收取保证金改版关联单据明细表
* </p>
*
* @author chenglong
* @since 2023-07-27
*/
@Service
@Slf4j
public class CollectBondRelationBillAppService {

    @Setter(onMethod_ = {@Autowired})
    private CollectBondRelationBillService collectBondRelationBillService;

    @Setter(onMethod_ = {@Autowired})
    private AttachmentService attachmentService;

    public CollectBondRelationBillV get(CollectBondRelationBillF collectBondRelationBillF){
        return collectBondRelationBillService.get(collectBondRelationBillF).orElse(null);
    }

    public CollectBondRelationBillListV list(CollectBondRelationBillListF collectBondRelationBillListF){
        return collectBondRelationBillService.list(collectBondRelationBillListF);
    }

    public String save(CollectBondRelationBillSaveF collectBondRelationBillF){
        return collectBondRelationBillService.save(collectBondRelationBillF);
    }

    public void update(CollectBondRelationBillUpdateF collectBondRelationBillF){
        collectBondRelationBillService.update(collectBondRelationBillF);
    }

    public boolean removeById(String id){
        return collectBondRelationBillService.removeById(id);
    }

    public PageV<CollectBondRelationBillV> page(PageF<CollectBondRelationBillPageF> request) {
        return collectBondRelationBillService.page(request);
    }

    public PageV<CollectBondRelationBillV> frontPage(PageF<SearchF<CollectBondRelationBillE>> request) {
        PageV<CollectBondRelationBillV> pageV = collectBondRelationBillService.frontPage(request);
        for (CollectBondRelationBillV record : pageV.getRecords()) {
            Optional.ofNullable(attachmentService.listOneById(record.getId())).ifPresent(v -> {
                record.setFilesRecord(attachmentService.listOneById(record.getId()))
                        .setFileKey(v.getFileKey())
                        .setFileName(v.getName());
            });
        }
        return pageV;
    }
}
