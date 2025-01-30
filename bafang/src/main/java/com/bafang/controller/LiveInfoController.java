package com.bafang.controller;

import cn.dev33.satoken.util.SaResult;
import com.bafang.component.Authentication;
import com.bafang.domain.dto.UserActionDTO;
import com.bafang.domain.dto.UserUploadDTO;
import com.bafang.domain.vo.UserLiveVO;
import com.bafang.service.ILiveInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bafang/live_info")
@Slf4j
@RequiredArgsConstructor
public class LiveInfoController {

    private final ILiveInfoService liveInfoService;

    private final Authentication authentication;
    //获取用户实况
    @GetMapping("/user")
    public SaResult getUserLive() {
        authentication.ac();
        List<UserLiveVO> userLiveVOS = liveInfoService.getUserLive();
        return SaResult.data(userLiveVOS);
    }

    //获取附近实况
    @GetMapping
    public SaResult getNearbyLive(@RequestParam("location") String location) {
        return SaResult.data(liveInfoService.getNearbyLive(location));
    }

    //上传用户实况
    @PostMapping
    public  SaResult uploadLive(@ModelAttribute UserUploadDTO userUploadDTO){
        authentication.ac();
        liveInfoService.uploadLive(userUploadDTO);
        return SaResult.ok();
    }

    @PostMapping("/action")
    public  SaResult likeOrDislikeLive(@ModelAttribute UserActionDTO userActionDTO){
        liveInfoService.likeOrDislikeLive(userActionDTO);
        return SaResult.ok();
    }

    @DeleteMapping("/delete")
    public  SaResult deleteLiveInfo(@RequestParam("uid") Long uid, @RequestParam("id") Long id){
        authentication.ac();
        liveInfoService.deleteLiveInfo(uid, id);
        return SaResult.ok();
    }

}
