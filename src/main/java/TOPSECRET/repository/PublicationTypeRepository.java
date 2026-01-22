package TOPSECRET.repository;

import TOPSECRET.domain.PublicationType;

import java.util.ArrayList;
import java.util.List;

public class PublicationTypeRepository {

    private final List<PublicationType> _publicationTypes = new ArrayList<>();

    public PublicationTypeRepository() {}

    public PublicationType addAndValidatePublicationType(PublicationType pubType)
    {
        if (pubType == null)
        {
            throw new IllegalArgumentException("Publication type can't be null");
        }

        if (_publicationTypes.contains(pubType))
        {
            throw new IllegalArgumentException("Publication type already exists");
        }

        _publicationTypes.add(pubType);

        return pubType;
    }

    public List<PublicationType> getAllPublicationTypes() { return List.copyOf(_publicationTypes); }

}
