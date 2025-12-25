package com.wishare.finance.domains.voucher.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.wishare.finance.apps.model.yuanyang.fo.BusinessProcessHandleF;
import com.wishare.finance.apps.model.yuanyang.fo.ProcessAccountBookF;
import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateLogicCodeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherTemplateLogicTypeEnum;
import com.wishare.finance.domains.voucher.strategy.bpm.summary.BpmSummaryStrategyContext;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.domains.voucher.strategy.summary.SummaryStrategyContext;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.utils.ScriptEngineUtil;
import com.wishare.starter.utils.ErrorAssertUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 凭证规则分录信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/28
 */
@Getter
@Setter
@Slf4j
public class VoucherTemplateEntryOBV {

    @ApiModelProperty(value = "借贷类型： credit贷方， debit借方", required = true)
    @NotBlank(message = "借贷类型不能为空")
    private String type;

    @ApiModelProperty(value = "分录科目id", required = true)
    @NotNull(message = "分录科目id不能为空")
    private Long subjectId;

    @ApiModelProperty(value = "分录科目id", required = true)
    @NotBlank(message = "分类科目id不能为空")
    private String subjectCode;

    @ApiModelProperty(value = "分录科目名称", required = true)
    @NotBlank(message = "分类科目名称不能为空")
    private String subjectName;

    @ApiModelProperty(value = "费项")
    private List<VoucherTemplateEntryChargeItem> chargeItem;

    @ApiModelProperty(value = "现金流id")
    private Long cashFlowId;

    @ApiModelProperty(value = "现金流code")
    private String cashFlowCode;

    @ApiModelProperty(value = "现金流名称")
    private String cashFlowName;

    @ApiModelProperty(value = "凭证模板分录辅助核算信息")
    private List<VoucherTemplateEntryAssiste> assisteItems;

    @ApiModelProperty(value = "BPM凭证模板过滤条件")
    private FilterConditions filterConditions;

    @ApiModelProperty(value = "凭证模板分录摘要信息")
    @Valid
    private List<VoucherTemplateEntrySummary> summary;

    @ApiModelProperty(value = "凭证模板逻辑项", required = true)
    @NotNull(message = "凭证模板逻辑不能为空")
    @Size(min = 1, max = 200, message = "凭证模板逻辑项长度有误，长度仅允许1 ~ 200")
    @Valid
    private List<VoucherTemplateEntryLogic> logics;


    /**
     * 生成摘要
     * @param businessBill
     * @return
     */
    public String generateSummary(VoucherBusinessBill businessBill){
        if (summary == null || summary.isEmpty()) {
            return "";
        }
        StringBuilder summaryBuilder = new StringBuilder();
        for (VoucherTemplateEntrySummary entrySummary : summary) {
            summaryBuilder.append(SummaryStrategyContext.getStrategy(entrySummary.getType()).summary(entrySummary, businessBill));
        }
        return summaryBuilder.toString();
    }


    public String generateSummary(BusinessProcessHandleF businessProcessHandleF, ProcessAccountBookF accountBook, int index) {
        if (summary == null || summary.isEmpty()) {
            return "";
        }
        StringBuilder summaryBuilder = new StringBuilder();
        for (VoucherTemplateEntrySummary entrySummary : summary) {
            summaryBuilder.append(BpmSummaryStrategyContext.getStrategy(entrySummary.getType()).summary(entrySummary, businessProcessHandleF, accountBook, index));
        }
        return summaryBuilder.toString();
    }

    /**
     * 校验逻辑是否正确
     */
    public void checkLogics(){
        ErrorAssertUtil.notNullThrow403(logics, ErrorMessage.VOUCHER_RULE_LOGIC_NOT_EXIST);
        //0.必须有一个数据并且是code
        //1.必须包含code
        //2.两个method、两个code不能在一起
        //3.如果有括号必须闭环
        //4.首尾不能是method
        //5.左括号左边必须是method或者第一位，右边必须是number或者code
        //6.右括号左边必须是number或者code，右边必须是method或者最后一位
        //7./号不能后面跟着0
        //8.code和number不能相邻
        //9.point两边必选是number
    }

    /**
     * 构建逻辑参数
     * @return
     */
    public String generateFormula(List<VoucherDetailOBV> details){
        StringBuilder formula = new StringBuilder();
        for (VoucherTemplateEntryLogic logic : logics) {
            if (VoucherTemplateLogicTypeEnum.分录金额.equalsByCode(logic.getType())) {
                formula.append(details.stream()
                        .filter(detail -> detail.getSubjectCode().startsWith(logic.getValue())).findAny()
                        .orElse(new VoucherDetailOBV().setOriginalAmount(0L))
                        .getOriginalAmount());
            } else {
                formula.append(logic.getValue());
            }
        }
        return formula.toString();
    }

