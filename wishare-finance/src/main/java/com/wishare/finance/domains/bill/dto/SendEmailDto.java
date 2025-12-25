package com.wishare.finance.domains.bill.dto;


import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptVDto;
import com.wishare.finance.infrastructure.conts.TextContentEnum;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.org.OrgTenantRv;
import com.wishare.finance.infrastructure.utils.TextContentUtil;
import com.wishare.starter.Global;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendEmailDto {


    @ApiModelProperty(value = "邮箱地址")
    String destEmailAddress;

    @ApiModelProperty(value = "主题")
    String subject;

    @ApiModelProperty("内容")
    String content;

    @ApiModelProperty(value = "附件")
    MultipartFile[] files;

    /**
     * @param statutoryBodyName
     * @return
     */
    public SendEmailDto doSubject(String statutoryBodyName) {
        this.subject = TextContentUtil.getEmailSubject(TextContentEnum.电子收据, new Object[]{statutoryBodyName});
        return this;
    }

    /**
     * @param receiptVDto
     * @return
     */
    public SendEmailDto doContent(ReceiptVDto receiptVDto) {
        OrgTenantRv orgTenantRv = Global.ac.getBean(OrgClient.class).tenantGetById(receiptVDto.getTenantId());
        String shortName = orgTenantRv.getShortName();
        Object[] data = new Object[]{receiptVDto.getRoomName(), StringUtils.isEmpty(shortName) ? "" : shortName, receiptVDto.getUrl(), receiptVDto.getCommunityName()};
        this.content = TextContentUtil.getEmailContent(TextContentEnum.电子收据, data);
        return this;
    }





}
