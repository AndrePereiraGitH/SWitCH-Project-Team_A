package TOPSECRET.repository;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;

import java.util.ArrayList;
import java.util.List;

public class PublicationTypeRepository {

    private final List<PublicationType> _publicationTypes = new ArrayList<>();

    public PublicationTypeRepository() {} //Constructor

    // Instantiates a publication type domain entity
    public PublicationType createPublicationType(TypeOfBinding typeOfBinding, String details)
    {
        PublicationType pubType = new PublicationType(typeOfBinding, details);
        return pubType;
    }

    //Validates if the instantiated publication type is either null or duplicated
    private boolean validatePublicationType(PublicationType pubType)
    {
        if (pubType == null) {
            return false;
        }
        return !_publicationTypes.contains(pubType); // Returns true if the publication type does not already exist in the list and false if it does
    }


    //Adds a publication type to the list if it passes validation
    public PublicationType addToList(PublicationType pubType)
    {
        if (!validatePublicationType(pubType)) { // if validation returns false
            throw new IllegalArgumentException("PublicationType already exists or is null");
        }

        _publicationTypes.add(pubType); // if it returns true, adds publication type to the list
        return pubType;
    }

    public List<PublicationType> getAllPublicationTypes() { return List.copyOf(_publicationTypes); }

}
