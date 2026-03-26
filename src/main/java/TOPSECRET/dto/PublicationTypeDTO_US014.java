package TOPSECRET.dto;


import TOPSECRET.domain.PublicationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object (DTO) for the PublicationType entity.
 *
 * <p>This DTO is responsible for transporting publication type
 * information from the domain layer to the presentation layer.</p>
 *
 * <p>It converts domain objects into a simplified representation
 * containing only the necessary data for the UI.</p>
 */
public class PublicationTypeDTO_US014 {

    public String _mediaFormat;

    /**
     * Creates a DTO from a PublicationType domain object.
     *
     * @param publicationType the domain object to convert
     */
    public PublicationTypeDTO_US014(PublicationType publicationType) {

        if(publicationType==null){
            throw new IllegalArgumentException("PublicationTypeDTO cannot be null");
        }

        this._mediaFormat = publicationType.getMediaFormat();
    }

    /**
     * Converts a list of domain objects into a list of DTOs.
     *
     * @param publicationTypes list of domain objects
     * @return list of DTO representations
     */
    // Creates a list of DTOs from a list of PublicationType domains
    public static List<PublicationTypeDTO_US014> fromDomainList(List<PublicationType> publicationTypes) {

        if(publicationTypes==null){
            throw new IllegalArgumentException("PublicationTypeDTO list cannot be null");
        }

        // Creates an empty list to store DTOs
        List<PublicationTypeDTO_US014> listOfPublicationTypeDTO_US014 = new ArrayList<>();

        // Iterates over all PublicationType
        // pT is a temporary variable tha refers to each PublicationType
        for (PublicationType pT : publicationTypes) {

            // a new DTO is created from the domain and the DTO extracts the data
            // add a new DTO to the list
            listOfPublicationTypeDTO_US014.add(new PublicationTypeDTO_US014(pT));
        }

        return listOfPublicationTypeDTO_US014;
    }

    // =======================
    // Getters

    public String getMediaFormat() {
        return _mediaFormat;
    }
    public void setMediaFormat(String mediaFormat) {}

}


