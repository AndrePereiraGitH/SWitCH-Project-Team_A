package TOPSECRET.dto.auction.response;

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
 * {@code finalPrice}, {@code auctionStatus}, {@code currentHighestBid}, {@code numberOfBids}),
 * as well as {@code listingTitle} sourced from the Listing aggregate, included for display convenience.</p>
 *
 * <p>Fields {@code buyoutPrice}, {@code finalPrice}, {@code closedAt} and {@code currentHighestBid}
 * may be {@code null}.</p>
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

    LocalDateTime auctionStartDateTime;
    LocalDateTime auctionEndDateTime;
    LocalDateTime closedAt;

    BigDecimal initialPrice;
    BigDecimal buyoutPrice;
    BigDecimal finalPrice;

    String auctionStatus;

    BigDecimal currentHighestBid;
    int numberOfBids;
}
