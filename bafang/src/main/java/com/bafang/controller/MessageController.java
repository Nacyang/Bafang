package com.bafang.controller;

import cn.dev33.satoken.util.SaResult;
import com.bafang.component.Authentication;
import com.bafang.service.IMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/bafang/message")
@RequiredArgsConstructor
public class MessageController {

    private final IMessageService messageService;

    private final Authentication authentication;
    @GetMapping("/getUserMessage")
    public SaResult getUserMessage() {
        authentication.ac();
        return SaResult.data(messageService.GetUserMessage());
    }
}
