package com.wishare.finance.infrastructure.remote.clients.base;

import com.wishare.finance.infrastructure.remote.fo.msg.MsgSmsRf;
import com.wishare.finance.infrastructure.remote.fo.msg.NoticeBusinessD;
import com.wishare.finance.infrastructure.remote.vo.MsgTemplateV;
import com.wishare.starter.annotations.OpenFeignClient;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/10/20
 * @Description:
 */
@OpenFeignClient(name = "wishare-msg", serverName = "消息中心服务", path = "/msg")
public interface MsgClient {

    @PostMapping(value = "/sms",name = "发送短信")
    String sms(@RequestParam("tag") @ApiParam("tag") @NotNull(message = "tag") Integer tag,
             @RequestParam("suffix") @ApiParam("suffix") String suffix,
             @Validated @RequestBody MsgSmsRf msgSmsRf);

    @ApiOperation(value = "新增站内信", notes = "新增站内信")
    @PostMapping("/noticeBusiness/add")
    void add(@Validated @RequestBody NoticeBusinessD noticeBusinessD);


    @ApiOperation(value = "查询模板",notes = "查询模板")
    @PostMapping("/template/selectList")
    List<MsgTemplateV> selectList(@RequestBody SearchF<?> searchF);
}
