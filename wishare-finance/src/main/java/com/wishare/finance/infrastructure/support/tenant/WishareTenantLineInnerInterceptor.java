package com.wishare.finance.infrastructure.support.tenant;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.wishare.finance.infrastructure.support.ApiData;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;

/**
 * 慧享租户插件
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/17
 */
public class WishareTenantLineInnerInterceptor extends TenantLineInnerInterceptor {

    public WishareTenantLineInnerInterceptor() {
        super(defaultTenantLineHandler());
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        if (WishareTenantContext.withIgnore()){
            return;
        }
        super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
    }

    private static TenantLineHandler defaultTenantLineHandler(){
        return new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                return new StringValue(ApiData.API.getTenantId().orElse(""));
            }

            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            @Override
            public boolean ignoreTable(String tableName) {
                return "file_save_info".equals(tableName) || tableName.startsWith("export_tmp_");
            }
        };
    }

}
