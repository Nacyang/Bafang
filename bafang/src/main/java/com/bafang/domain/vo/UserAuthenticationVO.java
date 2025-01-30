package com.bafang.domain.vo;

import lombok.Data;

@Data
public class UserAuthenticationVO
{
    private Long id;           // 用户ID
    private String username;    // 用户名
    private String idNumber;   //身份证号
    private String realName;   //真实姓名
}
