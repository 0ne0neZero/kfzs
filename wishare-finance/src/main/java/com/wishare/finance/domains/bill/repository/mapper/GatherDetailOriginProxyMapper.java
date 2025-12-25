package com.wishare.finance.domains.bill.repository.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Slf4j
public class GatherDetailOriginProxyMapper {
    @Autowired
    private DataSource dataSource;
    public int updateHandRecState(@Param("billId") Long billId, @Param("code") int code,
                                               @Param("supCpUnitId") String supCpUnitId){
        log.info("=======查询使用原生JDBC执行=======");
        // 创建 Configuration 对象
        Configuration configuration = new Configuration();
        configuration.setEnvironment(new Environment("development", new JdbcTransactionFactory(), dataSource));

        // 注册 Mapper 接口
        configuration.addMapper(GatherDetailMapper.class);

        // 构建 SqlSessionFactory 对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        // 使用 SqlSessionFactory 创建 SqlSession 对象并执行查询
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            GatherDetailMapper gatherDetailMapper = sqlSession.getMapper(GatherDetailMapper.class);
            int i = gatherDetailMapper.updateHandRecState(billId, code, supCpUnitId);
            return i;
        } catch (Exception e) {
            log.error("执行失败:{}", e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}
