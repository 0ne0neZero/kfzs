package com.wishare.finance.apps.model.report.fo;

import com.wishare.starter.beans.PageF;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 分页查询账单生成报表入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("分页查询账单生成报表入参")
public class GenerateBillReportF {

    @ApiModelProperty(value = "开始日期")
    private LocalDate startDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDate endDate;

    @ApiModelProperty("分页参数")
    PageF<SearchF<?>> query;

    /**
     * 校检入参
     */
    public void check() {
        checkStartAndEndDate();
    }

    /**
     * 校检开始日期和结束日期，需要同时存在或者同时不存在
     */
    private void checkStartAndEndDate() {
        if (getStartDate() != null && getEndDate() == null ) {
            throw BizException.throw400("结束日期不能为空");
        }
        if (getStartDate() == null && getEndDate() != null){
            throw BizException.throw400("开始日期不能为空");
        }
    }
}
