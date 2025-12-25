package com.wishare.contract.apps.fo.revision.attachment;


import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 关联附件管理表 新增请求参数，不会跟着表结构更新而更新
 * </p>
 *
 * @author chenglong
 * @since 2023-06-26
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "关联附件管理表新增请求参数", description = "关联附件管理表新增请求参数")
public class AttachmentSaveF {

    /**
     * 业务类型，参考FileSaveTypeEnum 不可为空
     */
    @ApiModelProperty(value = "业务类型，参考FileSaveTypeEnum", required = true)
    private Integer businessType;
    /**
     * 业务主键ID（该文件所属数据的主键ID） 不可为空
     */
    @ApiModelProperty(value = "业务主键ID（该文件所属数据的主键ID）", required = true)
    @NotBlank(message = "业务主键ID（该文件所属数据的主键ID）不可为空")
    @Length(message = "业务主键ID（该文件所属数据的主键ID）不可超过 40 个字符", max = 40)
    private String businessId;
    /**
     * fileVo
     */
    @ApiModelProperty(value = "fileVo", required = true)
    private FileVo fileVo;

    /**
     * 合同ID，添加该字段是为了上送影像系统附件
     */
    @ApiModelProperty(value = "合同ID")
    private String id;
}
