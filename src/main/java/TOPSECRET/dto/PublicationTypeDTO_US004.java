package TOPSECRET.dto;

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

    private TypeOfBinding _typeOfBinding;
    private String _details;


// --------------------------------- US004 starts ---------------------------------
    /**
     * Creates a PublicationTypeDTO.
     * @param _typeOfBinding Type of binding.
     * @param details Additional details.
     */
    public PublicationTypeDTO_US004(TypeOfBinding _typeOfBinding, String details) {
        this._typeOfBinding = _typeOfBinding;
        this._details = details;
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

    /**
     * Converts a PublicationType domain object into a DTO.
     * @param type PublicationType domain object.
     * @return PublicationTypeDTO object.
     */
    public static PublicationTypeDTO_US004 publicationTypeToDTO(PublicationType type) {

        return new PublicationTypeDTO_US004(
                type.getTypeOfBinding(),
                type.getDetails()
        );
    }

    /**
     * Converts a list of PublicationType domain objects into DTOs.
     * @param types List of PublicationType.
     * @return List of PublicationTypeDTO.
     */
    public List<PublicationTypeDTO_US004> publicationTypeListToDTO(List<PublicationType> types) {

        List<PublicationTypeDTO_US004> result = new ArrayList<>();

        for (PublicationType type : types) {
            result.add(publicationTypeToDTO(type));
        }

        return result;
    }

    /**
     * Converts this DTO into a PublicationType domain object.
     *
     * @return PublicationType domain object.
     */
    public PublicationType dtoToPublicationType() {
        return new PublicationType(
                this._typeOfBinding,
                this._details
        );
    }
    // --------------------------------- US004 ends ---------------------------------
}
