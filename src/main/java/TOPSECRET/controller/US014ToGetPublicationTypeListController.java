package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType.PublicationType;

import TOPSECRET.dto.PublicationTypeDTO_US014;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;

import java.util.List;
/**
 * Controller responsible for handling the use case US014:
 * "Get the list of publication types".
 *
 * <p>This controller coordinates the interaction between the UI layer
 * and the domain layer. It retrieves all PublicationType domain objects
 * from the repository and converts them into DTOs to be returned to the UI.</p>
 *
 * <p>The controller does not contain business logic.
 * Its responsibility is orchestration only.</p>
 */
public class US014ToGetPublicationTypeListController {

    /**
     * Repository responsible for storing and providing
     * access to PublicationType domain objects.
     */
    private final IPublicationTypeRepository publicationTypeRepository;

    /**
     * Creates a new controller instance.
     *
     * @param publicationTypeRepository the repository that provides
     *                                  publication type data
     */
    public US014ToGetPublicationTypeListController(IPublicationTypeRepository publicationTypeRepository) {

        if(publicationTypeRepository==null){
            throw new IllegalArgumentException("IPublicationTypeRepository cannot be null");
        }

        this.publicationTypeRepository = publicationTypeRepository;
    }

    /**
     * Retrieves all publication types from the repository
     * and converts them into DTO representations.
     *
     * @return a list of PublicationTypeDTO_US014 objects
     */
    public List<PublicationTypeDTO_US014> getAllPublicationTypes() {
        // Gets all publicationTypes domain objects from the repository
        List<PublicationType> publicationTypes =
                publicationTypeRepository.getAllPublicationTypes();
        // converts the domain objects into DTO
        return PublicationTypeDTO_US014.fromDomainList(publicationTypes);
    }

}
