package TOPSECRET.dto;

import TOPSECRET.domain.*;

import java.util.ArrayList;
import java.util.List;
/**
 * Mapper used in use case US028.
 *
 * This class converts Item domain objects
 * into AuctionDTO_US028 objects that can be sent to the UI.
 */
public class AuctionMapper_US028 {

    /**
     * Converts a list of Item objects into a list of AuctionDTO_US028.
     *
     * @param items list of items on auction
     * @return list of AuctionDTO_US028 objects
     */
    public List<AuctionDTO_US028> toDTO(List<Item> items) {

        // List that will store the converted DTOs
        List<AuctionDTO_US028> result = new ArrayList<>();

        // Go through each item in the list
        for (Item item : items) {

            // Get bibliographical data from the item's publication
            BibliographicalData bd =
                        item.getPublication().getBibliographicalData();

            // Get the title of the publication (used as auction name)
            String title = bd.getTitle();

            // Get the name of the publisher
            // Publisher is ONE object
                String publisherName = bd.getPublisher().getName();

            // Create a DTO and add it to the result list
            result.add(new AuctionDTO_US028(title,
                               List.of(publisherName)
               )
               );

            }

        // Return the list of DTOs
        return result;
    }
}

