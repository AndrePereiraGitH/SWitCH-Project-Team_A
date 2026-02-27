package TOPSECRET.domain.Factory;

import TOPSECRET.domain.DirectSale;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing;
import TOPSECRET.domain.User;

import java.util.List;

/**
 * Factory responsible for creating DirectSale instances,
 * ensuring that all listings are of sale type DIRECT_SALE.
 */

public class DirectSaleFactory {

    /**
     * Creates a DirectSale after validating that all listings
     * are of type DIRECT_SALE.
     *
     * @param listings the listings included in the sale
     * @param buyer    the buyer involved
     * @return a new DirectSale instance
     * @throws IllegalArgumentException if any listing is not DIRECT_SALE
     */

    public DirectSale create(List<Listing> listings, User buyer) {

        if (listings == null) {
            throw new IllegalArgumentException("Listings cannot be null");
        }

        boolean invalid = listings.stream()
                .anyMatch(l -> l.getSaleType() != SaleType.DIRECT_SALE);

        if (invalid) {
            throw new IllegalArgumentException(
                    "All listings must be of type DIRECT_SALE to create a DirectSale"
            );
        }

        return new DirectSale(listings, buyer, false);
    }

    /**
     * Creates a DirectSale with explicit finished state,
     * still enforcing that all listings are DIRECT_SALE.
     */
    public DirectSale create(List<Listing> listings, User buyer, boolean isFinished) {

        if (listings == null) {
            throw new IllegalArgumentException("Listings cannot be null");
        }

        boolean invalid = listings.stream()
                .anyMatch(l -> l.getSaleType() != SaleType.DIRECT_SALE);

        if (invalid) {
            throw new IllegalArgumentException(
                    "All listings must be of type DIRECT_SALE to create a DirectSale"
            );
        }

        return new DirectSale(listings, buyer, isFinished);
    }
}
