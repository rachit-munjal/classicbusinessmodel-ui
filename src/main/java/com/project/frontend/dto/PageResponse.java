package com.project.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageResponse<T> {

    private List<T> content = new ArrayList<>();
    private int totalPages;
    private long totalElements;
    private int number;   // current page (0-indexed)
    private int size;
    private boolean first;
    private boolean last;

    public List<T> getContent() {
        return content != null ? content : new ArrayList<>();
    }

    public boolean hasPreviousPage() {
        return number > 0;
    }

    public boolean hasNextPage() {
        return number < totalPages - 1;
    }

    public int getPreviousPage() {
        return Math.max(0, number - 1);
    }

    public int getNextPage() {
        return Math.min(Math.max(0, totalPages - 1), number + 1);
    }

    /** Returns a capped list of page numbers to show in pagination (max 7 pages visible). */
    public List<Integer> getPageRange() {
        List<Integer> pages = new ArrayList<>();
        int start = Math.max(0, number - 3);
        int end   = Math.min(totalPages - 1, number + 3);
        for (int i = start; i <= end; i++) {
            pages.add(i);
        }
        return pages;
    }
}
