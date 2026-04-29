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
        UserId userId = new UserId("user-123");

        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(InstanceNotFoundException.class,
                () -> repository.getByUserId(userId));

    }

    @Test
    void givenExistingLibrary_whenGetLibraryByUserId_thenReturnsLibrary() throws Exception {
        UserId userId = new UserId("user-123");

        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.of(libraryDMDouble));
        when(assembler.toDomainList(libraryDMDouble)).thenReturn(libraryDouble);

        Library result = repository.getByUserId(userId);

        assertSame(libraryDouble, result);
    }

    @Test
    void givenNullLibrary_whenSave_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));

    }

    @Test
    void givenUserWithExistingLibrary_whenSave_thenThrowsInstanceAlreadyExistsException() {
        UserId userId = new UserId("user-123");

        when(libraryDouble.getUserId()).thenReturn(userId);
        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.of(libraryDMDouble));

        assertThrows(InstanceAlreadyExistsException.class,
                () -> repository.save(libraryDouble));

    }

    @Test
    void givenValidLibrary_whenSave_thenReturnsSavedLibrary() throws Exception {
        UserId userId = new UserId("user-123");

        when(libraryDouble.getUserId()).thenReturn(userId);
        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(assembler.toDM(libraryDouble)).thenReturn(libraryDMDouble);
        when(jpaRepository.save(libraryDMDouble)).thenReturn(libraryDMDouble);
        when(assembler.toDomainList(libraryDMDouble)).thenReturn(libraryDouble);

        Library result = repository.save(libraryDouble);

        assertSame(libraryDouble, result);
    }

    @Test
    void givenNullUserId_whenExistsByUserId_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> repository.existsByUserId(null));
    }

    @Test
    void givenUserWithNoLibrary_whenExistsByUserId_thenReturnsFalse() {
        UserId userId = new UserId("user-123");

        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.empty());

        boolean result = repository.existsByUserId(userId);

        assertFalse(result);
    }

    @Test
    void givenUserWithLibrary_whenExistsByUserId_thenReturnsTrue() {
        UserId userId = new UserId("user-123");

        when(jpaRepository.findByUserId(userId)).thenReturn(Optional.of(libraryDMDouble));

        boolean result = repository.existsByUserId(userId);

        assertTrue(result);
    }
}