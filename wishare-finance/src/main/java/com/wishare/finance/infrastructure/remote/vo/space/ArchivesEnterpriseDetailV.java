package com.wishare.finance.infrastructure.remote.vo.space;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fengxiaolin
 * @date 2023/3/10
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "企业档案-详细信息", description = "企业档案-详细信息")
public class ArchivesEnterpriseDetailV {

    private ArchivesEnterpriseBaseV archivesEnterpriseBaseV;

    private List<ArchivesEnterpriseAssetV> archivesEnterpriseAssetVList = new ArrayList<>();

    private List<ArchivesEnterpriseParkingV> archivesEnterpriseParkingVList = new ArrayList<>();

    private List<ArchivesEnterpriseStoreroomV> archivesEnterpriseStoreroomVList;

    @ApiModelProperty("标签对象集合")
    private List<TagBaseRV> tags;

}