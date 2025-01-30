package com.bafang.service.Impl;

import com.bafang.common.BaseContext;
import com.bafang.domain.po.Message;
import com.bafang.domain.dto.UserActionDTO;
import com.bafang.domain.dto.UserUploadDTO;
import com.bafang.domain.po.LiveInfo;
import com.bafang.domain.po.UserLiveInfoVote;
import com.bafang.domain.vo.UserLiveVO;
import com.bafang.mapper.LiveInfoMapper;
import com.bafang.service.ILiveInfoService;

import com.bafang.service.IMessageService;
import com.bafang.service.IUserLiveInfoVoteService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LiveInfoServiceImpl extends ServiceImpl<LiveInfoMapper,LiveInfo> implements ILiveInfoService {

    // 地球半径，单位：千米
    private static final double EARTH_RADIUS = 6371;

    private final LiveInfoMapper liveInfoMapper;

    private final IMessageService messageService;

    private final IUserLiveInfoVoteService userLiveInfoVoteService;

    @Override
    public List<UserLiveVO> getUserLive() {
        Long userId = BaseContext.getCurrentId();
        return liveInfoMapper.getUserLives(userId,null);
    }

    @Override
    public void uploadLive(UserUploadDTO userUploadDTO) {
        LiveInfo liveInfo = new LiveInfo();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        // 转换为 LocalDateTime
        LocalDateTime expiryAt = LocalDateTime.parse(userUploadDTO.getExpiryAt(), formatter);
        if(LocalDateTime.now().isAfter(expiryAt)){
            throw new RuntimeException("过期时间不能早于当前时间");
        }
        BeanUtils.copyProperties(userUploadDTO,liveInfo);
        liveInfo.setExpiryAt(expiryAt);
        Long userId = BaseContext.getCurrentId();
        liveInfo.setUid(userId);
        if (userUploadDTO.getLevel() == 1){
            Message message = Message.builder()
                    .toUid(userId)
                    .title("发布实况")
                    .content("发布实况成功，待审核")
                    .build();
            messageService.save(message);
            liveInfo.setAuditStatus(0);
        }
        else {
            Message message = Message.builder()
                    .toUid(userId)
                    .title("发布实况")
                    .content("管理员发布实况成功")
                    .build();
            messageService.save(message);
            liveInfo.setAuditStatus(1);
        }
        this.save(liveInfo);
    }

    @Override
    public void deleteLiveInfo(Long uid, Long id) {
        LambdaQueryWrapper<LiveInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveInfo::getUid,uid)
                .eq(LiveInfo::getId,id);
        LiveInfo liveInfo = liveInfoMapper.selectOne(wrapper);
        if (liveInfo == null) {
            throw new RuntimeException("该实况不存在");
        }
        liveInfo.setDeletedAt(LocalDateTime.now());
        this.updateById(liveInfo);
    }

    @Override
    public void likeOrDislikeLive(UserActionDTO userActionDTO) {
        long userId = BaseContext.getCurrentId();

        LambdaQueryWrapper<UserLiveInfoVote> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserLiveInfoVote::getLiveInfoId,userActionDTO.getLiveInfoId())
                .eq(UserLiveInfoVote::getUid,userId);
        UserLiveInfoVote liveInfoVote = userLiveInfoVoteService.getOne(wrapper);

        UserLiveInfoVote userLiveInfoVote = new UserLiveInfoVote();
        BeanUtils.copyProperties(userActionDTO,userLiveInfoVote);
        userLiveInfoVote.setUid(userId);
        if(liveInfoVote == null){
            userLiveInfoVoteService.save(userLiveInfoVote);
        }
        else if(Objects.equals(liveInfoVote.getVoteType(), userActionDTO.getVoteType())){
            throw new RuntimeException("禁止相同操作");
        }
        else {
            userLiveInfoVote.setId(liveInfoVote.getId());
            userLiveInfoVoteService.updateById(userLiveInfoVote);
        }
    }

    @Override
    public List<UserLiveVO> getNearbyLive(String location) {
        List<UserLiveVO> userLives = liveInfoMapper.getUserLives(null,1);
        List<UserLiveVO> userLiveVOs = new ArrayList<>();
        for (UserLiveVO userLiveVO : userLives) {
            //距离小于一公里并且未失效的实况
            if (calculateDistance(location, userLiveVO.getLocation()) <= 1
                    && userLiveVO.getExpiryAt().isAfter(LocalDateTime.now())){
                userLiveVOs.add(userLiveVO);
            }
        }
        return userLiveVOs;
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
