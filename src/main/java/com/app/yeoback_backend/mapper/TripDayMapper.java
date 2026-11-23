package com.app.yeoback_backend.mapper;

import com.app.yeoback_backend.dto.TripDayDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TripDayMapper {
    // 일차 추가
    public void insert(TripDayDTO tripDayDTO);
    // 일차 한 건 조회
    public Optional<TripDayDTO> selectById(Long id);
    // 여행의 일차 목록
    public List<TripDayDTO> selectAllByTripId(Long tripId);
    // 일차 제목·한 문장 수정
    public void update(TripDayDTO tripDayDTO);
    // 여행의 일차 전체 삭제
    public void deleteAllByTripId(Long tripId);
}