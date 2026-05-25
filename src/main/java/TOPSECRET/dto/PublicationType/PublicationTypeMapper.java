package TOPSECRET.dto.PublicationType;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.dto.PublicationType.response.PublicationTypeResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Mapper responsible for converting {@link PublicationType}
 * domain objects into {@link PublicationTypeResponseDTO}
 * representations.
 *
 * <p>This mapper acts as a translation layer between the
 * domain layer and the API layer, ensuring that internal
 * domain objects are not exposed directly to external clients.</p>
 *
 * <p>The generated DTOs contain only the data required for
 * API responses and may later be enriched with HATEOAS links
 * in the representation assembler layer.</p>
 */
public final class PublicationTypeMapper {

    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * @throws UnsupportedOperationException always thrown to indicate
     *         that this class should not be instantiated
     */
    private PublicationTypeMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Converts a {@link PublicationType} domain object into
     * a {@link PublicationTypeResponseDTO}.
     *
     * @param publicationType the domain object to convert
     * @return the DTO representation of the publication type
     * @throws IllegalArgumentException if the publication type is null
     */
    public static PublicationTypeResponseDTO toDTO(PublicationType publicationType) {

       Objects.requireNonNull(publicationType, "PublicationType cannot be null");

        return new PublicationTypeResponseDTO(
                publicationType.id().getId(),
                publicationType.getMediaFormat());
    }

    /**
     * Converts a list of {@link PublicationType} domain objects
     * into a list of {@link PublicationTypeResponseDTO}.
     *
     * @param publicationTypeList the list of domain objects to convert
     * @return a list containing the DTO representations
     *         of the publication types
     * @throws IllegalArgumentException if the provided list is null
     */
    public static List<PublicationTypeResponseDTO> toDTOList(List<PublicationType> publicationTypeList) {

        Objects.requireNonNull(publicationTypeList, "PublicationTypeList cannot be null");


        List<PublicationTypeResponseDTO> dtoList = new ArrayList<>();

        for(PublicationType publicationType:publicationTypeList){
            dtoList.add(toDTO(publicationType));
        }

        return dtoList;
    }

}
