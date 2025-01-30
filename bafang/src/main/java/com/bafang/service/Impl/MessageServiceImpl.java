package com.bafang.service.Impl;

import com.bafang.common.BaseContext;
import com.bafang.domain.po.Message;
import com.bafang.mapper.MessageMapper;
import com.bafang.service.IMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    @Override
    public List<Message> GetUserMessage() {
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Message::getToUid, BaseContext.getCurrentId());
        return this.list(wrapper);
    }
}