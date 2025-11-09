package com.koodjohvi.movieapi.util;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PaginationValidator {

    private static final int MAX_PAGE_SIZE = 100;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MIN_PAGE_NUMBER = 0; // Page numbers start at 0

    /**
     * Validates Pageable parameters and throws IllegalArgumentException for invalid values
     */
    public void validatePageable(Pageable pageable) {
        validatePageNumber(pageable.getPageNumber());
        validatePageSize(pageable.getPageSize());
    }

    /**
     * Validates page number (must be >= 0)
     */
    private void validatePageNumber(int pageNumber) {
        if (pageNumber < MIN_PAGE_NUMBER) {
            throw new IllegalArgumentException(
                    String.format("Page number cannot be negative. Minimum page number is %d.", MIN_PAGE_NUMBER)
            );
        }
    }

    /**
     * Validates page size (must be between MIN_PAGE_SIZE and MAX_PAGE_SIZE)
     */
    private void validatePageSize(int pageSize) {
        if (pageSize < MIN_PAGE_SIZE) {
            throw new IllegalArgumentException(
                    String.format("Page size must be at least %d.", MIN_PAGE_SIZE)
            );
        }

        if (pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException(
                    String.format("Page size cannot exceed %d. Maximum allowed is %d.", pageSize, MAX_PAGE_SIZE)
            );
        }
    }

    /**
     * Safe alternative that returns default Pageable instead of throwing exception
     */
    public Pageable getSafePageable(Pageable pageable) {
        try {
            validatePageable(pageable);
            return pageable;
        } catch (IllegalArgumentException e) {
            // Log the validation error if needed
            System.err.println("Pagination validation failed: " + e.getMessage());
            // Return safe default
            return Pageable.ofSize(DEFAULT_PAGE_SIZE);
        }
    }

    /**
     * Get maximum allowed page size
     */
    public int getMaxPageSize() {
        return MAX_PAGE_SIZE;
    }

    /**
     * Get default page size
     */
    public int getDefaultPageSize() {
        return DEFAULT_PAGE_SIZE;
    }
}
