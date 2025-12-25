package com.wishare.finance.domains.invoicereceipt.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.accountbook.repository.mapper.AccountBookMapper;
import com.wishare.finance.domains.invoicereceipt.entity.invoicebook.InvoiceReceiveE;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author fufengwen
 * @date 2022/8/18
 * @Description:
 */
@Component
@Slf4j
public class InvoiceReceiveOriginMapper {

    @Autowired
    private DataSource dataSource;

    /**
     *
     * @param communityId
     * @param type
     * @return
     */
    public List<InvoiceReceiveE> getIdByCommunity(@Param("communityId") String communityId,
        @Param("type") Integer type){
        log.info("=======查询使用原生JDBC执行=======");
        // 创建 Configuration 对象
        Configuration configuration = new Configuration();
        configuration.setEnvironment(new Environment("development", new JdbcTransactionFactory(), dataSource));

        // 注册 Mapper 接口
        configuration.addMapper(InvoiceReceiveMapper.class);

        // 构建 SqlSessionFactory 对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        // 使用 SqlSessionFactory 创建 SqlSession 对象并执行查询
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            InvoiceReceiveMapper invoiceReceiveMapper = sqlSession.getMapper(InvoiceReceiveMapper.class);
            List<InvoiceReceiveE> result = invoiceReceiveMapper.getIdByCommunity(communityId, type);
            if(CollectionUtils.isEmpty(result)) {
                return new ArrayList<>();
            }
            return result;
        } catch (Exception e) {
            log.error("执行失败:{}", e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }



}
