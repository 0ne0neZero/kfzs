package com.wishare.finance.apps.model.invoice.invoice.dto;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import com.wishare.finance.infrastructure.utils.TenantUtil;
import com.wishare.starter.Global;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.env.Environment;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:
 *
 * @see com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
 *
 * @see com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptVDto;
 *
 *
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("收据信息V")
public class ReceiptVDto {

    @ApiModelProperty("收据id")
    private Long id;

    @ApiModelProperty("pdf链接")
    private String receiptUrl;

    @ApiModelProperty("三方签章pdf链接")
    private String signReceiptUrl;

    @ApiModelProperty("收据编号")
    private String invoiceReceiptNo;

    @ApiModelProperty("收据主表id")
    private Long invoiceReceiptId;

    @ApiModelProperty("法定单位")
    private String statutoryBodyName;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("空间名称")
    private String roomName;

    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty(value = "推送方式：-1,不推送,0,邮箱;1,手机;2,站内信")
    private List<Integer> pushMode;

    @ApiModelProperty(value = "是否需要发信息 1：需要，2：已发送 ,3:发送失败,4:不需要")
    private Integer sendStatus;

    @ApiModelProperty(value = "推送邮箱")
    private String email;

    @ApiModelProperty(value = "购方手机")
    private String buyerPhone;

    @ApiModelProperty(value = "客户手机号")
    private String customerPhone;

    @ApiModelProperty("创建人id")
    private String creator;

    @ApiModelProperty("创建人名称")
    private String creatorName;

    @ApiModelProperty("操作人ID")
    private String operator;

    @ApiModelProperty("修改人姓名")
    private String operatorName;

    @ApiModelProperty(value = "是否需要签章：0 - 是 1 - 否")
    private Integer signStatus;

    @ApiModelProperty(value = "开票状态：1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废 7 开票成功，签章失败 8.部分红冲")
    private Integer state;

    @ApiModelProperty(value = "签署申请编号")
    private String signApplyNo;

    @ApiModelProperty(value = "作废申请编号")
    private String voidSignApplyNo;

    @ApiModelProperty(value = "签章文件集合")
    private List<FileVo> signFileVos;

    @ApiModelProperty(value = "原文件集合")
    private List<FileVo> scriptFileVos;

    @ApiModelProperty(value = "作废收据文件")
    private List<FileVo> voidFileVos;


    @ApiModelProperty(value = "是否需要发信息 1：需要，2：已发送 ,3:发送失败 4:不需要")
    private Integer voidSendStatus;


    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;


    @ApiModelProperty(value = "给发短信使用的链接")
    private String url;

    @ApiModelProperty(value = "发短信使用的手机号")
    private String phone;

    @ApiModelProperty(value = "账单类型 0-非账单 1-应收账单， 2-预收账单， 3-临时收费账单")
    private Integer billType;

    @ApiModelProperty("收款单id")
    private Long gatherBillId;

    @ApiModelProperty("收款单编号")
    private String gatherBillNo;


    /**
     * 如果开启签章 则启用签章pdf链接
     * @return
     */
    public String getUrl() {
        //处理状态为空
        if(Objects.isNull(signStatus)){
           return getReceiptUrl();
        }
        if(TenantUtil.bf4()&&signStatus == 0){
            //远洋&&eSign
            String filePath = Global.ac.getBean(Environment.class).getProperty("wishare.file.host");
            return this.replacePath(filePath,"/api/finance/receipt/redirect?content="+signApplyNo);
        }
        //其他
        return signStatus == 0 ? getSignReceiptUrl():getReceiptUrl();
    }


    /**
     * 链接替换
     * @param urlString
     * @param replacement
     * @return
     */
    private static String replacePath(String urlString, String replacement) {
        URI uri;
        try {
            uri = new URI(urlString);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return urlString.replace(uri.getPath(), "") + replacement;

    }
}
