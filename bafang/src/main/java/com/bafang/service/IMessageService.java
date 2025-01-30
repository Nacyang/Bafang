package com.bafang.service;


import com.bafang.domain.po.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface IMessageService extends IService<Message> {
    List<Message> GetUserMessage();
}
