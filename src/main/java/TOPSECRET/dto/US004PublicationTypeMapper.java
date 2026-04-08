package TOPSECRET.dto;

import TOPSECRET.domain.PublicationType.PublicationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting {@link PublicationType} domain objects
 * into {@link US004PublicationTypeDTO} data transfer objects.
 *
 * <p>This class ensures separation between domain and presentation layers
 * by transforming domain entities into DTOs suitable for UI consumption.
 */
public class US004PublicationTypeMapper {

    /**
     * Converts a list of {@link PublicationType} objects into a list of DTOs.
     *
     * @param types list of PublicationType domain objects
     * @return list of PublicationTypeDTO_US004
     */
    public List<US004PublicationTypeDTO> toDTO(List<PublicationType> types) {

        List<US004PublicationTypeDTO> list = new ArrayList<>();

        for (PublicationType pt : types) {
            list.add(new US004PublicationTypeDTO(
                    pt.id(),
                    pt.getMediaFormat()
            ));
        }

        return list;
    }
}