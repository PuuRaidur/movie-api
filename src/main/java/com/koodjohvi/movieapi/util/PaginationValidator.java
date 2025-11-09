package com.koodjohvi.movieapi.util;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaginationValidator {

    private static final int MAX_PAGE_SIZE = 100;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MIN_PAGE_NUMBER = 0;

    public void validatePageable(Pageable pageable) {
        List<String> errors = new ArrayList<>();

        if (pageable.getPageNumber() < MIN_PAGE_NUMBER) {
            errors.add(String.format("Invalid page number: %d. Page numbers start at %d.",
                    pageable.getPageNumber(), MIN_PAGE_NUMBER));
        }

        if (pageable.getPageSize() < MIN_PAGE_SIZE) {
            errors.add(String.format("Invalid page size: %d. Page size must be at least %d.",
                    pageable.getPageSize(), MIN_PAGE_SIZE));
        }

        if (pageable.getPageSize() > MAX_PAGE_SIZE) {
            errors.add(String.format("Invalid page size: %d. Maximum allowed page size is %d.",
                    pageable.getPageSize(), MAX_PAGE_SIZE));
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(String.join(" ", errors));
        }
    }
}
