package com.app.yeoback_backend.domain;

import java.time.LocalDate;

import com.app.yeoback_backend.audit.Period;
import com.app.yeoback_backend.common.enumeration.TripCategory;
import com.app.yeoback_backend.common.enumeration.TripStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

// tbl_trip
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(of = "id", callSuper = false)
@SuperBuilder
public class TripVO extends Period {
    private Long id;                    // PK
    private Long memberId;              // 여행을 만든 회원 (tbl_member)
    private Long destinationId;         // 목적지 (tbl_destination)
    private Long departureId;           // 출발지 (tbl_destination)
    private TripCategory tripCategory;  // DOMESTIC / OVERSEAS
    private String tripTitle;           // 예: 도쿄, 우리의 3일
    private LocalDate tripStartDate;    // 2026-11-09
    private LocalDate tripEndDate;
    private TripStatus tripStatus;      // WRITE / SAVE / FINISH
}
