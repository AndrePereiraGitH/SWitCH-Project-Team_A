package TOPSECRET.dto;


/**
 * Data Transfer Object (DTO) used in US028 – "To get the list of items on auction of a given publisher".
 */
public record US028ListingDTO(String listingId, String price, String description, String listing) {

    /**
     * Compact canonical constructor that validates all fields upon instantiation.
     *
     * @throws NullPointerException if {@code listingId}, {@code price}, {@code description},
     *                              or {@code listing} is {@code null}
     */
    public US028ListingDTO {
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