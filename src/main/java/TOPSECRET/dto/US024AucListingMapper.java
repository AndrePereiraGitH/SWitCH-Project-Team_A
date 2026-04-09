package TOPSECRET.dto;

import TOPSECRET.domain.Listing.Listing;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class responsible for mapping {@link Listing} domain objects
 * to {@link US024AucListingDTO} data transfer objects.
 *
 * <p>This class is not meant to be instantiated.</p>
 */
public class US024AucListingMapper {

    private US024AucListingMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Converts a list of {@link Listing} objects into a list of {@link US024AucListingDTO} objects.
     *
     * @param listOfAuctionOfGivenPublication the list of {@link Listing} instances to convert;
     *                                        must not be {@code null}
     * @return a {@link List} of {@link US024AucListingDTO} corresponding to the provided listings;
     *         never {@code null}, may be empty
     * @throws IllegalArgumentException if {@code listOfAuctionOfGivenPublication} is {@code null}
     */
    public static List<US024AucListingDTO> toDTOs(List<Listing> listOfAuctionOfGivenPublication) {

        if (listOfAuctionOfGivenPublication == null) {
            throw new IllegalArgumentException("Listing list must not be null");
        }

        List<US024AucListingDTO> listOfAuctionListingsDTO = new ArrayList<>();

        for (Listing listing : listOfAuctionOfGivenPublication) {
            US024AucListingDTO aucDTO = toDTO(listing);
            listOfAuctionListingsDTO.add(aucDTO);
        }

        return listOfAuctionListingsDTO;
    }

    /**
     * Converts a single {@link Listing} domain object into a {@link US024AucListingDTO}.
     *
     * <p>The following fields are mapped:</p>
     * <ul>
     *   <li>{@code listingId}   — extracted via {@code listing.id().getId()}</li>
     *   <li>{@code price}       — derived from {@code listing.getPrice().toString()}</li>
     *   <li>{@code description} — extracted via {@code listing.getShortDescription()}</li>
     *   <li>{@code listing}     — extracted via {@code listing.getListingTitle()}</li>
     * </ul>
     *
     * @param listing the {@link Listing} to convert; must not be {@code null},
     *                and its {@code id()}, {@code getPrice()}, {@code getShortDescription()},
     *                and {@code getListingTitle()} must not return {@code null}
     * @return a {@link US024AucListingDTO} populated with data from the given {@link Listing}
     * @throws NullPointerException if {@code listing} or any of its required fields are {@code null}
     */
    public static US024AucListingDTO toDTO(Listing listing) {

        if(listing == null){
            throw new NullPointerException("Listing should not be null");
        }

        String listingId = listing.id().getId();
        String price = listing.getPrice().getPrice().toString();
        String description = listing.getShortDescription();
        String listingTitle = listing.getListingTitle();

        US024AucListingDTO aucDTO = new US024AucListingDTO(listingId, price, description, listingTitle);

        return aucDTO;
    }
}