package com.hobbyhop.domain.category.controller;

import com.hobbyhop.domain.category.dto.CategoryRequestDTO;
import com.hobbyhop.domain.category.dto.CategoryResponseDTO;
import com.hobbyhop.domain.category.service.CategoryService;
import com.hobbyhop.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
@SecurityRequirement(name = "Bearer Authentication")
public class CategoryRestController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse> makeCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO categoryResponseDTO = categoryService.makeCategory(categoryRequestDTO);

        return ResponseEntity.ok(ApiResponse.ok(categoryResponseDTO));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse> removeCategory(@PathVariable("categoryId") Long categoryId) {
        categoryService.removeCategory(categoryId);

        return ResponseEntity.ok(ApiResponse.ok("카테고리가 삭제되었습니다."));
    }


}
