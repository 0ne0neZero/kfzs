package com.wishare.finance.domains.bill.repository;/*
package com.wishare.amp.finance.domains.bill.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.amp.finance.apps.model.bill.fo.SettleChannelAndIdsF;
import com.wishare.amp.finance.apps.model.bill.vo.BillSettleChannelV;
import com.wishare.amp.finance.domains.bill.entity.BillSettleE;
import com.wishare.amp.finance.domains.bill.repository.mapper.BillSettleMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

*/
/**
 * @author xujian
 * @date 2022/9/5
 * @Description:
 *//*

@Service
public class BillSettleRepository extends ServiceImpl<BillSettleMapper, BillSettleE> {

    */
/**
     * 根据账单id获取结算记录
     *
     * @param billId
     * @return
     *//*

    public List<BillSettleE> listByBillId(Long billId) {
        LambdaQueryWrapper<BillSettleE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BillSettleE::getBillId, billId);
        queryWrapper.orderByDesc(BillSettleE::getGmtCreate);
        return baseMapper.selectList(queryWrapper);
    }

    */
/**
     * 根据账单ids批量获取结算记录
     *
     * @param billIds
     * @return
     *//*

    public List<BillSettleE> queryByBillIds(List<Long> billIds) {
        LambdaQueryWrapper<BillSettleE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BillSettleE::getBillId, billIds);
        queryWrapper.orderByDesc(BillSettleE::getGmtCreate);
        return baseMapper.selectList(queryWrapper);
    }

    */
/**
     * 根据账单ids获取账单结算方式
     * @param list
     * @return
     *//*

    public List<BillSettleChannelV> listBillSettleChannelByIds(List<Long> list) {
        return baseMapper.listBillSettleChannelByIds(list);
    }

    */
/**
     * 根据账单ids和结算方式获取对应的账单ids
     * @param form
     * @return
     *//*

    public List<Long> listBillIdsByIdsAndChannel(SettleChannelAndIdsF form) {
        LambdaQueryWrapper<BillSettleE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BillSettleE::getBillId, form.getBillIds());
        JSONObject channel = JSON.parseObject(form.getParams());
        Integer method = channel.getInteger("method");
        if (method == 1) {
            queryWrapper.eq(BillSettleE::getSettleChannel, channel.getJSONArray("value").getString(0));
        } else if (method == 2) {
            queryWrapper.in(BillSettleE::getSettleChannel, channel.getJSONArray("value").toJavaList(String.class));
        } else if (method == 3) {
            queryWrapper.notIn(BillSettleE::getSettleChannel, channel.getJSONArray("value").toJavaList(String.class));
        }
        List<BillSettleE> billSettleES = baseMapper.selectList(queryWrapper);
        return billSettleES.stream().map(BillSettleE::getBillId).collect(Collectors.toList());
    }

    */
/**
     * 根据票据id获取结算记录
     *
     * @param invoiceReceiptId
     * @return
     *//*

    public List<BillSettleE> listByInvoiceReceiptId(Long invoiceReceiptId) {
        LambdaQueryWrapper<BillSettleE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BillSettleE::getInvoiceReceiptId, invoiceReceiptId);
        queryWrapper.orderByDesc(BillSettleE::getGmtCreate);
        return baseMapper.selectList(queryWrapper);
    }

}
*/
