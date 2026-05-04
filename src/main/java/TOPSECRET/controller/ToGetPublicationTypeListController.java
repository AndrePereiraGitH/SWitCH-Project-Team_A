//package TOPSECRET.controller;
//
//import TOPSECRET.domain.PublicationType.PublicationType;
//
//import TOPSECRET.dto.US014PublicationTypeDTO;
//import TOPSECRET.dto.US014PublicationTypeMapper;
//import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
//
//import java.util.List;
///**
// * Controller responsible for handling the use case US014:
// * "Get the list of publication types".
// *
// * <p>This controller coordinates the interaction between the UI layer
// * and the domain layer. It retrieves all PublicationType domain objects
// * from the repository and converts them into DTOs to be returned to the UI.</p>
// *
// * <p>The controller does not contain business logic.
// * Its responsibility is orchestration only.</p>
// */
//public class ToGetPublicationTypeListController {
//
//    /**
//     * Repository responsible for storing and providing
//     * access to PublicationType domain objects.
//     */
//    private final IPublicationTypeRepository publicationTypeRepository;
//
//    /**
//     * Creates a new controller instance.
//     *
//     * @param publicationTypeRepository the repository that provides
//     *                                  publication type data
//     */
//    public ToGetPublicationTypeListController(IPublicationTypeRepository publicationTypeRepository) {
//
//        if(publicationTypeRepository==null){
//            throw new IllegalArgumentException("IPublicationTypeRepository cannot be null");
//        }
//
//        this.publicationTypeRepository = publicationTypeRepository;
//    }
//
//    /**
//     * Retrieves all publication types from the repository
//     * and converts them into DTO representations.
//     *
//     * @return a list of US014PublicationTypeMapper objects
//     */
//    public List<US014PublicationTypeDTO> getAllPublicationTypes() {
//
//        List<PublicationType> publicationTypes =
//                publicationTypeRepository.getAllPublicationTypes();
//
//        return US014PublicationTypeMapper.toDTOList(publicationTypes);
//    }
//
//}
