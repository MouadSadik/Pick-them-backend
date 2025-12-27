package com.cigi.pickthem.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "matches")
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_a_id", nullable = false)
    private TeamEntity teamA;

    @ManyToOne
    @JoinColumn(name = "team_b_id", nullable = false)
    private TeamEntity teamB;

    private Integer scoreA;
    private Integer scoreB;

    private int pointsWinA;
    private int pointsWinB;
    private int pointsDraw;

    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = false)
    private TourEntity tour;
}
