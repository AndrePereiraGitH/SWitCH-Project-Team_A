package TOPSECRET.controller.controllerCLI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.services.PublicationSampleService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.util.List;

/**
 * Unit tests for {@link ToAddPublicationToLibraryController}.
 *
 * <p>This test suite validates the behaviour of the controller responsible
 * for adding publication samples to a user's library.</p>
 *
 * <p>The controller dependencies are mocked using Mockito to ensure that
 * each test focuses exclusively on controller orchestration logic and not
 * on service or domain implementations.</p>
 *
 * <h2>Test coverage includes:</h2>
 * <ul>
 * <li>Validation of constructor arguments</li>
 * <li>Retrieval of publications not assigned to a library</li>
 * <li>Successful addition of a publication to a library</li>
 * <li>Handling of invalid arguments</li>
 * <li>Handling of users without libraries</li>
 * <li>Handling of service failures</li>
 * </ul>
 *
 * <p>The System Under Test (SUT) is instantiated individually within each
 * test method to guarantee test isolation and improve readability.</p>
 */
class ToAddPublicationToLibraryControllerTest {

    private ILibraryRepository libraryRepositoryDouble;
    private PublicationSampleService pubSampleServiceDouble;

    private Library libraryDouble;
    private UserId userIdDouble;
    private LibraryId libraryIdDouble;

    private PublicationSample publication1;
    private PublicationSample publication2;

    @BeforeEach
    void setup() throws InstanceNotFoundException {

        libraryRepositoryDouble = mock(ILibraryRepository.class);
        pubSampleServiceDouble = mock(PublicationSampleService.class);

        libraryDouble = mock(Library.class);
        userIdDouble = mock(UserId.class);
        libraryIdDouble = mock(LibraryId.class);

        publication1 = mock(PublicationSample.class);
        publication2 = mock(PublicationSample.class);

        when(libraryRepositoryDouble.getByUserId(userIdDouble))
                .thenReturn(libraryDouble);

        when(libraryDouble.id())
                .thenReturn(libraryIdDouble);
    }

    // -------------------------------------------------------------------------
    // Constructor tests
    // -------------------------------------------------------------------------

    @Test
    void constructor_nullLibraryRepository_throwsException() {
        // Arrange (handled by BeforeEach)

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ToAddPublicationToLibraryController(
                        null,
                        pubSampleServiceDouble));
    }

    @Test
    void constructor_nullPublicationService_throwsException() {
        // Arrange (handled by BeforeEach)

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new ToAddPublicationToLibraryController(
                        libraryRepositoryDouble,
                        null));
    }

    // -------------------------------------------------------------------------
    // getListOfPublicationsNotInLibrary
    // -------------------------------------------------------------------------

    @Test
    void getList_returnsPublicationsFromService() {
        // Arrange
        ToAddPublicationToLibraryController controller =
                new ToAddPublicationToLibraryController(
                        libraryRepositoryDouble,
                        pubSampleServiceDouble);

        when(pubSampleServiceDouble.findByUserIdAndLibraryIdIsNull(userIdDouble))
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
    void getList_emptyList_returnsEmpty() {
        // Arrange
        ToAddPublicationToLibraryController controller =
                new ToAddPublicationToLibraryController(
                        libraryRepositoryDouble,
                        pubSampleServiceDouble);

        when(pubSampleServiceDouble.findByUserIdAndLibraryIdIsNull(userIdDouble))
                .thenReturn(List.of());

        // Act
        List<PublicationSample> result =
                controller.getListOfPublicationsNotInLibrary(userIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getList_nullUserId_throwsException() {
        // Arrange
        ToAddPublicationToLibraryController controller =
                new ToAddPublicationToLibraryController(
                        libraryRepositoryDouble,
                        pubSampleServiceDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.getListOfPublicationsNotInLibrary(null));
    }

    // -------------------------------------------------------------------------
    // addPublicationToLibrary
    // -------------------------------------------------------------------------

    @Test
    void addPublication_validFlow_delegatesToServiceAndReturnsTrue()
            throws InstanceNotFoundException {
        // Arrange
        ToAddPublicationToLibraryController controller =
                new ToAddPublicationToLibraryController(
                        libraryRepositoryDouble,
                        pubSampleServiceDouble);

        PublicationSampleId idDouble = mock(PublicationSampleId.class);

        when(pubSampleServiceDouble.addPublicationToLibrary(idDouble, libraryIdDouble, userIdDouble))
                .thenReturn(publication1);

        // Act
        boolean result = controller.addPublicationToLibrary(userIdDouble, idDouble);

        // Assert
        assertTrue(result);
        verify(pubSampleServiceDouble, times(1))
                .addPublicationToLibrary(idDouble, libraryIdDouble, userIdDouble);
    }

    @Test
    void addPublication_returnsFalse_whenServiceReturnsNull()
            throws InstanceNotFoundException {
        // Arrange
        ToAddPublicationToLibraryController controller =
                new ToAddPublicationToLibraryController(
                        libraryRepositoryDouble,
                        pubSampleServiceDouble);

        PublicationSampleId idDouble = mock(PublicationSampleId.class);

        when(pubSampleServiceDouble.addPublicationToLibrary(idDouble, libraryIdDouble, userIdDouble))
                .thenReturn(null);

        // Act
        boolean result = controller.addPublicationToLibrary(userIdDouble, idDouble);

        // Assert
        assertFalse(result);
    }

    @Test
    void addPublication_nullUser_throwsException() {
        // Arrange
        ToAddPublicationToLibraryController controller =
                new ToAddPublicationToLibraryController(
                        libraryRepositoryDouble,
                        pubSampleServiceDouble);

        PublicationSampleId idDouble = mock(PublicationSampleId.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToLibrary(null, idDouble));
    }

    @Test
    void addPublication_nullPublicationId_throwsException() {
        // Arrange
        ToAddPublicationToLibraryController controller =
                new ToAddPublicationToLibraryController(
                        libraryRepositoryDouble,
                        pubSampleServiceDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.addPublicationToLibrary(userIdDouble, null));
    }

    @Test
    void addPublication_userHasNoLibrary_throwsException()
            throws InstanceNotFoundException {
        // Arrange
        ToAddPublicationToLibraryController controller =
                new ToAddPublicationToLibraryController(
                        libraryRepositoryDouble,
                        pubSampleServiceDouble);

        PublicationSampleId idDouble = mock(PublicationSampleId.class);

        when(libraryRepositoryDouble.getByUserId(userIdDouble))
                .thenThrow(new InstanceNotFoundException());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> controller.addPublicationToLibrary(userIdDouble, idDouble));
    }
}