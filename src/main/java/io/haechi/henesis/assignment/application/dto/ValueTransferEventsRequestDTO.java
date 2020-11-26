package io.haechi.henesis.assignment.application.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ValueTransferEventsRequestDTO {
    private String updatedAtGte;
    private String size;
}
