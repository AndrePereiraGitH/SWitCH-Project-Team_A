package TOPSECRET.controller;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.repository.PublicationTypeRepository;

public class US002_PublicationTypeController {

    private PublicationTypeRepository _pubTypeRepo;

    public US002_PublicationTypeController(PublicationTypeRepository pubTypeRepo){
        _pubTypeRepo = pubTypeRepo;
    }

    public PublicationType createAndAddPublicationTypes(MediaFormat mediaFormat, TypeOfBinding typeOfBinding, String details)
    {
        PublicationType pubType = new PublicationType(mediaFormat, typeOfBinding, details);
        return _pubTypeRepo.addAndValidatePublicationType(pubType);
    }

}
