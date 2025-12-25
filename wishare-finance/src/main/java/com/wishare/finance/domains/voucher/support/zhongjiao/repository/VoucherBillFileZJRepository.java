package com.wishare.finance.domains.voucher.support.zhongjiao.repository;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.process.enums.BusinessProcessType;
import com.wishare.finance.apps.process.service.DxJsBillProcessService;
import com.wishare.finance.apps.pushbill.fo.UpLoadFileF;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.FinanceProcessRecordZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherBillFileZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.entity.VoucherPushBillDetailDxZJ;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.PushBillApproveStateEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.enums.ZJTriggerEventBillTypeEnum;
import com.wishare.finance.domains.voucher.support.zhongjiao.repository.mapper.VoucherBillFileZJMapper;
import com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder.PushBillZJDomainService;
import com.wishare.finance.infrastructure.remote.clients.base.ChargeClient;
import com.wishare.finance.infrastructure.remote.enums.OperateTypeEnum;
import com.wishare.finance.infrastructure.remote.fo.bpm.ProcessStartF;
import com.wishare.finance.infrastructure.remote.vo.bpm.WflowModelHistorysV;
import com.wishare.finance.infrastructure.remote.vo.charge.ApproveFilter;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.tools.starter.vo.FileVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class VoucherBillFileZJRepository extends ServiceImpl<VoucherBillFileZJMapper, VoucherBillFileZJ> {
    @Resource
    private VoucherPushBillDxZJRepository voucherPushBillDxZJRepository;
    @Autowired
    private VoucherBillDetailDxZJRepository voucherBillDetailDxZJRepository;
    @Autowired
    @Lazy
    private PushBillZJDomainService pushBillZJDomainService;
    @Value("${finance.devFlag:0}")
    private Integer devFlag;
    @Autowired
    private ChargeClient chargeClient;
    @Autowired
    @Lazy
    private FinanceProcessRecordZJRepository financeProcessRecordZJRepository;
    @Autowired
    @Lazy
    private DxJsBillProcessService processService;

    public boolean addFileInfo(UpLoadFileF upLoadFileF) {
        this.removeByBillId(upLoadFileF.getVoucherBillId());
        for (FileVo file : upLoadFileF.getFiles()) {
            VoucherBillFileZJ voucherBillFileZJ = new VoucherBillFileZJ();
            voucherBillFileZJ.setVoucherBillId(upLoadFileF.getVoucherBillId());
            voucherBillFileZJ.setVoucherBillNo(upLoadFileF.getVoucherBillNo());
            voucherBillFileZJ.setFiles(JSON.toJSONString(file));
            if (null != upLoadFileF.getUploadFlag()){
                voucherBillFileZJ.setUploadFlag(upLoadFileF.getUploadFlag());
            } else {
                voucherBillFileZJ.setUploadFlag(0);
            }
            baseMapper.insert(voucherBillFileZJ);
        }
        if(StringUtils.isNotBlank(upLoadFileF.getReceiptRemark())){
            UpdateWrapper<VoucherBillDxZJ> wrapper = new UpdateWrapper<>();
            wrapper.set("receipt_remark", upLoadFileF.getReceiptRemark())
                    .eq("id", upLoadFileF.getVoucherBillId());
            voucherPushBillDxZJRepository.update(null, wrapper);
        }
        //跟新推送部门code
        if(StringUtils.isNotBlank(upLoadFileF.getExternalDepartmentCode())){
            UpdateWrapper<VoucherBillDxZJ> wrapper = new UpdateWrapper<>();
            wrapper.set("external_department_code", upLoadFileF.getExternalDepartmentCode())
                    .set("calculation_method", upLoadFileF.getCalculationMethod())
                    .eq("id", upLoadFileF.getVoucherBillId());
            voucherPushBillDxZJRepository.update(null, wrapper);
        }
        //计税方式
        if(Objects.nonNull(upLoadFileF.getCalculationMethod())){
            UpdateWrapper<VoucherBillDxZJ> wrapper = new UpdateWrapper<>();
            wrapper.set("calculation_method", upLoadFileF.getCalculationMethod())
                    .set("tax_type", upLoadFileF.getCalculationMethod())
                    .eq("id", upLoadFileF.getVoucherBillId());
            voucherPushBillDxZJRepository.update(null, wrapper);

            UpdateWrapper<VoucherPushBillDetailDxZJ> detwrapper = new UpdateWrapper<>();
            detwrapper.set("tax_type", upLoadFileF.getCalculationMethod())
                    .eq("voucher_bill_no", upLoadFileF.getVoucherBillNo());
            voucherBillDetailDxZJRepository.update(null, detwrapper);
        }

        //根据报账单号查询详情数据
        VoucherBillDxZJ voucherBill = voucherPushBillDxZJRepository.getVoucherBillDxZJByQuery(upLoadFileF.getVoucherBillNo());
        if(Objects.nonNull(voucherBill) && (voucherBill.getBillEventType().equals(ZJTriggerEventBillTypeEnum.对下结算计提.getCode()) || voucherBill.getBillEventType().equals(ZJTriggerEventBillTypeEnum.收入确认计提.getCode())  || voucherBill.getBillEventType().equals(ZJTriggerEventBillTypeEnum.收入确认实签.getCode()))){
            //测试环境，因发发起流程不联通，直接模拟 数据
            if(devFlag == 1){
                log.info("测试环境，模拟数据直接跳出");
                ApproveFilter approveFilter = chargeClient.getApprovePushBillFilter(voucherBill.getCommunityId(),
                        voucherBill.getBillEventType());
                voucherBill.setProcInstId(LocalDateTime.now().toString());
                voucherBill.setApproveState(PushBillApproveStateEnum.审核中.getCode());
                voucherBill.setPushState(4);
                voucherPushBillDxZJRepository.updateById(voucherBill);
                return true;
            }
            try {
                if(voucherBill.getBillEventType().equals(ZJTriggerEventBillTypeEnum.收入确认实签.getCode())){
                    FinanceProcessRecordZJ record = financeProcessRecordZJRepository.getOne(Wrappers.<FinanceProcessRecordZJ>lambdaQuery()
                            .eq(FinanceProcessRecordZJ::getMainDataId, voucherBill.getId())
                            .eq(FinanceProcessRecordZJ::getType, BusinessProcessType.SRQR_SQ_FORM.getCode())
                            .eq(FinanceProcessRecordZJ::getIsJsdProcess, 1)
                            .eq(FinanceProcessRecordZJ::getDeleted, 0)
                    );
                    if(Objects.nonNull(record)){
                        processService.createProcess(voucherBill.getId(), BusinessProcessType.SRQR_SQ_FORM);
                        return true;
                    }
                }

            //上传附件之后，自动发起审批流程
            pushBillZJDomainService.initiateApprove(voucherBill, voucherBill.getCommunityId(), voucherBill.getCommunityName(),
                    voucherBill.getBillEventType().equals(ZJTriggerEventBillTypeEnum.对下结算计提.getCode()) ? OperateTypeEnum.对下结算计提单 :
                            voucherBill.getBillEventType().equals(ZJTriggerEventBillTypeEnum.收入确认计提.getCode()) ? OperateTypeEnum.收入确认计提单 :
                                    OperateTypeEnum.收入确认实签单
            );
            } catch (Exception e){
                log.info("上传附件之后，自动发起审批流程发起异常：{}",e.getMessage());
                throw new OwlBizException("流程发起超时，请稍后重试！");
            }
        }
        return true;
    }

    public List<VoucherBillFileZJ> selectBySearch(PageF<SearchF<?>> form){
       return baseMapper.selectBySearch(RepositoryUtil.putLogicDeleted(form.getConditions().getQueryModel()).orderByDesc("id"));
    }
    public List<VoucherBillFileZJ> selectByVoucherBillId(Long voucherBillId){
        return baseMapper.selectByVoucherBillId(voucherBillId);
    }
    public List<VoucherBillFileZJ> selectByVoucherBillNo(String voucherBillNo){
        return baseMapper.selectByVoucherBillNo(voucherBillNo);
    }

    //根据报账单ID获取附件列表
    public List<VoucherBillFileZJ> getByBillIdList(List<Long> billIdList){
        return baseMapper.getByBillIdList(billIdList);
    }


    public Boolean deleteById(Long id){
        return baseMapper.deleteById(id) > 0;
    }

    public void removeByBillId(Long id) {
        this.remove(new LambdaQueryWrapper<VoucherBillFileZJ>().eq(VoucherBillFileZJ::getVoucherBillId, id));
    }
}
