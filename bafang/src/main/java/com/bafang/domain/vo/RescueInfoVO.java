package com.bafang.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RescueInfoVO {
    private Long rescueId;
    private String roadCondition;
    private String weatherCondition;
    private Integer assistanceType;
    private String suppliesNeeded;
    private String detailedAddress;
    private LocalDateTime createdAt;
    private String phoneNumber;
    private Integer status;
}
