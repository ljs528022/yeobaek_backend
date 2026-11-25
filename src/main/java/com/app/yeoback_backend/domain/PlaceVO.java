package com.app.yeoback_backend.domain;

import com.app.yeoback_backend.audit.Period;
import lombok.*;
import lombok.experimental.SuperBuilder;

// tbl_place
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(of = "id", callSuper = false)
@SuperBuilder
public class PlaceVO extends Period {
    private Long id;                  // PK
    private String googlePlaceId;     // 구글 장소 id (선택)
    private String kakaoPlaceId;      // 카카오 장소 id (선택)
    private String placeName;         // 츠타야 시부야점
    private String placeAddress;      // 주소
    private Double placeLat;          // 위도
    private Double placeLng;          // 경도
}
