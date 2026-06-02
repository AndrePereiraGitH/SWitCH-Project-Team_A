package TOPSECRET.controller.controllerREST;

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

import javax.management.InstanceAlreadyExistsException;
import java.nio.file.AccessDeniedException;
import java.util.List;

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

    @InjectMocks
    private UserRESTController controller;

    // ================================================================
    // POST /users — 201 CREATED
    // ================================================================

    @Test
    void registerUser_shouldReturnCreated_whenValidRequest() throws Exception {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);
        ResponseUserDTO responseDTO = new ResponseUserDTO("id", "user", "name", "email", "ADMIN");

        when(requestDouble.username()).thenReturn("newuser");
        when(requestDouble.role()).thenReturn("NORMAL_USER");
        when(userService.createAndSave(any(), any(), any(), any())).thenReturn(userDouble);
        when(userDouble.id()).thenReturn(userIdDouble);
        when(userLinkProvider.getLinksForUsers(userIdDouble))
                .thenReturn(List.of(Link.of("http://localhost/users").withRel("get-user")));

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toProfile(any())).thenReturn(mock(Profile.class));
            mapperMock.when(() -> UserMapper.toRole(any())).thenReturn(UserRole.NORMAL_USER);
            mapperMock.when(() -> UserMapper.toDTO(userDouble)).thenReturn(responseDTO);

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.registerUser(requestDouble, userIdDouble);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertTrue(response.getBody().hasLink("get-user"));
        }
    }

    // ================================================================
    // POST /users — 400 BAD REQUEST
    // ================================================================

    @Test
    void registerUser_shouldReturnBadRequest_whenIllegalArgumentException() throws Exception {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId userIdDouble = mock(UserId.class);

        when(requestDouble.username()).thenReturn("newuser");
        when(requestDouble.role()).thenReturn("NORMAL_USER");

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toProfile(any())).thenReturn(mock(Profile.class));
            mapperMock.when(() -> UserMapper.toRole(any())).thenReturn(UserRole.NORMAL_USER);
            when(userService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.registerUser(requestDouble, userIdDouble);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ================================================================
    // POST /users — 409 CONFLICT
    // ================================================================

    @Test
    void registerUser_shouldReturnConflict_whenInstanceAlreadyExistsException() throws Exception {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId userIdDouble = mock(UserId.class);

        when(requestDouble.username()).thenReturn("newuser");
        when(requestDouble.role()).thenReturn("NORMAL_USER");

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toProfile(any())).thenReturn(mock(Profile.class));
            mapperMock.when(() -> UserMapper.toRole(any())).thenReturn(UserRole.NORMAL_USER);
            when(userService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new InstanceAlreadyExistsException());

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.registerUser(requestDouble, userIdDouble);

            // Assert
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ================================================================
    // POST /users — 403 FORBIDDEN
    // ================================================================

    @Test
    void registerUser_shouldReturnForbidden_whenAccessDeniedException() throws Exception {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId userIdDouble = mock(UserId.class);

        when(requestDouble.username()).thenReturn("newuser");
        when(requestDouble.role()).thenReturn("NORMAL_USER");

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toProfile(any())).thenReturn(mock(Profile.class));
            mapperMock.when(() -> UserMapper.toRole(any())).thenReturn(UserRole.NORMAL_USER);
            when(userService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new AccessDeniedException("denied"));

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.registerUser(requestDouble, userIdDouble);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ================================================================
    // POST /users — 500 INTERNAL SERVER ERROR
    // ================================================================

    @Test
    void registerUser_shouldReturnInternalServerError_whenUnexpectedException() throws Exception {

        // Arrange
        RequestUserDTO requestDouble = mock(RequestUserDTO.class);
        UserId userIdDouble = mock(UserId.class);

        when(requestDouble.username()).thenReturn("newuser");
        when(requestDouble.role()).thenReturn("NORMAL_USER");

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toProfile(any())).thenReturn(mock(Profile.class));
            mapperMock.when(() -> UserMapper.toRole(any())).thenReturn(UserRole.NORMAL_USER);
            when(userService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<ResponseUserDTO> response =
                    controller.registerUser(requestDouble, userIdDouble);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ================================================================
    // GET /users/{id} — 200 OK
    // ================================================================

    @Test
    void getUser_shouldReturnOk_whenUserExists() {

        // Arrange
        UserId userIdDouble = mock(UserId.class);
        User userDouble = mock(User.class);
        ResponseUserDTO responseDTO = new ResponseUserDTO("id", "user", "name", "email", "ADMIN");

        when(userService.findByID(userIdDouble)).thenReturn(userDouble);
        when(userLinkProvider.getLinksForUsers(userIdDouble))
                .thenReturn(List.of(Link.of("http://localhost/users").withRel("get-user")));

        try (MockedStatic<UserMapper> mapperMock = mockStatic(UserMapper.class)) {
            mapperMock.when(() -> UserMapper.toDTO(userDouble)).thenReturn(responseDTO);

            // Act
            ResponseEntity<ResponseUserDTO> response = controller.getUser(userIdDouble);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertTrue(response.getBody().hasLink("get-user"));
            assertEquals(responseDTO, response.getBody());
        }
    }

    // ================================================================
    // GET /users/{id} — 404 NOT FOUND
    // ================================================================

    @Test
    void getUser_shouldReturnNotFound_whenUserDoesNotExist() {

        // Arrange
        UserId userIdDouble = mock(UserId.class);

        when(userService.findByID(userIdDouble)).thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<ResponseUserDTO> response = controller.getUser(userIdDouble);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ================================================================
    // GET /users/{id} — 500 INTERNAL SERVER ERROR
    // ================================================================

    @Test
    void getUser_shouldReturnInternalServerError_whenUnexpectedException() {

        // Arrange
        UserId userIdDouble = mock(UserId.class);

        when(userService.findByID(userIdDouble)).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<ResponseUserDTO> response = controller.getUser(userIdDouble);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }
}