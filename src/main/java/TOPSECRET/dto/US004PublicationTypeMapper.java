package TOPSECRET.dto;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting PublicationType domain objects
 * into US004PublicationTypeDTO.
 */
public class US004PublicationTypeMapper {

    /**
     * Converts a list of PublicationType domain objects into DTOs.
     *
     * @param types list of PublicationType domain objects
     * @return list of US004PublicationTypeDTO; returns empty list if input is null
     */
    public static List<US004PublicationTypeDTO> toDTOs(List<PublicationType> types) {
        if (types == null) return new ArrayList<>();

        List<US004PublicationTypeDTO> list = new ArrayList<>();

        for (PublicationType pt : types) {
            list.add(toDTO(pt));
        }

        return list;
    }

    /**
     * Converts a single PublicationType domain object into DTO.
     *
     * @param pt PublicationType domain object
     * @return corresponding US004PublicationTypeDTO
     * @throws IllegalArgumentException if pt is null
     */
    public static US004PublicationTypeDTO toDTO(PublicationType pt) {
        if (pt == null) {
            throw new IllegalArgumentException("Publication Type cannot be null");
        }

        return new US004PublicationTypeDTO(
                pt.id(),
                pt.getMediaFormat()
        );
    }
}