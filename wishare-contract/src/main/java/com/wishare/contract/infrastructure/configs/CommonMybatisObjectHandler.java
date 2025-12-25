//package com.wishare.contract.infrastructure.configs;
//
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import com.wishare.starter.utils.ThreadLocalUtil;
//import org.apache.ibatis.reflection.MetaObject;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Component
//public class CommonMybatisObjectHandler implements MetaObjectHandler {
//
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        this.setFieldValByName("gmtModify", LocalDateTime.now(), metaObject);
//        this.setFieldValByName("gmtCreate",LocalDateTime.now(), metaObject);
//        this.setFieldValByName("creator", ThreadLocalUtil.curIdentityInfo().getUserId(), metaObject);
//        this.setFieldValByName("operator", ThreadLocalUtil.curIdentityInfo().getUserId(), metaObject);
//        this.setFieldValByName("creatorName", ThreadLocalUtil.curIdentityInfo().getUserName(), metaObject);
//        this.setFieldValByName("operatorName", ThreadLocalUtil.curIdentityInfo().getUserName(), metaObject);
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        this.setFieldValByName("gmtModify", LocalDateTime.now(), metaObject);
//        this.setFieldValByName("operator", ThreadLocalUtil.curIdentityInfo().getUserId(), metaObject);
//        this.setFieldValByName("operatorName", ThreadLocalUtil.curIdentityInfo().getUserName(), metaObject);
//    }
//}
