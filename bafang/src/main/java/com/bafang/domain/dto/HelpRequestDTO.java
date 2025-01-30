package com.bafang.domain.dto;

import lombok.Data;

@Data
public class HelpRequestDTO {
    private Long uid;
    private String location;
    private Integer type;
    private String phoneNumber;
    private String detailedAddress;
    private String roadCondition;
    private String weatherCondition;
    private String utilitiesInfo;
    private Integer assistanceType;
    private String suppliesNeeded;
    private String introduction;
}
