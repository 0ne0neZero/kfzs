package com.wishare.contract.domains.vo.revision.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.wishare.component.imports.ExcelImport;
import com.wishare.component.imports.extension.easyexcel.EasyExcelImport;
import com.wishare.component.imports.extension.easyexcel.annotation.MustFill;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/11  11:54
 */
@Getter
@Setter
@ToString
public class IncomeContractImport extends EasyExcelImport implements ExcelImport {


    /**
     * 合同名称
     */
    @ExcelProperty({"合同名称"})
    @MustFill
    private String name;
    /**
     * 合同性质 1虚拟合同 0非虚拟合同
     */
    @ExcelProperty({"合同性质"})
    @MustFill
    private String contractNature;
    /**
     * 合同业务分类名称
     */
    @ExcelProperty({"业务分类"})
    private String categoryName;
    /**
     * 合同属性0普通合同 1框架合同 2补充协议 3结算合同
     */
    @ExcelProperty({"合同分类"})
    @MustFill
    private String contractType;
    /**
     * 引用范本
     */
    @ExcelProperty({"引用范本"})
    private String temp;
    /**
     * 甲方名称
     */
    @ExcelProperty({"甲方名称"})
    @MustFill
    private String partyAName;
    /**
     * 乙方名称
     */
    @ExcelProperty({"乙方名称"})
    @MustFill
    private String partyBName;
    /**
     * 所属公司名称
     */
    @ExcelProperty({"所属公司"})
    private String orgName;
    /**
     * 所属部门名称
     */
    @ExcelProperty({"所属部门"})
    private String departName;
    /**
     * 经办人
     */
    @ExcelProperty({"经办人"})
    private String signPerson;
    /**
     * 签约日期
     */
    @ExcelProperty({"签约日期"})
    private String signDate;
    /**
     * 合同金额
     */
    @ExcelProperty({"合同总额"})
    private String contractAmount;
    /**
     * 合同开始日期
     */
    @ExcelProperty({"合同开始日期"})
    private String gmtExpireStart;
    /**
     * 合同到期日期
     */
    @ExcelProperty({"合同到期日期"})
    private String gmtExpireEnd;

}
