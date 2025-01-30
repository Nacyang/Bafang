package com.bafang.controller;

import cn.dev33.satoken.util.SaResult;
import com.bafang.component.Authentication;
import com.bafang.domain.dto.HelpRequestDTO;
import com.bafang.domain.dto.RescueDTO;
import com.bafang.service.IHelpRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bafang/help_request")
@RequiredArgsConstructor
public class HelpRequestController {

    private final IHelpRequestService helpRequestService;

    private final Authentication authentication;

    @GetMapping("/user")
    public SaResult getUserHelpRequest() {
        authentication.ac();
        return SaResult.data(helpRequestService.getUserHelpRequest());
    }

    @GetMapping
    public SaResult getNearbyHelpRequest(@RequestParam String location) {
        authentication.ac();
        return SaResult.data(helpRequestService.getNearbyHelpRequest(location));
    }

    @PostMapping
    public SaResult uploadHelpRequest(@ModelAttribute HelpRequestDTO helpRequestDTO) {
        authentication.ac();
        helpRequestService.uploadHelpRequest(helpRequestDTO);
        return SaResult.ok();
    }

    @PostMapping("/accept")
    public SaResult acceptHelpRequest(@RequestParam Long helpRequestId) {
        authentication.ac();
        helpRequestService.acceptHelpRequest(helpRequestId);
        return SaResult.ok();
    }

    @PostMapping("/accomplish")
    public SaResult accomplishHelpRequest(@ModelAttribute RescueDTO rescueDTO) {
        authentication.ac();
        helpRequestService.accomplishHelpRequest(rescueDTO);
        return SaResult.ok();
    }
}