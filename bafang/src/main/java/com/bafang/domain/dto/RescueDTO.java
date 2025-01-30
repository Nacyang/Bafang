package com.bafang.domain.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RescueDTO {
    Long helpRequestID;
    Integer status;
}
