package com.wishare.contract.apps.service.revision.log;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.starter.Global;
import org.springframework.stereotype.Service;
import com.wishare.contract.domains.entity.revision.log.RevisionLogE;
import lombok.extern.slf4j.Slf4j;
import lombok.Setter;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.apps.fo.revision.log.RevisionLogPageF;
import com.wishare.contract.domains.service.revision.log.RevisionLogService;
import com.wishare.contract.domains.vo.revision.log.RevisionLogV;
import com.wishare.contract.apps.fo.revision.log.RevisionLogF;
import com.wishare.contract.apps.fo.revision.log.RevisionLogListF;
import com.wishare.contract.apps.fo.revision.log.RevisionLogSaveF;
import com.wishare.contract.apps.fo.revision.log.RevisionLogUpdateF;
import com.wishare.contract.domains.vo.revision.log.RevisionLogListV;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
* <p>
* 合同改版动态记录表
* </p>
*
* @author chenglong
* @since 2023-07-12
*/
@Service
@Slf4j
public class RevisionLogAppService {

    @Setter(onMethod_ = {@Autowired})
    private RevisionLogService revisionLogService;

    public RevisionLogV get(RevisionLogF revisionLogF){
        return revisionLogService.get(revisionLogF).orElse(null);
    }

    public RevisionLogListV list(RevisionLogListF revisionLogListF){
        return revisionLogService.list(revisionLogListF);
    }

    public String save(RevisionLogSaveF revisionLogF){
        return revisionLogService.save(revisionLogF);
    }

    public void update(RevisionLogUpdateF revisionLogF){
        revisionLogService.update(revisionLogF);
    }

    public List<RevisionLogV> getListLogById(String id) {
        return Global.mapperFacade.mapAsList(revisionLogService.list(new QueryWrapper<RevisionLogE>()
                .eq(RevisionLogE.BIZ_ID, id)
                .orderByDesc(RevisionLogE.GMT_CREATE)), RevisionLogV.class);
    }

    public boolean removeById(String id){
        return revisionLogService.removeById(id);
    }

    public PageV<RevisionLogV> page(PageF<RevisionLogPageF> request) {
        return revisionLogService.page(request);
    }

    public PageV<RevisionLogV> frontPage(PageF<SearchF<RevisionLogE>> request) {
        return revisionLogService.frontPage(request);
    }
}
