package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.dto.PublicationTypeDTO_US014;
import TOPSECRET.dto.PublicationTypeMapper_US014;
import TOPSECRET.repository.PublicationTypeRepository;

import java.util.List;

public class GetPublicationTypeListController {

    private final PublicationTypeRepository publicationTypeRepository;
    private final PublicationTypeMapper_US014 publicationTypeMapper_US014;

    public GetPublicationTypeListController(PublicationTypeRepository publicationTypeRepository, PublicationTypeMapper_US014 publicationTypeMapperUS014) {

        this.publicationTypeRepository = publicationTypeRepository;
        this.publicationTypeMapper_US014 = publicationTypeMapperUS014;
    }

    public List<PublicationTypeDTO_US014> getAllPublicationTypes() {

        List<PublicationType> publicationTypes =
                publicationTypeRepository.getAllPublicationTypes();

        return publicationTypeMapper_US014.toDTO(publicationTypes);
    }

}
