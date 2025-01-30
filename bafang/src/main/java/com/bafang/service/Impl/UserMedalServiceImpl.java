package com.bafang.service.Impl;


import com.bafang.common.BaseContext;
import com.bafang.domain.po.UserMedal;
import com.bafang.domain.vo.MedalCountVO;
import com.bafang.mapper.UserMedalMapper;
import com.bafang.service.IUserMedalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserMedalServiceImpl extends ServiceImpl<UserMedalMapper, UserMedal> implements IUserMedalService {

    @Override
    public List<UserMedal> getByType(int type) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        LocalDateTime today = LocalDateTime.now();
        return this.lambdaQuery()
                .eq(UserMedal::getMedalType, type)
                .between(UserMedal::getCreatedAt, oneWeekAgo, today)
                .list();
    }

    @Override
    public MedalCountVO getUserMedal() {
        List<UserMedal> userMedals = this.lambdaQuery()
                .eq(UserMedal::getUid,BaseContext.getCurrentId())
                .list();
        long countType1 = 0L;
        long countType2 = 0L;
        long countType3 = 0L;
        for (UserMedal userMedal : userMedals) {
            if(userMedal.getMedalType()==1){
                countType1++;
            }
            if(userMedal.getMedalType()==2){
                countType2++;
            }
            if(userMedal.getMedalType()==3){
                countType3++;
            }
        }
        return MedalCountVO.builder()
                .countType1(countType1)
                .countType2(countType2)
                .countType3(countType3)
                .rescueCountALL(countType1 + countType2 * 3 + countType3 * 9)
                .statusTime(LocalDateTime.now())
                .build();
    }
}