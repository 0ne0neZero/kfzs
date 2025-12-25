package com.wishare.finance.domains.bill.dto;


import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptVDto;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.vo.org.OrgTenantRv;
import com.wishare.finance.infrastructure.remote.vo.space.CommunityShortRV;
import com.wishare.starter.Global;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SmsDto {

    @ApiModelProperty(value = "电话号码")
    String mobileNum;

    @ApiModelProperty(value = "租户简称")
    String tenantShortName;

    @ApiModelProperty("房号")
    String roomName;

    @ApiModelProperty("下载链接")
    String pdfUrl;

    @ApiModelProperty(value = "项目电话")
    String phone;

    //作废使用
    @ApiModelProperty(value = "收据编号")
    String voidNo;


    /**
     * 项目电话
     *
     * @param communityId
     * @return
     */
    public SmsDto doPhone(String communityId) {
        CommunityShortRV communityInfo = Global.ac.getBean(SpaceClient.class).getCommunityInfo(communityId);
        this.phone = communityInfo == null ? "" : communityInfo.getContactsPhone();
        return this;
    }

    /**
     * 租户简称
     *
     * @param tenantId
     * @return
     */
    public SmsDto doTenantShortName(String tenantId) {
        OrgTenantRv orgTenantRv = Global.ac.getBean(OrgClient.class).tenantGetById(tenantId);
        this.tenantShortName = ObjectUtils.isEmpty(orgTenantRv) ? "" : orgTenantRv.getShortName();
        return this;
    }


    /**
     * 发短信的手机号优先级 phone(最高级的)>buyerPhone(开收据填写的)>customerPhone
     * @param receiptVDto
     * @return
     */
    public SmsDto doMobileNum(ReceiptVDto receiptVDto){
        if(StringUtils.isNotBlank(receiptVDto.getPhone())){
            this.mobileNum = receiptVDto.getPhone();
            return this;
        }
        final String buyerPhone = receiptVDto.getBuyerPhone();
        this.mobileNum = buyerPhone;
        if(StringUtils.isEmpty(buyerPhone)){
            this.mobileNum = receiptVDto.getCustomerPhone();
        }
        return this;
    }

    public SmsDto doVoidNo(Object o){
        if(Objects.nonNull(o)){
            this.voidNo = String.valueOf(o);
        }
        return this;
    }
}
