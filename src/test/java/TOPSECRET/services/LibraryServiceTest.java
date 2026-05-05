package TOPSECRET.services;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Library.LibraryFactory;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private ILibraryRepository libraryRepositoryDouble;

    @Mock
    private LibraryFactory libraryFactoryDouble;

    @InjectMocks
    private LibraryService service;

    // ----------------- Constructor Tests -----------------

    @Test
    void givenValidDependencies_thenCreatesSuccessfully() {
        // Act & Assert
        assertDoesNotThrow(() ->
                new LibraryService(libraryRepositoryDouble, libraryFactoryDouble));
    }

    @Test
    void givenNullRepository_thenThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new LibraryService(null, libraryFactoryDouble));
    }

    @Test
    void givenNullFactory_thenThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new LibraryService(libraryRepositoryDouble, null));
    }

    // ----------------- createAndSave() Tests -----------------

    @Test
    void givenValidLibraryNameAndUserId_whenCreateAndSave_thenReturnsLibrary()
            throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "My Library";
        UserId userIdDouble = mock(UserId.class);
        Library createdLibraryDouble = mock(Library.class);
        Library savedLibraryDouble = mock(Library.class);

        when(libraryFactoryDouble.create(libraryName, userIdDouble))
                .thenReturn(createdLibraryDouble);

        when(libraryRepositoryDouble.save(createdLibraryDouble))
                .thenReturn(savedLibraryDouble);

        // Act
        Library result = service.createAndSave(libraryName, userIdDouble);

        // Assert
        assertEquals(savedLibraryDouble, result);
    }

    @Test
    void givenValidLibraryNameAndUserId_whenCreateAndSave_thenResultIsNotNull()
            throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "My Library";
        UserId userIdDouble = mock(UserId.class);
        Library createdLibraryDouble = mock(Library.class);
        Library savedLibraryDouble = mock(Library.class);

        when(libraryFactoryDouble.create(libraryName, userIdDouble))
                .thenReturn(createdLibraryDouble);

        when(libraryRepositoryDouble.save(createdLibraryDouble))
                .thenReturn(savedLibraryDouble);

        // Act
        Library result = service.createAndSave(libraryName, userIdDouble);

        // Assert
        assertNotNull(result);
    }

    @Test
    void givenExistingLibraryForUser_whenCreateAndSave_thenThrowsInstanceAlreadyExistsException()
            throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "My Library";
        UserId userIdDouble = mock(UserId.class);
        Library createdLibraryDouble = mock(Library.class);

        when(libraryFactoryDouble.create(libraryName, userIdDouble))
                .thenReturn(createdLibraryDouble);

        when(libraryRepositoryDouble.save(createdLibraryDouble))
                .thenThrow(new InstanceAlreadyExistsException("User already has a library"));

        // Act & Assert
        assertThrows(InstanceAlreadyExistsException.class,
                () -> service.createAndSave(libraryName, userIdDouble));
    }
}