package TOPSECRET.dto;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object representing a PublicationType.
 * This class is responsible for:
 * - Safely transferring PublicationType data to the UI.
 * - Converting between PublicationType (domain) and PublicationTypeDTO.
 */

public class PublicationTypeDTO_US004 {

    private MediaFormat _mediaFormat;
    private TypeOfBinding _typeOfBinding;
    private String _details;


// --------------------------------- US004 starts ---------------------------------
    /**
     * Creates a PublicationTypeDTO.
     * @param typeOfBinding Type of binding.
     * @param details Additional details.
     */
    public PublicationTypeDTO_US004(MediaFormat mediaFormat, TypeOfBinding typeOfBinding, String details) {

        if (mediaFormat == null) {
            throw new IllegalArgumentException("MediaFormat cannot be null.");
        }

        if (typeOfBinding == null) {
            throw new IllegalArgumentException("TypeOfBinding cannot be null.");
        }

        if (details == null || details.isBlank()) {
            throw new IllegalArgumentException("Details cannot be null or blank.");
        }

        this._mediaFormat = mediaFormat;
        this._typeOfBinding = typeOfBinding;
        this._details = details;
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
                            type.getMediaFormat(),
                            type.getTypeOfBinding(),
                            type.getDetails()
                    )
            );
        }

        return result;
    }

    /**
     * Returns the media format.
     * @return MediaFormat
     */
    public MediaFormat getMediaFormat() {
        return _mediaFormat;
    }

    /**
     * Returns the type of binding.
     * @return TypeOfBinding.
     */
    public TypeOfBinding getTypeOfBinding() {
        return _typeOfBinding;
    }

    /**
     * Returns publication details.
     * @return Publication details.
     */
    public String getDetails() {
        return _details;
    }
    // --------------------------------- US004 ends ---------------------------------
}
