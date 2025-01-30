package com.bafang.domain.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class HelpRequestVO {

    private Long id;

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

    private Integer status;

    private String introduction;

    private LocalDateTime createdAt;

}
