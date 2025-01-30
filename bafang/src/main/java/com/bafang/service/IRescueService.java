package com.bafang.service;

import com.bafang.domain.dto.RankDTO;
import com.bafang.domain.po.RescueInfo;
import com.bafang.domain.vo.RescueInfoVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IRescueService extends IService<RescueInfo> {
    RescueInfo getByHelpId(Long id);

    List<RankDTO> getRank();

    List<RescueInfoVO> getRescueInfo();
}
