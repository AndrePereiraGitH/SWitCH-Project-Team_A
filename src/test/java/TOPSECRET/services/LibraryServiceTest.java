package TOPSECRET.services;

import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Library.LibraryFactory;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link LibraryService} class.
 * <p>
 * This test class verifies the behavior of the library service,
 * including service instantiation and library creation through the
 * {@link LibraryFactory} and {@link ILibraryRepository}.
 * </p>
 */
class LibraryServiceTest {

    
    //  ----------------- Constructor Tests  -----------------

    @Test
    void givenValidDependencies_thenCreatesSuccessfully() {
        // Arrange
        ILibraryRepository libraryRepositoryDouble = mock(ILibraryRepository.class);
        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);

        // Act + Assert
        assertDoesNotThrow(() ->
                new LibraryService(libraryRepositoryDouble, libraryFactoryDouble));
    }

    @Test
    void givenNullRepository_thenThrowsIllegalArgumentException() {
        // Arrange
        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new LibraryService(null, libraryFactoryDouble));
    }

    @Test
    void givenNullFactory_thenThrowsIllegalArgumentException() {
        // Arrange
        ILibraryRepository libraryRepositoryDouble = mock(ILibraryRepository.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new LibraryService(libraryRepositoryDouble, null));
    }

    //  ----------------- createAndSave() Tests  -----------------

    @Test
    void givenNullLibraryName_whenCreateAndSave_thenThrowsIllegalArgumentException() {
        // Arrange
        ILibraryRepository libraryRepositoryDouble = mock(ILibraryRepository.class);
        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        UserId userIdDouble = mock(UserId.class);

        LibraryService service = new LibraryService(libraryRepositoryDouble, libraryFactoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.createAndSave(null, userIdDouble));
    }

    @Test
    void givenBlankLibraryName_whenCreateAndSave_thenThrowsIllegalArgumentException() {
        // Arrange
        ILibraryRepository libraryRepositoryDouble = mock(ILibraryRepository.class);
        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        UserId userIdDouble = mock(UserId.class);

        LibraryService service = new LibraryService(libraryRepositoryDouble, libraryFactoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.createAndSave("   ", userIdDouble));
    }

    @Test
    void givenNullUserId_whenCreateAndSave_thenThrowsIllegalArgumentException() {
        // Arrange
        ILibraryRepository libraryRepositoryDouble = mock(ILibraryRepository.class);
        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);

        LibraryService service = new LibraryService(libraryRepositoryDouble, libraryFactoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.createAndSave("My Library", null));
    }

    @Test
    void givenValidLibraryNameAndUserId_whenCreateAndSave_thenReturnsLibrary()
            throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "My Library";
        UserId userIdDouble = mock(UserId.class);
        Library createdLibraryDouble = mock(Library.class);
        Library savedLibraryDouble = mock(Library.class);

        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        when(libraryFactoryDouble.create(libraryName, userIdDouble))
                .thenReturn(createdLibraryDouble);

        ILibraryRepository libraryRepositoryDouble = mock(ILibraryRepository.class);
        when(libraryRepositoryDouble.save(createdLibraryDouble))
                .thenReturn(savedLibraryDouble);

        LibraryService service = new LibraryService(libraryRepositoryDouble, libraryFactoryDouble);

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

        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        when(libraryFactoryDouble.create(libraryName, userIdDouble))
                .thenReturn(createdLibraryDouble);

        ILibraryRepository libraryRepositoryDouble = mock(ILibraryRepository.class);
        when(libraryRepositoryDouble.save(createdLibraryDouble))
                .thenReturn(savedLibraryDouble);

        LibraryService service = new LibraryService(libraryRepositoryDouble, libraryFactoryDouble);

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

        LibraryFactory libraryFactoryDouble = mock(LibraryFactory.class);
        when(libraryFactoryDouble.create(libraryName, userIdDouble))
                .thenReturn(createdLibraryDouble);

        ILibraryRepository libraryRepositoryDouble = mock(ILibraryRepository.class);
        when(libraryRepositoryDouble.save(createdLibraryDouble))
                .thenThrow(new InstanceAlreadyExistsException("User already has a library"));

        LibraryService service = new LibraryService(libraryRepositoryDouble, libraryFactoryDouble);

        // Act + Assert
        assertThrows(InstanceAlreadyExistsException.class,
                () -> service.createAndSave(libraryName, userIdDouble));
    }
}