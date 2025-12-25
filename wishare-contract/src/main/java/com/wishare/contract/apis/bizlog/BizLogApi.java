package com.wishare.contract.apis.bizlog;

import com.wishare.bizlog.apis.BizLoggerApi;
import com.wishare.bizlog.apis.BizLoggerVo;
import com.wishare.bizlog.repository.BizLogRepository;
import com.wishare.bizlog.starter.mybatisplus.repository.MybatisPlusBizLogRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 业务日志接口
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/10
 */
@Api(tags = {"业务日志"})
@Validated
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BizLogApi implements BizLoggerApi {

    private final MybatisPlusBizLogRepository mybatisPlusBizLogRepository;

    @Override
    public BizLogRepository bizLogRepository() {
        return mybatisPlusBizLogRepository;
    }

    @ApiOperation(value = "查询日志记录", notes = "查询日志记录")
    @GetMapping("/list")
    public List<BizLoggerVo> queryLog(@RequestParam("bizId") String bizId){
        return getByBizId(bizId);
    }

}
