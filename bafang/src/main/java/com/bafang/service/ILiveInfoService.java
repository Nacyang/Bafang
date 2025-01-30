package com.bafang.service;

import com.bafang.domain.dto.UserActionDTO;
import com.bafang.domain.dto.UserUploadDTO;
import com.bafang.domain.po.LiveInfo;
import com.bafang.domain.vo.UserLiveVO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ILiveInfoService extends IService<LiveInfo> {
    List<UserLiveVO> getUserLive();

    void uploadLive(UserUploadDTO userUploadDTO);

    void deleteLiveInfo(Long uid, Long id);

    void likeOrDislikeLive(UserActionDTO userActionDTO);

    List<UserLiveVO> getNearbyLive(@Param("location") String location);
}
