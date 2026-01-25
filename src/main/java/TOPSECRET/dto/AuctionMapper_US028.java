package TOPSECRET.dto;

import TOPSECRET.domain.*;

import java.util.ArrayList;
import java.util.List;

public class AuctionMapper_US028 {

    public List<AuctionDTO_US028> toDTO(List<Item> items) {

        List<AuctionDTO_US028> result = new ArrayList<>();


        for (Item item : items) {
           BibliographicalData bd =
                        item.getPublication().getBibliographicalData();

                String title = bd.getTitle();

                // Publisher is ONE object
                String publisherName = bd.getPublisher().getName();

               result.add(new AuctionDTO_US028(title,
                               List.of(publisherName)
               )
               );

            }


        return result;
    }
}

