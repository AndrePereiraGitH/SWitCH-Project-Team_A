package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.ListingId;

import java.util.List;

/**
 * Data Transfer Object (DTO) used in US023 to represent a listing
 * when retrieving items on direct sale for a given publication.
 * <p>
 * This record ensures immutability and validates all required fields.
 * </p>
 *
 * @param listingId     the unique identifier of the listing
 * @param listingTitle  the title of the listing
 * @param price         the price associated with the listing
 * @param pubTitles     the titles of the publications being sold
 * @param authorsName   list of author names; must not be null, empty, or contain blank values
 *
 * @throws IllegalArgumentException if any argument is null, blank, or invalid
 */
public record US023ListingDTO(ListingId listingId, String listingTitle, String price, List<String> pubTitles, List<String> authorsName) {

    public US023ListingDTO {
        if (listingId == null) throw new IllegalArgumentException("ListingId cannot be null");
        if (listingTitle == null || listingTitle.isBlank()) throw new IllegalArgumentException("ListingTitle cannot be null or blank");
        if (price == null || price.isBlank()) throw new IllegalArgumentException("Price cannot be null or blank");
        if (pubTitles == null || pubTitles.isEmpty()) throw new IllegalArgumentException("ItemTitle cannot be null or empty");
        if (authorsName == null || authorsName.isEmpty() || authorsName.stream().anyMatch(String::isBlank))
            throw new IllegalArgumentException("Authors cannot be null, empty or contain blank names");
    }
}