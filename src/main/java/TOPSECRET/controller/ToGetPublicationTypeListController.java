package TOPSECRET.controller;

import TOPSECRET.domain.PublicationType.PublicationType;

import TOPSECRET.dto.US014PublicationTypeDTO;
import TOPSECRET.dto.US014PublicationTypeMapper;
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
public class ToGetPublicationTypeListController {

    /**
     * Repository responsible for storing and providing
     * access to PublicationType domain objects.
     */
    private final IPublicationTypeRepository publicationTypeRepository;
    private final US014PublicationTypeMapper publicationTypeMapper;

    /**
     * Creates a new controller instance.
     *
     * @param publicationTypeRepository the repository that provides
     *                                  publication type data
     */
    public ToGetPublicationTypeListController(IPublicationTypeRepository publicationTypeRepository, US014PublicationTypeMapper publicationTypeMapper) {

        if(publicationTypeRepository==null){
            throw new IllegalArgumentException("IPublicationTypeRepository cannot be null");
        }
        if(publicationTypeMapper==null){
            throw new IllegalArgumentException("PublicationTypeMapper cannot be null");
        }

        this.publicationTypeRepository = publicationTypeRepository;
        this.publicationTypeMapper = publicationTypeMapper;
    }

    /**
     * Retrieves all publication types from the repository
     * and converts them into DTO representations.
     *
     * @return a List<US014PublicationTypeDTO> objects
     */
    public List<US014PublicationTypeDTO> findAll() {

        List<PublicationType> publicationTypes =
                publicationTypeRepository.findAll();

        return publicationTypeMapper.toDTOList(publicationTypes);
    }

}
