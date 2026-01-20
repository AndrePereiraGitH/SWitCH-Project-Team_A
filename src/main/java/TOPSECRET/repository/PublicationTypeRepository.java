package TOPSECRET.repository;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;

import java.util.ArrayList;
import java.util.List;

public class PublicationTypeRepository {

    private final List<PublicationType> _publicationTypes = new ArrayList<>();

    public PublicationTypeRepository() {}

    public PublicationType addAndValidatePublicationType(PublicationType pubType)
    {
        boolean added = _publicationTypes.add(pubType);

        if (!added) {
            throw new IllegalArgumentException("Publication type already exists");
        }

        _publicationTypes.add(pubType);

        return pubType;
    }

    public List<PublicationType> getAllPublicationTypes() { return List.copyOf(_publicationTypes); }

}
