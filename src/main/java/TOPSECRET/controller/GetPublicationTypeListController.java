package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType;
import TOPSECRET.dto.PublicationTypeDTO_US014;
import TOPSECRET.dto.PublicationTypeMapper_US014;
import TOPSECRET.repository.PublicationTypeRepository;

import java.util.List;
/**
 * Controller for US014 – Retrieve the list of publication types.
 * This controller coordinates the interaction between:
 * - PublicationTypeRepository (data source)
 * - PublicationTypeMapper_US014 (conversion to DTOs)
 * It exposes application services used by the UI to retrieve
 * publication types in a safe and presentation-ready format.
 */
public class GetPublicationTypeListController {

    private final PublicationTypeRepository publicationTypeRepository;
    private final PublicationTypeMapper_US014 publicationTypeMapper_US014;

    public GetPublicationTypeListController(PublicationTypeRepository publicationTypeRepository, PublicationTypeMapper_US014 publicationTypeMapperUS014) {

        this.publicationTypeRepository = publicationTypeRepository;
        this.publicationTypeMapper_US014 = publicationTypeMapperUS014;
    }

    public List<PublicationTypeDTO_US014> getAllPublicationTypes() {
        // Gets all publicationTypes domain objects from the repository
        List<PublicationType> publicationTypes =
                publicationTypeRepository.getAllPublicationTypes();
        // converts the domain objects into DTO
        return publicationTypeMapper_US014.toDTO(publicationTypes);
    }

}
