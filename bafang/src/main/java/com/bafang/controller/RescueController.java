package com.bafang.controller;


import cn.dev33.satoken.util.SaResult;
import com.bafang.component.Authentication;
import com.bafang.service.IRescueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bafang/rescue")
@RequiredArgsConstructor
public class RescueController {

    private final IRescueService rescueService;

    private final Authentication authentication;
    @GetMapping("/rank")
    public SaResult getRescueLeaderboard() {
        authentication.ac();
        return SaResult.data(rescueService.getRank());
    }

    @GetMapping("/user")
    public SaResult getUserRescueInfo() {
        authentication.ac();
        return SaResult.data(rescueService.getRescueInfo());
    }
}
