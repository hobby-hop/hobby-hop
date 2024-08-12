package com.hobbyhop.global.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;
    @Builder.Default
    private boolean isDesc = true;
    @Builder.Default
    private String sortBy = "createdAt";

    public Pageable getPageable(String... props) {
        if(!isDesc) {
            return PageRequest.of(this.page - 1, this.size, Sort.by(props).ascending());
        } else {
            return PageRequest.of(this.page - 1, this.size, Sort.by(props).descending());
        }
    }
}