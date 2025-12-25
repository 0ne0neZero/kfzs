package com.wishare.finance.domains.invoicereceipt.repository.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptStatisticsDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptVDto;
import com.wishare.finance.domains.invoicereceipt.dto.ReceiptMessageDto;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description:
 */
@Mapper
public interface ReceiptMapper extends BaseMapper<ReceiptE> {

    /**
     * 分页查询收据列表
     *
     * @param of
     * @param queryModel
     * @return
     */
    Page<ReceiptDto> queryPage(Page<?> of, @Param("ew") QueryWrapper<?> queryModel);



    /**
     * 根据id查询 收据信息
     *
     * @param queryModel
     * @return
     */
    @InterceptorIgnore(tenantLine = "on")
    List<ReceiptVDto> queryByElement(@Param("ew") QueryWrapper<?> queryModel);


    /**
     * 统计收据信息
     *
     * @param queryModel
     * @return
     */
    ReceiptStatisticsDto statistics(@Param("ew") QueryWrapper<?> queryModel);


    /**
     * 通过账单ids和状态查询收据
     *
     * @param billIds
     * @param invoiceReceiptState
     */
    Integer getByBillIds(@Param("billIds") List<Long> billIds,@Param("invoiceReceiptState") Integer invoiceReceiptState);

    /**
     * 根据invoiceReceiptId获取收据推送所需信息
     * @param invoiceReceiptIds
     * @return
     */
    List<ReceiptMessageDto> getReceiptMessages(@Param("invoiceReceiptIds") List<Long> invoiceReceiptIds);

    /**
     * 查询正在签署和签署成功节点内的数据
     * ,LocalDateTime time
     */
    @InterceptorIgnore(tenantLine = "on")
    List<ReceiptE> signingInProgress(@Param("signSealStatusList")List<Integer> signSealStatusList,@Param("gmtCreate1") LocalDateTime gmtCreate1);

    /**
     * 查询正在作废、作废成功节点内的数据 还未发送短信
     * ,LocalDateTime time
     */
    @InterceptorIgnore(tenantLine = "on")
    List<ReceiptE> signingVoidProgress(@Param("signVoidStatusList")List<Integer> signVoidStatusList,@Param("gmtCreate1") LocalDateTime gmtCreate1);
    @InterceptorIgnore(tenantLine = "on")
    @Select("select * from receipt where sign_apply_no = #{signApplyNo}")
    ReceiptE queryBySignApplyNo(@Param("signApplyNo") String signApplyNo);

}
