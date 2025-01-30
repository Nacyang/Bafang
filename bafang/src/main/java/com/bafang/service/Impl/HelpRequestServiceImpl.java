package com.bafang.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.bafang.common.BaseContext;
import com.bafang.component.Authentication;
import com.bafang.domain.dto.HelpRequestDTO;
import com.bafang.domain.dto.RescueDTO;
import com.bafang.domain.po.HelpRequest;

import com.bafang.domain.po.Message;
import com.bafang.domain.po.RescueInfo;
import com.bafang.domain.po.UserMedal;
import com.bafang.domain.vo.HelpRequestVO;
import com.bafang.mapper.HelpRequestMapper;
import com.bafang.service.IHelpRequestService;
import com.bafang.service.IMessageService;
import com.bafang.service.IRescueService;
import com.bafang.service.IUserMedalService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class HelpRequestServiceImpl extends ServiceImpl<HelpRequestMapper,HelpRequest> implements IHelpRequestService {

    // 地球半径，单位：千米
    private static final double EARTH_RADIUS = 6371;

    private final Authentication authentication;

    private final IMessageService messageService;

    private final IRescueService rescueService;

    private final IUserMedalService userMedalService;

    @Override
    public void uploadHelpRequest(HelpRequestDTO helpRequestDTO) {
        HelpRequest helpRequest = new HelpRequest();
        BeanUtils.copyProperties(helpRequestDTO,helpRequest);
        helpRequest.setUid(BaseContext.getCurrentId());
        if(authentication.isAdmin())
        {
            helpRequest.setAuditStatus(1);
            Message message = Message.builder()
                    .toUid(helpRequest.getUid())
                    .title("发布求助")
                    .content("管理员发布求助成功")
                    .build();
            messageService.save(message);
        }
        else {
            helpRequest.setAuditStatus(0);
            Message message = Message.builder()
                    .toUid(helpRequest.getUid())
                    .title("发布求助")
                    .content("发布求助成功，待审核")
                    .build();
            messageService.save(message);
        }
        this.save(helpRequest);
    }

    @Override
    @Transactional
    public void acceptHelpRequest(Long helpRequestId) {
        HelpRequest helpRequest = this.getById(helpRequestId);
        if(helpRequest == null){
            throw new RuntimeException("求助信息不存在");
        }

        Long userId = rescueService.getByHelpId(helpRequest.getId()).getUid();
        if(userId.equals(BaseContext.getCurrentId())){
            throw new RuntimeException("无法接取自己发布的求助");
        }

        if(isAccept(helpRequest)){
            throw new RuntimeException("该求助已经被接取");
        }
        RescueInfo rescueInfo = RescueInfo.builder()
                .helpId(helpRequest.getId())
                .uid(BaseContext.getCurrentId())
                .status(1)
                .build();
        rescueService.save(rescueInfo);
        Message message1 = Message.builder()
                .toUid(BaseContext.getCurrentId())
                .title("接取求助")
                .content("接取求助成功")
                .build();
        messageService.save(message1);
        Message message2 = Message.builder()
                .toUid(helpRequest.getUid())
                .title("求助被接取")
                .content("已有人接取您的求助,接取时间："+ LocalDateTime.now())
                .build();
        messageService.save(message2);
    }

    @Override
    @Transactional
    public void accomplishHelpRequest(RescueDTO rescueDTO) {
        RescueInfo rescue = rescueService.getByHelpId(rescueDTO.getHelpRequestID());
        if(rescue.getStatus() != 1){
            throw new RuntimeException("求助已结束");
        }
        LambdaUpdateWrapper<RescueInfo> wrapper1 = new LambdaUpdateWrapper<>();
        wrapper1.eq(RescueInfo::getHelpId, rescueDTO.getHelpRequestID())
                .set(RescueInfo::getStatus, rescueDTO.getStatus());
        rescueService.update(wrapper1);
        LambdaUpdateWrapper<HelpRequest> wrapper2 = new LambdaUpdateWrapper<>();
        wrapper2.eq(HelpRequest::getId, rescueDTO.getHelpRequestID())
                .set(HelpRequest::getStatus, rescueDTO.getStatus());
        this.update(wrapper2);
        //增加奖牌
        UserMedal userMedal = UserMedal.builder()
                .uid(BaseContext.getCurrentId())
                .medalType(1)
                .build();
        userMedalService.save(userMedal);
    }

    @Override
    public List<HelpRequestVO> getUserHelpRequest() {
        LambdaQueryWrapper<HelpRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HelpRequest::getUid, BaseContext.getCurrentId())
                .orderByDesc(HelpRequest::getCreatedAt);
        List<HelpRequest> helpRequests = this.list(wrapper);
        return BeanUtil.copyToList(helpRequests, HelpRequestVO.class);
    }

    @Override
    @Transactional
    public List<HelpRequestVO> getNearbyHelpRequest(String location) {
        //查询已经审核过的求助
        LambdaQueryWrapper<HelpRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HelpRequest::getAuditStatus, 1)
                .eq(HelpRequest::getStatus,1)
                .orderByDesc(HelpRequest::getCreatedAt);
        List<HelpRequest> helpRequests = this.list(wrapper);
        List<HelpRequest> helpRequestVOs = new ArrayList<>();
        for (HelpRequest helpRequest : helpRequests) {
            //距离小于一公里并且未被接取的求助
            if (calculateDistance(location, helpRequest.getLocation()) <= 1 && !isAccept(helpRequest)){
                helpRequestVOs.add(helpRequest);
            }
        }
        return BeanUtil.copyToList(helpRequestVOs, HelpRequestVO.class);
    }

    private boolean isAccept(HelpRequest helpRequest) {
        return helpRequest.getType() == 1 && rescueService.getByHelpId(helpRequest.getId()) != null;
    }

    // 计算两个经纬度之间的距离
    private static double calculateDistance(String LatLon1, String LatLon2) {
        // 将坐标字符串按逗号分割
        String[] parts1 = LatLon1.split(",");
        String[] parts2 = LatLon2.split(",");

        // 获取经度和纬度（注意：东经和北纬默认是正数）
        double lat1 = Double.parseDouble(parts1[0]);
        double lon1 = Double.parseDouble(parts1[1]);
        double lat2 = Double.parseDouble(parts2[0]);
        double lon2 = Double.parseDouble(parts2[1]);

        // 将经纬度从度数转换为弧度
        lat1 = toRadians(lat1);
        lon1 = toRadians(lon1);
        lat2 = toRadians(lat2);
        lon2 = toRadians(lon2);

        // Haversine公式
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dlon / 2) * Math.sin(dlon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 计算距离
        return EARTH_RADIUS * c;
    }

    // 将度数转换为弧度
    private static double toRadians(double degree) {
        return degree * Math.PI / 180.0;
    }
}
