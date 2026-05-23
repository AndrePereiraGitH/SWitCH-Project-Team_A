package TOPSECRET.dto.Auction.response;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Outbound DTO representing an auction returned to the client.
 *
 * <p>Extends {@link RepresentationModel} to support HATEOAS hypermedia links,
 * allowing clients to navigate related resources directly from the response.</p>
 *
 * <p>Contains the auction's own data ({@code auctionId}, {@code listingId}, {@code auctionStartDateTime},
 * {@code auctionEndDateTime}, {@code closedAt}, {@code initialPrice}, {@code buyoutPrice},
 * {@code finalPrice}, {@code auctionStatus}), as well as denormalized fields sourced
 * from the Listing aggregate ({@code listingTitle}, {@code listingDescription})
 * included for display convenience.</p>
 *
 * <p>Fields {@code buyoutPrice}, {@code finalPrice} and {@code closedAt} may be {@code null} —
 * the former if the auction has no buyout price, the latter two if the auction
 * has not yet ended.</p>
 *
 * <p>Equality is based solely on this class's own fields, excluding inherited
 * fields from {@link RepresentationModel} ({@code callSuper = false}).</p>
 *
 * @see org.springframework.hateoas.RepresentationModel
 */

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@ToString
@Getter
public class AuctionDTO extends RepresentationModel<AuctionDTO> {

    String auctionId;
    String listingId;

    String listingTitle;
    String listingDescription;

    LocalDateTime auctionStartDateTime;
    LocalDateTime auctionEndDateTime;
    LocalDateTime closedAt;

    BigDecimal initialPrice;
    BigDecimal buyoutPrice;
    BigDecimal finalPrice;

    String auctionStatus;
}
