package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Validated
@Getter
@Setter
@ApiModel("人员档案新增类")
public class AddPersonF {

    /**
     * 人员基本信息主键
     */
    @ApiModelProperty(value = "人员基本信息主键， 新增是不填")
    @JSONField(name = "pk_psndoc")
    private String pkPsndoc;

    /**
     * 所属业务单元
     */
    @ApiModelProperty(value = "所属业务单元", required = true)
    @JSONField(name = "pk_org")
    @NotBlank(message = "所属业务单元不能为空")
    @Size(max = 20, message = "所属业务单元最大长度为20")
    private String pkOrg;

    /**
     * 人员编码
     */
    @ApiModelProperty(value = "人员编码", required = true)
    @JSONField(name = "code")
    @NotBlank(message = "人员编码不能为空")
    @Size(max = 40, message = "人员编码最大长度为40")
    private String code;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", required = true)
    @JSONField(name = "name")
    @NotBlank(message = "姓名不能为空")
    @Size(max = 200, message = "姓名最大长度为200")
    private String name;

    /**
     * 曾用名
     */
    @ApiModelProperty("曾用名")
    @JSONField(name = "usedname")
    @Size(max = 200, message = "曾用名最大长度为200")
    private String usedName;

    /**
     * 出生日期
     */
    @ApiModelProperty("出生日期")
    @JSONField(name = "birthdate")
    @Size(max = 10, message = "出生日期最大长度为10")
    private String birthdate;

    /**
     * 性别 1=男;2=女;
     */
    @ApiModelProperty("性别 1=男;2=女;")
    @JSONField(name = "sex")
    private Integer sex;

    /**
     * 证件类型
     */
    @ApiModelProperty("证件类型 "
            + "CN01=身份证\n"
            + "CN02=护照\n"
            + "CN04=外国人")
    @JSONField(name = "idtype")
    @Size(max = 20, message = "证件类型最大长度为20")
    private String idtype;

    /**
     * 证件号
     */
    @ApiModelProperty("证件号")
    @JSONField(name = "id")
    @Size(max = 25, message = "证件号最大长度为25")
    private String id;

    /**
     * 助记码
     */
    @ApiModelProperty("助记码")
    @JSONField(name = "mnecode")
    @Size(max = 50, message = "助记码最大长度为50")
    private String mnecode;

    /**
     * 参加工作日期 2014-08-28
     */
    @ApiModelProperty(value = "参加工作日期 2014-08-28", required = true)
    @JSONField(name = "joinworkdate")
    @Size(max = 10, message = "参加工作日期最大长度为10")
    private String joinWorkDate;

    /**
     * 家庭地址
     */
    @ApiModelProperty("家庭地址")
    @JSONField(name = "addr")
    @Size(max = 20, message = "家庭地址最大长度为20")
    private String addr;

    /**
     * 办公电话
     */
    @ApiModelProperty("办公电话")
    @JSONField(name = "officephone")
    @Size(max = 30, message = "办公电话最大长度为30")
    private String officePhone;

    /**
     * 家庭电话
     */
    @ApiModelProperty("家庭电话")
    @JSONField(name = "homephone")
    @Size(max = 30, message = "家庭电话最大长度为30")
    private String homePhone;

    /**
     * 手机
     */
    @ApiModelProperty("手机")
    @JSONField(name = "mobile")
    @Size(max = 30, message = "手机最大长度为30")
    private String mobile;

    /**
     * 电子邮件
     */
    @ApiModelProperty("电子邮件")
    @JSONField(name = "email")
    @Size(max = 50, message = "电子邮件最大长度为50")
    private String email;

    /**
     * 启用状态
     * 1=未启用;
     * 2=已启用;
     * 3=已停用;
     */
    @ApiModelProperty("启用状态"
            + "1=未启用;\n"
            + "2=已启用;\n"
            + "3=已停用;")
    @JSONField(name = "enablestate")
    private Integer enableState = 2;

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
     * 用工性质
     * 01=正式员工；02=外包；03=实习生
     */
    @ApiModelProperty("用工性质 01=正式员工；02=外包；03=实习生")
    @JSONField(name = "def1")
    @Size(max = 101, message = "用工性质最大长度为101")
    private String def1;

    @ApiModelProperty(value = "人员职工信息", required = true)
    private List<NccPersonJobF> psnjob;

}
