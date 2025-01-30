package com.bafang.service;


import com.bafang.domain.dto.RealNameDTO;
import com.bafang.domain.po.User;
import com.bafang.domain.vo.UserLoginVO;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IUserService extends IService<User> {
    User getByName(String username);

    void Authenticate(RealNameDTO realNameDTO);

    UserLoginVO login(User user);
}
