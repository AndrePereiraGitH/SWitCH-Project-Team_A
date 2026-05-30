package TOPSECRET.dto.auction.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Inbound DTO for placing a bid on an existing auction.
 *
 * <p>Contains the monetary value of the bid to be placed.
 * The bidder's identity is resolved from the request context
 * and is not included in this DTO.</p>
 *
 * @param value the monetary value of the bid; must not be {@code null}
 */
public record PlaceBidDTO(

    @NotNull(message = "value can't be null")
    BigDecimal value) {}
