package com.serasa.weighing.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "weighings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Weighing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scale_id", nullable = false)
    private Scale scale;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private TransportTransaction transaction;

    @Column(nullable = false)
    private String plate;

    @Column(name = "gross_weight", nullable = false)
    private Double grossWeight;

    @Column(nullable = false)
    private Double tare;

    @Column(name = "net_weight", nullable = false)
    private Double netWeight;

    @Column(name = "grain_type", nullable = false)
    private String grainType;

    @Column(name = "cargo_cost", nullable = false)
    private BigDecimal cargoCost;

    @Column(name = "weighed_at", nullable = false)
    private Instant weighedAt;
}
