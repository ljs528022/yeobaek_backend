package com.app.yeoback_backend.controller.trip;

import com.app.yeoback_backend.service.TripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/trip")
@RequiredArgsConstructor
@Slf4j
public class TripController {
    private final TripService tripService;

    // 새 여행 만들기 페이지 이동 (Mobile 09)
    @GetMapping("/create")
    public String goToCreatePage() {
        // TODO -> 경로 상의 필요함
        return "trip/create";
    }

    // 나의 일정(여행 목록) 페이지 이동 (Mobile 11)
    @GetMapping("/list")
    public String goToListPage() {
        // TODO -> 경로 상의 필요함
        return "trip/list";
    }

    // 일정 추가 페이지 이동 (Mobile 10)
    @GetMapping("/schedule")
    public String goToSchedulePage(@RequestParam Long tripId, Model model) {
        model.addAttribute("tripId", tripId);
        // TODO -> 경로 상의 필요함
        return "trip/schedule";
    }

    // 날짜별 일정 페이지 이동 (Mobile 12)
    @GetMapping("/day")
    public String goToDayPage(@RequestParam Long tripId,
                              @RequestParam(required = false) Integer dayNumber,
                              Model model) {
        model.addAttribute("tripId", tripId);
        model.addAttribute("dayNumber", dayNumber == null ? 1 : dayNumber);
        // TODO -> 경로 상의 필요함
        return "trip/day";
    }
}