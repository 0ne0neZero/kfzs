package com.wishare.finance.domains.bill.repository.proxy;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OriginProxyMapper<T> {
    @Autowired
    private DataSource dataSource;

    /**
     * 注册并获取mapper实例
     * @param
     * @return
     */
    public Object registerMapper(Class mapperClass) {
        Configuration configuration = new Configuration();
        configuration.setEnvironment(new Environment("development", new JdbcTransactionFactory(), dataSource));
        // 注册 Mapper 接口
        configuration.addMapper(mapperClass);
        // 构建 SqlSessionFactory 对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        // 使用 SqlSessionFactory 创建 SqlSession 对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.getMapper(mapperClass);
        } catch (Exception e) {
            log.error("获取mapper失败:{}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
