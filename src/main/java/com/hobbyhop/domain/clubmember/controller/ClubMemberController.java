package com.hobbyhop.domain.clubmember.controller;


import com.hobbyhop.domain.clubmember.service.ClubMemberService;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}")
@SecurityRequirement(name = "Bearer Authentication")
public class ClubMemberController {
    private final ClubMemberService clubMemberService;

    @Operation(summary = "모임의 관리자인지 체크")
    @GetMapping("/checkPermission")
    public ApiResponse<?> checkPermission(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("clubId") Long clubId) {
        return ApiResponse.ok(clubMemberService.isAdminMember(clubId, userDetails.getUser().getId()));
    }

    @Operation(summary = "모임의 멤버인지 체크")
    @GetMapping("/checkClubMember")
    public ApiResponse<?> checkClubMember(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable("clubId") Long clubId) {
        return ApiResponse.ok(clubMemberService.isClubMember(clubId, userDetails.getUser().getId()));
    }
}
