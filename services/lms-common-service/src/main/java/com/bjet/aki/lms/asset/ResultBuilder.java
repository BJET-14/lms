package com.bjet.aki.lms.asset;

public class ResultBuilder {
    public static <T, R> R build(T entity, ResultMapper<T, R> mapper) {
        return mapper.map(entity);
    }
}
