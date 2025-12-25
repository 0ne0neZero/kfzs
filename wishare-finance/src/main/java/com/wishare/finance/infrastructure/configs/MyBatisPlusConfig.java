package com.wishare.finance.infrastructure.configs;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.support.ApiData;
import com.wishare.finance.infrastructure.support.tenant.WishareTenantLineInnerInterceptor;
import com.wishare.starter.exception.SysException;
import com.wishare.starter.interfaces.ApiBase;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

/**
 * @Author 永遇乐 yeoman<76164451@.qq.com>
 * @line --------------------------------
 * @Date 2022/04/02
 */
@Slf4j
@MapperScan(value = "com.wishare.**.**.repository")
@Configuration
public class MyBatisPlusConfig implements ApiBase {
    //分页插件——新的分页插件，旧版本PaginationInterceptor失效了
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //interceptor.addInnerInterceptor(tenantLineInnerInterceptor());
        interceptor.addInnerInterceptor(new WishareTenantLineInnerInterceptor());
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    private TenantLineInnerInterceptor tenantLineInnerInterceptor() {
        return new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                String curTenantId = curIdentityInfo().getTenantId();
                return Objects.nonNull(curTenantId) ? new StringValue(curTenantId) : null;
            }

            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            @Override
            public boolean ignoreTable(String tableName) {
                return "file_save_info".equals(tableName);
            }
        });
    }

    /**
     * 数据库表主键id 生成器
     *
     * @return
     */
    @Bean
    public IdentifierGenerator identifierGenerator() {
        return new IdentifierGenerator() {
            private final HashMap<String, String> TABLE_MAP = new HashMap<>();

            @Override
            public Number nextId(Object entity) {
                Class<?> entityClass = entity.getClass();
                String tableName = TABLE_MAP.get(entityClass.getName());
                String className = entityClass.getName();
                if (StringUtils.isBlank(tableName)) {
                    if (!entityClass.isAnnotationPresent(TableName.class)) {
                        Class<?> superClass = entityClass.getSuperclass();
                        if (!superClass.isAnnotationPresent(TableName.class)) {
                            log.error("@TableName notFound,id can't generation,please config entity [@TableName.value]!");
                            throw SysException.throwOthers("系統错误，请联系统管理员！");
                        }
                        tableName = superClass.getAnnotation(TableName.class).value();
                        className = superClass.getName();
                    } else {
                        tableName = entityClass.getAnnotation(TableName.class).value();
                    }
                }
                if (StringUtils.isBlank(tableName)) {
                    log.error("@TableName value is blank,id can't generation,please config entity [@TableName.value]!");
                    throw SysException.throwOthers("系統错误，请联系统管理员！");
                }
                TABLE_MAP.put(className, tableName);
                return IdentifierFactory.getInstance().generateLongIdentifier(tableName);
            }
        };
    }


    @Bean
    @Primary
    public MetaObjectHandler metaObjectHandler() {
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                //创建人数据
                this.strictInsertFill(metaObject, "creator", () -> ApiData.API.getUserId().orElse("administrator"), String.class);
                this.strictInsertFill(metaObject, "creatorName", () -> ApiData.API.getUserName().orElse("系统默认"), String.class);
                //创建时间
                this.strictInsertFill(metaObject, "gmtCreate", LocalDateTime::now, LocalDateTime.class);

                //操作人数据
                this.strictInsertFill(metaObject, "operator", () -> ApiData.API.getUserId().orElse("administrator"), String.class);
                this.strictInsertFill(metaObject, "operatorName", () -> ApiData.API.getUserName().orElse("系统默认"), String.class);
                //操作时间
                this.strictInsertFill(metaObject, "gmtModify", LocalDateTime::now, LocalDateTime.class);

                //添加租户隔离
                this.strictInsertFill(metaObject, "tenantId", () -> ApiData.API.getTenantId().get(), String.class);
                //系统来源(先默认收费系统)
                this.strictInsertFill(metaObject, "sysSource", () -> 1, Integer.class);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
//                //操作人数据
//                this.strictInsertFill(metaObject, "operator", () -> apiData.getUserId().orElse("dxclay"), String.class);
//                this.strictInsertFill(metaObject, "operatorName", () -> apiData.getUserName().orElse("dxclay"), String.class);
//                //操作时间
//                this.strictInsertFill(metaObject, "gmtModify", LocalDateTime::now, LocalDateTime.class);

                /*
                    有坑:MetaObjectHandler提供的默认方法的策略均为: 如果属性有值则不覆盖, 如果填充值为null则不填充。
                 */
                //操作时间
                this.setFieldValByName("gmtModify",LocalDateTime.now(), metaObject);
                //操作人数据
                this.setFieldValByName("operator", ApiData.API.getUserId().orElse("administrator"), metaObject);
                this.setFieldValByName("operatorName", ApiData.API.getUserName().orElse("系统默认"), metaObject);
            }
        };
    }

}
