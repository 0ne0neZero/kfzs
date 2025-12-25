package com.wishare.finance.domains.voucher.repository.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import com.wishare.finance.domains.voucher.entity.CloseAccount;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CloseAccountOriginMapper {

    @Autowired
    private DataSource dataSource;

    /**
     * 批量插入，绕过shardingjdbc
     * @param closeAccounts
     */
    public void saveBatch(Collection<CloseAccount> closeAccounts) {
        Configuration configuration = new Configuration();
        configuration.setEnvironment(new Environment("development", new JdbcTransactionFactory(), dataSource));
        // 注册 Mapper 接口
        configuration.addMapper(CloseAccountMapper.class);
        // 构建 SqlSessionFactory 对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        // 使用 SqlSessionFactory 创建 SqlSession 对象并执行查询
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CloseAccountMapper closeAccountMapper = sqlSession.getMapper(CloseAccountMapper.class);
            closeAccountMapper.saveBatchCloseAccounts(closeAccounts);
            sqlSession.commit();
        } catch (Exception e) {
            log.error("批量插入关账记录失败:", e);
            throw e;
        }
    }

}
