package com.app.yeoback_backend.domain;

import java.time.LocalDate;

import com.app.yeoback_backend.audit.Period;
import lombok.*;
import lombok.experimental.SuperBuilder;

// tbl_trip_day
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(of = "id", callSuper = false)
@SuperBuilder
public class TripDayVO extends Period {
    private Long id;              // PK
    private Long tripId;          // 어느 여행의 일차인지 (FK)
    private LocalDate tripDay;    // 2026-11-09
    private String tripDayTitle;  // 예: 첫날, 시부야 산책 (선택)
}
