package TOPSECRET.dto;

import TOPSECRET.domain.Listing.Listing;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting {@link Listing} domain objects to {@link US021DirSaleListingDTO}.
 *
 * <p>This class is responsible for transforming domain objects into Data Transfer Objects
 * for US021 - Get list of items on direct sale of a given genre. The mapper extracts
 * the necessary fields from the Listing aggregate and creates an immutable DTO.</p>
 *
 * <p>This mapper has no external dependencies because all data required for the DTO
 * (listingId, listingTitle, price, shortDescription, sellerId) is already available
 * directly from the {@link Listing} aggregate.</p>
 */
public class US021DirSaleListingMapper {
    /**
     * Converts a single {@link Listing} domain object to a {@link US021DirSaleListingDTO}.
     *
     * <p>This method extracts the following fields from the Listing:
     * <ul>
     *     <li>listingId - converted from {@link TOPSECRET.domain.ValueObjects.ListingId} to String</li>
     *     <li>listingTitle - the title of the listing</li>
     *     <li>price - the price value as {@link java.math.BigDecimal}</li>
     *     <li>shortDescription - a brief description of the listing</li>
     *     <li>sellerId - converted from {@link TOPSECRET.domain.ValueObjects.UserId} to String</li>
     * </ul>
     * </p>
     *
     * @param listing the Listing domain object to convert; must not be {@code null}
     * @return a new {@link US021DirSaleListingDTO} instance containing the extracted data
     * @throws NullPointerException if the listing parameter is {@code null}
     */
    public US021DirSaleListingDTO toDTO(Listing listing) {
        return new US021DirSaleListingDTO(
                listing.id().toString(),
                listing.getListingTitle(),
                listing.getPrice().getPrice(),
                listing.getShortDescription(),
                listing.getSellerId().toString()
        );
    }

    /**
     * Converts a list of {@link Listing} domain objects to a list of {@link US021DirSaleListingDTO}.
     *
     * <p>This method iterates through the provided list and converts each Listing
     * using the {@link #toDTO(Listing)} method. The order of elements in the
     * resulting list is the same as the order of the input list.</p>
     *
     * @param listings the list of Listing domain objects to convert; must not be {@code null}
     * @return a list of {@link US021DirSaleListingDTO} instances in the same order as the input;
     *         returns an empty list if the input list is empty
     * @throws NullPointerException if the listings parameter is {@code null} or if any
     *                              element in the list is {@code null}
     */
    public List<US021DirSaleListingDTO> toDTOs(List<Listing> listings) {
        return listings.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
