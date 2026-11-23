package com.app.yeoback_backend.dto;

import com.app.yeoback_backend.domain.TripDayVO;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class TripDayDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long tripId;          // 어느 여행의 일차인지
    private LocalDate tripDay;    // "2026-11-09"
    private String tripDayTitle;

    // 이 날의 일정 목록
    private List<ScheduleDTO> schedules = new ArrayList<>();

    // 일차
    public TripDayVO toTripDayVO() {
        return TripDayVO.builder()
                .id(id)
                .tripId(tripId)
                .tripDay(tripDay)
                .tripDayTitle(tripDayTitle)
                .build();
    }
}
