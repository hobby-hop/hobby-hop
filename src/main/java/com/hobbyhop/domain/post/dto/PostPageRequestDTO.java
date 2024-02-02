package com.hobbyhop.domain.post.dto;

import com.hobbyhop.global.request.PageRequestDTO;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class PostPageRequestDTO extends PageRequestDTO {
    public PostPageRequestDTO(int page, int size, boolean isDesc) {
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    private String keyword;
}
