package com.app.yeoback_backend.repository;

import com.app.yeoback_backend.dto.ScheduleDTO;
import com.app.yeoback_backend.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleDAO {
    private final ScheduleMapper scheduleMapper;
    // 일정 추가
    public void save(ScheduleDTO scheduleDTO) {
        scheduleMapper.insert(scheduleDTO);
    }
    // 일정 한 건 조회
    public Optional<ScheduleDTO> findById(Long id) {
        return scheduleMapper.selectById(id);
    }
    // 일차의 일정 목록 (시간순)
    public List<ScheduleDTO> findAllByTripDayId(Long tripDayId) {
        return scheduleMapper.selectAllByTripDayId(tripDayId);
    }
    // 일정 수정
    public void update(ScheduleDTO scheduleDTO) {
        scheduleMapper.update(scheduleDTO);
    }
    // 일정 삭제
    public void delete(Long id) {
        scheduleMapper.delete(id);
    }
    // 여행의 일정 전체 삭제 (여행 삭제할 때)
    public void deleteAllByTripId(Long tripId) {
        scheduleMapper.deleteAllByTripId(tripId);
    }
}