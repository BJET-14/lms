package com.bjet.aki.asset;

public interface ResultMapper<T,R> {
    R map(T entity);
}
