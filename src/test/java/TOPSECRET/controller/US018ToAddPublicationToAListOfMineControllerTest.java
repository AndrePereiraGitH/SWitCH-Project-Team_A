package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US018;
import TOPSECRET.dto.PublicationSampleDTO_US018;
import TOPSECRET.repository.CollectionOfPublicationsRepository;
import TOPSECRET.repository.ILibraryRepository;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;

import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for US018 - To add one of my publication to a list of mine
 * SUT: US018ToAddPublicationToAListOfMineController
 * All repositories are mocked in order to isolate controller behavior.
 */
class US018ToAddPublicationToAListOfMineControllerTest {

    /**
     * Helper method to create the controller with mocked dependencies.
     */
    private US018ToAddPublicationToAListOfMineController createController(
            User user,
            CollectionOfPublicationsRepository collectionRepo,
            ILibraryRepository libraryRepo,
            IPublicationSampleRepository publicationSampleRepo) {

        return new US018ToAddPublicationToAListOfMineController(
                user,
                collectionRepo,
                libraryRepo,
                publicationSampleRepo
        );
    }

    /**
     * Ensures constructor throws exception when user is null.
     */
    @Test
    void shouldThrowExceptionWhenUserIsNull() {

        // ARRANGE
        CollectionOfPublicationsRepository collectionRepoDouble = mock(CollectionOfPublicationsRepository.class);
        ILibraryRepository libraryRepoDouble = mock(ILibraryRepository.class);
        IPublicationSampleRepository publicationSampleDouble = mock(IPublicationSampleRepository.class);

        // ACT + ARRANGE
        assertThrows(IllegalArgumentException.class,
                () -> new US018ToAddPublicationToAListOfMineController(
                        null,
                        collectionRepoDouble,
                        libraryRepoDouble,
                        publicationSampleDouble)
        );
    }

    /**
     * Ensures constructor throws exception when collection repository is null.
     */
    @Test
    void shouldThrowExceptionWhenCollectionRepoIsNull() {

        // ARRANGE
        User userDouble = mock(User.class);
        ILibraryRepository libraryRepoDouble = mock(ILibraryRepository.class);
        IPublicationSampleRepository publicationSampleDouble = mock(IPublicationSampleRepository.class);

        // ACT + ARRANGE
        assertThrows(IllegalArgumentException.class,
                () -> new US018ToAddPublicationToAListOfMineController(
                        userDouble,
                        null,
                        libraryRepoDouble,
                        publicationSampleDouble
                ));
    }

    /**
     * Ensures constructor throws exception when library repository is null.
     */
    @Test
    void shouldThrowExceptionWhenLibraryRepoIsNull() {

        // ARRANGE
        User userDouble = mock(User.class);
        CollectionOfPublicationsRepository collectionRepoDouble = mock(CollectionOfPublicationsRepository.class);
        IPublicationSampleRepository publicationSampleDouble = mock(IPublicationSampleRepository.class);

        // ACT + ARRANGE
        assertThrows(IllegalArgumentException.class,
                () -> new US018ToAddPublicationToAListOfMineController(
                        userDouble,
                        collectionRepoDouble,
                        null,
                        publicationSampleDouble
                ));
    }

    /**
     * Ensures constructor throws exception when publicationSampleRepo is null.
     */
    @Test
    void shouldThrowExceptionWhenPublicationRepoIsNull() {

        // ARRANGE
        User userDouble = mock(User.class);
        CollectionOfPublicationsRepository collectionRepoDouble = mock(CollectionOfPublicationsRepository.class);
        ILibraryRepository libraryRepoDouble = mock(ILibraryRepository.class);

        // ACT + ARRANGE
        assertThrows(IllegalArgumentException.class,
                () -> new US018ToAddPublicationToAListOfMineController(
                        userDouble,
                        collectionRepoDouble,
                        libraryRepoDouble,
                        null
                ));
    }

    /**
     * Ensures user collections are retrieved and converted to DTOs.
     */
    @Test
    void shouldReturnUserCollections() {

        // Arrange
        User userDouble = mock(User.class);
        CollectionOfPublicationsRepository collectionRepoDouble = mock(CollectionOfPublicationsRepository.class);

        ILibraryRepository libraryRepoDouble = mock(ILibraryRepository.class);
        IPublicationSampleRepository publicationRepoDouble = mock(IPublicationSampleRepository.class);

        US018ToAddPublicationToAListOfMineController sut =
                createController(userDouble, collectionRepoDouble, libraryRepoDouble, publicationRepoDouble);

        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);
        Genre genreDouble = mock(Genre.class);
        User ownerDouble = mock(User.class);

        when(collectionDouble.getCollectionName()).thenReturn("MyCollection");
        when(collectionDouble.getCollectionGenre()).thenReturn(genreDouble);
        when(collectionDouble.getOwner()).thenReturn(ownerDouble);

        when(genreDouble.getGenreType()).thenReturn("Fantasy");
        when(ownerDouble.getUserName()).thenReturn("john");

        when(collectionRepoDouble.getUserCollections(userDouble))
                .thenReturn(List.of(collectionDouble));

        // Act
        List<CollectionOfPublicationsDTO_US018> result =
                sut.getUserCollections(userDouble);

