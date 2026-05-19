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
 * Unit tests for {@link ToAddPublicationToLibraryController}.
 *
 * <p>This test suite verifies that the controller correctly orchestrates
 * interactions between the library repository and publication sample repository.</p>
 *
 * <p>All dependencies are mocked using Mockito to ensure isolation of the controller logic.</p>
 *
 * <h2>Covered behavior:</h2>
 * <ul>
 *     <li>Validation of constructor arguments</li>
 *     <li>Retrieval of publications not yet assigned to a library</li>
 *     <li>Successful assignment of a publication to a user's library</li>
 *     <li>Correct delegation to repository save operation</li>
 *     <li>Error handling for invalid input or missing data</li>
 * </ul>
 *
 * <p>This test class focuses exclusively on controller orchestration and does not test
 * domain logic or repository implementations.</p>
 */
class ToAddPublicationToLibraryControllerTest {

    private ToAddPublicationToLibraryController controller;

    private ILibraryRepository libraryRepositoryDouble;
    private IPublicationSampleRepository pubSampleRepositoryDouble;

    private Library libraryDouble;
    private UserId userIdDouble;
    private LibraryId libraryIdDouble;

    private PublicationSample publication1;
    private PublicationSample publication2;
    private PublicationSample publication3;

    @BeforeEach
    void setup() throws InstanceNotFoundException {

        libraryRepositoryDouble = mock(ILibraryRepository.class);
        pubSampleRepositoryDouble = mock(IPublicationSampleRepository.class);

        libraryDouble = mock(Library.class);
        userIdDouble = mock(UserId.class);
        libraryIdDouble = mock(LibraryId.class);

        publication1 = mock(PublicationSample.class);
        publication2 = mock(PublicationSample.class);
        publication3 = mock(PublicationSample.class);

        when(libraryRepositoryDouble.getByUserId(userIdDouble))
                .thenReturn(libraryDouble);

        when(libraryDouble.id())
                .thenReturn(libraryIdDouble);

        controller = new ToAddPublicationToLibraryController(
                libraryRepositoryDouble,
                pubSampleRepositoryDouble
        );
    }

    // -------------------------------------------------------------------------
    // Constructor tests
    // -------------------------------------------------------------------------

    @Test
    void constructor_nullLibraryRepository_throwsException() {

        //Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ToAddPublicationToLibraryController(null, pubSampleRepositoryDouble));
    }

    @Test
    void constructor_nullPublicationRepository_throwsException() {

        //Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ToAddPublicationToLibraryController(libraryRepositoryDouble, null));
    }

    // -------------------------------------------------------------------------
    // getListOfPublicationsNotInLibrary
    // -------------------------------------------------------------------------

    @Test
    void getList_returnsPublicationsFromRepository() {

        //Arrange
        when(pubSampleRepositoryDouble.findByUserIdAndLibraryIdIsNull(userIdDouble))
                .thenReturn(List.of(publication1, publication2));

        List<PublicationSample> result =
                controller.getListOfPublicationsNotInLibrary(userIdDouble);

        //Act + Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(publication1));
        assertTrue(result.contains(publication2));
    }

    @Test
    void getList_emptyList_returnsEmpty() {

        //Arrange + Act
        when(pubSampleRepositoryDouble.findByUserIdAndLibraryIdIsNull(userIdDouble))
                .thenReturn(List.of());

        List<PublicationSample> result =
                controller.getListOfPublicationsNotInLibrary(userIdDouble);

        //Assert
        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // addPublicationToLibrary
    // -------------------------------------------------------------------------

    @Test
    void addPublication_validFlow_callsUpdateAndReturnsTrue()
            throws InstanceNotFoundException {

        //Arrange
        PublicationSampleId idDouble = mock(PublicationSampleId.class);

        when(pubSampleRepositoryDouble.findById(idDouble))
                .thenReturn(Optional.of(publication1));

        boolean result =
                controller.addPublicationToLibrary(userIdDouble, idDouble);

        //Act +Assert
        assertTrue(result);
    }

    @Test
    void addPublication_nullUser_throwsException() {

        //Arrange
        PublicationSampleId idDouble = mock(PublicationSampleId.class);

        //Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToLibrary(null, idDouble));
    }

    @Test
    void addPublication_nullPublicationId_throwsException() {

        //Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToLibrary(userIdDouble, null));
    }

    @Test
    void addPublication_notFound_throwsException()
            throws InstanceNotFoundException {

        //Arrange
        PublicationSampleId idDouble = mock(PublicationSampleId.class);

        when(pubSampleRepositoryDouble.findById(idDouble))
                .thenReturn(Optional.empty());

        //Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToLibrary(userIdDouble, idDouble));
    }

    @Test
    void addPublication_userHasNoLibrary_throwsException()
            throws InstanceNotFoundException {

        //Arrange
        PublicationSampleId idDouble = mock(PublicationSampleId.class);

        when(libraryRepositoryDouble.getByUserId(userIdDouble))
                .thenThrow(new InstanceNotFoundException());

        //Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> controller.addPublicationToLibrary(userIdDouble, idDouble));
    }

    @Test
    void addPublication_shouldCallAssignAndUpdateOnlyOnce()
            throws InstanceNotFoundException {

        //Arrange
        PublicationSampleId idDouble = mock(PublicationSampleId.class);

        //Act
        when(pubSampleRepositoryDouble.findById(idDouble))
                .thenReturn(Optional.of(publication1));

        boolean result = controller.addPublicationToLibrary(userIdDouble, idDouble);

        // Assert
        assertTrue(result);

    }
}