package com.app.yeoback_backend.service;

import com.app.yeoback_backend.common.enumeration.TripStatus;
import com.app.yeoback_backend.dto.ScheduleDTO;
import com.app.yeoback_backend.dto.TripCompanionDTO;
import com.app.yeoback_backend.dto.TripDTO;
import com.app.yeoback_backend.dto.TripDayDTO;
import com.app.yeoback_backend.repository.ScheduleDAO;
import com.app.yeoback_backend.repository.TripCompanionDAO;
import com.app.yeoback_backend.repository.TripDAO;
import com.app.yeoback_backend.repository.TripDayDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TripService {
    private final TripDAO tripDAO;
    private final TripCompanionDAO tripCompanionDAO;
    private final TripDayDAO tripDayDAO;
    private final ScheduleDAO scheduleDAO;

    // ===================== 여행 =====================

    // 여행 만들기 (여행 → 동행 → 일차 → 일정 순서로 저장)
    public TripDTO createTrip(TripDTO tripDTO, Long memberId) {
        tripDTO.setMemberId(memberId);
        // 상태를 안 보내면 '작성 중'으로 시작
        if (tripDTO.getTripStatus() == null) {
            tripDTO.setTripStatus(TripStatus.WRITE);
        }
        // 1. 여행 저장 → tripDTO에 새 id가 채워짐
        tripDAO.save(tripDTO);
        Long tripId = tripDTO.getId();

        // 2. 동행 저장
        saveCompanions(tripId, tripDTO.getCompanions());

        // 3. 일차 저장 (화면에서 안 보냈으면 출발일~도착일로 자동 생성)
        List<TripDayDTO> days = tripDTO.getDays();
        if (days == null || days.isEmpty()) {
            days = makeDays(tripDTO.getTripStartDate(), tripDTO.getTripEndDate());
            tripDTO.setDays(days);
        }
        for (TripDayDTO day : days) {
            day.setTripId(tripId);
            tripDayDAO.save(day);

            // 4. 그 일차의 일정 저장
            for (ScheduleDTO schedule : nullToEmpty(day.getSchedules())) {
                schedule.setTripDayId(day.getId());
                schedule.setMemberId(memberId);
                scheduleDAO.save(schedule);
            }
        }
        return tripDTO;
    }

    // 여행 한 건 조회 (동행 · 일차 · 일정까지 모두 담아서)
    @Transactional(readOnly = true)
    public TripDTO getTrip(Long id, Long memberId) {
        TripDTO tripDTO = findMyTrip(id, memberId);

        tripDTO.setCompanions(tripCompanionDAO.findAllByTripId(id));

        List<TripDayDTO> days = tripDayDAO.findAllByTripId(id);
        for (TripDayDTO day : days) {
            day.setSchedules(scheduleDAO.findAllByTripDayId(day.getId()));
        }
        tripDTO.setDays(days);
        return tripDTO;
    }

    // 내 여행 목록 (목록 화면이라 동행 · 일차는 담지 않음)
    @Transactional(readOnly = true)
    public List<TripDTO> getMyTrips(Long memberId) {
        return tripDAO.findAllByMemberId(memberId);
    }

    // 여행 수정 (여행 정보 + 동행은 지우고 다시 저장)
    public void updateTrip(TripDTO tripDTO, Long memberId) {
        findMyTrip(tripDTO.getId(), memberId);
        tripDTO.setMemberId(memberId);
        tripDAO.update(tripDTO);

        tripCompanionDAO.deleteAllByTripId(tripDTO.getId());
        saveCompanions(tripDTO.getId(), tripDTO.getCompanions());
    }

    // 여행 삭제 (저장의 반대 순서: 일정 → 일차 → 동행 → 여행)
    public void deleteTrip(Long id, Long memberId) {
        findMyTrip(id, memberId);
        scheduleDAO.deleteAllByTripId(id);
        tripDayDAO.deleteAllByTripId(id);
        tripCompanionDAO.deleteAllByTripId(id);
        tripDAO.delete(id);
    }

    // ===================== 일차 =====================

    // 일차 제목 수정
    public void updateDay(TripDayDTO tripDayDTO, Long memberId) {
        TripDayDTO day = tripDayDAO.findById(tripDayDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("일차를 찾을 수 없습니다."));
        findMyTrip(day.getTripId(), memberId);
        tripDayDAO.update(tripDayDTO);
    }

    // ===================== 일차별 일정 =====================

    // 일차의 일정 목록
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getSchedules(Long tripDayId, Long memberId) {
        checkMyDay(tripDayId, memberId);
        return scheduleDAO.findAllByTripDayId(tripDayId);
    }

    // 일정 추가
    public ScheduleDTO addSchedule(ScheduleDTO scheduleDTO, Long memberId) {
        checkMyDay(scheduleDTO.getTripDayId(), memberId);
        scheduleDTO.setMemberId(memberId);
        scheduleDAO.save(scheduleDTO);
        return scheduleDTO;
    }

    // 일정 수정
    public void updateSchedule(ScheduleDTO scheduleDTO, Long memberId) {
        ScheduleDTO schedule = scheduleDAO.findById(scheduleDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다."));
        checkMyDay(schedule.getTripDayId(), memberId);
        scheduleDAO.update(scheduleDTO);
    }

    // 일정 삭제
    public void deleteSchedule(Long id, Long memberId) {
        ScheduleDTO schedule = scheduleDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다."));
        checkMyDay(schedule.getTripDayId(), memberId);
        scheduleDAO.delete(id);
    }

    // ===================== 내부에서만 쓰는 메서드 =====================

    // 로그인한 회원 id (JWT에는 이메일이 들어 있어서 이메일로 조회)
    @Transactional(readOnly = true)
    public Long getMemberIdByEmail(String memberEmail) {
        return tripDAO.findMemberIdByEmail(memberEmail)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
    }

    // 내 여행인지 확인하고 가져오기
    private TripDTO findMyTrip(Long id, Long memberId) {
        TripDTO tripDTO = tripDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("여행을 찾을 수 없습니다."));
        if (!tripDTO.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("내 여행만 볼 수 있습니다.");
        }
        return tripDTO;
    }

    // 일차가 내 여행에 속하는지 확인
    private void checkMyDay(Long tripDayId, Long memberId) {
        TripDayDTO day = tripDayDAO.findById(tripDayId)
                .orElseThrow(() -> new IllegalArgumentException("일차를 찾을 수 없습니다."));
        findMyTrip(day.getTripId(), memberId);
    }

    // 동행 저장
    private void saveCompanions(Long tripId, List<TripCompanionDTO> companions) {
        for (TripCompanionDTO companion : nullToEmpty(companions)) {
            companion.setTripId(tripId);
            tripCompanionDAO.save(companion);
        }
    }

    // 출발일 ~ 도착일로 일차 만들기 (11.09~11.11 → 11.09, 11.10, 11.11)
    private List<TripDayDTO> makeDays(LocalDate startDate, LocalDate endDate) {
        List<TripDayDTO> days = new ArrayList<>();
        if (startDate == null || endDate == null) {
            return days;
        }
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            TripDayDTO day = new TripDayDTO();
            day.setTripDay(date);
            days.add(day);
        }
        return days;
    }

    // null 목록을 빈 목록으로 (for문에서 오류 방지)
    private <T> List<T> nullToEmpty(List<T> list) {
        return list == null ? new ArrayList<>() : list;
    }
}