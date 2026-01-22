package TOPSECRET.repository;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;

import java.util.ArrayList;
import java.util.List;

public class PublicationTypeRepository {

    private final List<PublicationType> _publicationTypes = new ArrayList<>();

    public PublicationTypeRepository() {}

    public PublicationType createPublicationType(MediaFormat mediaFormat, TypeOfBinding typeOfBinding, String details)
    {
        PublicationType pubType = new PublicationType(mediaFormat, typeOfBinding, details);
        return pubType;
    }

    //Validates if the created publication is either null or duplicated
    private boolean validatePublicationType(PublicationType pubType)
    {
        if (pubType == null) {
            return false;
        }
        return !_publicationTypes.contains(pubType);
    }


    //Adds a publication type to the repo list if it passes validation
    public PublicationType addToList(PublicationType pubType)
    {
        if (!validatePublicationType(pubType)) {
            throw new IllegalArgumentException("PublicationType already exists or is null");
        }

        _publicationTypes.add(pubType);
        return pubType;
    }

    public List<PublicationType> getAllPublicationTypes() { return List.copyOf(_publicationTypes); }

}
