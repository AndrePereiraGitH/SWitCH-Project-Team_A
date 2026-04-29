package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.assembler.LibraryAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.LibraryDM;
import TOPSECRET.persistence.repositoryjpa.jpa.ILibraryJPARepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryRepositoryJPAImplTest {

    @Mock
    private ILibraryJPARepository jpaRepository;

    @Mock
    private LibraryAssembler assembler;

    @InjectMocks
    private LibraryRepositoryJPAImpl repository;

    @Mock
    private Library libraryDouble;

    @Mock
    private LibraryDM libraryDMDouble;

    @Test
    void constructor_shouldThrowException_whenJpaRepositoryIsNull() {
        assertThrows(NullPointerException.class, () ->
                new LibraryRepositoryJPAImpl(null, assembler)
        );
    }

    @Test
    void constructor_shouldThrowException_whenAssemblerIsNull() {
        assertThrows(NullPointerException.class, () ->
                new LibraryRepositoryJPAImpl(jpaRepository, null)
        );
    }

    @Test
    void givenNullUserId_whenGetLibraryByUserId_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> repository.getByUserId(null));

    }

    @Test
    void givenUserWithNoLibrary_whenGetLibraryByUserId_thenThrowsInstanceNotFoundException() {
        // Arrange
        UserId userId = new UserId("user-123");

        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class,
                () -> repository.getByUserId(userId));
    }

    @Test
    void givenExistingLibrary_whenGetLibraryByUserId_thenReturnsLibrary() throws Exception {
        // Arrange
        UserId userId = new UserId("user-123");

        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.of(libraryDMDouble));
        when(assembler.toDomain(libraryDMDouble)).thenReturn(libraryDouble);

        // Act
        Library result = repository.getByUserId(userId);

        // Assert
        assertSame(libraryDouble, result);
    }

    @Test
    void givenNullLibrary_whenSave_thenThrowsIllegalArgumentException() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));
    }

    @Test
    void givenUserWithExistingLibrary_whenSave_thenThrowsInstanceAlreadyExistsException() {
        // Arrange
        UserId userId = new UserId("user-123");

        when(libraryDouble.getUserId()).thenReturn(userId);
        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.of(libraryDMDouble));

        // Act & Assert
        assertThrows(InstanceAlreadyExistsException.class,
                () -> repository.save(libraryDouble));
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

        // Act
        Library result = repository.save(libraryDouble);

        // Assert
        assertSame(libraryDouble, result);
    }

    @Test
    void givenNullUserId_whenExistsByUserId_thenThrowsIllegalArgumentException() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.existsByUserId(null));
    }

    @Test
    void givenUserWithNoLibrary_whenExistsByUserId_thenReturnsFalse() {
        // Arrange
        UserId userId = new UserId("user-123");

        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act
        boolean result = repository.existsByUserId(userId);

        // Assert
        assertFalse(result);
    }

    @Test
    void givenUserWithLibrary_whenExistsByUserId_thenReturnsTrue() {
        // Arrange
        UserId userId = new UserId("user-123");

        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.of(libraryDMDouble));

        // Act
        boolean result = repository.existsByUserId(userId);

        // Assert
        assertTrue(result);
    }
}