package com.app.yeoback_backend.mapper;

import com.app.yeoback_backend.dto.PlaceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface PlaceMapper {
    // 장소 추가
    public void insert(PlaceDTO placeDTO);
    // 장소 한 건 조회
    public Optional<PlaceDTO> selectById(Long id);
    // 외부 장소 id(구글 / 카카오)로 이미 저장된 장소 조회
    public Optional<PlaceDTO> selectByExternalId(@Param("googlePlaceId") String googlePlaceId,
                                                 @Param("kakaoPlaceId") String kakaoPlaceId);
}
