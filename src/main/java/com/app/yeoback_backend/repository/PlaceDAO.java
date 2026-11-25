package com.app.yeoback_backend.repository;

import com.app.yeoback_backend.dto.PlaceDTO;
import com.app.yeoback_backend.mapper.PlaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlaceDAO {
    private final PlaceMapper placeMapper;
    // 장소 추가
    public void save(PlaceDTO placeDTO) {
        placeMapper.insert(placeDTO);
    }
    // 장소 한 건 조회
    public Optional<PlaceDTO> findById(Long id) {
        return placeMapper.selectById(id);
    }
    // 외부 장소 id(구글 / 카카오)로 이미 저장된 장소 조회
    public Optional<PlaceDTO> findByExternalId(String googlePlaceId, String kakaoPlaceId) {
        return placeMapper.selectByExternalId(googlePlaceId, kakaoPlaceId);
    }
}
