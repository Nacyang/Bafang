package com.bafang.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.bafang.common.BaseContext;
import com.bafang.component.Authentication;
import com.bafang.domain.dto.AccountDTO;
import com.bafang.domain.dto.ModifyInfoDTO;
import com.bafang.domain.dto.RealNameDTO;
import com.bafang.domain.po.User;
import com.bafang.domain.vo.UserAuthenticationVO;
import com.bafang.domain.vo.UserDetailVO;
import com.bafang.domain.vo.UserLoginVO;
import com.bafang.service.IUserMedalService;
import com.bafang.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/bafang/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    private final IUserMedalService userMedalService;

    private final Authentication authentication;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public SaResult register(@ModelAttribute AccountDTO accountDTO) {

        if(accountDTO.getPassword()==null){
            return SaResult.error("密码不能为空");
        }

        User user = userService.getByName(accountDTO.getUsername());
        if (user != null) {
            return SaResult.error("用户已存在");
        }
        user = User.builder()
                .username(accountDTO.getUsername())
                .password(passwordEncoder.encode(accountDTO.getPassword()))
                .role(1)
                .build();
        userService.save(user);
        UserLoginVO loginVO = userService.login(user);
        return SaResult.data(loginVO);
    }

    @PostMapping("/login")
    public SaResult login(@ModelAttribute AccountDTO accountDTO) {

        User user = userService.getByName(accountDTO.getUsername());

        if(user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getDeletedAt() != null) {
            throw new RuntimeException("用户已删除");
        }
        if(!passwordEncoder.matches(accountDTO.getPassword(), user.getPassword())){
            throw new RuntimeException("密码错误");
        }

        UserLoginVO loginVO = userService.login(user);
        return SaResult.data(loginVO);
    }

    @GetMapping
    public SaResult getUserInfo(@RequestParam("uid") Long uid) {
        User user = userService.getById(uid);
        UserDetailVO userDetailVO = new UserDetailVO();
        BeanUtils.copyProperties(user, userDetailVO);
        return SaResult.data(userDetailVO);
    }

    @PostMapping("/real_name_authentication")
    public SaResult realNameAuthentication(@ModelAttribute RealNameDTO realNameDTO) {
        userService.Authenticate(realNameDTO);
        return SaResult.ok();
    }

    @GetMapping("/real_name_authentication")
    public SaResult getUserAuthentication() {
        User user = userService.getById(BaseContext.getCurrentId());
        UserAuthenticationVO userAuthenticationVO = new UserAuthenticationVO();
        BeanUtils.copyProperties(user, userAuthenticationVO);
        return SaResult.data(userAuthenticationVO);
    }

    @PutMapping("/modify_info")
    public SaResult modifyUserInfo(@ModelAttribute ModifyInfoDTO modifyInfoDTO) {
        if(modifyInfoDTO.getPhoneNumber().length()!=11||modifyInfoDTO.getEmergencyNumber().length()!=11){
            throw new RuntimeException("电话号码错误");
        }
        User user = userService.getById(BaseContext.getCurrentId());
        BeanUtils.copyProperties(modifyInfoDTO, user);
        userService.updateById(user);
        UserDetailVO userDetailVO = new UserDetailVO();
        BeanUtils.copyProperties(user, userDetailVO);
        return SaResult.data(userDetailVO);
    }

    @DeleteMapping("/delete_user")
    public SaResult deleteUserInfo(@RequestParam("uid") Long uid) {
        User user = userService.getById(uid);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setDeletedAt(LocalDateTime.now());
        userService.updateById(user);
        return SaResult.ok();
    }

    @GetMapping("/update_token")
    public SaResult updateToken(@RequestParam("uid") Long uid) {
        StpUtil.updateLastActiveToNow();
        return SaResult.data(StpUtil.getTokenValue());
    }

    @GetMapping("/getUserMedal")
    public SaResult getUserMedal() {
        authentication.ac();
        return SaResult.data(userMedalService.getUserMedal());
    }
}
