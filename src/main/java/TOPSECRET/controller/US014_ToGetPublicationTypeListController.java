package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.repository.PublicationTypeRepository;

import java.util.List;

public class US014_ToGetPublicationTypeListController {

    private final PublicationTypeRepository _publicationTypeRepository;

    public US014_ToGetPublicationTypeListController (PublicationTypeRepository publicationTypeRepository) {
        this._publicationTypeRepository = publicationTypeRepository;
    }

    public List<PublicationType> getAllPublicationTypes(){
        return _publicationTypeRepository.getAllPublicationTypes();
    }

}
