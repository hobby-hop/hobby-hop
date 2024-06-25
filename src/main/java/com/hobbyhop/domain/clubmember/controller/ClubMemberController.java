package com.hobbyhop.domain.clubmember.controller;


import com.hobbyhop.domain.clubmember.service.ClubMemberService;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}")
public class ClubMemberController {
    private final ClubMemberService clubMemberService;

    @Operation(summary = "모임의 관리자인지 체크")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/checkPermission")
    public ApiResponse<?> checkPermission(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("clubId") Long clubId) {
        return ApiResponse.ok(clubMemberService.isAdminMember(clubId, userDetails.getUser().getId()));
    }

    @Operation(summary = "모임의 멤버인지 체크")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/checkClubMember")
    public ApiResponse<?> checkClubMember(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("clubId") Long clubId) {
        return ApiResponse.ok(clubMemberService.isClubMember(clubId, userDetails.getUser().getId()));
    }

    @Operation(summary = "모임 탈퇴")
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/clubmembers/{userId}")
    public ApiResponse<?> leaveMember(@PathVariable("clubId") Long clubId, @PathVariable("userId") Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        clubMemberService.leaveMember(clubId, userDetails.getUser(), userId);
        return ApiResponse.ok("처리 완료");
    }
}
