package com.bafang.domain.po;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class HelpRequest {
    @Id
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

    private Integer status = 0;

    private String introduction;

    private Integer auditStatus = 0;

    private Long auditedBy;

    private String auditComment;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedAt;

    private LocalDateTime auditedAt;

    private LocalDateTime deletedAt;
}
