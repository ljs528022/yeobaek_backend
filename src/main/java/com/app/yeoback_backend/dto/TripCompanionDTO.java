package com.app.yeoback_backend.dto;

import com.app.yeoback_backend.common.enumeration.CompanionRelation;
import com.app.yeoback_backend.domain.TripCompanionVO;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter @Setter @ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class TripCompanionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long tripId;                          // 어느 여행의 동행인지
    private Long memberId;                        // 동행이 여백 회원이면 회원 id
    private Boolean companionIsMember = false;    // 여백 회원인지
    private String companionName;                 // 민준
    private String companionContact;              // 연락처 (선택)
    private CompanionRelation companionRelation;  // "PENDING" / "DONE" / "REJECT"

    // 동행
    public TripCompanionVO toTripCompanionVO() {
        return TripCompanionVO.builder()
                .id(id)
                .tripId(tripId)
                .memberId(memberId)
                .companionIsMember(companionIsMember)
                .companionName(companionName)
                .companionContact(companionContact)
                .companionRelation(companionRelation)
                .build();
    }
}
