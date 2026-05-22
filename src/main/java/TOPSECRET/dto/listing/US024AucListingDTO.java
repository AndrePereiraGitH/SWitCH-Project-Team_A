package TOPSECRET.dto.listing;


/**
 * Data Transfer Object representing an auction listing.
 *
 * @param listingId   the unique identifier of the listing; must not be {@code null}
 * @param price       the price associated with the auction listing; must not be {@code null}
 * @param description a textual description of the auction item; must not be {@code null}
 * @param listing     the publication title of the auction listing; must not be {@code null}
 * @throws NullPointerException if any parameter is {@code null}
 */
public record US024AucListingDTO(String listingId, String price, String description, String listing) {

    /**
     * Compact canonical constructor that validates all fields upon instantiation.
     *
     * @throws NullPointerException if {@code listingId}, {@code price}, {@code description},
     *                              or {@code listing} is {@code null}
     */
    public US024AucListingDTO {
        if(listingId == null){
            throw new NullPointerException("ListingId can not be null");
        }

        if(price == null){
            throw new NullPointerException("Price can not be null");
        }

        if(description == null){
            throw new NullPointerException("Description can not be null");
        }

        if(listing == null){
            throw new NullPointerException("Publication titles can not be null");
        }

    }
}
