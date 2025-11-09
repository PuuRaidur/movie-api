package com.koodjohvi.movieapi.util;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PaginationValidator {
    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 20;

    public void validatePageable(Pageable pageable) {
        if (pageable.getPageNumber() < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }

        if (pageable.getPageSize() < 1) {
            throw new IllegalArgumentException("Page size must be at least 1");
        }

        if (pageable.getPageSize() > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("Page size cannot exceed " + MAX_PAGE_SIZE);
        }
    }

    public Pageable getSafePageable(Pageable pageable) {
        try {
            validatePageable(pageable);
            return pageable;
        } catch (IllegalArgumentException e) {
            // Return safe default if validation fails
            return Pageable.ofSize(DEFAULT_PAGE_SIZE);
        }
    }
}