        // Assert
        assertEquals(1, result.size());
    }

    /**
     * Ensures exception is thrown when user parameter is null.
     */
    @Test
    void shouldThrowExceptionWhenGetUserCollectionsUserIsNull() {

        // Arrange
        US018ToAddPublicationToAListOfMineController sut =
                createController(
                        mock(User.class),
                        mock(CollectionOfPublicationsRepository.class),
                        mock(ILibraryRepository.class),
                        mock(IPublicationSampleRepository.class)
                );

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> sut.getUserCollections(null));
    }

    /**
     * Ensures available publications are correctly returned for a collection.
     */
    @Test
    void shouldReturnPublicationsAvailableForCollection() throws InstanceNotFoundException {

        // Arrange
        User userDouble = mock(User.class);
        CollectionOfPublicationsRepository collectionRepoDouble = mock(CollectionOfPublicationsRepository.class);
        ILibraryRepository libraryRepoDouble = mock(ILibraryRepository.class);
        IPublicationSampleRepository publicationRepoDouble = mock(IPublicationSampleRepository.class);

        US018ToAddPublicationToAListOfMineController sut =
                createController(userDouble, collectionRepoDouble, libraryRepoDouble, publicationRepoDouble);

        CollectionOfPublicationsDTO_US018 collectionDTODouble = mock(CollectionOfPublicationsDTO_US018.class);

        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);
        Library libraryDouble = mock(Library.class);

        PublicationSample pub1Double = mock(PublicationSample.class);

        BibliographicalData bdDouble = mock(BibliographicalData.class);
        Edition editionDouble = mock(Edition.class);
        User ownerDouble = mock(User.class);

        when(pub1Double.getBibliographicalData()).thenReturn(bdDouble);
        when(pub1Double.getEdition()).thenReturn(editionDouble);
        when(pub1Double.getOwner()).thenReturn(ownerDouble);

        when(bdDouble.getTitle()).thenReturn("Clean Code");
        when(editionDouble.getEditionNumber()).thenReturn("1");
        when(ownerDouble.getUserName()).thenReturn("john");

        when(userDouble.getUserName()).thenReturn("john");

        when(collectionRepoDouble.collectionDTOToDomain(collectionDTODouble))
                .thenReturn(collectionDouble);

        when(libraryRepoDouble.getLibraryOfUser("john"))
                .thenReturn(libraryDouble);

        when(libraryDouble.getAllPublications())
                .thenReturn(List.of(pub1Double));

        when(collectionDouble.getPublications())
                .thenReturn(List.of());

        // Act
        List<PublicationSampleDTO_US018> result =
                sut.getPublicationsForCollection(collectionDTODouble);

        // ASSERT
        assertEquals(1, result.size());
    }

    /**
     * Ensures exception is thrown when selectedCollectionDTO is null.
     */
    @Test
    void shouldThrowExceptionWhenCollectionDTOIsNull() {

        // Arrange
        US018ToAddPublicationToAListOfMineController sut =
                createController(
                        mock(User.class),
                        mock(CollectionOfPublicationsRepository.class),
                        mock(ILibraryRepository.class),
                        mock(IPublicationSampleRepository.class)
                );

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.getPublicationsForCollection(null));
    }

    /**
     * Ensures publications are successfully added to a collection.
     */
    @Test
    void shouldAddPublicationsToCollection() throws InstanceNotFoundException {

        // ARRANGE
        User userDouble = mock(User.class);
        CollectionOfPublicationsRepository collectionRepoDouble = mock(CollectionOfPublicationsRepository.class);
        ILibraryRepository libraryRepo = mock(ILibraryRepository.class);
        IPublicationSampleRepository publicationRepo = mock(IPublicationSampleRepository.class);

        US018ToAddPublicationToAListOfMineController sut =
                createController(userDouble, collectionRepoDouble, libraryRepo, publicationRepo);

        CollectionOfPublicationsDTO_US018 collectionDTO = mock(CollectionOfPublicationsDTO_US018.class);
        PublicationSampleDTO_US018 publicationDTO = mock(PublicationSampleDTO_US018.class);

        Library library = mock(Library.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        PublicationSample publication = mock(PublicationSample.class);

        when(userDouble.getUserName()).thenReturn("john");

        when(libraryRepo.getLibraryOfUser("john"))
                .thenReturn(library);

        when(collectionRepoDouble.collectionDTOToDomain(collectionDTO))
                .thenReturn(collection);

        when(publicationRepo.PublicationSampleDtoToDomain(library, publicationDTO))
                .thenReturn(publication);

        when(collectionRepoDouble.addPublicationToCollection(userDouble, library, collection, publication))
                .thenReturn(true);

        // ACT
        boolean result =
                sut.addPublicationToCollection(
                        collectionDTO,
                        List.of(publicationDTO)
                );

        // ASSERT
        assertTrue(result);
    }

    /**
     * Ensures exception is thrown when publication DTO list is empty.
     */
    @Test
    void shouldThrowExceptionWhenPublicationListIsEmpty() {

        // ARRANGE
        US018ToAddPublicationToAListOfMineController sut =
                createController(
                        mock(User.class),
                        mock(CollectionOfPublicationsRepository.class),
                        mock(ILibraryRepository.class),
                        mock(IPublicationSampleRepository.class)
                );

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.addPublicationToCollection(
                        mock(CollectionOfPublicationsDTO_US018.class),
                        List.of()
                ));
    }
}