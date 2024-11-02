package org.self.learn.es.explore.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author ll
 * @since 2024-10-27 11:00
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestIndex {
    @JsonProperty("AvgTicketPrice")
    private BigDecimal avgTicketPrice;
}
