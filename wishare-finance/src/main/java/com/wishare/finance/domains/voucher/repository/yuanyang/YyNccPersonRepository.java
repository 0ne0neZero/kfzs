package com.wishare.finance.domains.voucher.repository.yuanyang;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.voucher.entity.yuanyang.YyNccPersonE;
import com.wishare.finance.domains.voucher.repository.yuanyang.mapper.YyNccPersonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class YyNccPersonRepository extends ServiceImpl<YyNccPersonMapper, YyNccPersonE> {

}
