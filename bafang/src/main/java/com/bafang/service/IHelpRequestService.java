package com.bafang.service;

import com.bafang.domain.dto.HelpRequestDTO;
import com.bafang.domain.dto.RescueDTO;
import com.bafang.domain.po.HelpRequest;
import com.bafang.domain.vo.HelpRequestVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IHelpRequestService extends IService<HelpRequest> {

    List<HelpRequestVO> getUserHelpRequest();

    List<HelpRequestVO> getNearbyHelpRequest(String location);

    void uploadHelpRequest(HelpRequestDTO helpRequestDTO);

    void acceptHelpRequest(Long helpRequestId);

    void accomplishHelpRequest(RescueDTO rescueDTO);
}
