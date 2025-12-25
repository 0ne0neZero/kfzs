package com.wishare.contract.apps.fo.revision.income;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同订立信息拓展表附件信息实体
 * </p>
 *
 * @author chenglong
 * @since 2023-09-22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractFjxxF {
    @ApiModelProperty("附件ID")
    private String uid;

    @ApiModelProperty("附件名称")
    private String name;

    @ApiModelProperty("附件业务类型")
    private String suffix;

    @ApiModelProperty("附件业务类型翻译")
    private String suffixname;

    @ApiModelProperty("文件大小")
    private String size;

    @ApiModelProperty("fileKey")
    private String fileKey;

    @ApiModelProperty("fileId")
    private String fileId;

    @ApiModelProperty("filename")
    private String filename;

    @ApiModelProperty("busitype")
    private String busitype = "01";

}
