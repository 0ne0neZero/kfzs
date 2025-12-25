package com.wishare.finance.apps.service.fangyuan;

import com.wishare.finance.apps.model.fangyuan.vo.ParkRenewInfoPageV;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.entity.PayInfo;
import com.wishare.finance.domains.fangyuan.dto.ParkRenewInfoPageDto;
import com.wishare.finance.domains.fangyuan.entity.ParkRenewInfo;
import com.wishare.finance.domains.fangyuan.repository.ParkRenewInfoRepository;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ParkRenewInfoAppService {

    private final ParkRenewInfoRepository parkRenewInfoRepository;

    public Boolean addParkRenewInfo(ParkRenewInfo parkRenewInfo) {
        parkRenewInfo.setDeleted(0);
        return parkRenewInfoRepository.save(parkRenewInfo);
    }

    public Boolean updateParkRenewInfo(ParkRenewInfo parkRenewInfo) {
        return parkRenewInfoRepository.updateById(parkRenewInfo);
    }

    public PageV<ParkRenewInfoPageV> getParkRenewInfoList(PageF<SearchF<?>> pageF) {
        PageV<ParkRenewInfoPageDto> parkRenewInfoList = parkRenewInfoRepository.getParkRenewInfoList(pageF);
        List<ParkRenewInfoPageV> pageVList = new ArrayList<>();
        if (parkRenewInfoList != null && CollectionUtils.isNotEmpty(parkRenewInfoList.getRecords())) {
            for (ParkRenewInfoPageDto record : parkRenewInfoList.getRecords()) {
                ParkRenewInfoPageV parkRenewInfoPageV = new ParkRenewInfoPageV();
                parkRenewInfoPageV.setResultCode(record.getResultCode());
                parkRenewInfoPageV.setResultMsg(record.getResultMsg());
                String[] split = record.getBillNo().split(",");
                parkRenewInfoPageV.setBillNoList(Arrays.asList(split));
                parkRenewInfoPageV.setIdList(Arrays.asList(record.getId().split(",")));
                parkRenewInfoPageV.setRoomId(record.getRoomId());
                parkRenewInfoPageV.setRoomName(record.getRoomName());
                parkRenewInfoPageV.setStartTime(record.getStartTime());
                parkRenewInfoPageV.setEndTime(record.getEndTime());
                parkRenewInfoPageV.setPayTime(record.getPayTime());
                parkRenewInfoPageV.setTotalAmount(record.getTotalAmount());
                parkRenewInfoPageV.setReceivableAmount(record.getReceivableAmount());
                parkRenewInfoPageV.setSettleAmount(record.getSettleAmount());
                parkRenewInfoPageV.setActualPayAmount(record.getActualPayAmount());
                parkRenewInfoPageV.setChargeItemId(record.getChargeItemId());
                parkRenewInfoPageV.setChargeItemName(record.getChargeItemName());
                parkRenewInfoPageV.setCustomerName(record.getCommunityName());
                parkRenewInfoPageV.setCommunityId(record.getCommunityId());
                parkRenewInfoPageV.setCommunityName(record.getCommunityName());
                parkRenewInfoPageV.setUnitPrice(record.getUnitPrice());
                parkRenewInfoPageV.setSettleState(record.getSettleState());
                parkRenewInfoPageV.setPayerName(record.getPayerName());
                parkRenewInfoPageV.setPayerPhone(record.getPayerPhone());
                pageVList.add(parkRenewInfoPageV);
            }

//            for (ParkRenewInfoPageV record : parkRenewInfoList.getRecords()) {
//                StringBuilder stringBuilder = new StringBuilder("");
//                if (record != null && CollectionUtils.isNotEmpty(record.getPayInfos())) {
//                    for (PayInfo payInfo : record.getPayInfos()) {
//                        stringBuilder.append(SettleChannelEnum.valueNameOfByCode(payInfo.getPayChannel())).append(",");
//                    }
//                }
//                String string = stringBuilder.toString();
//                if (string.length() == 0) {
//                    record.setPayInfosString(string);
//                } else {
//                    record.setPayInfosString(string.substring(0, string.length() - 1));
//                }
//            }


        }
        return PageV.of(parkRenewInfoList.getPageNum(), parkRenewInfoList.getPageSize(), parkRenewInfoList.getTotal(), pageVList);
    }

    public ParkRenewInfo getParkRenewInfo(Long billId) {
        return parkRenewInfoRepository.getByBillId(billId);
    }
}