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
        if (pubType == null) {
            throw new IllegalArgumentException("Publication type can't be null");
        }

        _publicationTypes.add(pubType);

        return pubType;
    }

    public List<PublicationType> getAllPublicationTypes() { return List.copyOf(_publicationTypes); }

}
