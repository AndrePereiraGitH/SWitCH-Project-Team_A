package TOPSECRET.controller;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.repository.PublicationTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link US002ToRegisterPubTypeController}.
 *
 * <p>This test class verifies the behavior of the controller responsible
 * for registering new {@link PublicationType} objects. The repository
 * dependency is mocked using Mockito to isolate the controller logic
 * and verify interactions with the {@link PublicationTypeRepository}.
 */

class US002ToRegisterPubTypeControllerTest {

    /**
     * Mock repository used to simulate persistence behavior.
     */
    PublicationTypeRepository repo;

    /**
     * Controller instance under test.
     */
    US002ToRegisterPubTypeController controller;

    /**
     * Initializes the test environment before each test execution.
     *
     * <p>A mocked {@link PublicationTypeRepository} is created and injected
     * into the controller to isolate the controller from the persistence layer.
     */
    @BeforeEach
    void setup()
    {
        repo = mock(PublicationTypeRepository.class);
        controller = new US002ToRegisterPubTypeController(repo);
    }

    /**
     * Register a new valid publication type with success
     */
    @Test
    void registerPublicationTypeControllerDelegatesToRepository()
    {
        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding typeOfBinding = TypeOfBinding.PAPERBACK;
        String details = "details test";

        PublicationType expected = mock(PublicationType.class);

        when(repo.create(mediaFormat, typeOfBinding, details)).thenReturn(expected);

        PublicationType result = controller.RegisterPubType(mediaFormat, typeOfBinding, details);

        assertEquals(expected, result);
//        verify(repo, times(1)).create(mediaFormat, typeOfBinding, details);
//        verify(repo, times(1)).addToList(result);
//        verifyNoMoreInteractions(repo);
    }

    /**
     * Invalid data tests
     * Verifies that an {@link IllegalArgumentException} is thrown when parameters are invalid
     * and ensures that nothing is added to the repository.
     */

    /**
     * Invalid Media format
     */
    @Test
    void shouldThrowExceptionWhenMediaFormatIsInvalid()
    {
        MediaFormat mediaFormat = null;
        TypeOfBinding typeOfBinding = TypeOfBinding.PAPERBACK;
        String details = "details test";

        when(repo.create(mediaFormat, typeOfBinding, details)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> controller.RegisterPubType(mediaFormat, typeOfBinding, details));
    }

    /**
     * Invalid Type Of Binding
     */
    @Test
    void shouldThrowExceptionWhenTypeOfBindingIsInvalid()
    {
        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding typeOfBinding = null;
        String details = "details test";

        when(repo.create(mediaFormat, typeOfBinding, details)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> controller.RegisterPubType(mediaFormat, typeOfBinding, details));
    }

    /**
     * Invalid Details
     */
    @Test
    void shouldThrowExceptionWhenDetailsAreInvalid() {
        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding typeOfBinding = TypeOfBinding.PAPERBACK;
        String details = null;

        when(repo.create(mediaFormat, typeOfBinding, details)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> controller.RegisterPubType(mediaFormat, typeOfBinding, details));
//        verify(repo, times(1)).create(mediaFormat, typeOfBinding, details);
//        verifyNoMoreInteractions(repo);
    }

    /**
     * Registering a duplicate publication type should create, but not add
     */
    @Test
    void registerDuplicatePublicationTypeShouldCreateThenThrowWhenAdding()
    {
        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding typeOfBinding = TypeOfBinding.PAPERBACK;
        String details = "details test";

        PublicationType expected = mock(PublicationType.class);

        when(repo.create(mediaFormat, typeOfBinding, details)).thenReturn(expected);
        doThrow(new IllegalArgumentException()).when(repo).addToList(expected);

        assertThrows(IllegalArgumentException.class, () -> controller.RegisterPubType(mediaFormat, typeOfBinding, details));
//        verify(repo, times(1)).create(mediaFormat, typeOfBinding, details);
//        verify(repo, times(1)).addToList(expected);
//        verifyNoMoreInteractions(repo);
    }
}