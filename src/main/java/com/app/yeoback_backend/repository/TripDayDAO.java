package com.app.yeoback_backend.repository;

import com.app.yeoback_backend.dto.TripDayDTO;
import com.app.yeoback_backend.mapper.TripDayMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TripDayDAO {
    private final TripDayMapper tripDayMapper;
    // 일차 추가
    public void save(TripDayDTO tripDayDTO) {
        tripDayMapper.insert(tripDayDTO);
    }
    // 일차 한 건 조회
    public Optional<TripDayDTO> findById(Long id) {
        return tripDayMapper.selectById(id);
    }
    // 여행의 일차 목록
    public List<TripDayDTO> findAllByTripId(Long tripId) {
        return tripDayMapper.selectAllByTripId(tripId);
    }
    // 일차 제목·한 문장 수정
    public void update(TripDayDTO tripDayDTO) {
        tripDayMapper.update(tripDayDTO);
    }
    // 여행의 일차 전체 삭제
    public void deleteAllByTripId(Long tripId) {
        tripDayMapper.deleteAllByTripId(tripId);
    }
}