package com.flexisaf.enrollmentservice.dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class ResponseList<T> {
    private List<T> payload;
    private String timestamp;
    private int currentPage;
    private String direction;
    private long totalSize;
    private long totalPages;
    private String status = "Retrieved Successfully";

    public ResponseList(Page<T> page) {
        initialize(page);
    }

    public ResponseList(List<T> list){
        this.payload = list;
        this.totalPages = 1;
        this.totalSize = list.size();
        this.currentPage = 0;
        this.timestamp = LocalDateTime.now().toString();
    }



    public void initialize(Page<T> page){
        this.payload = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalSize = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.direction = page.getSort().toString();
        this.timestamp = LocalDateTime.now().toString();
    }

    public Page<T> convertListToPage(List<T> list, Pageable pageable){
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), list.size());
        final Page<T> page = new PageImpl<>(list.subList(start, end),
                pageable, list.size());
        return page;
    }

    public static <M,T> ResponseList<T> createResponseList(Page<M> data, Function<M, T> transformer){
        ResponseList<T> responseList = new ResponseList<>();
        responseList.setPayload(data.getContent().stream().map(transformer).collect(Collectors.toList()));
        responseList.setDirection(data.getSort().toString());
        responseList.setCurrentPage(data.getNumber());
        responseList.setTimestamp(LocalDateTime.now().toString());
        responseList.setTotalSize(data.getTotalElements());
        responseList.setTotalPages(data.getTotalPages());

        return responseList;
    }
}
