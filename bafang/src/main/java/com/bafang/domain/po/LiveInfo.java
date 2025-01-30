package com.bafang.domain.po;

import com.baomidou.mybatisplus.annotation.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("live_info")
public class LiveInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long uid;

    private String location;

    private String weatherCondition;

    private String detailedAddress;

    private String roadCondition;

    private String utilitiesInfo;

    private Integer level;

    private String introduction;

    private Integer auditStatus = 0;

    private String auditedBy;

    private String auditComment;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    private LocalDateTime auditedAt;

    private LocalDateTime expiryAt;
}
