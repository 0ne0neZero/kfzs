package com.wishare.contract.domains.mapper.revision.attachment;

import com.wishare.contract.domains.entity.revision.attachment.AttachmentE;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 关联附件管理表
 * </p>
 *
 * @author chenglong
 * @since 2023-06-26
 */
@Mapper
public interface AttachmentMapper extends BaseMapper<AttachmentE> {

    //删除无效附件
    void deleteInvalidFileById(@Param("businessType") Integer businessType, @Param("businessId") String businessId, @Param("ids") List<String> ids);

}
