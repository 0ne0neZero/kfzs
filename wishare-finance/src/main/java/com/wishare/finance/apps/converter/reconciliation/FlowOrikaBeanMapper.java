package com.wishare.finance.apps.converter.reconciliation;

import com.wishare.finance.apps.model.reconciliation.fo.ClaimFlowF;
import com.wishare.finance.domains.reconciliation.command.ClaimFlowCommand;
import com.wishare.finance.domains.reconciliation.entity.FlowDetailE;
import com.wishare.finance.domains.reconciliation.enums.FlowTypeStatusEnum;
import com.wishare.finance.infrastructure.easyexcel.FlowDetailData;
import com.wishare.starter.Global;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 账单orika字段映射
 *
 * @author yancao
 */
@Configuration
@AutoConfigureAfter(MapperFactory.class)
public class FlowOrikaBeanMapper implements CommandLineRunner {

    @Override
    public void run(String... args) {

        //导入流水金额转Long，类型转为数值
        Global.mapperFactory.classMap(FlowDetailData.class, FlowDetailE.class).exclude("settleAmount").exclude("type").byDefault().customize(
                new CustomMapper<>() {
                    @Override
                    public void mapAtoB(FlowDetailData data, FlowDetailE eo, MappingContext context) {
                        eo.setSettleAmount(data.getSettleAmount().multiply(BigDecimal.valueOf(100)).longValue());
                        FlowTypeStatusEnum flowTypeStatusEnum = FlowTypeStatusEnum.valueOfByName(data.getType());
                        if(Objects.nonNull(flowTypeStatusEnum)){
                            eo.setType(flowTypeStatusEnum.getCode());
                        }
                    }
                }).register();

        //流水认领金额转Long
        Global.mapperFactory.classMap(ClaimFlowF.class, ClaimFlowCommand.class).exclude("claimAmount").byDefault().customize(
                new CustomMapper<>() {
                    @Override
                    public void mapAtoB(ClaimFlowF fo, ClaimFlowCommand co, MappingContext context) {
                        co.setClaimAmount(fo.getClaimAmount().multiply(BigDecimal.valueOf(100)).longValue());
                    }
                }).register();
    }

}
