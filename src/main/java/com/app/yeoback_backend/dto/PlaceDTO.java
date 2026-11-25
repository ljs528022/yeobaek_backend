package com.app.yeoback_backend.dto;

import com.app.yeoback_backend.domain.PlaceVO;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter @Setter @ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class PlaceDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String googlePlaceId;     // 구글 장소 id (선택)
    private String kakaoPlaceId;      // 카카오 장소 id (선택)
    private String placeName;
    private String placeAddress;
    private Double placeLat;          // 위도
    private Double placeLng;          // 경도

    // 장소
    public PlaceVO toPlaceVO() {
        return PlaceVO.builder()
                .id(id)
                .googlePlaceId(googlePlaceId)
                .kakaoPlaceId(kakaoPlaceId)
                .placeName(placeName)
                .placeAddress(placeAddress)
                .placeLat(placeLat)
                .placeLng(placeLng)
                .build();
    }
}
