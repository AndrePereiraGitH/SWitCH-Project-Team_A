package TOPSECRET.dto;

import TOPSECRET.domain.PublicationType.PublicationType;

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
public class US014PublicationTypeMapper {

    /**
     * Creates a DTO from a PublicationType domain object.
     *
     * @param publicationType the domain object to convert
     */
    public US014PublicationTypeDTO toDTO(PublicationType publicationType) {

        if(publicationType==null){
            throw new IllegalArgumentException("PublicationType cannot be null");
        }

        return new US014PublicationTypeDTO(publicationType.id().getId(),
                publicationType.getMediaFormat());
    }

    /**
     * Converts a list of domain objects into a list of DTOs.
     *
     * @param publicationTypes list of domain objects
     * @return list of DTO representations
     */
    // Creates a list of DTOs from a list of PublicationType domains
    public List<US014PublicationTypeDTO> toDTOList( List<PublicationType> publicationTypes) {

        if(publicationTypes==null){
            throw new IllegalArgumentException("PublicationType list cannot be null");
        }

        List<US014PublicationTypeDTO> dtoList = new ArrayList<>();

        for(PublicationType publicationType:publicationTypes){
            dtoList.add(toDTO(publicationType));
        }

        return dtoList;
    }
}