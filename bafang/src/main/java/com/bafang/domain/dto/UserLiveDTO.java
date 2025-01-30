package com.bafang.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserLiveDTO {
    private Long id;
    private String location;
    private String weatherCondition;
    private String roadCondition;
    private String utilitiesInfo;
    private String introduction;
    private LocalDateTime createdAt;
}
