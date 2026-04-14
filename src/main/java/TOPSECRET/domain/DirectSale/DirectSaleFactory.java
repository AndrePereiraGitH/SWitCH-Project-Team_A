package TOPSECRET.domain.DirectSale;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
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
     * @param listingId the listings included in the sale; must not be null and must all be DIRECT_SALE
     * @param buyer    the buyer associated with the sale
     * @return a new DirectSale instance in an unfinished state
     */

    public DirectSale create(List<ListingId> listingId, UserId buyer, IListingRepository listingRepo) {

        if (listingId == null) {
            throw new IllegalArgumentException("Listings cannot be null");
        }

        if (listingRepo == null) {
            throw new IllegalArgumentException("Listings Repository cannot be null");
        }

        List<Listing> listings = listingId.stream()
                .map(id -> listingRepo.findListingById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Listing not found: " + id)))
                .toList();

        boolean allDirectSale = listings.stream()
                .allMatch(listing -> listing.getSaleType() == SaleType.DIRECT_SALE);


        if (!allDirectSale) {
            throw new IllegalArgumentException("All listings must be of type DIRECT_SALE");
        }

        return new DirectSale(listingId, buyer, false);
    }
}
