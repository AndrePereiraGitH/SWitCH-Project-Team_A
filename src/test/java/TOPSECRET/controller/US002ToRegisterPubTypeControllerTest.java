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
 * Unit tests for US002 controller.
 * Uses mock for {@link PublicationTypeRepository} dependency
 * Verifies interactions
 */

class US002ToRegisterPubTypeControllerTest {

    /**
     * Initial setup for tests
     * Creates a mock repository
     * Creates a controller object
     */
    PublicationTypeRepository repo;
    US002ToRegisterPubTypeController controller;

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
        verify(repo, times(1)).create(mediaFormat, typeOfBinding, details);
        verify(repo, times(1)).addToList(result);
        verifyNoMoreInteractions(repo);
    }

    /**
     * Registering an invalid publication should throw an exception when creating
     */
    @Test
    void registerInvalidPublicationTypeShouldThrowExceptionWhenCreating()
    {
        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding typeOfBinding = TypeOfBinding.PAPERBACK;
        String details = null;

        when(repo.create(mediaFormat, typeOfBinding, details)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> controller.RegisterPubType(mediaFormat, typeOfBinding, details));
        verify(repo, times(1)).create(mediaFormat, typeOfBinding, details);
        verifyNoMoreInteractions(repo);
    }

    /**
     *
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
        verify(repo, times(1)).create(mediaFormat, typeOfBinding, details);
        verify(repo, times(1)).addToList(expected);
        verifyNoMoreInteractions(repo);

    }

}