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

    @Mock
    private UserId userIdDouble;

    @Mock
    private UserId requesterIdDouble;

    @Mock
    private UserId ownerIdDouble;

    @Mock
    private User userDouble;

    @Mock
    private Library libraryDouble;

    @Mock
    private Library createdLibraryDouble;

    @Mock
    private Library savedLibraryDouble;

    @Mock
    private LibraryId idDouble;

    @InjectMocks
    private LibraryService service;

    // =========================================================
    // createAndSave()
    // =========================================================

    @Test
    void givenValidLibraryNameAndUserId_whenCreateAndSave_thenReturnsLibrary()
            throws Exception {

        // ARRANGE
        String libraryName = "My Library";

        when(userRepositoryDouble.findByID(userIdDouble))
                .thenReturn(Optional.of(userDouble));
        when(policyDouble.canCreateLibrary(userDouble))
                .thenReturn(true);
        when(libraryFactoryDouble.create(libraryName, userIdDouble))
                .thenReturn(createdLibraryDouble);
        when(libraryRepositoryDouble.save(createdLibraryDouble))
                .thenReturn(savedLibraryDouble);

        // ACT
        Library result = service.createAndSave(libraryName, userIdDouble);

        // ASSERT
        assertEquals(savedLibraryDouble, result);
    }

    @Test
    void givenUserNotFound_whenCreateAndSave_thenThrowsIllegalArgumentException() {

        // ARRANGE
        String libraryName = "My Library";

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
    void givenAdminAndLibrariesExist_whenFindAll_thenReturnsAllLibraries()
            throws InstanceNotFoundException {

        // ARRANGE
        Library libraryDouble2 = mock(Library.class);

        when(userRepositoryDouble.findByID(requesterIdDouble))
                .thenReturn(Optional.of(userDouble));
        when(policyDouble.canGetAllLibrary(userDouble))
                .thenReturn(true);
        when(libraryRepositoryDouble.findAll())
                .thenReturn(List.of(libraryDouble, libraryDouble2));

        // ACT
        List<Library> result = service.findAll(requesterIdDouble);

        // ASSERT
        assertEquals(2, result.size());
    }

    @Test
    void givenAdminAndNoLibraries_whenFindAll_thenReturnsEmptyList()
            throws InstanceNotFoundException {

        // ARRANGE
        when(userRepositoryDouble.findByID(requesterIdDouble))
                .thenReturn(Optional.of(userDouble));
        when(policyDouble.canGetAllLibrary(userDouble))
                .thenReturn(true);
        when(libraryRepositoryDouble.findAll())
                .thenReturn(List.of());

        // ACT
        List<Library> result = service.findAll(requesterIdDouble);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void givenUserNotFound_whenFindAll_thenThrowsInstanceNotFoundException() {

        // ARRANGE
        when(userRepositoryDouble.findByID(requesterIdDouble))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                InstanceNotFoundException.class,
                () -> service.findAll(requesterIdDouble)
        );
    }

    @Test
    void givenNonAdmin_whenFindAll_thenThrowsAccessDeniedException() {

        // ARRANGE
        when(userRepositoryDouble.findByID(requesterIdDouble))
                .thenReturn(Optional.of(userDouble));
        when(policyDouble.canGetAllLibrary(userDouble))
                .thenReturn(false);

        // ACT + ASSERT
        assertThrows(
                AccessDeniedException.class,
                () -> service.findAll(requesterIdDouble)
        );
    }

    // =========================================================
    // findById()
    // =========================================================

    @Test
    void givenOwnerAndExistingId_whenFindById_thenReturnsLibrary()
            throws InstanceNotFoundException {

        // ARRANGE
        when(libraryRepositoryDouble.findById(idDouble))
                .thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getUserId()).thenReturn(ownerIdDouble);
        when(userRepositoryDouble.findByID(requesterIdDouble))
                .thenReturn(Optional.of(userDouble));
        when(policyDouble.canGetLibrary(userDouble, ownerIdDouble))
                .thenReturn(true);

        // ACT
        Library result = service.findById(idDouble, requesterIdDouble);

        // ASSERT
        assertEquals(libraryDouble, result);
    }

    @Test
    void givenNonExistingId_whenFindById_thenThrowsInstanceNotFoundException() {

        // ARRANGE
        when(libraryRepositoryDouble.findById(idDouble))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                InstanceNotFoundException.class,
                () -> service.findById(idDouble, requesterIdDouble)
        );
    }

    @Test
    void givenUserNotFound_whenFindById_thenThrowsInstanceNotFoundException() {

        // ARRANGE
        when(libraryRepositoryDouble.findById(idDouble))
                .thenReturn(Optional.of(libraryDouble));
        when(userRepositoryDouble.findByID(requesterIdDouble))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                InstanceNotFoundException.class,
                () -> service.findById(idDouble, requesterIdDouble)
        );
    }

    @Test
    void givenNonOwnerNonAdmin_whenFindById_thenThrowsAccessDeniedException() {

        // ARRANGE
        when(libraryRepositoryDouble.findById(idDouble))
                .thenReturn(Optional.of(libraryDouble));
        when(libraryDouble.getUserId()).thenReturn(ownerIdDouble);
        when(userRepositoryDouble.findByID(requesterIdDouble))
                .thenReturn(Optional.of(userDouble));
        when(policyDouble.canGetLibrary(userDouble, ownerIdDouble))
                .thenReturn(false);

        // ACT + ASSERT
        assertThrows(
                AccessDeniedException.class,
                () -> service.findById(idDouble, requesterIdDouble)
        );
    }

    @Test
    void givenNullId_whenFindById_thenThrowsNullPointerException() {

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> service.findById(null, requesterIdDouble)
        );
    }

    // =========================================================
    // findByUserId()
    // =========================================================

    @Test
    void givenOwnerAndExistingUserId_whenFindByUserId_thenReturnsLibrary()
            throws InstanceNotFoundException {

        // ARRANGE
        when(userRepositoryDouble.findByID(requesterIdDouble))
                .thenReturn(Optional.of(userDouble));
        when(policyDouble.canGetLibrary(userDouble, ownerIdDouble))
                .thenReturn(true);
        when(libraryRepositoryDouble.findByUserId(ownerIdDouble))
                .thenReturn(Optional.of(libraryDouble));

        // ACT
        Library result = service.findByUserId(ownerIdDouble, requesterIdDouble);

        // ASSERT
        assertEquals(libraryDouble, result);
    }

    @Test
    void givenNonExistingUserId_whenFindByUserId_thenThrowsInstanceNotFoundException() {

        // ARRANGE
        when(userRepositoryDouble.findByID(requesterIdDouble))
                .thenReturn(Optional.of(userDouble));
        when(policyDouble.canGetLibrary(userDouble, ownerIdDouble))
                .thenReturn(true);
        when(libraryRepositoryDouble.findByUserId(ownerIdDouble))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                InstanceNotFoundException.class,
                () -> service.findByUserId(ownerIdDouble, requesterIdDouble)
        );
    }

    @Test
    void givenRequesterNotFound_whenFindByUserId_thenThrowsInstanceNotFoundException() {

        // ARRANGE
        when(userRepositoryDouble.findByID(requesterIdDouble))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                InstanceNotFoundException.class,
                () -> service.findByUserId(ownerIdDouble, requesterIdDouble)
        );
    }

    @Test
    void givenNonOwnerNonAdmin_whenFindByUserId_thenThrowsAccessDeniedException() {

        // ARRANGE
        when(userRepositoryDouble.findByID(requesterIdDouble))
                .thenReturn(Optional.of(userDouble));
        when(policyDouble.canGetLibrary(userDouble, ownerIdDouble))
                .thenReturn(false);

        // ACT + ASSERT
        assertThrows(
                AccessDeniedException.class,
                () -> service.findByUserId(ownerIdDouble, requesterIdDouble)
        );
    }

    @Test
    void givenNullUserId_whenFindByUserId_thenThrowsIllegalArgumentException() {

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> service.findByUserId(null, requesterIdDouble)
        );
    }
}