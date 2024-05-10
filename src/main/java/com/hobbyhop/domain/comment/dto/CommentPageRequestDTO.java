package com.hobbyhop.domain.comment.dto;

import com.hobbyhop.global.request.PageRequestDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Pageable;

@Getter
@SuperBuilder
public class CommentPageRequestDTO extends PageRequestDTO {
    public CommentPageRequestDTO(int page, int size, boolean isDesc) {
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

    public void setStandard(String standard) {
        this.standard = standard;
    }

    private String standard;
}
