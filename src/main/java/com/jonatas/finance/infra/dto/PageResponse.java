package com.jonatas.finance.infra.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public record PageResponse<TData>(

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp,

    int status,

    List<TData> data,

    PageMetadata meta
) {

    public record PageMetadata(
        Integer page,
        Integer size,
        Integer totalElements,
        Integer totalPages
    ) {
    }


    public static <TData> PageResponse<TData> from(Page<TData> page) {
        return new PageResponse<>(
            LocalDateTime.now(),
            200,
            page.getContent(),
            new PageMetadata(
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalPages()
            )
        );
    }


}
