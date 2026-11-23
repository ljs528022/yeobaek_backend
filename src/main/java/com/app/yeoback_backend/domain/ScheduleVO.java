package com.app.yeoback_backend.domain;

import java.time.LocalDateTime;

import com.app.yeoback_backend.audit.Period;
import lombok.*;
import lombok.experimental.SuperBuilder;

// tbl_schedule
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(of = "id", callSuper = false)
@SuperBuilder
public class ScheduleVO extends Period {
    private Long id;                          // PK
    private Long tripDayId;                   // 어느 일차의 일정인지 (FK)
    private Long memberId;                    // 일정을 만든 회원
    private Long placeId;                     // 장소 (tbl_place, 선택)
    private String scheduleTitle;             // 호텔, 츠타야 ...
    private LocalDateTime scheduleStartTime;  // 2026-11-09T09:00:00
    private String scheduleMemo;              // 메모 (선택)
    private Boolean scheduleAiSource;         // AI 추천으로 추가했는지
}
