package com.app.yeoback_backend.mapper;

import com.app.yeoback_backend.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ScheduleMapper {
    // 일정 추가
    public void insert(ScheduleDTO scheduleDTO);
    // 일정 한 건 조회
    public Optional<ScheduleDTO> selectById(Long id);
    // 일차의 일정 목록
    public List<ScheduleDTO> selectAllByTripDayId(Long tripDayId);
    // 일정 수정
    public void update(ScheduleDTO scheduleDTO);
    // 일정 삭제
    public void delete(Long id);
    // 여행의 일정 전체 삭제
    public void deleteAllByTripId(Long tripId);
}