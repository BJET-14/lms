package com.bjet.aki.lms.asset;

public interface ResultMapper<T,R> {
    R map(T entity);
}
