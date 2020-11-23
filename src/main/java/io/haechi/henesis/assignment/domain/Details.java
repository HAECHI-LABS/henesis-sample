package io.haechi.henesis.assignment.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Details {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(name="fromAddress")
    private String from;
    @Column(name="toAddress")
    private String to;
    private String hash;
    private String status;
    private String error;

    @Builder
    public Details (String from,
                    String to,
                    String hash,
                    String status,
                    String error){
        this.from = from;
        this.to = to;
        this.hash = hash;
        this.status = status;
        this.error = error;
    }
}
