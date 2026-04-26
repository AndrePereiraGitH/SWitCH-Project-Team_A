package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.assembler.LibraryAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.LibraryDM;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link LibraryRepositoryJPAImpl}.
 * <p>
 * This test class verifies the JPA repository implementation including
 * retrieval by user ID, saving libraries, and verifying library existence.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class LibraryRepositoryJPAImplTest {

    @Mock
    private ILibraryJPARepository jpaRepository;

    @Mock
    private LibraryAssembler assembler;

    @Mock
    private Library libraryDouble;

    @Mock
    private LibraryDM libraryDMDouble;

    //  ----------------- getLibraryByUserId() Tests  -----------------

    @Test
    void givenNullUserId_whenGetLibraryByUserId_thenThrowsIllegalArgumentException() {
        // Arrange
        LibraryRepositoryJPAImpl repository = new LibraryRepositoryJPAImpl(jpaRepository, assembler);
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.getLibraryByUserId(null));

        verifyNoInteractions(jpaRepository);
        verifyNoInteractions(assembler);
    }

    @Test
    void givenUserWithNoLibrary_whenGetLibraryByUserId_thenThrowsInstanceNotFoundException() {
        // Arrange
        UserId userId = new UserId("user-123");
        LibraryRepositoryJPAImpl repository = new LibraryRepositoryJPAImpl(jpaRepository, assembler);

        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> repository.getLibraryByUserId(userId));

        verify(jpaRepository).findByUserId(userId);
        verifyNoInteractions(assembler);
    }

    @Test
    void givenExistingLibrary_whenGetLibraryByUserId_thenReturnsLibrary() throws Exception {
        // Arrange
        UserId userId = new UserId("user-123");
        LibraryRepositoryJPAImpl repository = new LibraryRepositoryJPAImpl(jpaRepository, assembler);

        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.of(libraryDMDouble));
        when(assembler.toDomain(libraryDMDouble)).thenReturn(libraryDouble);

        // Act
        Library result = repository.getLibraryByUserId(userId);

        // Assert
        assertSame(libraryDouble, result);
        verify(jpaRepository).findByUserId(userId);
        verify(assembler).toDomain(libraryDMDouble);
    }

    //  ----------------- save() Tests  -----------------

    @Test
    void givenNullLibrary_whenSave_thenThrowsIllegalArgumentException() {
        // Arrange
        LibraryRepositoryJPAImpl repository = new LibraryRepositoryJPAImpl(jpaRepository, assembler);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));

        verifyNoInteractions(jpaRepository);
        verifyNoInteractions(assembler);
    }

    @Test
    void givenUserWithExistingLibrary_whenSave_thenThrowsInstanceAlreadyExistsException() {
        // Arrange
        UserId userId = new UserId("user-123");
        LibraryRepositoryJPAImpl repository = new LibraryRepositoryJPAImpl(jpaRepository, assembler);

        when(libraryDouble.getUserId()).thenReturn(userId);
        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.of(libraryDMDouble));

        // Act + Assert
        assertThrows(InstanceAlreadyExistsException.class,
                () -> repository.save(libraryDouble));

        verify(jpaRepository).findByUserId(userId);
        verify(assembler, never()).toDM(any());
        verify(jpaRepository, never()).save(any());
    }

    @Test
    void givenValidLibrary_whenSave_thenReturnsSavedLibrary() throws Exception {
        // Arrange
        UserId userId = new UserId("user-123");

        when(libraryDouble.getUserId()).thenReturn(userId);
        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(assembler.toDM(libraryDouble)).thenReturn(libraryDMDouble);
        when(jpaRepository.save(libraryDMDouble)).thenReturn(libraryDMDouble);
        when(assembler.toDomain(libraryDMDouble)).thenReturn(libraryDouble);
        LibraryRepositoryJPAImpl repository = new LibraryRepositoryJPAImpl(jpaRepository, assembler);

        // Act
        Library result = repository.save(libraryDouble);

        // Assert
        assertSame(libraryDouble, result);
        verify(jpaRepository).findByUserId(userId);
        verify(assembler).toDM(libraryDouble);
        verify(jpaRepository).save(libraryDMDouble);
        verify(assembler).toDomain(libraryDMDouble);
    }

    //  ----------------- verifyIfUserHasALibrary() Tests  -----------------

    @Test
    void givenNullUserId_whenVerifyIfUserHasALibrary_thenThrowsIllegalArgumentException() {
        // Arrange
        LibraryRepositoryJPAImpl repository = new LibraryRepositoryJPAImpl(jpaRepository, assembler);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.verifyIfUserHasALibrary(null));

        verifyNoInteractions(jpaRepository);
    }

    @Test
    void givenUserWithNoLibrary_whenVerifyIfUserHasALibrary_thenReturnsFalse() {
        // Arrange
        UserId userId = new UserId("user-123");
        LibraryRepositoryJPAImpl repository = new LibraryRepositoryJPAImpl(jpaRepository, assembler);

        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act
        boolean result = repository.verifyIfUserHasALibrary(userId);

        // Assert
        assertFalse(result);
        verify(jpaRepository).findByUserId(userId);
    }

    @Test
    void givenUserWithLibrary_whenVerifyIfUserHasALibrary_thenReturnsTrue() {
        // Arrange
        UserId userId = new UserId("user-123");
        LibraryRepositoryJPAImpl repository = new LibraryRepositoryJPAImpl(jpaRepository, assembler);

        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.of(libraryDMDouble));

        // Act
        boolean result = repository.verifyIfUserHasALibrary(userId);

        // Assert
        assertTrue(result);
        verify(jpaRepository).findByUserId(userId);
    }
}