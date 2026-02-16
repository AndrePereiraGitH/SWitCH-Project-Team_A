package TOPSECRET.dto;


import TOPSECRET.domain.PublicationType;
/**
 * Data Transfer Object (DTO) for PublicationType.
 *
 * This class represents the data that is sent to the UI layer.
 * It contains only simple values and no domain logic.
 */
public class PublicationTypeDTO_US014 {

    public String _typeOfBinding;
    public String _details;

    public PublicationTypeDTO_US014(PublicationType publicationType) {
        this._typeOfBinding = publicationType.getTypeOfBinding().name();
        this._details = publicationType.getDetails();
    }
}

