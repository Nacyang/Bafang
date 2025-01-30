package com.bafang.mapper;

import com.bafang.domain.po.HelpRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HelpRequestMapper extends BaseMapper<HelpRequest> {
}
