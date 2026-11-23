package com.app.yeoback_backend.mapper;

import com.app.yeoback_backend.dto.TripDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TripMapper {
    // 여행 만들기
    public void insert(TripDTO tripDTO);
    // 여행 한 건 조회
    public Optional<TripDTO> selectById(Long id);
    // 내 여행 목록
    public List<TripDTO> selectAllByMemberId(Long memberId);
    // 여행 수정
    public void update(TripDTO tripDTO);
    // 여행 삭제
    public void delete(Long id);
    // 로그인한 회원 id 조회 (이메일로)
    public Optional<Long> selectMemberIdByEmail(String memberEmail);
}