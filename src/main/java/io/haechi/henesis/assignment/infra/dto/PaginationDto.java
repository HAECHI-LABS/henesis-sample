package io.haechi.henesis.assignment.infra.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaginationDto<T> {
    private Meta pagination;
    private List<T> results = new ArrayList<>();

    public PaginationDto(
            String nextUrl,
            String previousUrl,
            int totalCount,
            List<T> results
    ) {
        this.pagination = new Meta(
                nextUrl,
                previousUrl,
                totalCount
        );
        this.results = results;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Meta {
        private String nextUrl;
        private String previousUrl;
        private int totalCount;
    }
}
