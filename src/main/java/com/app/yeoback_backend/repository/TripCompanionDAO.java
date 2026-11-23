package com.app.yeoback_backend.repository;

import com.app.yeoback_backend.dto.TripCompanionDTO;
import com.app.yeoback_backend.mapper.TripCompanionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TripCompanionDAO {
    private final TripCompanionMapper tripCompanionMapper;
    // 동행 추가
    public void save(TripCompanionDTO tripCompanionDTO) {
        tripCompanionMapper.insert(tripCompanionDTO);
    }
    // 여행의 동행 목록
    public List<TripCompanionDTO> findAllByTripId(Long tripId) {
        return tripCompanionMapper.selectAllByTripId(tripId);
    }
    // 여행의 동행 전체 삭제 (수정할 때 지우고 다시 저장)
    public void deleteAllByTripId(Long tripId) {
        tripCompanionMapper.deleteAllByTripId(tripId);
    }
}