package TOPSECRET.domain.DirectSale;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.List;

/**
 * Factory responsible for creating DirectSale instances,
 * ensuring that all listings are of sale type DIRECT_SALE.
 */

public class DirectSaleFactory {

    /**
     * Creates a new DirectSale after validating that all provided listings
     * are of type DIRECT_SALE.
     *
     * <p>This method always creates a DirectSale in an unfinished state
     * (isFinished = false). A DirectSale represents the buyer's shopping cart,
     * which begins as an open and modifiable collection of listings. The sale
     * is only marked as finished when a corresponding Transaction is created,
     * meaning the buyer has completed the purchase process.</p>
     *
     * @param listings the listings included in the sale; must not be null and must all be DIRECT_SALE
     * @param buyer    the buyer associated with the sale
     * @return a new DirectSale instance in an unfinished state
     * @throws IllegalArgumentException if listings is null or contains any listing not of type DIRECT_SALE
     */

    public DirectSale create(List<ListingId> listings, UserId buyer) {

        if (listings == null) {
            throw new IllegalArgumentException("Listings cannot be null");
        }

//        boolean invalid = listings.stream()
//                .anyMatch(l -> l.getSaleType() != SaleType.DIRECT_SALE);

        boolean invalid = true;

        if (invalid) {
            throw new IllegalArgumentException(
                    "All listings must be of type DIRECT_SALE to create a DirectSale"
            );
        }

        return new DirectSale(listings, buyer, false);
    }
}
