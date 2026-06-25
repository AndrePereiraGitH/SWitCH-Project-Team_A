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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

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

    // =========================================================================
    // registerUser
    // =========================================================================

    @Test
    void registerUser_shouldReturnCreated_whenValidRequest() throws Exception {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId requesterIdDouble = mock(UserId.class);
        UserId createdUserIdDouble = mock(UserId.class);
        User createdUserDouble = mock(User.class);
        ResponseUserDTO responseDTO = new ResponseUserDTO("id", "user", "name", "email", "ADMIN");

        when(requestDouble.username()).thenReturn("newuser");
        when(requestDouble.role()).thenReturn("NORMAL_USER");
        when(createdUserDouble.id()).thenReturn(createdUserIdDouble);
        when(createdUserIdDouble.getUserId()).thenReturn("user-001");
        when(userService.createAndSave(any(), any(), any(), any())).thenReturn(createdUserDouble);
        when(userLinkProvider.getLinksForUserResource(any(UserId.class), any(UserId.class)))
                .thenReturn(List.of(Link.of("http://localhost/users").withRel("get-user")));

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId("user-001")).thenReturn(requesterIdDouble);
            mapperMock.when(() -> UserMapper.toProfile(requestDouble)).thenReturn(mock(Profile.class));
            mapperMock.when(() -> UserMapper.toRole("NORMAL_USER")).thenReturn(UserRole.NORMAL_USER);
            mapperMock.when(() -> UserMapper.toDTO(createdUserDouble)).thenReturn(responseDTO);

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.registerUser(requestDouble, "user-001");

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().hasLink("self"));
            assertTrue(response.getBody().hasLink("get-user"));
        }
    }

    @Test
    void registerUser_shouldReturnBadRequest_whenIllegalArgumentException() throws Exception {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId requesterIdDouble = mock(UserId.class);

        when(requestDouble.username()).thenReturn("newuser");
        when(requestDouble.role()).thenReturn("NORMAL_USER");

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId(any())).thenReturn(requesterIdDouble);
            mapperMock.when(() -> UserMapper.toProfile(any())).thenReturn(mock(Profile.class));
            mapperMock.when(() -> UserMapper.toRole(any())).thenReturn(UserRole.NORMAL_USER);
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

    @Test
    void registerUser_shouldReturnConflict_whenInstanceAlreadyExistsException() throws Exception {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId requesterIdDouble = mock(UserId.class);

        when(requestDouble.username()).thenReturn("newuser");
        when(requestDouble.role()).thenReturn("NORMAL_USER");

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId(any())).thenReturn(requesterIdDouble);
            mapperMock.when(() -> UserMapper.toProfile(any())).thenReturn(mock(Profile.class));
            mapperMock.when(() -> UserMapper.toRole(any())).thenReturn(UserRole.NORMAL_USER);
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

    @Test
    void registerUser_shouldReturnForbidden_whenAccessDeniedException() throws Exception {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId requesterIdDouble = mock(UserId.class);

        when(requestDouble.username()).thenReturn("newuser");
        when(requestDouble.role()).thenReturn("NORMAL_USER");

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId(any())).thenReturn(requesterIdDouble);
            mapperMock.when(() -> UserMapper.toProfile(any())).thenReturn(mock(Profile.class));
            mapperMock.when(() -> UserMapper.toRole(any())).thenReturn(UserRole.NORMAL_USER);
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

    @Test
    void registerUser_shouldReturnInternalServerError_whenUnexpectedException() throws Exception {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId requesterIdDouble = mock(UserId.class);

        when(requestDouble.username()).thenReturn("newuser");
        when(requestDouble.role()).thenReturn("NORMAL_USER");

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId(any())).thenReturn(requesterIdDouble);
            mapperMock.when(() -> UserMapper.toProfile(any())).thenReturn(mock(Profile.class));
            mapperMock.when(() -> UserMapper.toRole(any())).thenReturn(UserRole.NORMAL_USER);
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

    // =========================================================================
    // getAllUsers
    // =========================================================================

    @Test
    void getAllUsers_shouldReturnOk_whenUsersExist() throws Exception {

        // Arrange
        User userDouble = mock(User.class);
        UserId requesterIdDouble = mock(UserId.class);
        UserId userIdDouble = mock(UserId.class);
        ResponseUserDTO responseDTO = new ResponseUserDTO("id", "user", "name", "email", "ADMIN");

        when(userService.findAll(requesterIdDouble)).thenReturn(List.of(userDouble));
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

    @Test
    void getAllUsers_shouldAddProviderLinksToEachDto_whenLinkProviderReturnsLinks() throws Exception {

        // ARRANGE
        User userDouble = mock(User.class);
        UserId requesterIdDouble = mock(UserId.class);
        UserId userIdDouble = mock(UserId.class);
        ResponseUserDTO responseDTO = new ResponseUserDTO("id", "user", "name", "email", "ADMIN");
        Link link = Link.of("http://localhost/users/id").withRel("get-user");

        when(userService.findAll(requesterIdDouble)).thenReturn(List.of(userDouble));
        when(userDouble.id()).thenReturn(userIdDouble);
        when(userIdDouble.getUserId()).thenReturn("user-001");
        when(userLinkProvider.getLinksForUserResource(any(UserId.class), any(UserId.class)))
                .thenReturn(List.of(link));

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId("admin-001")).thenReturn(requesterIdDouble);
            mapperMock.when(() -> UserMapper.toDTO(userDouble)).thenReturn(responseDTO);

            // ACT
            ResponseEntity<List<ResponseUserDTO>> response =
                    controller.getAllUsers("admin-001");

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().get(0).hasLink("get-user"));
        }
    }

    @Test
    void getAllUsers_shouldReturnOk_whenNoUsersExist() {

        // Arrange
        UserId requesterIdDouble = mock(UserId.class);

        when(userService.findAll(requesterIdDouble)).thenReturn(List.of());

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

    @Test
    void getAllUsers_shouldReturnBadRequest_whenIllegalArgumentException() {

        // ARRANGE
        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toUserId(any()))
                    .thenThrow(new IllegalArgumentException("Invalid ID"));

            // ACT
            ResponseEntity<List<ResponseUserDTO>> response =
                    controller.getAllUsers("invalid-id");

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    void getAllUsers_shouldReturnForbidden_whenAccessIsDenied() {

        // ARRANGE
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            mapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(userService.findAll(any())).thenThrow(new AccessDeniedException("forbidden"));

            // ACT
            ResponseEntity<List<ResponseUserDTO>> response = controller.getAllUsers("user-1");

            // ASSERT
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Test
    void getAllUsers_shouldReturnNotFound_whenUserDoesNotExist() {

        // ARRANGE
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            mapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(userService.findAll(any())).thenThrow(new NoSuchElementException("not found"));

            // ACT
            ResponseEntity<List<ResponseUserDTO>> response = controller.getAllUsers("unknown");

            // ASSERT
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void getAllUsers_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        UserId requesterIdDouble = mock(UserId.class);

        when(userService.findAll(requesterIdDouble)).thenThrow(new RuntimeException());

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

    // =========================================================================
    // getUser
    // =========================================================================

    @Test
    void getUser_shouldReturnOk_whenUserExists() throws Exception {

        // Arrange
        User userDouble = mock(User.class);
        UserId requesterIdDouble = mock(UserId.class);
        UserId targetUserIdDouble = mock(UserId.class);
        ResponseUserDTO responseDTO = new ResponseUserDTO("id", "user", "name", "email", "ADMIN");

        when(userService.findByIDNew(targetUserIdDouble, requesterIdDouble)).thenReturn(userDouble);
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
            assertNotNull(response.getBody());
            assertTrue(response.getBody().hasLink("get-user"));
            assertEquals(responseDTO, response.getBody());
        }
    }

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

    @Test
    void getUser_shouldReturnForbidden_whenAccessIsDenied() {

        // ARRANGE
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            mapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(userService.findByIDNew(any(), any())).thenThrow(new AccessDeniedException("forbidden"));

            // ACT
            ResponseEntity<ResponseUserDTO> response = controller.getUser("user-1", "other-user");

            // ASSERT
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Test
    void getUser_shouldReturnNotFound_whenUserDoesNotExist() {

        // Arrange
        UserId requesterIdDouble = mock(UserId.class);
        UserId targetUserIdDouble = mock(UserId.class);

        when(userService.findByIDNew(targetUserIdDouble, requesterIdDouble))
                .thenThrow(new NoSuchElementException());

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

    @Test
    void getUser_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        UserId requesterIdDouble = mock(UserId.class);
        UserId targetUserIdDouble = mock(UserId.class);

        when(userService.findByIDNew(targetUserIdDouble, requesterIdDouble))
                .thenThrow(new RuntimeException());

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

    // =========================================================================
    // optionsUsers  (OPTIONS /users)
    // =========================================================================

    @Test
    void optionsUsers_shouldReturnOkWithPostAndOptions_whenUserCanRegister() {

        // ARRANGE
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            mapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(userService.canRegisterUser(any())).thenReturn(true);
            when(userService.canFindAllUsers(any())).thenReturn(false);

            // ACT
            ResponseEntity<Void> response = controller.optionsUsers("admin-1");

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            Set<HttpMethod> allowed = response.getHeaders().getAllow();
            assertTrue(allowed.contains(HttpMethod.POST));
            assertTrue(allowed.contains(HttpMethod.OPTIONS));
            assertFalse(allowed.contains(HttpMethod.GET));
        }
    }

    @Test
    void optionsUsers_shouldReturnOkWithGetAndOptions_whenUserCanFindAll() {

        // ARRANGE
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            mapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(userService.canRegisterUser(any())).thenReturn(false);
            when(userService.canFindAllUsers(any())).thenReturn(true);

            // ACT
            ResponseEntity<Void> response = controller.optionsUsers("admin-1");

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            Set<HttpMethod> allowed = response.getHeaders().getAllow();
            assertTrue(allowed.contains(HttpMethod.GET));
            assertTrue(allowed.contains(HttpMethod.OPTIONS));
            assertFalse(allowed.contains(HttpMethod.POST));
        }
    }

    @Test
    void optionsUsers_shouldReturnOkWithOnlyOptions_whenUserCanDoNeither() {

        // ARRANGE
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            mapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(userService.canRegisterUser(any())).thenReturn(false);
            when(userService.canFindAllUsers(any())).thenReturn(false);

            // ACT
            ResponseEntity<Void> response = controller.optionsUsers("user-1");

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            Set<HttpMethod> allowed = response.getHeaders().getAllow();
            assertTrue(allowed.contains(HttpMethod.OPTIONS));
            assertFalse(allowed.contains(HttpMethod.POST));
            assertFalse(allowed.contains(HttpMethod.GET));
        }
    }

    @Test
    void optionsUsers_shouldReturn404_whenUserNotFound() {

        // ARRANGE
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            mapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(userService.canRegisterUser(any()))
                    .thenThrow(new NoSuchElementException("User not found"));

            // ACT
            ResponseEntity<Void> response = controller.optionsUsers("unknown-id");

            // ASSERT
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void optionsUsers_shouldReturn400_whenUserIdIsInvalid() {

        // ARRANGE
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            mapper.when(() -> UserMapper.toUserId(any()))
                    .thenThrow(new IllegalArgumentException("Invalid UUID"));

            // ACT
            ResponseEntity<Void> response = controller.optionsUsers("not-a-uuid");

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void optionsUsers_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {

        // ARRANGE
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            mapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(userService.canRegisterUser(any()))
                    .thenThrow(new RuntimeException("Unexpected"));

            // ACT
            ResponseEntity<Void> response = controller.optionsUsers("any-id");

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================================
    // optionsUserById  (OPTIONS /users/{id})
    // =========================================================================

    @Test
    void optionsUserById_shouldReturnOkWithGetAndOptions_whenUserCanView() {

        // ARRANGE
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            mapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(userService.canFindUserById(any(), any())).thenReturn(true);

            // ACT
            ResponseEntity<Void> response = controller.optionsUserById("user-1", "admin-1");

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            Set<HttpMethod> allowed = response.getHeaders().getAllow();
            assertTrue(allowed.contains(HttpMethod.GET));
            assertTrue(allowed.contains(HttpMethod.OPTIONS));
        }
    }

    @Test
    void optionsUserById_shouldReturnOkWithOnlyOptions_whenUserCannotView() {

        // ARRANGE
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            mapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(userService.canFindUserById(any(), any())).thenReturn(false);

            // ACT
            ResponseEntity<Void> response = controller.optionsUserById("user-1", "other-user");

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            Set<HttpMethod> allowed = response.getHeaders().getAllow();
            assertTrue(allowed.contains(HttpMethod.OPTIONS));
            assertFalse(allowed.contains(HttpMethod.GET));
        }
    }

    @Test
    void optionsUserById_shouldReturn404_whenRequesterNotFound() {

        // ARRANGE
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            mapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(userService.canFindUserById(any(), any()))
                    .thenThrow(new NoSuchElementException("User not found"));

            // ACT
            ResponseEntity<Void> response = controller.optionsUserById("user-1", "unknown-id");

            // ASSERT
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void optionsUserById_shouldReturn400_whenUserIdIsInvalid() {

        // ARRANGE
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            mapper.when(() -> UserMapper.toUserId(any()))
                    .thenThrow(new IllegalArgumentException("Invalid UUID"));

            // ACT
            ResponseEntity<Void> response = controller.optionsUserById("not-a-uuid", "admin-1");

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void optionsUserById_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {

        // ARRANGE
        try (MockedStatic<UserMapper> mapper = mockStatic(UserMapper.class)) {
            mapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(userService.canFindUserById(any(), any()))
                    .thenThrow(new RuntimeException("Unexpected"));

            // ACT
            ResponseEntity<Void> response = controller.optionsUserById("user-1", "admin-1");

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }
}