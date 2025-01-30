package com.bafang.service.Impl;

import com.bafang.domain.po.UserLiveInfoVote;
import com.bafang.mapper.UserLiveInfoVoteMapper;
import com.bafang.service.IUserLiveInfoVoteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLiveInfoVoteServiceImpl extends ServiceImpl<UserLiveInfoVoteMapper,UserLiveInfoVote> implements IUserLiveInfoVoteService {

}