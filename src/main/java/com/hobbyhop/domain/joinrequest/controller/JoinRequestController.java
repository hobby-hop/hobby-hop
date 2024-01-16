package com.hobbyhop.domain.joinrequest.controller;

import com.hobbyhop.domain.joinrequest.dto.JoinRequestDTO;
import com.hobbyhop.domain.joinrequest.dto.JoinResponseDTO;
import com.hobbyhop.domain.joinrequest.service.JoinRequestService;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.security.userdetails.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clubs/{clubId}/requests")
@SecurityRequirement(name = "Bearer Authentication")
public class JoinRequestController {
    private final JoinRequestService joinRequestService;

    @Operation(summary = "가입 신청")
    @PostMapping
    public ApiResponse<?> sendRequest(@PathVariable("clubId") Long clubId,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        JoinResponseDTO joinResponseDTO = joinRequestService.sendRequest(clubId, userDetails.getUser());
        return ApiResponse.ok(joinResponseDTO);
    }

    @Operation(summary = "가입 신청 조회")
    @GetMapping
    public ApiResponse<?> getRequests(@PathVariable("clubId") Long clubId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<JoinResponseDTO> list = joinRequestService.getRequestByClub(clubId, userDetails.getUser());
        return ApiResponse.ok(list);
    }

    @Operation(summary = "가입 신청에 대한 처리")
    @PutMapping("/{requestId}")
    public ApiResponse<?> processRequest(@PathVariable("requestId") Long requestId, @RequestBody JoinRequestDTO joinRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        joinRequestService.processRequest(requestId, joinRequestDTO.getStatus());
        return ApiResponse.ok("성공적으로 처리되었습니다.");
    }

}
