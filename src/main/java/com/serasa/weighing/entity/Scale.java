package com.serasa.weighing.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "scales")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Scale {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(name = "api_key", nullable = false, unique = true)
    private String apiKey;
}
