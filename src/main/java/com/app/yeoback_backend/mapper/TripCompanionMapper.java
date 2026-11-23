package com.app.yeoback_backend.mapper;

import com.app.yeoback_backend.dto.TripCompanionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TripCompanionMapper {
    // 동행 추가
    public void insert(TripCompanionDTO tripCompanionDTO);
    // 여행의 동행 목록
    public List<TripCompanionDTO> selectAllByTripId(Long tripId);
    // 여행의 동행 전체 삭제
    public void deleteAllByTripId(Long tripId);
}