package com.jonatas.finance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "resposta paginada")
public record PageResponse<TData>(

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp,

    @Schema(example = "200")
    int status,

    List<TData> data,

    PageMetadata meta
) {

    @Schema(description = "metadata da paginação")
    public record PageMetadata(
        @Schema(example = "1", description = "pagina atual")
        Integer page,
        @Schema(example = "20", description = "tamanho da pagina")
        Integer size,
        @Schema(example = "100", description = "total de registros da consulta")
        Integer totalElements,
        @Schema(example = "5", description = "pagina atual")
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
