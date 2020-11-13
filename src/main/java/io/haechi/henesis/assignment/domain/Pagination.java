package io.haechi.henesis.assignment.domain;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pagination<T> {
    private Meta pagination;
    private List<T> results = new ArrayList<>();

    public Pagination(
            Meta pagination,
            List<T> results
    ) {
        this.pagination = new Meta(
                pagination.getNextUrl(),
                pagination.getPreviousUrl(),
                pagination.getTotalCount()
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
