package com.app.yeoback_backend.repository;

import com.app.yeoback_backend.dto.TripDTO;
import com.app.yeoback_backend.mapper.TripMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TripDAO {
    private final TripMapper tripMapper;
    // 여행 만들기
    public void save(TripDTO tripDTO) {
        tripMapper.insert(tripDTO);
    }
    // 여행 한 건 조회
    public Optional<TripDTO> findById(Long id) {
        return tripMapper.selectById(id);
    }
    // 내 여행 목록
    public List<TripDTO> findAllByMemberId(Long memberId) {
        return tripMapper.selectAllByMemberId(memberId);
    }
    // 여행 수정
    public void update(TripDTO tripDTO) {
        tripMapper.update(tripDTO);
    }
    // 여행 삭제
    public void delete(Long id) {
        tripMapper.delete(id);
    }
    // 로그인한 회원 id 조회 (이메일로)
    public Optional<Long> findMemberIdByEmail(String memberEmail) {
        return tripMapper.selectMemberIdByEmail(memberEmail);
    }
}
