package TOPSECRET.repository;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Factory.FactoryPublicationType;
import TOPSECRET.domain.PublicationType;
import org.apache.commons.collections.Factory;

import javax.print.attribute.standard.Media;
import java.util.ArrayList;
import java.util.List;

public class PublicationTypeRepository {

    private final List<PublicationType> _publicationTypes = new ArrayList<>();

    // Empty constructor
    public PublicationTypeRepository() {}

    public PublicationType create(MediaFormat mediaFormat, TypeOfBinding typeOfBinding, String details)
    {
        FactoryPublicationType factory = new FactoryPublicationType();
        PublicationType p1 = factory.create(mediaFormat, typeOfBinding, details);
        return p1;
    }

    // Validates if the instantiated publication type is either null or duplicated
    private boolean validatePublicationType(PublicationType pubType)
    {
        if (pubType == null) {
            return false;
        }
        return !_publicationTypes.contains(pubType); // Returns true if the publication type does not already exist in the list and false if it does
    }


    // Adds a publication type to the list if it passes validation
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
