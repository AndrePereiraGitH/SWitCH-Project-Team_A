//package TOPSECRET.controller;
//
//import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
//import TOPSECRET.domain.Library.Library;
//import TOPSECRET.domain.PublicationSample.PublicationSample;
//import TOPSECRET.domain.User;
//import TOPSECRET.dto.CollectionOfPublicationsDTO_US018;
//import TOPSECRET.dto.PublicationSampleDTO_US018;
//import TOPSECRET.repository.CollectionOfPublicationsRepository;
//import TOPSECRET.domain.Library.ILibraryRepository;
//import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
//
//import javax.management.InstanceNotFoundException;
//import java.util.ArrayList;
//import java.util.List;
//
//
//
//public class US018ToAddPublicationToAListOfMineController {
//
//    private final CollectionOfPublicationsRepository collectionRepo;
//    private final ILibraryRepository libraryRepo;
//    private final IPublicationSampleRepository publicationSampleRepo;
//
//
//    private final User user;
//
//
//    public US018ToAddPublicationToAListOfMineController(
//            User user,
//            CollectionOfPublicationsRepository collectionRepo,
//            ILibraryRepository libraryRepo,
//            IPublicationSampleRepository publicationSampleRepo) {
//
//        if (user == null) throw new IllegalArgumentException("User cannot be null");
//        if (collectionRepo == null) throw new IllegalArgumentException("CollectionOfPublicationsRepository cannot be null");
//        if (libraryRepo == null) throw new IllegalArgumentException("LibraryRepository cannot be null");
//        if (publicationSampleRepo == null) throw new IllegalArgumentException("IPublicationSampleRepositoryMem cannot be null");
//
//        this.user = user;
//        this.collectionRepo = collectionRepo;
//        this.libraryRepo = libraryRepo;
//        this.publicationSampleRepo = publicationSampleRepo;
//    }
//
//
//    public List<CollectionOfPublicationsDTO_US018> getUserCollections(User user) {
//
//        if (user == null) {
//            throw new IllegalArgumentException("user cannot be null");
//        }
//
//        List<CollectionOfPublications> userCollections = collectionRepo.getUserCollections(user);
//
//        // No diagrama: Controller -> DTO : collectionOfPublicationToDTO(userCollections)
//        return CollectionOfPublicationsDTO_US018.collectionOfPublicationToDTO(userCollections);
//    }
//
//    public List<PublicationSampleDTO_US018> getPublicationsForCollection(
//            CollectionOfPublicationsDTO_US018 selectedCollectionDTO) throws InstanceNotFoundException {
//
//        if (selectedCollectionDTO == null) {
//            throw new IllegalArgumentException("selectedCollectionDTO cannot be null");
//        }
//
//        CollectionOfPublications userCollectionSelected =
//                collectionRepo.collectionDTOToDomain(selectedCollectionDTO);
//
//        Library userLibrary = libraryRepo.getLibraryOfUser(user.getUserName());
//
//        List<PublicationSample> allUserPublications = userLibrary.getAllPublications();
//
//        List<PublicationSample> publicationsInCollection = userCollectionSelected.getPublications();
//
//        List<PublicationSample> publicationAvailable = diff(allUserPublications, publicationsInCollection);
//
//        return PublicationSampleDTO_US018.publicationToDTO(publicationAvailable);
//    }
//
//
//    public boolean addPublicationToCollection(
//            CollectionOfPublicationsDTO_US018 collectionSelectedDTO,
//            List<PublicationSampleDTO_US018> selectedPublicationDTO) throws InstanceNotFoundException {
//
//        if (collectionSelectedDTO == null) {
//            throw new IllegalArgumentException("collectionSelectedDTO cannot be null");
//        }
//        if (selectedPublicationDTO == null || selectedPublicationDTO.isEmpty()) {
//            throw new IllegalArgumentException("selectedPublicationDTO cannot be null or empty");
//        }
//
//        Library userLibrary = libraryRepo.getLibraryOfUser(user.getUserName());
//
//        CollectionOfPublications userCollection =
//                collectionRepo.collectionDTOToDomain(collectionSelectedDTO);
//
//        boolean success = true;
//
//        for (PublicationSampleDTO_US018 pubDto : selectedPublicationDTO) {
//
//            PublicationSample publicationSample =
//                    publicationSampleRepo.PublicationSampleDtoToDomain(userLibrary, pubDto);
//
//            boolean added = collectionRepo.addPublicationToCollection(
//                    user,
//                    userLibrary,
//                    userCollection,
//                    publicationSample
//            );
//
//            if (!added) {
//                success = false;
//            }
//        }
//        return success;
//    }
//>>>>>>> Stashed changes
//
//
//    private List<PublicationSample> diff(
//            List<PublicationSample> allUserPublications,
//            List<PublicationSample> publicationsInCollection) {
//
//        List<PublicationSample> result = new ArrayList<>();
//
//        for (PublicationSample p : allUserPublications) {
//            if (!publicationsInCollection.contains(p)) {
//                result.add(p);
//            }
//        }
//
//        return result;
//    }
//}
//
