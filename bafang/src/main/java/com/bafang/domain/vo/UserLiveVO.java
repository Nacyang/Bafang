package com.bafang.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLiveVO {
    private Long id;
    private Long uid;
    private String location;
    private String detailedAddress;
    private String weatherCondition;
    private String roadCondition;
    private String utilitiesInfo;
    private Integer level;
    private String introduction;
    private LocalDateTime createdAt;
    private  LocalDateTime expiryAt;
    private Integer likeNum;
    private Integer dislikeNum;
    private Integer likeStatus;
}
