package com.app.yeoback_backend.domain;

import com.app.yeoback_backend.audit.Period;
import com.app.yeoback_backend.common.enumeration.CompanionRelation;
import lombok.*;
import lombok.experimental.SuperBuilder;

// tbl_trip_companion
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(of = "id", callSuper = false)
@SuperBuilder
public class TripCompanionVO extends Period {
    private Long id;                              // PK
    private Long tripId;                          // 어느 여행의 동행인지 (FK)
    private Long memberId;                        // 동행이 여백 회원이면 회원 id (아니면 null)
    private Boolean companionIsMember;            // 여백 회원인지
    private String companionName;                 // 민준
    private String companionContact;              // 연락처 (선택)
    private CompanionRelation companionRelation;  // PENDING / DONE / REJECT
}
