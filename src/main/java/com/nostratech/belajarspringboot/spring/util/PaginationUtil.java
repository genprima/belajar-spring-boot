package com.nostratech.belajarspringboot.spring.util;

import org.springframework.data.domain.Sort;

public class PaginationUtil {

    public static Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        }
        return Sort.Direction.DESC;
    }
}
