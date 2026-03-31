package TOPSECRET.dto;


import TOPSECRET.domain.PublicationType.PublicationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object representing a PublicationType.
 * This class is responsible for:
 * - Safely transferring PublicationType data to the UI.
 * - Converting between PublicationType (domain) and PublicationTypeDTO.
 */

public class PublicationTypeDTO_US004 {

    private String _mediaFormat;


// --------------------------------- US004 starts ---------------------------------
    /**
     * Creates a PublicationTypeDTO.
     */
    public PublicationTypeDTO_US004(String mediaFormat) {

        if (mediaFormat == null) {
            throw new IllegalArgumentException("MediaFormat cannot be null.");
        }

        this._mediaFormat = mediaFormat;

    }

    /**
     * Converts domain PublicationType objects into DTOs.
     *
     * @param publicationTypes list of PublicationType domain objects
     * @return list of PublicationTypeDTO_US004
     */
    public static List<PublicationTypeDTO_US004> publicationTypesToDTO(
            List<PublicationType> publicationTypes) {

        List<PublicationTypeDTO_US004> result = new ArrayList<>();

        for (PublicationType type : publicationTypes) {

            result.add(
                    new PublicationTypeDTO_US004(
                            type.getMediaFormat()
                    )
            );
        }

        return result;
    }

    /**
     * Returns the media format.
     * @return MediaFormat
     */
    public String getMediaFormat() {
        return _mediaFormat;
    }

    // --------------------------------- US004 ends ---------------------------------
}
