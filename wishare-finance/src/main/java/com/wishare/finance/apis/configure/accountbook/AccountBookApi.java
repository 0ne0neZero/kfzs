package com.wishare.finance.apis.configure.accountbook;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wishare.finance.apps.model.configure.accountbook.dto.AccountBookDtoStr;
import com.wishare.finance.apps.model.configure.accountbook.dto.AccountbookDTO;
import com.wishare.finance.apps.model.configure.accountbook.fo.AddAccountBookF;
import com.wishare.finance.apps.model.configure.accountbook.fo.UpdateAccountBookF;
import com.wishare.finance.apps.service.configure.accountbook.AccountBookAppService;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookAssociationE;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.accountbook.repository.mapper.AccountBookAssociationMapper;
import com.wishare.finance.domains.configure.accountbook.repository.mapper.AccountBookMapper;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xujian
 * @date 2022/8/18
 * @Description:
 */
@Api(tags = {"账簿管理"})
@RestController
@RequestMapping("/accountBook")
@RequiredArgsConstructor
public class AccountBookApi implements ApiBase {

    private final AccountBookAppService accountBookAppService;

    private final AccountBookAssociationMapper accountBookAssociationMapper;
    private final AccountBookMapper accountBookMapper;

    @PostMapping("/page")
    @ApiOperation(value = "分页查询账簿列表", notes = "分页查询账簿列表")
    public PageV<AccountBookDtoStr> queryPage(@Validated @RequestBody PageF<SearchF<?>> form) {
        return accountBookAppService.queryPage(form);
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询账簿列表", notes = "查询账簿列表")
    public List<AccountBookDtoStr> queryList(@Validated @RequestBody SearchF<?> form) {
        return accountBookAppService.queryList(form);
    }

    @ApiOperation(value = "新增账簿", notes = "新增账簿")
    @PostMapping("/add")
    public Long addAccountBook(@RequestBody @Validated AddAccountBookF form) {
        return accountBookAppService.addAccountBook(form);
    }

    @ApiOperation(value = "同步账簿", notes = "同步账簿")
    @PostMapping("/sync")
    public Boolean syncAccountBook(@RequestBody @Validated @Size(max = 200, message = "同步的账簿最大数量不能大于200") List<AddAccountBookF> addAccountBookFS) {
        return CollectionUtils.isEmpty(addAccountBookFS) ? false : accountBookAppService.syncAccountBook(addAccountBookFS);
    }

    @ApiOperation(value = "手动同步账簿", notes = "手动同步账簿")
    @PostMapping("/clickSync")
    public Map<String,Object> clickSyncAccountBook() {
        return accountBookAppService.clickSyncAccountBook();
    }

    @ApiOperation(value = "修改账簿", notes = "修改账簿")
    @PostMapping("/update")
    public Long updateAccountBook(@RequestBody @Validated UpdateAccountBookF form) {
        return accountBookAppService.updateAccountBook(form);
    }

    @ApiOperation(value = "删除账簿", notes = "删除账簿")
    @DeleteMapping("/delete/{id}")
    public Boolean deleteAccountBook(@PathVariable("id") Long id) {
        return accountBookAppService.deleteAccountBook(id);
    }

    @ApiOperation(value = "根据id启用或禁用账簿", notes = "根据id启用或禁用账簿")
    @PostMapping("/enable/{id}/{disableState}")
    public Boolean enable(@PathVariable("id") Long id, @PathVariable("disableState") Integer disableState) {
        if (null == disableState || null == DataDisabledEnum.valueOfByCode(disableState)) {
            throw BizException.throw400(ErrMsgEnum.DISABLE_STATE_EXCEPTION.getErrMsg());
        }
        return accountBookAppService.enable(id, disableState);
    }

    @ApiOperation(value = "根据id获取账簿详情", notes = "根据id获取账簿详情")
    @GetMapping("/detail/{id}")
    public AccountbookDTO detailAccountBook(@PathVariable("id") Long id) {
        AccountbookDTO result = accountBookAppService.detailAccountBook(id);
        return result;
    }

    @ApiOperation(value = "根据名称搜索账簿", notes = "根据名称搜索账簿")
    @GetMapping("/search")
    public List<AccountBookE> searchAccountBook(@RequestParam(value = "name", required = false, defaultValue = "") String name) {

        return accountBookAppService.searchAccountBook(name);
    }

    @ApiOperation(value = "根据费项+成本中心+法定单位查询账簿", notes = "根据费项+成本中心+法定单位查询账簿")
    @GetMapping("/selectListByCostChargeStatutory")
    public List<AccountBookE> selectListByCostChargeStatutory(@RequestParam(value = "costCenterId") Long costCenterId,
                                                            @RequestParam(value = "chargeItemId") Long chargeItemId,
                                                            @RequestParam(value = "statutoryBodyId") Long statutoryBodyId) {
        return accountBookAppService.selectListByCostChargeStatutory(costCenterId,chargeItemId,statutoryBodyId);
    }


    @GetMapping("/haveBooks")
    @ApiOperation(value = "用户账簿配置信息", notes = "用户账簿配置信息")
    public AccountBookAssociationE haveAccountBooks(@RequestParam  @NotNull(message = "userId为空") Long userId) {
        return accountBookAssociationMapper.selectOne(Wrappers.<AccountBookAssociationE>lambdaQuery().eq(AccountBookAssociationE::getUserId, userId));
    }

    @GetMapping("/books")
    @ApiOperation(value = "该用户存在的账簿列表", notes = "该用户存在的账簿列表")
    public List<AccountBookMiniV> books(@RequestParam  @NotNull(message = "userId为空") Long userId) {
        AccountBookAssociationE one = accountBookAssociationMapper.selectOne(Wrappers.<AccountBookAssociationE>lambdaQuery().eq(AccountBookAssociationE::getUserId, userId));
        if (ObjectUtil.isNull(one) || one.getAssociation()==2){
            return Collections.emptyList();
        }

        List<AccountBookE> selectList = accountBookMapper.selectList(Wrappers.<AccountBookE>lambdaQuery()
                        .select(AccountBookE::getId,AccountBookE::getName)
                .in(one.getAssociation()==1,AccountBookE::getId, StrUtil.split(one.getAccountBookIds(), ","))
        );
        ArrayList<AccountBookMiniV> list = new ArrayList<>(selectList.size());
        for (AccountBookE e : selectList) {
            list.add(AccountBookMiniV.builder().accountBookId(e.getId()).accountBookName(e.getName()).build());
        }
        return list;
    }


    @PostMapping("/editUserBooks")
    @ApiOperation(value = "编辑或者新增用户账簿配置", notes = "编辑或者新增用户账簿配置")
    public Boolean editUserBooks(@RequestBody @Validated AccountBookAssociationV dto) {
        if (dto.getAssociation()==1 && StrUtil.isBlank(dto.getAccountBookIds())){
            throw new BizException(402,"用户参数错误");
        }
        if (ObjectUtil.isNotNull(dto.getId())){
            accountBookAssociationMapper.updateById(AccountBookAssociationE.builder().id(dto.getId())
                            .association(dto.getAssociation()).accountBookIds(dto.getAccountBookIds())
                    .build());
        }else {

            boolean exists = accountBookAssociationMapper.exists(Wrappers.<AccountBookAssociationE>lambdaQuery().eq(AccountBookAssociationE::getUserId, dto.getUserId()));
            if (exists){
                throw new BizException(402,"该用户账簿信息已存在");
            }
            accountBookAssociationMapper.insert(AccountBookAssociationE.builder().userId(dto.getUserId())
                    .association(dto.getAssociation()).accountBookIds(dto.getAccountBookIds())
                    .build());
        }
        return true;
    }
}
