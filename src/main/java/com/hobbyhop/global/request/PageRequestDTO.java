package com.hobbyhop.global.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;
    private String keyword;
    @Builder.Default
    private boolean isDesc = true;

    public Pageable getPageable(String... props) {
        if(isDesc == false) {
            return PageRequest.of(this.page - 1, this.size, Sort.by(props).ascending());
        } else {
            return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
        }
    }
}