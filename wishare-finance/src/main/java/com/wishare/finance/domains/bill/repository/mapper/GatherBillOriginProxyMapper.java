package com.wishare.finance.domains.bill.repository.mapper;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.bill.dto.PayListDto;
import com.wishare.finance.domains.bill.repository.proxy.OriginProxyMapper;
import java.util.List;
import javax.sql.DataSource;
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

@Component
@Slf4j
public class GatherBillOriginProxyMapper{
    @Autowired
    private DataSource dataSource;
    public Page<PayListDto> payInvoiceList(Page<Object> page,
        @Param(Constants.WRAPPER) QueryWrapper<?> queryWrapper,
        @Param("gatherBillTableName") String gatherBillTableName,
        @Param("gatherDetailTableName") String gatherDetailTableName){
        Configuration configuration = new Configuration();
        configuration.setEnvironment(new Environment("development", new JdbcTransactionFactory(), dataSource));
        // 注册 Mapper 接口
        configuration.addMapper(GatherBillMapper.class);
        // 构建 SqlSessionFactory 对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        // 使用 SqlSessionFactory 创建 SqlSession 对象
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            GatherBillMapper mapper = sqlSession.getMapper(GatherBillMapper.class);
            log.info("分页参数:current:{},size:{}",page.getCurrent(), page.getSize());
            List<PayListDto> payListDtoPage = mapper.payInvoiceList(page, queryWrapper,
                gatherBillTableName, gatherDetailTableName);
            Integer total = mapper.payInvoiceListCnt(queryWrapper,
                gatherBillTableName, gatherDetailTableName);
            Page<PayListDto> result = new Page<>(page.getCurrent(), page.getSize());
            result.setTotal(total);
            result.setRecords(payListDtoPage);
            return result;
        } catch (Exception e) {
            log.error("获取mapper失败:{}", e.getMessage());
            e.printStackTrace();
        }
        return new Page<>(page.getCurrent(), page.getSize());

    }
}
