package com.cigi.pickthem.domain.entities;

import com.cigi.pickthem.domain.enums.MatchResult;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "predictions", uniqueConstraints = {
                @UniqueConstraint(columnNames = { "user_id", "match_id" })
})
public class PredictionEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "predicted_score_a")
        private Integer predictedScoreA;

        @Column(name = "predicted_score_b")
        private Integer predictedScoreB;

        @Enumerated(EnumType.STRING)
        @Column(name = "predicted_result", nullable = false)
        private MatchResult predictedResult;

        private Integer points;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private UserEntity user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "match_id", nullable = false)
        private MatchEntity match;
        //private boolean pointsDispatched;
        @Enumerated(EnumType.STRING)
        private MatchResult calculatedForResult;

}
