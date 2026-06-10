package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.LibraryAuthorizationPolicy;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Library.LibraryFactory;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {

    @Mock
    private ILibraryRepository libraryRepositoryDouble;

    @Mock
    private LibraryFactory libraryFactoryDouble;

    @Mock
    private IUserRepository userRepositoryDouble;

    @Mock
    private LibraryAuthorizationPolicy policyDouble;

    @InjectMocks
    private LibraryService service;

    // ----------------- createAndSave() -----------------

    @Test
    void givenValidLibraryNameAndUserId_whenCreateAndSave_thenReturnsLibrary()
            throws Exception {

        // ARRANGE
        String libraryName = "My Library";

        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        Library createdLibraryDouble = mock(Library.class);
        Library savedLibraryDouble = mock(Library.class);

        when(userRepositoryDouble.findByID(userIdDouble))
                .thenReturn(Optional.of(userDouble));

        when(policyDouble.canCreateLibrary(userDouble))
                .thenReturn(true);

        when(libraryFactoryDouble.create(libraryName, userIdDouble))
                .thenReturn(createdLibraryDouble);

        when(libraryRepositoryDouble.save(createdLibraryDouble))
                .thenReturn(savedLibraryDouble);

        // ACT
        Library result =
                service.createAndSave(libraryName, userIdDouble);

        // ASSERT
        assertEquals(savedLibraryDouble, result);
    }

    @Test
    void givenUserNotFound_whenCreateAndSave_thenThrowsIllegalArgumentException() {

        // ARRANGE
        String libraryName = "My Library";

        UserId userIdDouble = mock(UserId.class);

        when(userRepositoryDouble.findByID(userIdDouble))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.createAndSave(libraryName, userIdDouble)
        );
    }

    @Test
    void givenUserWithoutPermission_whenCreateAndSave_thenThrowsAccessDeniedException() {

        // ARRANGE
        String libraryName = "My Library";

        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        when(userRepositoryDouble.findByID(userIdDouble))
                .thenReturn(Optional.of(userDouble));

        when(policyDouble.canCreateLibrary(userDouble))
                .thenReturn(false);

        // ACT + ASSERT
        assertThrows(
                AccessDeniedException.class,
                () -> service.createAndSave(libraryName, userIdDouble)
        );
    }

    @Test
    void givenExistingLibraryForUser_whenCreateAndSave_thenThrowsInstanceAlreadyExistsException()
            throws Exception {

        // ARRANGE
        String libraryName = "My Library";

        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);

        Library createdLibraryDouble = mock(Library.class);

        when(userRepositoryDouble.findByID(userIdDouble))
                .thenReturn(Optional.of(userDouble));

        when(policyDouble.canCreateLibrary(userDouble))
                .thenReturn(true);

        when(libraryFactoryDouble.create(libraryName, userIdDouble))
                .thenReturn(createdLibraryDouble);

        when(libraryRepositoryDouble.save(createdLibraryDouble))
                .thenThrow(new InstanceAlreadyExistsException("exists"));

        // ACT + ASSERT
        assertThrows(
                InstanceAlreadyExistsException.class,
                () -> service.createAndSave(libraryName, userIdDouble)
        );
    }

// =========================================================
// findAll()
// =========================================================

    @Test
    void givenLibrariesExist_whenFindAll_thenReturnsAllLibraries() {

        // ARRANGE
        Library libraryDouble1 = mock(Library.class);
        Library libraryDouble2 = mock(Library.class);

        when(libraryRepositoryDouble.findAll())
                .thenReturn(List.of(libraryDouble1, libraryDouble2));

        // ACT
        List<Library> result = service.findAll();

        // ASSERT
        assertEquals(2, result.size());
    }

    @Test
    void givenNoLibraries_whenFindAll_thenReturnsEmptyList() {

        // ARRANGE
        when(libraryRepositoryDouble.findAll())
                .thenReturn(List.of());

        // ACT
        List<Library> result = service.findAll();

        // ASSERT
        assertTrue(result.isEmpty());
    }

// =========================================================
// findById()
// =========================================================

    @Test
    void givenExistingId_whenFindById_thenReturnsLibrary() throws InstanceNotFoundException {

        // ARRANGE
        LibraryId idDouble = mock(LibraryId.class);
        Library libraryDouble = mock(Library.class);

        when(libraryRepositoryDouble.findById(idDouble))
                .thenReturn(Optional.of(libraryDouble));

        // ACT
        Library result = service.findById(idDouble);

        // ASSERT
        assertEquals(libraryDouble, result);
    }

    @Test
    void givenNonExistingId_whenFindById_thenThrowsIllegalArgumentException() {

        // ARRANGE
        LibraryId idDouble = mock(LibraryId.class);

        when(libraryRepositoryDouble.findById(idDouble))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                InstanceNotFoundException.class,
                () -> service.findById(idDouble)
        );
    }

    @Test
    void givenNullId_whenFindById_thenThrowsNullPointerException() {

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> service.findById(null)
        );
    }

// =========================================================
// findByUserId()
// =========================================================

    @Test
    void givenExistingUserId_whenFindByUserId_thenReturnsLibrary() throws InstanceNotFoundException {

        // ARRANGE
        UserId userIdDouble = mock(UserId.class);
        Library libraryDouble = mock(Library.class);

        when(libraryRepositoryDouble.findByUserId(userIdDouble))
                .thenReturn(Optional.of(libraryDouble));

        // ACT
        Library result = service.findByUserId(userIdDouble);

        // ASSERT
        assertEquals(libraryDouble, result);
    }

    @Test
    void givenNonExistingUserId_whenFindByUserId_thenThrowsIllegalArgumentException() {

        // ARRANGE
        UserId userIdDouble = mock(UserId.class);

        when(libraryRepositoryDouble.findByUserId(userIdDouble))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                InstanceNotFoundException.class,
                () -> service.findByUserId(userIdDouble)
        );
    }

    @Test
    void givenNullUserId_whenFindByUserId_thenThrowsIllegalArgumentException() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.findByUserId(null)
        );
    }

}
