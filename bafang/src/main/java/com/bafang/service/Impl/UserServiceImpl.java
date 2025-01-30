package com.bafang.service.Impl;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.bafang.common.BaseContext;
import com.bafang.domain.dto.RealNameDTO;
import com.bafang.domain.po.User;
import com.bafang.domain.vo.UserLoginVO;
import com.bafang.mapper.UserMapper;
import com.bafang.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final RedisTemplate<String,Object> redisTemplate;

    private final UserMapper userMapper;

    @Override
    public User getByName(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }

    @Override
    @Transactional
    public void Authenticate(RealNameDTO realNameDTO) {
        User user = userMapper.selectById(BaseContext.getCurrentId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if(!isValidIdCard(realNameDTO.getIdNumber())){
            throw new RuntimeException("身份证号码错误");
        }
        user.setIsRealNameAuthentication(true);
        user.setRealName(realNameDTO.getRealName());
        user.setIdNumber(realNameDTO.getIdNumber());
        userMapper.updateById(user);
        redisTemplate.opsForValue().set(BaseContext.getCurrentId().toString(),user.getRole());
    }

    @Override
    public UserLoginVO login(User user) {
        StpUtil.login(user.getId());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        UserLoginVO loginVO = UserLoginVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .token(tokenInfo.getTokenValue())
                .build();
        // 得到用户id
        Long userId = StpUtil.getLoginIdAsLong();
        // 设置当前登录用户id
        BaseContext.setCurrentId(userId);
        System.out.println("id:"+BaseContext.getCurrentId());
        return loginVO;
    }

    private static boolean isValidIdCard(String id) {

        // 身份证号码的正则表达式
        String ID_CARD_REGEX = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

        if (id == null || id.isEmpty()) {
            return false; // 空字符串无效
        }

        // 匹配身份证格式
        Pattern pattern = Pattern.compile(ID_CARD_REGEX);
        Matcher matcher = pattern.matcher(id);

        if (!matcher.matches()) {
            return false; // 格式不匹配
        }

        // 校验位验证
        return isValidCheckDigit(id);
    }

    // 校验位计算
    private static boolean isValidCheckDigit(String id) {

        if (id.length() != 18) {
            return false;
        }

        char[] idChars = id.toCharArray();
        int[] factors = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] checkDigits = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (idChars[i] - '0') * factors[i];
        }

        char calculatedCheckDigit = checkDigits[sum % 11];
        return calculatedCheckDigit == idChars[17]; // 比较计算的校验位和实际校验位
    }
}