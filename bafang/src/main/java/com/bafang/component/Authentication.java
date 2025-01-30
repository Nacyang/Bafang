package com.bafang.component;

import com.bafang.common.BaseContext;
import com.bafang.domain.po.User;
import com.bafang.service.IUserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class Authentication {

    private final RedisTemplate<String,Object> redisTemplate;

    private final IUserService userService;

    public void ac()
    {
        if(redisTemplate.opsForValue().get(BaseContext.getCurrentId().toString()) != null){
           log.info("实名认证通过");
        }
        else {
            User user = userService.getById(BaseContext.getCurrentId());
            if(!user.getIsRealNameAuthentication()){
                throw new RuntimeException("用户未实名认证");
            }
            else {
                redisTemplate.opsForValue().set(BaseContext.getCurrentId().toString(),user.getRole());
            }
        }
    }
    public Boolean isAdmin()
    {
        Object value = redisTemplate.opsForValue().get(BaseContext.getCurrentId().toString());
        int role = 1;
        if (value != null) {
            role = Integer.parseInt(value.toString());
        }
        return role == 2;
    }
}
