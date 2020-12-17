package io.haechi.henesis.assignment.domain;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pagination {
    private String nextUrl;
    private String previousUrl;
    private Integer totalCount;
    private Integer pageSize;

    private Pagination (
            String nextUrl,
            String previousUrl,
            Integer totalCount
    ){
        this.nextUrl=nextUrl;
        this.previousUrl=previousUrl;
        this.totalCount = totalCount;
    }

    public static Pagination of(
            String nextUrl,
            String previousUrl,
            Integer totalCount
    ){
        return new Pagination(
                nextUrl,
                previousUrl,
                totalCount
        );
    }

}
