package com.wishare.finance.apps.scheduler.ncc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.service.voucher.VoucherInferenceAppServiceFactory;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import com.wishare.finance.domains.voucher.service.OldVoucherRuleDomainService;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.xxl.job.core.handler.annotation.XxlJob;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/25 15:51
 * @version: 1.0.0
 */
@Component
public class BillInferenceSchedule {

    @Autowired
    private OldVoucherRuleDomainService oldVoucherRuleDomainService;

    @Autowired
    private VoucherInferenceAppServiceFactory voucherInferenceAppServiceFactory;

    @XxlJob("getVoucherRuleForInference")
    public void getVoucherRuleForInference() {
        String now = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm");

        // 分页获取
        PageF<SearchF<?>> form = new PageF<>();
        form.setConditions(new SearchF<>());
        form.setPageNum(1L);
        form.setPageSize(1000L);
        boolean isLast = false;
        do {
            PageV<VoucherRuleE> page = oldVoucherRuleDomainService.listForInference(form);
            if (!CollectionUtils.isEmpty(page.getRecords())) {
                readyForInference(page.getRecords(), now);
            }
            form.setPageNum(form.getPageNum() + 1);
            isLast = page.isLast();
        } while (!isLast);

    }

    // 找出可以推凭的数据推凭
    private void readyForInference(List<VoucherRuleE> records, String now) {
        for (VoucherRuleE record : records) {
            if (StringUtils.isNotEmpty(record.getExecuteTime())) {
                JSONObject jsonObject = JSON.parseObject(record.getExecuteTime());
                String timeStr = jsonObject.getString("time");
                if (StringUtils.isEmpty(timeStr) || timeStr.contains("null")) {
                    continue;
                }
                String frequency = jsonObject.getString("frequency");
                String time = "";
                switch (frequency) {
                    case "day":
                        time = getNowDay() + timeStr;
                        break;
                    case "month":
                        time = getMonth(jsonObject.getLongValue("before")) + timeStr;
                        break;
                    case "year":
                        time = getNowYear() + timeStr;
                        break;
                    default:
                        break;
                }
                if (includeNow(time, now)) {
                    voucherInferenceAppServiceFactory.getInstance(EventTypeEnum.valueOfByCodeByEvent(record.getEventType())).inference(record,
                        false);
                }
            }
        }
    }

    private boolean includeNow(String time, String now) {
        long second = str2TimeStamp(now);
        long other = str2TimeStamp(time);
        return (second - 600) <= other && second >= other;
    }

    /**
     * 字符串转时间戳(秒)
     * @param time
     * @return
     */
    private long str2TimeStamp(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }

    /**
     * 获取当前日期
     * @return
     */
    private String getNowDay() {
        return DateFormatUtils.format(new Date(), "yyyy-MM-dd ");
    }

    /**
     * 获取月数据
     */
    private String getMonth(long before) {
        return LocalDate.from(LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).minusDays(before)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd "));
    }

    /**
     * 获取当前年数据
     */
    private String getNowYear() {
        return DateFormatUtils.format(new Date(), "yyyy-");
    }
}
