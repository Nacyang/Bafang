package com.bafang.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailVO
{
    private Long id;           // 用户ID
    private String username;    // 用户名
    private Integer role;       // 用户权限 (1: 普通用户, 2: 管理员)
    private String phoneNumber; // 电话号码
    private Boolean isRealNameAuthentication;
    private String emergencyContact;
    private String emergencyNumber;
}