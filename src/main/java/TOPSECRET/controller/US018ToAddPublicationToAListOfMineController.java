package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications;
import TOPSECRET.domain.Library;
import TOPSECRET.domain.PublicationSample;
import TOPSECRET.domain.User;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US018;
import TOPSECRET.dto.PublicationSampleDTO_US018;
import TOPSECRET.repository.CollectionOfPublicationsRepository;
import TOPSECRET.repository.ILibraryRepository;
import TOPSECRET.repository.PublicationSampleRepositoryMem;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;


/**
 * Controller for US018 — "As a User, I want to add one of my publications to a list of mine".
 * <p>
 * This controller is exclusively responsible for coordinating
 * the application flow of this use case.
 * </p>
 * <p>
 * Responsibilities:
 *     <li>Retrieve the collections that belong to the user.</li>
 *     <li>Retrieve the publications available to be added to a selected collection.</li>
 *     <li>Add one or more selected publications to a selected collection.</li>
 * </ul>
 * <p>
 * Important design constraints (as defined in the sequence diagram):
 * </p>
 * <ul>
 *     <li>DTO to Domain conversions of user-selected data are performed in the repositories.</li>
 *     <li>This controller belongs exclusively to US018.</li>
 *     <li>Public method names strictly follow the sequence diagram.</li>
 * </ul>
 */
public class US018ToAddPublicationToAListOfMineController {

    private final CollectionOfPublicationsRepository collectionRepo;
    private final ILibraryRepository libraryRepo;
    private final PublicationSampleRepositoryMem publicationSampleRepo;

    /**
     * Authenticated/active user in the execution context of this US.
     * <p>
     * This allows keeping method signatures clean
     * (e.g., {@code getPublicationsForCollection(selectedCollectionDTO)} without a user parameter),
     * while still being able to retrieve the user's Library.
     * </p>
     */
    private final User user;

    /**
     * Constructs the controller for US018.
     *
     * @param user the authenticated user executing the use case
     * @param collectionRepo repository responsible for collections
     * @param libraryRepo repository responsible for user libraries
     * @param publicationSampleRepo repository responsible for PublicationSample conversion
     * @throws IllegalArgumentException if any argument is null
     */
    public US018ToAddPublicationToAListOfMineController(
            User user,
            CollectionOfPublicationsRepository collectionRepo,
            ILibraryRepository libraryRepo,
            PublicationSampleRepositoryMem publicationSampleRepo) {

        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (collectionRepo == null) throw new IllegalArgumentException("CollectionOfPublicationsRepository cannot be null");
        if (libraryRepo == null) throw new IllegalArgumentException("LibraryRepository cannot be null");
        if (publicationSampleRepo == null) throw new IllegalArgumentException("PublicationSampleRepositoryMem cannot be null");

        this.user = user;
        this.collectionRepo = collectionRepo;
        this.libraryRepo = libraryRepo;
        this.publicationSampleRepo = publicationSampleRepo;
    }

    /**
     * Retrieves all collections that belong to the given user.
     * @param user the owner of the collections
     * @return list of the user's collections represented as DTOs
     * @throws IllegalArgumentException if user is null
     */
    public List<CollectionOfPublicationsDTO_US018> getUserCollections(User user) {

        if (user == null) {
            throw new IllegalArgumentException("user cannot be null");
        }

        List<CollectionOfPublications> userCollections = collectionRepo.getUserCollections(user);

        // No diagrama: Controller -> DTO : collectionOfPublicationToDTO(userCollections)
        return CollectionOfPublicationsDTO_US018.collectionOfPublicationToDTO(userCollections);
    }

    /**
     * Retrieves the publications available to be added to the selected collection.
     * <p>
     * The available publications are calculated as:
     * all publications in the user's library
     * minus
     * the publications already present in the selected collection.
     * </p>
     * @param selectedCollectionDTO the selected collection (DTO)
     * @return list of available publications as DTOs
     * @throws IllegalArgumentException if selectedCollectionDTO is null
     * @throws InstanceNotFoundException if the user's library does not exist
     */
    public List<PublicationSampleDTO_US018> getPublicationsForCollection(
            CollectionOfPublicationsDTO_US018 selectedCollectionDTO) throws InstanceNotFoundException {

        if (selectedCollectionDTO == null) {
            throw new IllegalArgumentException("selectedCollectionDTO cannot be null");
        }

        CollectionOfPublications userCollectionSelected =
                collectionRepo.collectionDTOToDomain(selectedCollectionDTO);

        Library userLibrary = libraryRepo.getLibraryOfUser(user.getUserName());

        List<PublicationSample> allUserPublications = userLibrary.getAllPublications();

        List<PublicationSample> publicationsInCollection = userCollectionSelected.getPublications();

        List<PublicationSample> publicationAvailable = diff(allUserPublications, publicationsInCollection);

        return PublicationSampleDTO_US018.publicationToDTO(publicationAvailable);
    }

    /**
     * Adds one or more selected publications to the selected collection.
     * <p>
     * The selected DTOs are converted to domain objects
     * using the appropriate repositories as defined in the sequence diagram.
     * </p>
     * @param collectionSelectedDTO the selected collection (DTO)
     * @param selectedPublicationDTO list of selected publications (DTO)
     * @return true if all publications are successfully added; false otherwise
     * @throws IllegalArgumentException if inputs are null or empty
     * @throws InstanceNotFoundException if the user's library does not exist
     */
    public boolean addPublicationToCollection(
            CollectionOfPublicationsDTO_US018 collectionSelectedDTO,
            List<PublicationSampleDTO_US018> selectedPublicationDTO) throws InstanceNotFoundException {

        if (collectionSelectedDTO == null) {
            throw new IllegalArgumentException("collectionSelectedDTO cannot be null");
        }
        if (selectedPublicationDTO == null || selectedPublicationDTO.isEmpty()) {
            throw new IllegalArgumentException("selectedPublicationDTO cannot be null or empty");
        }

        Library userLibrary = libraryRepo.getLibraryOfUser(user.getUserName());

        CollectionOfPublications userCollection =
                collectionRepo.collectionDTOToDomain(collectionSelectedDTO);

        boolean success = true;

        for (PublicationSampleDTO_US018 pubDto : selectedPublicationDTO) {

            PublicationSample publicationSample =
                    publicationSampleRepo.PublicationSampleDtoToDomain(userLibrary, pubDto);

            boolean added = collectionRepo.addPublicationToCollection(
                    user,
                    userLibrary,
                    userCollection,
                    publicationSample
            );

            if (!added) {
                success = false;
            }
        }
        return success;
    }

    /**
     * Computes the difference between two publication lists.
     *
     * @param allUserPublications all publications in the user's library
     * @param publicationsInCollection publications already in the collection
     * @return list of publications that are not yet in the collection
     */
    private List<PublicationSample> diff(
            List<PublicationSample> allUserPublications,
            List<PublicationSample> publicationsInCollection) {

        List<PublicationSample> result = new ArrayList<>();

        for (PublicationSample p : allUserPublications) {
            if (!publicationsInCollection.contains(p)) {
                result.add(p);
            }
        }

        return result;
    }
}

