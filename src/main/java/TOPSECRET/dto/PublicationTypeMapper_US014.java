package TOPSECRET.dto;

import TOPSECRET.domain.PublicationType;

import java.util.ArrayList;
import java.util.List;

public class PublicationTypeMapper_US014 {

    // Creates a DTO from a PublicationType domain
    public PublicationTypeDTO_US014 toDTO(PublicationType publicationType) {

        return new PublicationTypeDTO_US014(publicationType);
    }

    // Creates a list of DTOs from a list of PublicationType domains
    public List<PublicationTypeDTO_US014> toDTO(List<PublicationType> publicationTypes) {
        // Creates an empty list to store DTOs
        List<PublicationTypeDTO_US014> listOfPublicationTypeDTO_US014 = new ArrayList<>();

        // Iterates over all PublicationType
        // pT is a temporary variable tha refers to each PublicationType
        for (PublicationType pT : publicationTypes) {

            // a new DTO is created from the domain and the DTO extracts the data
            // add a new DTO to the list
            listOfPublicationTypeDTO_US014.add(toDTO(pT));
        }

        return listOfPublicationTypeDTO_US014;
    }

}
