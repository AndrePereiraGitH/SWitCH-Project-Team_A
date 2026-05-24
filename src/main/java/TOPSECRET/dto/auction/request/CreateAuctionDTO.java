package TOPSECRET.dto.auction.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Inbound DTO for creating a new auction.
 *
 * <p>Encapsulates all data required to register an auction associated with an existing
 * listing, including the time window, the starting price, and the buyout price.</p>
 *
 * <p>All fields are validated via Jakarta Bean Validation annotations.
 * {@code listingId} must not be blank, {@code startDateTime}, {@code endDateTime}
 * and {@code initialPrice} must not be {@code null}. {@code buyoutPrice} is optional.</p>
 *
 * @param listingId      the identifier of the listing to which the auction will be attached;
 *                       must not be {@code blank}
 * @param startDateTime  the date and time at which the auction starts; must not be {@code null}
 * @param endDateTime    the date and time at which the auction ends; must not be {@code null}
 * @param initialPrice   the opening bid price for the auction; must not be {@code null}
 * @param buyoutPrice    the immediate purchase price that closes the auction early;
 *                       may be {@code null}
 */

public record CreateAuctionDTO(

        @NotBlank(message = "listingId can´t be null or empty")
        String listingId,

        @NotNull(message = "startDateTime can't be null")
        LocalDateTime startDateTime,

        @NotNull(message = "endDateTime can't be null")
        LocalDateTime endDateTime,

        @NotNull(message = "initialPrice can't be null")
        BigDecimal initialPrice,

        BigDecimal buyoutPrice) {
}

