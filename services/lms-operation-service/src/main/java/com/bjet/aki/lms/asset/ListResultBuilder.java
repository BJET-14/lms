package com.bjet.aki.lms.asset;

import java.util.List;
import java.util.stream.Collectors;

public class ListResultBuilder {
    public static <T,R> List<R> build(List<T> items, ResultMapper<T,R> mapper){
        return (List<R>) items.stream().map(item -> mapper.map(item)).collect(Collectors.toList());

    }
}
