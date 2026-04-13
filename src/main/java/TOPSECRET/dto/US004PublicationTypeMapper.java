package TOPSECRET.dto;

import TOPSECRET.domain.PublicationType.PublicationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper responsible for converting PublicationType domain objects
 * into US004PublicationTypeDTO.
 */
public class US004PublicationTypeMapper {

    public static List<US004PublicationTypeDTO> toDTO(List<PublicationType> types) {
        if (types == null) return new ArrayList<>();

        List<US004PublicationTypeDTO> list = new ArrayList<>();

        for (PublicationType pt : types) {
            list.add(toDTO(pt));
        }

        return list;
    }

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