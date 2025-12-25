package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Valid
@Getter
@Setter
@ApiModel("nc人员任职")
@Accessors(chain = true)
public class NccPersonJobF {

    /**
     * 人员工作信息主键 新增不填
     */
    @ApiModelProperty("人员工作信息主键")
    @JSONField(name = "pk_psnjob")
    private String pkPsnJob;

    /**
     * 所属集团
     * 默认集团：远洋亿家集团 G
     */
    @ApiModelProperty(value = "所属集团 默认集团：远洋亿家集团 G", required = true)
    @JSONField(name = "pk_group")
    @NotBlank(message = "所属集团不能为空")
    @Size(max = 20, message = "所属集团最大长度为20")
    private String pkGroup = "G";

    /**
     * 任职业务单元
     * 收费系统传公司编码
     */
    @ApiModelProperty(value = "任职业务单元 收费系统传公司编码", required = true)
    @JSONField(name = "pk_org")
    @NotBlank(message = "任职业务单元不能为空")
    @Size(max = 20, message = "任职业务单元最大长度为20")
    private String pkOrg;

    /**
     * 员工编号
     * 默认同主表人员编码
     */
    @ApiModelProperty(value = "员工编号 默认同主表人员编码", required = true)
    @JSONField(name = "psncode")
    @NotBlank(message = "默认同主表人员编码不能为空")
    @Size(max = 40, message = "默认同主表人员编码最大长度为40")
    private String psnCode;

    /**
     * 人员类别
     * 01=在职；02=试用；03=离职
     */
    @ApiModelProperty(value = "人员类别 01=在职；02=试用；03=离职", required = true)
    @JSONField(name = "pk_psncl")
    @NotBlank(message = "人员类别不能为空")
    @Size(max = 20, message = "人员类别最大长度为20")
    private String pkPsncl;

    /**
     * 所在部门
     * 收费系统传所在部门编码
     */
    @ApiModelProperty(value = "所在部门 收费系统传所在部门编码", required = true)
    @JSONField(name = "pk_dept")
    @NotBlank(message = "所在部门不能为空")
    @Size(max = 20, message = "所在部门最大长度为20")
    private String pkDept;

    /**
     * 主职
     * Y / N 默认Y
     */
    @ApiModelProperty(value = "主职 Y / N 默认Y", required = true)
    @JSONField(name = "ismainjob")
    @NotBlank(message = "主职不能为空")
    private char isMainJob = 'Y';

    /**
     * 任职开始日期
     * 样例：2022-03-15
     */
    @ApiModelProperty(value = "任职开始日期 样例：2022-03-15", required = true)
    @JSONField(name = "indutydate")
    @NotBlank(message = "任职开始日期不能为空")
    @Size(max = 10, message = "任职开始日期最大长度为10")
    private String inDutyDate;

    /**
     * 任职结束日期
     * 样例：2022-03-15
     */
    @ApiModelProperty("任职结束日期 样例：2022-03-15")
    @JSONField(name = "enddutydate")
    @Size(max = 10, message = "样例：2022-03-15最大长度为10")
    private String endDutyDate;

    /**
     * 职务
     */
    @ApiModelProperty("职务")
    @JSONField(name = "pk_job")
    @Size(max = 20, message = "职务最大长度为20")
    private String pkJob;

    /**
     * 工种
     */
    @ApiModelProperty("工种")
    @JSONField(name = "worktype")
    @Size(max = 20, message = "工种最大长度为20")
    private String workType;

    /**
     * 职务称谓
     */
    @ApiModelProperty("职务称谓")
    @JSONField(name = "jobname")
    @Size(max = 20, message = "职务称谓最大长度为20")
    private String jobName;

    /**
     * 岗位
     */
    @ApiModelProperty("岗位")
    @JSONField(name = "pk_post")
    @Size(max = 20, message = "岗位最大长度为20")
    private String pkPost;

}
