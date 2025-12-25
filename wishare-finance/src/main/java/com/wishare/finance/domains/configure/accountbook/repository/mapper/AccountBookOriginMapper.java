package com.wishare.finance.domains.configure.accountbook.repository.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
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
 * @author xujian
 * @date 2022/8/18
 * @Description: 账簿mapper
 */
@Component
@Slf4j
public class AccountBookOriginMapper{

    @Autowired
    private DataSource dataSource;

    /**
     * 分页查询账簿列表
     *
     * @param page
     * @param queryModel
     * @return
     */
    public Page<AccountBookE> queryPage(Page<?> page, @Param("ew") QueryWrapper<?> queryModel){
        log.info("=======分页查询账簿列表使用原生JDBC执行=======");
        // 创建 Configuration 对象
        Configuration configuration = new Configuration();
        configuration.setEnvironment(new Environment("development", new JdbcTransactionFactory(), dataSource));

        // 注册 Mapper 接口
        configuration.addMapper(AccountBookMapper.class);

        // 构建 SqlSessionFactory 对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        // 使用 SqlSessionFactory 创建 SqlSession 对象并执行查询
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            AccountBookMapper accountBookMapper = sqlSession.getMapper(AccountBookMapper.class);
            List<AccountBookE> result = accountBookMapper.queryPage(page, queryModel);
            if(CollectionUtils.isEmpty(result)) {
                return new Page<>();
            }
            Page r = new Page();
            r.setRecords(result);
            r.setTotal(result.size());
            return r;
        } catch (Exception e) {
            log.error("执行失败:{}", e.getMessage());
            e.printStackTrace();
        }
        return new Page<>();
    }



}
