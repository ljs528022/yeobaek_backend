package com.app.yeoback_backend.dto;

import com.app.yeoback_backend.domain.ScheduleVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter @ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class ScheduleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long tripDayId;                   // 어느 일차의 일정인지
    // 로그인 회원 id, 화면에서 받지 않고 서버에서 넣음
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long memberId;
    private Long placeId;                     // 장소 (선택)
    private String scheduleTitle;             // 호텔, 츠타야 ...
    private LocalDateTime scheduleStartTime;  // "2026-11-09T09:00:00"
    private String scheduleMemo;
    private Boolean scheduleAiSource = false; // AI 추천으로 추가했는지

    // 일차별 일정
    public ScheduleVO toScheduleVO() {
        return ScheduleVO.builder()
                .id(id)
                .tripDayId(tripDayId)
                .memberId(memberId)
                .placeId(placeId)
                .scheduleTitle(scheduleTitle)
                .scheduleStartTime(scheduleStartTime)
                .scheduleMemo(scheduleMemo)
                .scheduleAiSource(scheduleAiSource)
                .build();
    }
}
