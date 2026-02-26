package TOPSECRET.controller;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Factory.FactoryPublicationType;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.repository.PublicationTypeRepository;

public class US002ToRegisterPubTypeController {

    private PublicationTypeRepository _pubTypeRepo;

    //Constructor
    public US002ToRegisterPubTypeController(PublicationTypeRepository pubTypeRepo)
    {
        this._pubTypeRepo = pubTypeRepo;
    }

    //Register Publication Type in Repository
    public PublicationType RegisterPubType(MediaFormat mediaFormat, TypeOfBinding typeOfBinding, String details)
    {
        PublicationType pubType = _pubTypeRepo.create(mediaFormat, typeOfBinding, details);
        _pubTypeRepo.addToList(pubType);

        return pubType;
    }
}