    /**
     * 逻辑计算
     * @param logicValues 逻辑参数
     * @return 结果值
     */
    public long evalLogic(Map<String, BigDecimal> logicValues, List<VoucherDetailOBV> details){
        if (Objects.isNull(logicValues) || logicValues.isEmpty()){
            return 0;
        }
        String formula = generateFormula(details);
        log.debug("logicValues:{}", JSON.toJSONString(logicValues));
        for (Map.Entry<String, BigDecimal> entry : logicValues.entrySet()){
            log.debug(formula);
            log.debug("entry:{}", JSON.toJSONString(entry));
            formula = formula.replaceAll("\\$\\{" + entry.getKey() + "}", entry.getValue().toString());
        }
        try {
            formula = formula.replaceAll("\\$\\{", "").replaceAll("}", "");
            //针对负负情况的处理
            formula= formula.replaceAll("--","+");
            return new BigDecimal(ScriptEngineUtil.eval(formula).toString()).setScale(0, RoundingMode.HALF_UP).longValue();
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws ScriptException {


        List<VoucherTemplateEntryLogic> list = new ArrayList<>();

        list.add(new VoucherTemplateEntryLogic("code", "${receivablePayable}"));
        list.add(new VoucherTemplateEntryLogic("method", "/"));
        list.add(new VoucherTemplateEntryLogic("bracket", "("));
        list.add(new VoucherTemplateEntryLogic("number", "1"));
        list.add(new VoucherTemplateEntryLogic("method", "+"));
        list.add(new VoucherTemplateEntryLogic("number", "0"));
        list.add(new VoucherTemplateEntryLogic("point", "."));
        list.add(new VoucherTemplateEntryLogic("number", "0"));
        list.add(new VoucherTemplateEntryLogic("number", "6"));
        list.add(new VoucherTemplateEntryLogic("bracket", ")"));

        String formula = "";
        for (VoucherTemplateEntryLogic voucherTemplateEntryLogic : list) {
            formula += voucherTemplateEntryLogic.getValue();
        }
        System.out.println("计算公式" + formula);

        //字符替换
        formula = formula.replaceAll("\\$\\{" + "receivablePayable" + "}", "36996");
        System.out.println("赋值替换：" + formula);

        ScriptEngine script = new ScriptEngineManager().getEngineByName("JavaScript");
        Object eval = script.eval(formula);
        System.out.println("值" + eval);


        VoucherTemplateEntryOBV aa = new VoucherTemplateEntryOBV();
        aa.logics = list;

        //
        List<VoucherDetailOBV> detailOBVS = JSONObject.parseObject("[{\"type\": \"debit\", \"summary\": \"扬州远洋宸章雅苑项目减免物业管理费\", \"subjectId\": 1294789085701023, \"debitAmount\": -36996, \"subjectCode\": \"11310101\", \"subjectName\": \"应收账款/基础物业服务/物业管理费\", \"assisteItems\": [{\"code\": \"0000001C\", \"name\": \"业主\", \"type\": 5, \"ascCode\": \"0004\", \"ascName\": \"客商\"}, {\"code\": \"02YZ00000002\", \"name\": \"扬州远洋宸章雅苑项目\", \"type\": 7, \"ascCode\": \"0010\", \"ascName\": \"项目\"}], \"chargeItemId\": 11893882195347, \"creditAmount\": 0, \"incTaxAmount\": -36996, \"chargeItemCode\": \"101001\", \"chargeItemName\": \"物业管理费\", \"originalAmount\": -36996}, {\"type\": \"credit\", \"summary\": \"扬州远洋宸章雅苑项目减免物业管理费\", \"subjectId\": 1294789103021416, \"debitAmount\": 0, \"subjectCode\": \"5101010101\", \"subjectName\": \"营业收入/基础物业服务/物业管理费/物业管理费\", \"assisteItems\": [{\"code\": \"04\", \"name\": \"6.0%\", \"type\": 6, \"ascCode\": \"0066\", \"ascName\": \"增值税税率\"}, {\"code\": \"0000001C\", \"name\": \"业主\", \"type\": 5, \"ascCode\": \"0004\", \"ascName\": \"客商\"}, {\"code\": \"02YZ00000002\", \"name\": \"扬州远洋宸章雅苑项目\", \"type\": 7, \"ascCode\": \"0010\", \"ascName\": \"项目\"}], \"chargeItemId\": 11893882195347, \"creditAmount\": -33630, \"incTaxAmount\": -36996, \"chargeItemCode\": \"101001\", \"chargeItemName\": \"物业管理费\", \"originalAmount\": -33630}, {\"type\": \"credit\", \"summary\": \"扬州远洋宸章雅苑项目减免物业管理费\", \"subjectId\": 1294789097201232, \"debitAmount\": 0, \"subjectCode\": \"21710112\", \"subjectName\": \"应交税费/应交增值税/销项税额\", \"assisteItems\": [{\"code\": \"04\", \"name\": \"6.0%\", \"type\": 6, \"ascCode\": \"0066\", \"ascName\": \"增值税税率\"}, {\"code\": \"02YZ00000002\", \"name\": \"扬州远洋宸章雅苑项目\", \"type\": 7, \"ascCode\": \"0010\", \"ascName\": \"项目\"}], \"chargeItemId\": 11893882195347, \"creditAmount\": -3366, \"incTaxAmount\": -36996, \"chargeItemCode\": \"101001\", \"chargeItemName\": \"物业管理费\", \"originalAmount\": -3366}]", new TypeReference<List<VoucherDetailOBV>>() {
        });
        HashMap<String, BigDecimal> map = new HashMap<>();
        map.put(VoucherTemplateLogicCodeEnum.增值税税率.getCode(),new BigDecimal("0.06"));
        long l = aa.evalLogic(map, detailOBVS);

        System.out.println(l);

    }

}
