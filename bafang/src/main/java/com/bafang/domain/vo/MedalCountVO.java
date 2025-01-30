package com.bafang.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedalCountVO {

    private Long countType1;
    private Long countType2;
    private Long countType3;
    private Long rescueCountALL;
    private LocalDateTime statusTime;
}
