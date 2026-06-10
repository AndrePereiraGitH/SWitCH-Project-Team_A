package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.ValueObjects.LibraryId;
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
import java.util.List;
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

    // ============ Constructor Tests ============

    @Test
    void constructor_shouldThrowException_whenJpaRepositoryIsNull() {
        // Arrange + Act + Assert
        assertThrows(NullPointerException.class,
                () -> new LibraryRepositoryJPAImpl(null, assembler));
    }

    @Test
    void constructor_shouldThrowException_whenAssemblerIsNull() {
        // Arrange + Act + Assert
        assertThrows(NullPointerException.class,
                () -> new LibraryRepositoryJPAImpl(jpaRepository, null));
    }

    // ============ findByUserId ============

    @Test
    void givenNullUserId_whenFindByUserId_thenThrowsIllegalArgumentException() {
        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.findByUserId(null));
    }

    @Test
    void givenExistingUserId_whenFindByUserId_thenReturnsLibrary() {

        // Arrange
        UserId userId = mock(UserId.class);

        when(jpaRepository.findByUserId(userId))
                .thenReturn(Optional.of(libraryDMDouble));

        when(assembler.toDomain(libraryDMDouble))
                .thenReturn(libraryDouble);

        // Act
        Optional<Library> result = repository.findByUserId(userId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(libraryDouble, result.get());
    }

    @Test
    void givenNonExistingUserId_whenFindByUserId_thenReturnsEmpty() {

        // Arrange
        UserId userId = mock(UserId.class);

        when(jpaRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        // Act
        Optional<Library> result = repository.findByUserId(userId);

        // Assert
        assertTrue(result.isEmpty());
    }

    // ============ save ============

    @Test
    void givenNullLibrary_whenSave_thenThrowsIllegalArgumentException() {
        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));
    }

    @Test
    void givenUserWithExistingLibrary_whenSave_thenThrowsInstanceAlreadyExistsException() {

        // Arrange
        UserId userId = mock(UserId.class);

        when(libraryDouble.getUserId()).thenReturn(userId);
        when(jpaRepository.findByUserId(userId))
                .thenReturn(Optional.of(libraryDMDouble));

        // Act + Assert
        assertThrows(InstanceAlreadyExistsException.class,
                () -> repository.save(libraryDouble));
    }

    @Test
    void givenValidLibrary_whenSave_thenReturnsSavedLibrary() throws Exception {

        // Arrange
        UserId userId = mock(UserId.class);

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

    // ============ existsByUserId ============

    @Test
    void givenNullUserId_whenExistsByUserId_thenThrowsIllegalArgumentException() {
        // Arrange + Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.existsByUserId(null));
    }

    @Test
    void givenUserWithNoLibrary_whenExistsByUserId_thenReturnsFalse() {

        // Arrange
        UserId userId = mock(UserId.class);
        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act
        boolean result = repository.existsByUserId(userId);

        // Assert
        assertFalse(result);
    }

    @Test
    void givenUserWithLibrary_whenExistsByUserId_thenReturnsTrue() {

        // Arrange
        UserId userId = mock(UserId.class);
        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.of(libraryDMDouble));

        // Act
        boolean result = repository.existsByUserId(userId);

        // Assert
        assertTrue(result);
    }

    // ============ findAll ============

    @Test
    void givenNoLibraries_whenFindAll_thenReturnsEmptyList() {

        // Arrange
        when(jpaRepository.findAll()).thenReturn(List.of());

        // Act
        List<Library> result = repository.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void givenOneLibrary_whenFindAll_thenReturnsOneLibrary() {

        // Arrange
        when(jpaRepository.findAll()).thenReturn(List.of(libraryDMDouble));
        when(assembler.toDomain(libraryDMDouble)).thenReturn(libraryDouble);

        // Act
        List<Library> result = repository.findAll();

        // Assert
        assertEquals(1, result.size());
        assertSame(libraryDouble, result.get(0));
    }

    @Test
    void givenTwoLibraries_whenFindAll_thenReturnsBoth() {

        // Arrange
        LibraryDM dm2 = mock(LibraryDM.class);
        Library lib2 = mock(Library.class);

        when(jpaRepository.findAll()).thenReturn(List.of(libraryDMDouble, dm2));
        when(assembler.toDomain(libraryDMDouble)).thenReturn(libraryDouble);
        when(assembler.toDomain(dm2)).thenReturn(lib2);

        // Act
        List<Library> result = repository.findAll();

        // Assert
        assertEquals(2, result.size());
    }

    // ============ findById ============

    @Test
    void givenExistingId_whenFindById_thenReturnsLibrary() {

        // Arrange
        LibraryId id = mock(LibraryId.class);

        when(jpaRepository.findById(id)).thenReturn(Optional.of(libraryDMDouble));
        when(assembler.toDomain(libraryDMDouble)).thenReturn(libraryDouble);

        // Act
        Optional<Library> result = repository.findById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(libraryDouble, result.get());
    }

    @Test
    void givenNonExistingId_whenFindById_thenReturnsEmpty() {

        // Arrange
        LibraryId id = mock(LibraryId.class);

        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Library> result = repository.findById(id);

        // Assert
        assertTrue(result.isEmpty());
    }
}