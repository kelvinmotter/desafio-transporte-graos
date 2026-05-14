package com.serasa.weighing.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "grain_types")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GrainType {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "purchase_price_per_ton", nullable = false)
    private BigDecimal purchasePricePerTon;
}
