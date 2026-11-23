package com.app.yeoback_backend.dto;

import com.app.yeoback_backend.common.enumeration.TripCategory;
import com.app.yeoback_backend.common.enumeration.TripStatus;
import com.app.yeoback_backend.domain.TripVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class TripDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    // 로그인 회원 id, 화면에서 받지 않고 서버에서 넣음
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long memberId;
    private Long destinationId;         // 목적지 (tbl_destination)
    private Long departureId;           // 출발지 (tbl_destination)
    private TripCategory tripCategory;  // "DOMESTIC" / "OVERSEAS"
    private String tripTitle;
    private LocalDate tripStartDate;    // "2026-11-09"
    private LocalDate tripEndDate;
    private TripStatus tripStatus;      // "WRITE" / "SAVE" / "FINISH"

    // 여행 일차 (일차 안에 일정 목록이 들어 있음)
    private List<TripDayDTO> days = new ArrayList<>();

    // 함께 가는 사람
    private List<TripCompanionDTO> companions = new ArrayList<>();

    // 여행
    public TripVO toTripVO() {
        return TripVO.builder()
                .id(id)
                .memberId(memberId)
                .destinationId(destinationId)
                .departureId(departureId)
                .tripCategory(tripCategory)
                .tripTitle(tripTitle)
                .tripStartDate(tripStartDate)
                .tripEndDate(tripEndDate)
                .tripStatus(tripStatus)
                .build();
    }
}
