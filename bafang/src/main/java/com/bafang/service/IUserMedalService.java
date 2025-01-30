package com.bafang.service;



import com.bafang.domain.po.UserMedal;
import com.bafang.domain.vo.MedalCountVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface IUserMedalService extends IService<UserMedal> {
    List<UserMedal> getByType(int type);

    MedalCountVO getUserMedal();
}
