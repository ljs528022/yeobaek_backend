package com.app.yeoback_backend.controller.trip;

import com.app.yeoback_backend.aop.annotation.LogStatus;
import com.app.yeoback_backend.dto.ScheduleDTO;
import com.app.yeoback_backend.dto.TripDTO;
import com.app.yeoback_backend.dto.TripDayDTO;
import com.app.yeoback_backend.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.app.yeoback_backend.auth.CustomUserDetails;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trip")
@RequiredArgsConstructor
@Slf4j
public class TripAPIController {
    private final TripService tripService;

    // ===================== 여행 =====================

    // 여행 만들기
    @PostMapping("/create")
    @LogStatus
    public ResponseEntity<?> create(@AuthenticationPrincipal CustomUserDetails user, @RequestBody TripDTO tripDTO) {
        log.info("tripDTO {}", tripDTO);
        TripDTO saved = tripService.createTrip(tripDTO, user.getId());

        return ResponseEntity.ok(Map.of("message", "여행 만들기 성공", "trip", saved));
    }

    // 여행 한 건 조회 (동행 · 일차 · 일정 포함)
    @GetMapping("/detail")
    @LogStatus
    public ResponseEntity<?> detail(@AuthenticationPrincipal CustomUserDetails user, @RequestParam Long id) {
        TripDTO trip = tripService.getTrip(id, user.getId());

        return ResponseEntity.ok(Map.of("message", "여행 조회 성공", "trip", trip));
    }

    // 내 여행 목록
    @GetMapping("/list")
    @LogStatus
    public ResponseEntity<?> list(@AuthenticationPrincipal CustomUserDetails user) {
        List<TripDTO> trips = tripService.getMyTrips(user.getId());

        return ResponseEntity.ok(Map.of("message", "여행 목록 조회 성공", "trips", trips));
    }

    // 여행 수정
    @PutMapping("/update")
    @LogStatus
    public ResponseEntity<?> update(@AuthenticationPrincipal CustomUserDetails user, @RequestBody TripDTO tripDTO) {
        log.info("tripDTO {}", tripDTO);
        tripService.updateTrip(tripDTO, user.getId());

        return ResponseEntity.ok(Map.of("message", "여행 수정 성공"));
    }

    // 여행 삭제
    @DeleteMapping("/delete")
    @LogStatus
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomUserDetails user, @RequestParam Long id) {
        tripService.deleteTrip(id, user.getId());

        return ResponseEntity.ok(Map.of("message", "여행 삭제 성공"));
    }

    // ===================== 일차 =====================

    // 일차 제목 · 오늘의 한 문장 수정
    @PutMapping("day/update")
    @LogStatus
    public ResponseEntity<?> updateDay(@AuthenticationPrincipal CustomUserDetails user, @RequestBody TripDayDTO tripDayDTO) {
        log.info("tripDayDTO {}", tripDayDTO);
        tripService.updateDay(tripDayDTO, user.getId());

        return ResponseEntity.ok(Map.of("message", "일차 수정 성공"));
    }

    // ===================== 일차별 일정 =====================

    // 일차의 일정 목록
    @GetMapping("schedule/list")
    @LogStatus
    public ResponseEntity<?> scheduleList(@AuthenticationPrincipal CustomUserDetails user, @RequestParam Long tripDayId) {
        List<ScheduleDTO> schedules = tripService.getSchedules(tripDayId, user.getId());

        return ResponseEntity.ok(Map.of("message", "일정 목록 조회 성공", "schedules", schedules));
    }

    // 일정 추가
    @PostMapping("schedule/add")
    @LogStatus
    public ResponseEntity<?> addSchedule(@AuthenticationPrincipal CustomUserDetails user, @RequestBody ScheduleDTO scheduleDTO) {
        log.info("scheduleDTO {}", scheduleDTO);
        ScheduleDTO saved = tripService.addSchedule(scheduleDTO, user.getId());

        return ResponseEntity.ok(Map.of("message", "일정 추가 성공", "schedule", saved));
    }

    // 일정 수정
    @PutMapping("schedule/update")
    @LogStatus
    public ResponseEntity<?> updateSchedule(@AuthenticationPrincipal CustomUserDetails user, @RequestBody ScheduleDTO scheduleDTO) {
        log.info("scheduleDTO {}", scheduleDTO);
        tripService.updateSchedule(scheduleDTO, user.getId());

        return ResponseEntity.ok(Map.of("message", "일정 수정 성공"));
    }

    // 일정 삭제
    @DeleteMapping("schedule/delete")
    @LogStatus
    public ResponseEntity<?> deleteSchedule(@AuthenticationPrincipal CustomUserDetails user, @RequestParam Long id) {
        tripService.deleteSchedule(id, user.getId());

        return ResponseEntity.ok(Map.of("message", "일정 삭제 성공"));
    }

}
