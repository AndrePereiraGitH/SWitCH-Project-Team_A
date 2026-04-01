package TOPSECRET.dto;

import TOPSECRET.domain.PublicationSample.PublicationSample;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO used exclusively in US018
 * to represent selectable publication samples.
 */
public class PublicationSampleDTO_US018 {

    private final String title;
    private final String editionNumber;
    private final String ownerUsername;

    public PublicationSampleDTO_US018(String title,
                                      String editionNumber,
                                      String ownerUsername) {
        this.title = title;
        this.editionNumber = editionNumber;
        this.ownerUsername = ownerUsername;
    }

    /**
     * Converts domain PublicationSample objects
     * into DTOs for US018.
     *
//     * @param publications list of PublicationSample
     * @return list of PublicationSampleDTO_US018
     */
//    public static List<PublicationSampleDTO_US018> publicationToDTO(
//            List<PublicationSample> publications) {
//
//        List<PublicationSampleDTO_US018> result = new ArrayList<>();
//
//        for (PublicationSample p : publications) {
//
//            result.add(
//                    new PublicationSampleDTO_US018(
//                            p.getBibliographicalData().getTitle(),
//                            p.getEdition().getEditionNumber(),
//                            p.getOwnerId().getUserName()
//                    )
//            );
//        }
//
//        return result;
//    }

    public String getTitle() {
        return title;
    }

    public String getEditionNumber() {
        return editionNumber;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }
}