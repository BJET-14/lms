package com.bjet.aki.lms.asset;

import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

public class PagedResultBuilder {
    public static <T,R> PagedResult<R> build(Page<T> page, ResultMapper<T,R> mapper){
        return new PagedResult<>(
                page.getContent()
                        .stream()
                        .map(entity -> mapper.map(entity))
                        .collect(Collectors.toList()),

                page.getSize(),
                page.getNumber(),
                page.getTotalPages());

    }
}
