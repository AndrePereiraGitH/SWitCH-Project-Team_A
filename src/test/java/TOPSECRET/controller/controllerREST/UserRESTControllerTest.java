package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.UserAuthorizationPolicy;
import TOPSECRET.controller.controllerREST.root.linkProvider.UserLinkProvider;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.Profile;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.user.UserMapper;
import TOPSECRET.dto.user.request.RequestUserDTO;
import TOPSECRET.dto.user.response.ResponseUserDTO;
import TOPSECRET.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserRESTController}.
 *
 * <p>Validates HTTP responses, exception handling and DTO mapping.</p>
 */
@ExtendWith(MockitoExtension.class)
class UserRESTControllerTest {

    @Mock
    private UserLinkProvider userLinkProvider;

    @Mock
    private UserService userService;

    @Mock
    private UserAuthorizationPolicy userAuthorizationPolicy;

    @InjectMocks
    private UserRESTController controller;

    // ================================================================
    // POST /users ? 201 CREATED
    // ================================================================

    @Test
    void registerUser_shouldReturnCreated_whenValidRequest() throws Exception {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId requesterIdDouble = mock(UserId.class);
        UserId createdUserIdDouble = mock(UserId.class);
        User requesterDouble = mock(User.class);
        User createdUserDouble = mock(User.class);
        ResponseUserDTO responseDTO = new ResponseUserDTO("id", "user", "name", "email", "ADMIN");

        when(requestDouble.username()).thenReturn("newuser");
        when(requestDouble.role()).thenReturn("NORMAL_USER");

        when(userService.findByID(requesterIdDouble)).thenReturn(requesterDouble);
        when(userAuthorizationPolicy.canRegisterUser(requesterDouble)).thenReturn(true);

        when(requesterDouble.id()).thenReturn(requesterIdDouble);
        when(createdUserDouble.id()).thenReturn(createdUserIdDouble);
        when(createdUserIdDouble.getUserId()).thenReturn("user-001");

        when(userService.createAndSave(any(), any(), any(), any()))
                .thenReturn(createdUserDouble);

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toProfile(requestDouble)).thenReturn(mock(Profile.class));
            mapperMock.when(() -> UserMapper.toRole("NORMAL_USER")).thenReturn(UserRole.NORMAL_USER);
            mapperMock.when(() -> UserMapper.toDTO(createdUserDouble)).thenReturn(responseDTO);
            mapperMock.when(() -> UserMapper.toUserId("user-001")).thenReturn(requesterIdDouble);

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.registerUser(requestDouble, "user-001");

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().hasLink("self"));
        }
    }

    // ================================================================
    // POST /users ? 400 BAD REQUEST
    // ================================================================

    @Test
    void registerUser_shouldReturnBadRequest_whenIllegalArgumentException() throws Exception {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId requesterIdDouble = mock(UserId.class);
        User requesterDouble = mock(User.class);

        when(requestDouble.username()).thenReturn("newuser");
        when(requestDouble.role()).thenReturn("NORMAL_USER");

        when(userService.findByID(requesterIdDouble)).thenReturn(requesterDouble);
        when(userAuthorizationPolicy.canRegisterUser(requesterDouble)).thenReturn(true);
        when(requesterDouble.id()).thenReturn(requesterIdDouble);

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toProfile(any())).thenReturn(mock(Profile.class));
            mapperMock.when(() -> UserMapper.toRole(any())).thenReturn(UserRole.NORMAL_USER);
            mapperMock.when(() -> UserMapper.toUserId(any())).thenReturn(requesterIdDouble);
            when(userService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.registerUser(requestDouble, "user-001");

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ================================================================
    // POST /users ? 409 CONFLICT
    // ================================================================

    @Test
    void registerUser_shouldReturnConflict_whenInstanceAlreadyExistsException() throws Exception {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId requesterIdDouble = mock(UserId.class);
        User requesterDouble = mock(User.class);

        when(requestDouble.username()).thenReturn("newuser");
        when(requestDouble.role()).thenReturn("NORMAL_USER");

        when(userService.findByID(requesterIdDouble)).thenReturn(requesterDouble);
        when(userAuthorizationPolicy.canRegisterUser(requesterDouble)).thenReturn(true);
        when(requesterDouble.id()).thenReturn(requesterIdDouble);

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toProfile(any())).thenReturn(mock(Profile.class));
            mapperMock.when(() -> UserMapper.toRole(any())).thenReturn(UserRole.NORMAL_USER);
            mapperMock.when(() -> UserMapper.toUserId(any())).thenReturn(requesterIdDouble);
            when(userService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new InstanceAlreadyExistsException());

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.registerUser(requestDouble, "user-001");

            // Assert
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ================================================================
    // POST /users ? 403 FORBIDDEN
    // ================================================================

    @Test
    void registerUser_shouldReturnForbidden_whenAccessDeniedException() throws Exception {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId requesterIdDouble = mock(UserId.class);
        User requesterDouble = mock(User.class);

        when(requestDouble.username()).thenReturn("newuser");
        when(requestDouble.role()).thenReturn("NORMAL_USER");

        when(userService.findByID(requesterIdDouble)).thenReturn(requesterDouble);
        when(userAuthorizationPolicy.canRegisterUser(requesterDouble)).thenReturn(true);
        when(requesterDouble.id()).thenReturn(requesterIdDouble);

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toProfile(any())).thenReturn(mock(Profile.class));
            mapperMock.when(() -> UserMapper.toRole(any())).thenReturn(UserRole.NORMAL_USER);
            mapperMock.when(() -> UserMapper.toUserId(any())).thenReturn(requesterIdDouble);
            when(userService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new AccessDeniedException("denied"));

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.registerUser(requestDouble, "user-001");

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ================================================================
    // POST /users ? 403 FORBIDDEN
    // ================================================================

    @Test
    void registerUser_shouldReturnForbidden_whenRequesterIsNotAdmin() throws InstanceAlreadyExistsException {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId requesterIdDouble = mock(UserId.class);
        User requesterDouble = mock(User.class);

        when(userService.findByID(requesterIdDouble)).thenReturn(requesterDouble);
        when(userAuthorizationPolicy.canRegisterUser(requesterDouble)).thenReturn(false);

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId(any())).thenReturn(requesterIdDouble);

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.registerUser(requestDouble, "user-001");

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertNull(response.getBody());
            verify(userService, never()).createAndSave(any(), any(), any(), any());
        }
    }

    // ================================================================
    // POST /users ? 500 INTERNAL SERVER ERROR
    // ================================================================

    @Test
    void registerUser_shouldReturnInternalServerError_whenUnexpectedException() throws Exception {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId requesterIdDouble = mock(UserId.class);
        User requesterDouble = mock(User.class);

        when(requestDouble.username()).thenReturn("newuser");
        when(requestDouble.role()).thenReturn("NORMAL_USER");

        when(userService.findByID(requesterIdDouble)).thenReturn(requesterDouble);
        when(userAuthorizationPolicy.canRegisterUser(requesterDouble)).thenReturn(true);
        when(requesterDouble.id()).thenReturn(requesterIdDouble);

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toProfile(any())).thenReturn(mock(Profile.class));
            mapperMock.when(() -> UserMapper.toRole(any())).thenReturn(UserRole.NORMAL_USER);
            mapperMock.when(() -> UserMapper.toUserId(any())).thenReturn(requesterIdDouble);
            when(userService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.registerUser(requestDouble, "user-001");

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ================================================================
    // GET /users ? 200 OK (with results)
    // ================================================================

    @Test
    void getAllUsers_shouldReturnOk_whenUsersExist() throws Exception {

        // Arrange
        User requesterDouble = mock(User.class);
        User userDouble = mock(User.class);
        UserId requesterIdDouble = mock(UserId.class);
        UserId userIdDouble = mock(UserId.class);
        ResponseUserDTO responseDTO = new ResponseUserDTO("id", "user", "name", "email", "ADMIN");

        when(userService.findByID(requesterIdDouble)).thenReturn(requesterDouble);
        when(userAuthorizationPolicy.canFindAllUsers(requesterDouble)).thenReturn(true);

        when(userService.findAll()).thenReturn(List.of(userDouble));

        when(requesterDouble.id()).thenReturn(requesterIdDouble);
        when(userDouble.id()).thenReturn(userIdDouble);
        when(userIdDouble.getUserId()).thenReturn("user-001");

        when(userLinkProvider.getLinksForUserResource(any(UserId.class), any(UserId.class)))
                .thenReturn(List.of());

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId("admin-001")).thenReturn(requesterIdDouble);
            mapperMock.when(() -> UserMapper.toDTO(userDouble)).thenReturn(responseDTO);

            // Act
            ResponseEntity<List<ResponseUserDTO>> response =
                    controller.getAllUsers("admin-001");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
        }
    }

    // ================================================================
    // GET /users ? 200 OK (empty list)
    // ================================================================

    @Test
    void getAllUsers_shouldReturnOk_whenNoUsersExist() {

        // Arrange
        User requesterDouble = mock(User.class);
        UserId requesterIdDouble = mock(UserId.class);

        when(userService.findByID(requesterIdDouble)).thenReturn(requesterDouble);
        when(userAuthorizationPolicy.canFindAllUsers(requesterDouble)).thenReturn(true);
        when(userService.findAll()).thenReturn(List.of());

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId(any())).thenReturn(requesterIdDouble);

            // Act
            ResponseEntity<List<ResponseUserDTO>> response =
                    controller.getAllUsers("admin-001");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().isEmpty());
        }
    }

    // ================================================================
    // GET /users ? 403 FORBIDDEN
    // ================================================================

    @Test
    void getAllUsers_shouldReturnForbidden_whenRequesterIsNotAdmin() {

        // Arrange
        User requesterDouble = mock(User.class);
        UserId requesterIdDouble = mock(UserId.class);

        when(userService.findByID(requesterIdDouble)).thenReturn(requesterDouble);
        when(userAuthorizationPolicy.canFindAllUsers(requesterDouble)).thenReturn(false);

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId(any())).thenReturn(requesterIdDouble);

            // Act
            ResponseEntity<List<ResponseUserDTO>> response =
                    controller.getAllUsers("user-001");

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertNull(response.getBody());
            verify(userService, never()).findAll();
        }
    }

    // ================================================================
    // GET /users ? 500 INTERNAL SERVER ERROR
    // ================================================================

    @Test
    void getAllUsers_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        User requesterDouble = mock(User.class);
        UserId requesterIdDouble = mock(UserId.class);

        when(userService.findByID(requesterIdDouble)).thenReturn(requesterDouble);
        when(userAuthorizationPolicy.canFindAllUsers(requesterDouble)).thenReturn(true);
        when(userService.findAll()).thenThrow(new RuntimeException());

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId(any())).thenReturn(requesterIdDouble);

            // Act
            ResponseEntity<List<ResponseUserDTO>> response =
                    controller.getAllUsers("admin-001");

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ================================================================
    // GET /users/{id} ? 200 OK
    // ================================================================

    @Test
    void getUser_shouldReturnOk_whenUserExists() throws Exception {

        // Arrange
        User requesterDouble = mock(User.class);
        User userDouble = mock(User.class);
        UserId requesterIdDouble = mock(UserId.class);
        UserId targetUserIdDouble = mock(UserId.class);
        ResponseUserDTO responseDTO = new ResponseUserDTO("id", "user", "name", "email", "ADMIN");

        when(userService.findByID(requesterIdDouble)).thenReturn(requesterDouble);
        when(userService.findByID(targetUserIdDouble)).thenReturn(userDouble);

        when(userAuthorizationPolicy.canFindUserById(requesterDouble, targetUserIdDouble))
                .thenReturn(true);

        when(requesterDouble.id()).thenReturn(requesterIdDouble);

        when(userLinkProvider.getLinksForUserResource(requesterIdDouble, targetUserIdDouble))
                .thenReturn(List.of(Link.of("http://localhost/users").withRel("get-user")));

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId("user-001")).thenReturn(targetUserIdDouble);
            mapperMock.when(() -> UserMapper.toUserId("requester-001")).thenReturn(requesterIdDouble);
            mapperMock.when(() -> UserMapper.toDTO(userDouble)).thenReturn(responseDTO);

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.getUser("user-001", "requester-001");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().hasLink("get-user"));
            assertEquals(responseDTO, response.getBody());
        }
    }

    // ================================================================
    // GET /users/{id} ? 403 FORBIDDEN
    // ================================================================

    @Test
    void getUser_shouldReturnForbidden_whenRequesterCannotAccessUser() {

        // Arrange
        User requesterDouble = mock(User.class);
        UserId requesterIdDouble = mock(UserId.class);
        UserId targetUserIdDouble = mock(UserId.class);

        when(userService.findByID(requesterIdDouble)).thenReturn(requesterDouble);
        when(userAuthorizationPolicy.canFindUserById(requesterDouble, targetUserIdDouble))
                .thenReturn(false);

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId("user-001")).thenReturn(targetUserIdDouble);
            mapperMock.when(() -> UserMapper.toUserId("requester-001")).thenReturn(requesterIdDouble);

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.getUser("user-001", "requester-001");

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ================================================================
    // GET /users/{id} ? 400 BAD REQUEST
    // ================================================================

    @Test
    void getUser_shouldReturnBadRequest_whenMalformedId() {

        // Arrange
        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId(any()))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.getUser("id-invalido", "requester-001");

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ================================================================
    // GET /users/{id} ? 404 NOT FOUND
    // ================================================================

    @Test
    void getUser_shouldReturnNotFound_whenUserDoesNotExist() {

        // Arrange
        User requesterDouble = mock(User.class);
        UserId requesterIdDouble = mock(UserId.class);
        UserId targetUserIdDouble = mock(UserId.class);

        when(userService.findByID(requesterIdDouble)).thenReturn(requesterDouble);
        when(userAuthorizationPolicy.canFindUserById(requesterDouble, targetUserIdDouble))
                .thenReturn(true);
        when(userService.findByID(targetUserIdDouble)).thenThrow(new NoSuchElementException());

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId("user-001")).thenReturn(targetUserIdDouble);
            mapperMock.when(() -> UserMapper.toUserId("requester-001")).thenReturn(requesterIdDouble);

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.getUser("user-001", "requester-001");

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ================================================================
    // GET /users/{id} ? 500 INTERNAL SERVER ERROR
    // ================================================================

    @Test
    void getUser_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        UserId requesterIdDouble = mock(UserId.class);
        UserId targetUserIdDouble = mock(UserId.class);

        when(userService.findByID(requesterIdDouble)).thenThrow(new RuntimeException());

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId("user-001")).thenReturn(targetUserIdDouble);
            mapperMock.when(() -> UserMapper.toUserId("requester-001")).thenReturn(requesterIdDouble);

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.getUser("user-001", "requester-001");

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNull(response.getBody());
        }
    }
}