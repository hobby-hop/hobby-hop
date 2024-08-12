package com.hobbyhop.domain.club.controller;

import com.hobbyhop.domain.club.dto.ClubModifyDTO;
import com.hobbyhop.domain.club.dto.ClubPageRequestDTO;
import com.hobbyhop.domain.club.dto.ClubRequestDTO;
import com.hobbyhop.domain.club.service.ClubService;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
public class ClubController {
    private final ClubService clubService;

    @Operation(summary = "새로운 모임 생성")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public ApiResponse<?> makeClub(@Valid @RequestBody ClubRequestDTO clubRequestDTO,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(clubService.makeClub(clubRequestDTO, userDetails.getUser()));
    }

    @Operation(summary = "모든 모임 리스트 조회")
    @GetMapping
    public ApiResponse<?> getClubList(ClubPageRequestDTO pageRequestDTO) {
        return ApiResponse.ok(clubService.getAllClubs(pageRequestDTO));
    }

    @Operation(summary = "모임 조회")
    @GetMapping("/{clubId}")
    public ApiResponse<?> getClub(@PathVariable("clubId") Long clubId) {
        return ApiResponse.ok(clubService.getClub(clubId));
    }

    @Operation(summary = "내가 가입한 모임 조회")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/my")
    public ApiResponse<?> getMyClubs(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(clubService.getMyClubs(userDetails.getUser()));
    }

    @Operation(summary = "모임 정보 수정")
    @SecurityRequirement(name = "Bearer Authentication")
    @PatchMapping("/{clubId}")
    public ApiResponse<?> modifyClub(@PathVariable("clubId") Long clubId,
                                     @Valid @RequestBody ClubModifyDTO clubModifyDTO,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(clubService.modifyClub(clubId, clubModifyDTO, userDetails.getUser()));
    }

    @Operation(summary = "모임 삭제")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/{clubId}")
    public ApiResponse<?> removeClub(@PathVariable("clubId") Long clubId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        clubService.removeClubById(clubId, userDetails.getUser());

        return ApiResponse.ok("삭제 성공");
    }
}
