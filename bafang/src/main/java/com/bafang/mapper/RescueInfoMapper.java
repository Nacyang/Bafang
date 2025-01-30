package com.bafang.mapper;

import com.bafang.domain.dto.RankDTO;
import com.bafang.domain.po.RescueInfo;
import com.bafang.domain.vo.RescueInfoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RescueInfoMapper extends BaseMapper<RescueInfo> {

    List<RankDTO> getRank();

    List<RescueInfoVO> getRescueInfo(Long id);
}
