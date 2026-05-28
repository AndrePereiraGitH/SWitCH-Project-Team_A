//package TOPSECRET.services;
//
//import TOPSECRET.controller.controllerREST.root.authorizationPolicy.LibraryAuthorizationPolicy;
//import TOPSECRET.domain.Library.ILibraryRepository;
//import TOPSECRET.domain.Library.Library;
//import TOPSECRET.domain.Library.LibraryFactory;
//import TOPSECRET.domain.User.IUserRepository;
//import TOPSECRET.domain.User.User;
//import TOPSECRET.domain.ValueObjects.UserId;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.access.AccessDeniedException;
//
//import javax.management.InstanceAlreadyExistsException;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class LibraryServiceTest {
//
//    @Mock
//    private ILibraryRepository libraryRepositoryDouble;
//
//    @Mock
//    private LibraryFactory libraryFactoryDouble;
//
//    @Mock
//    private IUserRepository userRepositoryDouble;
//
//    @Mock
//    private LibraryAuthorizationPolicy policyDouble;
//
//    @InjectMocks
//    private LibraryService service;
//
//    // ----------------- createAndSave() SUCCESS -----------------
//
//    @Test
//    void givenValidLibraryNameAndUserId_whenCreateAndSave_thenReturnsLibrary()
//            throws Exception {
//
//        // Arrange
//        String libraryName = "My Library";
//        UserId userIdDouble = mock(UserId.class);
//        User userDouble = mock(User.class);
//        Library createdLibraryDouble = mock(Library.class);
//        Library savedLibraryDouble = mock(Library.class);
//
//        when(userRepositoryDouble.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
//        when(policyDouble.canCreateLibrary(userDouble)).thenReturn(true);
//        when(libraryFactoryDouble.create(libraryName, userIdDouble)).thenReturn(createdLibraryDouble);
//        when(libraryRepositoryDouble.save(createdLibraryDouble)).thenReturn(savedLibraryDouble);
//
//        // Act
//        Library result = service.createAndSave(libraryName, userIdDouble);
//
//        // Assert
//        assertEquals(savedLibraryDouble, result);
//    }
//
//    // ----------------- USER NOT FOUND -----------------
//
//    @Test
//    void givenUserNotFound_whenCreateAndSave_thenThrowsIllegalArgumentException() {
//
//        // Arrange
//        String libraryName = "My Library";
//        UserId userIdDouble = mock(UserId.class);
//
//        when(userRepositoryDouble.findByID(userIdDouble)).thenReturn(Optional.empty());
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> service.createAndSave(libraryName, userIdDouble));
//    }
//
//    // ----------------- ACCESS DENIED -----------------
//
//    @Test
//    void givenUserWithoutPermission_whenCreateAndSave_thenThrowsAccessDenied() {
//
//        // Arrange
//        String libraryName = "My Library";
//        UserId userIdDouble = mock(UserId.class);
//        User userDouble = mock(User.class);
//
//        when(userRepositoryDouble.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
//        when(policyDouble.canCreateLibrary(userDouble)).thenReturn(false);
//
//        // Act + Assert
//        assertThrows(AccessDeniedException.class,
//                () -> service.createAndSave(libraryName, userIdDouble));
//    }
//
//    // ----------------- LIBRARY ALREADY EXISTS -----------------
//
//    @Test
//    void givenExistingLibraryForUser_whenCreateAndSave_thenThrowsInstanceAlreadyExistsException()
//            throws Exception {
//
//        // Arrange
//        String libraryName = "My Library";
//        UserId userIdDouble = mock(UserId.class);
//        User userDouble = mock(User.class);
//        Library createdLibraryDouble = mock(Library.class);
//
//        when(userRepositoryDouble.findByID(userIdDouble)).thenReturn(Optional.of(userDouble));
//        when(policyDouble.canCreateLibrary(userDouble)).thenReturn(true);
//        when(libraryFactoryDouble.create(libraryName, userIdDouble)).thenReturn(createdLibraryDouble);
//        when(libraryRepositoryDouble.save(createdLibraryDouble))
//                .thenThrow(new InstanceAlreadyExistsException("exists"));
//
//        // Act + Assert
//        assertThrows(InstanceAlreadyExistsException.class,
//                () -> service.createAndSave(libraryName, userIdDouble));
//    }
//}