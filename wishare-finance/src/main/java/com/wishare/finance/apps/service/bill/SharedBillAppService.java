package com.wishare.finance.apps.service.bill;

import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.infrastructure.conts.ShareJdbcConst;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yyx
 * @date 2023/7/11 11:44
 * 账单分表业务处理
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SharedBillAppService {

    @Value(value = "${spring.shardingsphere.rules.sharding.sharding-algorithms.receivable-bill-algorithms.props.sharding-count:256}")
    private Integer receiveableBillShardingJdbcSize;

    @Value(value = "${spring.shardingsphere.rules.sharding.sharding-algorithms.gather-bill-algorithms.props.sharding-count:256}")
    private Integer gatherBillShardingJdbcSize;

    @Value(value = "${spring.shardingsphere.rules.sharding.sharding-algorithms.gather-detail-algorithms.props.sharding-count:256}")
    private Integer gatherDetailShardingJdbcSize;

    @Value(value = "${spring.shardingsphere.rules.sharding.sharding-algorithms.bill-account-hand-algorithms-algorithms.props.sharding-count:256}")
    private Integer billAccountHandShardingJdbcSize;


    @Value(value = "${spring.shardingsphere.rules.sharding.sharding-algorithms.bill-approve-algorithms.props.sharding-count:256}")
    private Integer billApproveShardingJdbcSize;

    /**
     * 根据表名获取该表的分表数量
     * @param tableName
     * @return
     */
    private Integer getShardingJdbcSizeByTableName(String tableName) {
        switch (tableName) {
            case TableNames.RECEIVABLE_BILL: return receiveableBillShardingJdbcSize;
            case TableNames.GATHER_BILL: return gatherBillShardingJdbcSize;
            case TableNames.GATHER_DETAIL: return gatherDetailShardingJdbcSize;
            case TableNames.BILL_ACCOUNT_HAND: return billAccountHandShardingJdbcSize;
            case TableNames.BILL_APPROVE: return billApproveShardingJdbcSize;
            default: throw new IllegalArgumentException("不支持的账单分表类型!");
        }
    }

    /**
     * 根据表名和分表字段获取分表名称
     * @param form 参数
     * @return {@link String}
     */
    public String getShareTableName(PageF<SearchF<?>> form, String tableName, String shardingColumnName) {
        if(StringUtils.isBlank(tableName)) {
            throw new IllegalArgumentException("请传入表名!");
        }
        if(StringUtils.isBlank(shardingColumnName)) {
            throw new IllegalArgumentException("请传入分表字段!");
        }
        List<Field> collect = form.getConditions().getFields().stream().filter(a -> shardingColumnName.equals(a.getName())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(collect)) {
            throw new IllegalArgumentException("查询条件不含该字段:" + shardingColumnName + " !");
        }
        Field field = collect.get(0);
        return tableName + "_" +Math.abs(field.getValue().toString().hashCode() % getShardingJdbcSizeByTableName(tableName));
    }

    /**
     * 手动获取具体表名称
     * @param form 参数
     * @return {@link String}
     */
    public String getShareTableName(SearchF<?> form, String tableName, String shardingColumnName) {
        if(StringUtils.isBlank(tableName)) {
            throw new IllegalArgumentException("请传入表名!");
        }
        if(StringUtils.isBlank(shardingColumnName)) {
            throw new IllegalArgumentException("请传入分表字段!");
        }
        List<Field> collect = form.getFields().stream().filter(a -> shardingColumnName.equals(a.getName())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(collect)) {
            throw new IllegalArgumentException("查询条件不含该字段:" + shardingColumnName + " !");
        }
        Field field = collect.get(0);
        return tableName + "_" +Math.abs(field.getValue().toString().hashCode() % getShardingJdbcSizeByTableName(tableName));
    }

    /**
     * 手动获取具体表名称
     * @param supCpUnitId 参数
     * @return {@link String}
     */
    public String getShareTableName(String supCpUnitId, String tableName) {
        if(StringUtils.isBlank(tableName)) {
            throw new IllegalArgumentException("请传入表名!");
        }
        return tableName + "_" +Math.abs(supCpUnitId.hashCode() % getShardingJdbcSizeByTableName(tableName));
    }

//    public static void main(String[] args) {
//        System.out.println(Math.abs("67ccf0a97607cb68f6461c598a447516".hashCode() % 256));
//    }

}
