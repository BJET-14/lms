package com.bjet.aki.lms.asset;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PagedResult<T> {

    private List<T> content;

    private int pageSize;
    private int pageNumber;
    private int pageCount;

    public PagedResult() {
        this.pageCount = 0;
        this.pageNumber = 0;
        this.pageSize = 0;
        this.content = new ArrayList<>();
    }

    public PagedResult(List<T> content, int pageSize, int pageNumber, int pageCount) {
        this.content = content;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.pageCount = pageCount;
    }

    public static final <X> PagedResult<X> EmptyPage(){
        return new PagedResult<X>();
    }
}

