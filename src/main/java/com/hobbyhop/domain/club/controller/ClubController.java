package com.hobbyhop.domain.club.controller;

import com.hobbyhop.domain.club.dto.ClubRequestDTO;
import com.hobbyhop.domain.club.service.ClubService;
import com.hobbyhop.domain.clubmember.service.ClubMemberService;
import com.hobbyhop.global.request.PageRequestDTO;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
@SecurityRequirement(name = "Bearer Authentication")
public class ClubController {
    private final ClubService clubService;
    private final ClubMemberService clubMemberService;

    @Operation(summary = "새로운 모임 생성")
    @PostMapping
    public ApiResponse<?> makeClub(@RequestBody ClubRequestDTO clubRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(clubService.makeClub(clubRequestDTO, userDetails.getUser()));
    }

    // TODO : implement 검색, 카테고리별 정렬
    @Operation(summary = "모든 모임 리스트 조회")
    @GetMapping
    public ApiResponse<?> getClubList(PageRequestDTO pageRequestDTO) {
        return ApiResponse.ok(clubService.getAllClubs(pageRequestDTO));
    }

    @Operation(summary = "모임 조회")
    @GetMapping("/{clubId}")
    public ApiResponse<?> getClub(@PathVariable("clubId") Long clubId) {
        return ApiResponse.ok(clubService.getClub(clubId));
    }

    @Operation(summary = "내가 가입한 모임 조회")
    @GetMapping("/my")
    public ApiResponse<?> getMyClubs(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(clubService.getMyClubs(userDetails.getUser()));
    }

    @Operation(summary = "모임 정보 수정")
    @PatchMapping("/{clubId}")
    public ApiResponse<?> modifyClub(@PathVariable("clubId") Long clubId, @RequestBody ClubRequestDTO clubRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(clubService.modifyClub(clubId, clubRequestDTO, userDetails.getUser()));
    }

    @Operation(summary = "모임 삭제")
    @DeleteMapping("/{clubId}")
    public ApiResponse<?> removeClub(@PathVariable("clubId") Long clubId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        clubService.removeClubById(clubId, userDetails.getUser());
        return ApiResponse.ok("삭제 성공");
    }

    @Operation(summary = "모임 탈퇴")
    @DeleteMapping("/{clubId}/clubmembers")
    public ApiResponse<?> leaveClub(@PathVariable("clubId") Long clubId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        clubMemberService.removeMember(clubId, userDetails.getUser());
        return ApiResponse.ok("탈퇴 완료");
    }
}
