package com.hobbyhop.domain.club.dto;

import com.hobbyhop.global.request.PageRequestDTO;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@NoArgsConstructor
public class ClubPageRequestDTO extends PageRequestDTO {
    public ClubPageRequestDTO(int page, int size, boolean isDesc) {
        super(page, size, isDesc);
    }

    @Override
    public int getPage() {
        return super.getPage();
    }

    @Override
    public int getSize() {
        return super.getSize();
    }

    @Override
    public boolean isDesc() {
        return super.isDesc();
    }

    @Override
    public void setPage(int page) {
        super.setPage(page);
    }

    @Override
    public void setSize(int size) {
        super.setSize(size);
    }

    @Override
    public void setDesc(boolean isDesc) {
        super.setDesc(isDesc);
    }

    @Override
    public Pageable getPageable(String... props) {
        return super.getPageable(props);
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    private String keyword;
    private Long categoryId;

}
