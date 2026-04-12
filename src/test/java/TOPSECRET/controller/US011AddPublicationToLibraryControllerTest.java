package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Unit tests for {@link US011AddPublicationToLibraryController} using Mockito.
 * <p>
 * This test class verifies the behavior of the controller in isolation by mocking
 * all its collaborators, namely {@link ILibraryRepository} and
 * {@link IPublicationSampleRepository}. The goal is to ensure that the controller
 * correctly delegates responsibilities to its dependencies and returns the expected results.
 * </p>
 */
class US011AddPublicationToLibraryControllerTest {

    private US011AddPublicationToLibraryController controller;

    private ILibraryRepository libraryRepositoryDouble;

    private IPublicationSampleRepository pubSampleRepositoryDouble;

    private Library libraryDouble;

    private UserId userIdDouble;

    private LibraryId libraryIdDouble;

    private PublicationSample publication1;
    private PublicationSample publication2;
    private PublicationSample publication3;

    @BeforeEach
    void preparation() throws InstanceNotFoundException {
        libraryRepositoryDouble = mock(ILibraryRepository.class);
        pubSampleRepositoryDouble = mock(IPublicationSampleRepository.class);
        libraryDouble = mock(Library.class);
        userIdDouble = mock(UserId.class);
        libraryIdDouble = mock(LibraryId.class);

        publication1 = mock(PublicationSample.class);
        publication2 = mock(PublicationSample.class);
        publication3 = mock(PublicationSample.class);

        when(libraryRepositoryDouble.getLibraryByUserId(userIdDouble)).thenReturn(libraryDouble);
        when(libraryDouble.id()).thenReturn(libraryIdDouble);

        controller = new US011AddPublicationToLibraryController(
                libraryRepositoryDouble,
                pubSampleRepositoryDouble);
    }

    // -------------------------------------------------------------------------
    // Constructor tests
    // -------------------------------------------------------------------------

    @Test
    void constructor_nullLibraryRepository() {
        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US011AddPublicationToLibraryController(null, pubSampleRepositoryDouble));
    }

    @Test
    void constructor_nullPublicationSampleRepository() {
        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US011AddPublicationToLibraryController(libraryRepositoryDouble, null));
    }

    // -------------------------------------------------------------------------
    // getListOfPublicationsNotInLibrary() tests
    // -------------------------------------------------------------------------

    @Test
    void getListOfPublicationsNotInLibrary_returnsPublicationsFromRepository() {
        // Arrange
        when(pubSampleRepositoryDouble.getAllPublicationsFromUserWithoutLibrary(userIdDouble))
                .thenReturn(List.of(publication1, publication2));

        // Act
        List<PublicationSample> result =
                controller.getListOfPublicationsNotInLibrary(userIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(publication1));
        assertTrue(result.contains(publication2));
    }

    @Test
    void getListOfPublicationsNotInLibrary_noPublicationsAvailable_returnsEmptyList() {
        // Arrange
        when(pubSampleRepositoryDouble.getAllPublicationsFromUserWithoutLibrary(userIdDouble))
                .thenReturn(List.of());

        // Act
        List<PublicationSample> result =
                controller.getListOfPublicationsNotInLibrary(userIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getListOfPublicationsNotInLibrary_allPublicationsAvailable_returnsAll() {
        // Arrange
        when(pubSampleRepositoryDouble.getAllPublicationsFromUserWithoutLibrary(userIdDouble))
                .thenReturn(List.of(publication1, publication2, publication3));

        // Act
        List<PublicationSample> result =
                controller.getListOfPublicationsNotInLibrary(userIdDouble);

        // Assert
        assertEquals(3, result.size());
        assertTrue(result.contains(publication1));
        assertTrue(result.contains(publication2));
        assertTrue(result.contains(publication3));
    }

    // -------------------------------------------------------------------------
    // addPublicationToLibrary() tests
    // -------------------------------------------------------------------------

    @Test
    void addPublicationToLibrary_validPublication_returnsTrue()
            throws InstanceNotFoundException {
        // Arrange
        PublicationSampleId idDouble = mock(PublicationSampleId.class);
        when(pubSampleRepositoryDouble.findPublicationSampleById(idDouble))
                .thenReturn(Optional.of(publication1));

        // Act
        boolean result = controller.addPublicationToLibrary(userIdDouble, idDouble);

        // Assert
        assertTrue(result);
    }

    @Test
    void addPublicationToLibrary_nullPublicationSampleId_throwsException() {
        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToLibrary(userIdDouble, null));
    }

    @Test
    void addPublicationToLibrary_nonExistingId_throwsException()
            throws InstanceNotFoundException {
        // Arrange
        PublicationSampleId idDouble = mock(PublicationSampleId.class);
        when(pubSampleRepositoryDouble.findPublicationSampleById(idDouble))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToLibrary(userIdDouble, idDouble));
    }

    @Test
    void addPublicationToLibrary_userHasNoLibrary_throwsException()
            throws InstanceNotFoundException {
        // Arrange
        PublicationSampleId idDouble = mock(PublicationSampleId.class);
        when(libraryRepositoryDouble.getLibraryByUserId(userIdDouble))
                .thenThrow(new InstanceNotFoundException("This user does not have a library"));

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> controller.addPublicationToLibrary(userIdDouble, idDouble));
    }
}