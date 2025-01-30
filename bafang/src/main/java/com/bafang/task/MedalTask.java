package com.bafang.task;


import com.bafang.domain.po.UserMedal;
import com.bafang.service.IUserMedalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class MedalTask {

    private final IUserMedalService userMedalService;
    @Scheduled(cron = "0 0 0 * * 1")
    public void processMedal() {
        log.info("定时处理奖牌");
        // Step 1: 合成 type = 1 -> type = 2
        processMedals(1, 2);

        // Step 2: 合成 type = 2 -> type = 3
        processMedals(2, 3);
    }

    private void processMedals(int currentType, int nextType) {
        // 查询当前类型的奖牌（过去一周的时间范围可以根据需求调整）
        List<UserMedal> currentMedals = userMedalService.getByType(currentType);

        // 按用户分组
        Map<Long, List<UserMedal>> userMedalsGrouped = currentMedals.stream()
                .collect(Collectors.groupingBy(UserMedal::getUid));

        // 准备插入新的奖牌和删除已使用的奖牌
        List<UserMedal> newMedals = new ArrayList<>();
        List<Long> usedMedalIds = new ArrayList<>();

        userMedalsGrouped.forEach((uid, medals) -> {
            int newMedalCount = medals.size() / 3; // 每 3 枚奖牌合成 1 枚更高级的奖牌
            if (newMedalCount > 0) {
                // 添加新奖牌
                for (int i = 0; i < newMedalCount; i++) {
                    UserMedal userMedal = UserMedal.builder()
                            .uid(uid)
                            .medalType(nextType)
                            .build();
                    newMedals.add(userMedal); // 下一个级别的奖牌
                }
                // 收集前newMedalCount * 3个奖牌的ID
                usedMedalIds.addAll(
                        medals.stream()
                                .limit(newMedalCount * 3)
                                .map(UserMedal::getId)
                                .toList()
                );
            }
        });

        // 执行批量插入新奖牌
        if (!newMedals.isEmpty()) {
            userMedalService.saveBatch(newMedals);
        }

        // 批量删除已使用的奖牌
        if (!usedMedalIds.isEmpty()) {
            userMedalService.removeByIds(usedMedalIds);
        }
    }
}
