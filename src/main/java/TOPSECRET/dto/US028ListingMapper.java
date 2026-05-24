package TOPSECRET.dto;


import TOPSECRET.domain.Listing.Listing;

import java.util.ArrayList;
import java.util.List;
/**
 * Mapper responsible for converting {@link Listing} domain objects into
 * {@link US028ListingDTO} instances for user story US028.
 */
public class US028ListingMapper {

    public List<US028ListingDTO> toDTO(List<Listing> listings){

        if (listings == null) {
            throw new NullPointerException("List of listings cannot be null");
        }

        List<US028ListingDTO> listOfListingsDTO = new ArrayList<>();

        for (Listing listing : listings) {
            US028ListingDTO dto = new US028ListingDTO(
                    listing.id().toString(),
                    listing.getPrice().toString(),
                    listing.getShortDescription(),
                    listing.getListingTitle()
            );

            listOfListingsDTO.add(dto);
        }

        return listOfListingsDTO;
    }
}