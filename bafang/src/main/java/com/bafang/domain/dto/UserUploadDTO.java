package com.bafang.domain.dto;

import lombok.Data;

@Data
public class UserUploadDTO {
    private String location;
    private String detailedAddress;
    private String weatherCondition;
    private String roadCondition;
    private String utilitiesInfo;
    private Integer level;
    private String introduction;
    private String expiryAt;
}
