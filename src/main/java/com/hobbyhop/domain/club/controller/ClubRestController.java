package com.hobbyhop.domain.club.controller;

import com.hobbyhop.domain.club.dto.ClubRequestDTO;
import com.hobbyhop.domain.club.dto.ClubResponseDTO;
import com.hobbyhop.domain.club.service.ClubService;
import com.hobbyhop.global.request.PageRequestDTO;
import com.hobbyhop.global.response.ApiResponse;
import com.hobbyhop.global.response.PageResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/clubs")
@SecurityRequirement(name = "Bearer Authentication")
public class ClubRestController {
    private final ClubService clubService;

    // TODO : implement 자신이 속한 그룹만 가져오는 api 필요함.

    @Operation(summary = "새로운 모임 생성")
    @PostMapping
    public ResponseEntity<ApiResponse> makeClub(@RequestBody ClubRequestDTO clubRequestDTO) {
        ClubResponseDTO clubResponseDTO = clubService.makeClub(clubRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok(clubResponseDTO));
    }

    // TODO : implement 검색, 카테고리별 정렬, 검색
    @Operation(summary = "모든 모임 리스트 조회")
    @GetMapping
    public ResponseEntity<ApiResponse> getClubList(PageRequestDTO pageRequestDTO) {
        PageResponseDTO<ClubResponseDTO> list = clubService.getAllClubs(pageRequestDTO);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @Operation(summary = "모임 조회")
    @GetMapping("/{clubId}")
    public ResponseEntity<ApiResponse> getClub(@PathVariable("clubId") Long clubId) {
        ClubResponseDTO clubResponseDTO = clubService.getClub(clubId);
        return ResponseEntity.ok(ApiResponse.ok(clubResponseDTO));
    }

    @Operation(summary = "모임 정보 수정")
    @PatchMapping("/{clubId}")
    public ResponseEntity<ApiResponse> modifyClub(@PathVariable("clubId") Long clubId, @RequestBody ClubRequestDTO clubRequestDTO) {
      ClubResponseDTO clubResponseDTO = clubService.modifyClub(clubId, clubRequestDTO);
      return ResponseEntity.ok(ApiResponse.ok(clubResponseDTO));
    }

    @Operation(summary = "모임 삭제")
    @DeleteMapping("/{clubId}")
    public ResponseEntity<ApiResponse> removeClub(@PathVariable("clubId") Long clubId) {
        clubService.removeClubById(clubId);
        return ResponseEntity.ok(ApiResponse.ok("삭제 성공"));
    }
}
