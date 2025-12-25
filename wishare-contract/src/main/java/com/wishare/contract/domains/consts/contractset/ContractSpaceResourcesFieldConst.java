package com.wishare.contract.domains.consts.contractset;

import java.util.Arrays;
import java.util.List;
/**
* <p>
* ContractSpaceResources 表字段常量 <br>
*
* id,contractId,tenantId,code,name,category,position,businessType,pricingStandard,resourceRates,quantityArea,totalPrice,startTime,endTime,state,creator,creatorName,gmtCreate,operator,operatorName,gmtModify,deleted
* </p>
*
* @author wangrui
* @since 2022-12-28
*/
public interface ContractSpaceResourcesFieldConst {

    /**
     * 主键
     */
    String ID = "id";

    /**
     * 合同id
     */
    String CONTRACT_ID = "contractId";

    /**
     * 租户id
     */
    String TENANT_ID = "tenantId";

    /**
     * 资源编码
     */
    String CODE = "code";

    /**
     * 资源名称
     */
    String NAME = "name";

    /**
     * 资源分类
     */
    String CATEGORY = "category";

    /**
     * 资源位置
     */
    String POSITION = "position";

    /**
     * 资源业态
     */
    String BUSINESS_TYPE = "businessType";

    /**
     * 定价标准
     */
    String PRICING_STANDARD = "pricingStandard";

    /**
     * 资源单价
     */
    String RESOURCE_RATES = "resourceRates";

    /**
     * 数量/面积
     */
    String QUANTITY_AREA = "quantityArea";

    /**
     * 总价（元）
     */
    String TOTAL_PRICE = "totalPrice";

    /**
     * 开始日期
     */
    String START_TIME = "startTime";

    /**
     * 结束日期
     */
    String END_TIME = "endTime";

    /**
     * 资源状态
     */
    String STATE = "state";

    /**
     * 创建人
     */
    String CREATOR = "creator";

    /**
     * 创建人名称
     */
    String CREATOR_NAME = "creatorName";

    /**
     * 创建时间
     */
    String GMT_CREATE = "gmtCreate";

    /**
     * 操作人
     */
    String OPERATOR = "operator";

    /**
     * 操作人名称
     */
    String OPERATOR_NAME = "operatorName";

    /**
     * 操作时间
     */
    String GMT_MODIFY = "gmtModify";

    /**
     * 是否删除  0 正常 1 删除
     */
    String DELETED = "deleted";


    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     * 请不要直接添加或者删除该列表中的内容转而使用apache下的CollectionUtils.retainAll()进行交集操作
     */
    List<String> ALLOW_FIELDS = Arrays.asList(ID,
                                    CONTRACT_ID,
                                    TENANT_ID,
                                    CODE,
                                    NAME,
                                    CATEGORY,
                                    POSITION,
                                    BUSINESS_TYPE,
                                    PRICING_STANDARD,
                                    RESOURCE_RATES,
                                    QUANTITY_AREA,
                                    TOTAL_PRICE,
                                    START_TIME,
                                    END_TIME,
                                    STATE,
                                    CREATOR,
                                    CREATOR_NAME,
                                    GMT_CREATE,
                                    OPERATOR,
                                    OPERATOR_NAME,
                                    GMT_MODIFY,
                                    DELETED);
    /**
     *
     * 可以被前端选择展示的字段列表，默认是表的全部字段，
     */
    String[] ALLOW_FIELDS_ARRAY = ALLOW_FIELDS.toArray(new String[0]);
}
