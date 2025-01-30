package com.bafang.mapper;

import com.bafang.domain.po.LiveInfo;
import com.bafang.domain.vo.UserLiveVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LiveInfoMapper extends BaseMapper<LiveInfo> {
    List<UserLiveVO> getUserLives(Long userId,Integer auditStatus);
}
