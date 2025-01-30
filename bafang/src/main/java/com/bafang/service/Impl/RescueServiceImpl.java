package com.bafang.service.Impl;

import com.bafang.common.BaseContext;
import com.bafang.domain.dto.RankDTO;
import com.bafang.domain.po.RescueInfo;
import com.bafang.domain.vo.RescueInfoVO;
import com.bafang.mapper.RescueInfoMapper;
import com.bafang.service.IRescueService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RescueServiceImpl extends ServiceImpl<RescueInfoMapper, RescueInfo> implements IRescueService {

    private final RescueInfoMapper rescueInfoMapper;

    @Override
    public RescueInfo getByHelpId(Long id) {
        LambdaQueryWrapper<RescueInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RescueInfo::getHelpId, id)
                .isNull(RescueInfo::getCompletedAt);
        return this.getOne(wrapper,false);
    }

    @Override
    public List<RankDTO> getRank() {
        return rescueInfoMapper.getRank();
    }

    @Override
    public List<RescueInfoVO> getRescueInfo() {
        return rescueInfoMapper.getRescueInfo(BaseContext.getCurrentId());
    }
}
