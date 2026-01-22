package TOPSECRET.controller;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.repository.PublicationTypeRepository;

public class US002_RegisterPubTypeController {

    private PublicationTypeRepository _pubTypeRepo;

    public US002_RegisterPubTypeController(PublicationTypeRepository pubTypeRepo){
        _pubTypeRepo = pubTypeRepo;
    }

    public PublicationType RegisterPubType(MediaFormat mediaFormat, TypeOfBinding typeOfBinding, String details)
    {
        PublicationType pubType = _pubTypeRepo.createPublicationType(mediaFormat, typeOfBinding, details);
        _pubTypeRepo.addToList(pubType);

        return pubType;
    }

}
