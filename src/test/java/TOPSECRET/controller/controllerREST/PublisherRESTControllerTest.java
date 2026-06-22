package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.PublisherLinkProvider;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.publisher.PublisherMapper;
import TOPSECRET.dto.publisher.request.RequestPublisherDTO;
import TOPSECRET.dto.publisher.response.ResponsePublisherDTO;
import TOPSECRET.dto.user.UserMapper;
import TOPSECRET.services.PublisherService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpMethod;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class PublisherRESTControllerTest {

    @Mock
    private PublisherLinkProvider publisherLinkProvider;

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private PublisherRESTController controller;

    // ==========================================================
    // CREATE - SUCCESS
    // ==========================================================

    @Test
    void createPublisher_shouldReturnCreated_onSuccess() throws InstanceNotFoundException {

        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);
        UserId userId = mock(UserId.class);
        Publisher publisher = mock(Publisher.class);
        PublisherID publisherID = mock(PublisherID.class);
        Link providerLink = mock(Link.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");
        when(publisher.id()).thenReturn(publisherID);
        when(publisherService.createAndSave(any(), any(), any(), any())).thenReturn(publisher);
        when(publisherLinkProvider.getLinks(userId)).thenReturn(List.of(providerLink));

        try (MockedStatic<PublisherMapper> publisherMapper = mockStatic(PublisherMapper.class);
             MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class)) {

            ResponsePublisherDTO dto = mock(ResponsePublisherDTO.class);
            List<Link> captured = new ArrayList<>();
            doAnswer(inv -> { captured.add(inv.getArgument(0)); return dto; }).when(dto).add(any(Link.class));

            userMapper.when(() -> UserMapper.toUserId(any())).thenReturn(userId);
            publisherMapper.when(() -> PublisherMapper.toContact(any())).thenReturn(mock(Contact.class));
            publisherMapper.when(() -> PublisherMapper.toDTO(publisher)).thenReturn(dto);

            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, "user-1");

            assertEquals(CREATED, response.getStatusCode());
            assertEquals(dto, response.getBody());
            assertFalse(captured.isEmpty());
            assertTrue(captured.contains(providerLink));
        }
    }

    // ==========================================================
    // CREATE - BAD REQUEST
    // ==========================================================

    @Test
    void createPublisher_shouldReturnBadRequest_onIllegalArgumentException() throws InstanceNotFoundException {

        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");

        try (MockedStatic<PublisherMapper> publisherMapper = mockStatic(PublisherMapper.class);
             MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class)) {

            userMapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            publisherMapper.when(() -> PublisherMapper.toContact(any())).thenReturn(mock(Contact.class));
            when(publisherService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, "user-1");

            assertEquals(BAD_REQUEST, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
    // CREATE - FORBIDDEN
    // ==========================================================

    @Test
    void createPublisher_shouldReturnForbidden_onAccessDeniedException() throws InstanceNotFoundException {

        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");

        try (MockedStatic<PublisherMapper> publisherMapper = mockStatic(PublisherMapper.class);
             MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class)) {

            userMapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            publisherMapper.when(() -> PublisherMapper.toContact(any())).thenReturn(mock(Contact.class));
            when(publisherService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new AccessDeniedException("Denied"));

            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, "user-1");

            assertEquals(FORBIDDEN, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
    // CREATE - INTERNAL ERROR
    // ==========================================================

    @Test
    void createPublisher_shouldReturnInternalServerError_onException() throws InstanceNotFoundException {

        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");

        try (MockedStatic<PublisherMapper> publisherMapper = mockStatic(PublisherMapper.class);
             MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class)) {

            userMapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            publisherMapper.when(() -> PublisherMapper.toContact(any())).thenReturn(mock(Contact.class));
            when(publisherService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new RuntimeException());

            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, "user-1");

            assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
    // CREATE - NOT FOUND
    // ==========================================================

    @Test
    void createPublisher_shouldReturnNotFound_whenUserDoesNotExist() throws InstanceNotFoundException {

        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);
        Publisher publisher = mock(Publisher.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");
        when(publisherService.createAndSave(any(), any(), any(), any())).thenReturn(publisher);
        when(publisherLinkProvider.getLinks(any(UserId.class)))
                .thenThrow(new InstanceNotFoundException("User not found"));

        try (MockedStatic<PublisherMapper> publisherMapper = mockStatic(PublisherMapper.class);
             MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class)) {

            userMapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));
            publisherMapper.when(() -> PublisherMapper.toContact(any())).thenReturn(mock(Contact.class));
            publisherMapper.when(() -> PublisherMapper.toDTO(any())).thenReturn(mock(ResponsePublisherDTO.class));

            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, "user-1");

            assertEquals(NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
    // GET ALL - SUCCESS
    // ==========================================================

    @Test
    void getAllPublishers_shouldReturnOk_whenPublishersExist() {

        Publisher publisher = mock(Publisher.class);
        PublisherID id = mock(PublisherID.class);
        ResponsePublisherDTO dto = mock(ResponsePublisherDTO.class);

        when(publisher.id()).thenReturn(id);
        when(publisherService.findAll()).thenReturn(List.of(publisher));

        try (MockedStatic<PublisherMapper> mapper = mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toDTO(any())).thenReturn(dto);

            ResponseEntity<List<ResponsePublisherDTO>> response = controller.getAllPublishers();

            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertEquals(dto, response.getBody().get(0));
        }
    }

    // ==========================================================
    // GET ALL - EMPTY
    // ==========================================================

    @Test
    void getAllPublishers_shouldReturnOK_whenListEmpty() {

        when(publisherService.findAll()).thenReturn(List.of());

        ResponseEntity<List<ResponsePublisherDTO>> response = controller.getAllPublishers();

        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    // ==========================================================
    // GET ALL - BAD REQUEST
    // ==========================================================

    @Test
    void getAllPublishers_shouldReturnBadRequest_onIllegalArgumentException() {

        when(publisherService.findAll()).thenThrow(new IllegalArgumentException());

        ResponseEntity<List<ResponsePublisherDTO>> response = controller.getAllPublishers();

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ==========================================================
    // GET ALL - INTERNAL ERROR
    // ==========================================================

    @Test
    void getAllPublishers_shouldReturnInternalServerError_onException() {

        when(publisherService.findAll()).thenThrow(new RuntimeException());

        ResponseEntity<List<ResponsePublisherDTO>> response = controller.getAllPublishers();

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ==========================================================
    // GET BY ID - SUCCESS
    // ==========================================================

    @Test
    void getPublisher_shouldReturnOk_whenFound() {

        PublisherID id = mock(PublisherID.class);
        Publisher publisher = mock(Publisher.class);
        ResponsePublisherDTO dto = mock(ResponsePublisherDTO.class);

        when(publisherService.findById(id)).thenReturn(publisher);

        try (MockedStatic<PublisherMapper> mapper = mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toPublisherId(any())).thenReturn(id);
            mapper.when(() -> PublisherMapper.toDTO(any())).thenReturn(dto);

            ResponseEntity<ResponsePublisherDTO> response = controller.getPublisher("pub-1");

            assertEquals(OK, response.getStatusCode());
            assertEquals(dto, response.getBody());
        }
    }

    // ==========================================================
    // GET BY ID - BAD REQUEST
    // ==========================================================

    @Test
    void getPublisher_shouldReturnBadRequest_whenIdFormatIsInvalid() {

        try (MockedStatic<PublisherMapper> mapper = mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toPublisherId(any()))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<ResponsePublisherDTO> response = controller.getPublisher("invalid-id");

            assertEquals(BAD_REQUEST, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
    // GET BY ID - NOT FOUND
    // ==========================================================

    @Test
    void getPublisher_shouldReturnNotFound_whenMissing() {

        PublisherID id = mock(PublisherID.class);

        when(publisherService.findById(id)).thenThrow(new EntityNotFoundException());

        try (MockedStatic<PublisherMapper> mapper = mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toPublisherId(any())).thenReturn(id);

            ResponseEntity<ResponsePublisherDTO> response = controller.getPublisher("pub-1");

            assertEquals(NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
    // GET BY ID - INTERNAL ERROR
    // ==========================================================

    @Test
    void getPublisher_shouldReturnInternalServerError_onException() {

        PublisherID id = mock(PublisherID.class);

        when(publisherService.findById(id)).thenThrow(new RuntimeException());

        try (MockedStatic<PublisherMapper> mapper = mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toPublisherId(any())).thenReturn(id);

            ResponseEntity<ResponsePublisherDTO> response = controller.getPublisher("pub-1");

            assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
// OPTIONS - /publishers
// ==========================================================

    @Test
    void optionsPublishers_shouldReturnGetAndPost_whenAdmin() throws InstanceNotFoundException {

        // ARRANGE
        when(publisherService.canCreate(any(UserId.class))).thenReturn(true);

        try (MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class)) {
            userMapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));

            // ACT
            ResponseEntity<?> response = controller.optionsPublishers("admin-1");

            // ASSERT
            assertEquals(OK, response.getStatusCode());
            assertTrue(response.getHeaders().getAllow().contains(HttpMethod.GET));
            assertTrue(response.getHeaders().getAllow().contains(HttpMethod.POST));
            assertTrue(response.getHeaders().getAllow().contains(HttpMethod.OPTIONS));
        }
    }

    @Test
    void optionsPublishers_shouldReturnGetOnly_whenNormalUser() throws InstanceNotFoundException {

        // ARRANGE
        when(publisherService.canCreate(any(UserId.class))).thenReturn(false);

        try (MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class)) {
            userMapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));

            // ACT
            ResponseEntity<?> response = controller.optionsPublishers("user-1");

            // ASSERT
            assertEquals(OK, response.getStatusCode());
            assertTrue(response.getHeaders().getAllow().contains(HttpMethod.GET));
            assertTrue(response.getHeaders().getAllow().contains(HttpMethod.OPTIONS));
            assertFalse(response.getHeaders().getAllow().contains(HttpMethod.POST));
        }
    }

    @Test
    void optionsPublishers_shouldReturnNotFound_whenUserDoesNotExist() throws InstanceNotFoundException {

        // ARRANGE
        when(publisherService.canCreate(any(UserId.class)))
                .thenThrow(new InstanceNotFoundException("User not found"));

        try (MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class)) {
            userMapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));

            // ACT
            ResponseEntity<?> response = controller.optionsPublishers("user-1");

            // ASSERT
            assertEquals(NOT_FOUND, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    void optionsPublishers_shouldReturnBadRequest_whenIllegalArgument() {

        // ARRANGE
        try (MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class)) {
            userMapper.when(() -> UserMapper.toUserId(any()))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<?> response = controller.optionsPublishers("invalid");

            // ASSERT
            assertEquals(BAD_REQUEST, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    void optionsPublishers_shouldReturnInternalError_whenException() throws InstanceNotFoundException {

        // ARRANGE
        when(publisherService.canCreate(any(UserId.class)))
                .thenThrow(new RuntimeException());

        try (MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class)) {
            userMapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));

            // ACT
            ResponseEntity<?> response = controller.optionsPublishers("user-1");

            // ASSERT
            assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    void optionsPublishers_shouldNeverAllowDelete() throws InstanceNotFoundException {

        // ARRANGE
        when(publisherService.canCreate(any(UserId.class))).thenReturn(true);

        try (MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class)) {
            userMapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));

            // ACT
            ResponseEntity<?> response = controller.optionsPublishers("admin-1");

            // ASSERT
            assertFalse(response.getHeaders().getAllow().contains(HttpMethod.DELETE));
        }
    }

    @Test
    void optionsPublishers_shouldNeverAllowPut() throws InstanceNotFoundException {

        // ARRANGE
        when(publisherService.canCreate(any(UserId.class))).thenReturn(true);

        try (MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class)) {
            userMapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));

            // ACT
            ResponseEntity<?> response = controller.optionsPublishers("admin-1");

            // ASSERT
            assertFalse(response.getHeaders().getAllow().contains(HttpMethod.PUT));
        }
    }

    @Test
    void optionsPublishers_shouldNeverAllowPatch() throws InstanceNotFoundException {

        // ARRANGE
        when(publisherService.canCreate(any(UserId.class))).thenReturn(true);

        try (MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class)) {
            userMapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));

            // ACT
            ResponseEntity<?> response = controller.optionsPublishers("admin-1");

            // ASSERT
            assertFalse(response.getHeaders().getAllow().contains(HttpMethod.PATCH));
        }
    }

    @Test
    void optionsPublishers_shouldReturnEmptyBody() throws InstanceNotFoundException {

        // ARRANGE
        when(publisherService.canCreate(any(UserId.class))).thenReturn(false);

        try (MockedStatic<UserMapper> userMapper = mockStatic(UserMapper.class)) {
            userMapper.when(() -> UserMapper.toUserId(any())).thenReturn(mock(UserId.class));

            // ACT
            ResponseEntity<?> response = controller.optionsPublishers("user-1");

            // ASSERT
            assertNull(response.getBody());
        }
    }

// ==========================================================
// OPTIONS - /publishers/{id}
// ==========================================================

    @Test
    void optionsPublisherById_shouldReturnOk_withAllowHeader() {

        // ACT
        ResponseEntity<?> response = controller.optionsPublisherById();

        // ASSERT
        assertEquals(OK, response.getStatusCode());
        assertTrue(response.getHeaders().getAllow().contains(HttpMethod.GET));
        assertTrue(response.getHeaders().getAllow().contains(HttpMethod.OPTIONS));
    }

    @Test
    void optionsPublisherById_shouldReturnEmptyBody() {

        // ACT
        ResponseEntity<?> response = controller.optionsPublisherById();

        // ASSERT
        assertNull(response.getBody());
    }

    @Test
    void optionsPublisherById_shouldNeverAllowPost() {

        // ACT
        ResponseEntity<?> response = controller.optionsPublisherById();

        // ASSERT
        assertFalse(response.getHeaders().getAllow().contains(HttpMethod.POST));
    }

    @Test
    void optionsPublisherById_shouldNeverAllowDelete() {

        // ACT
        ResponseEntity<?> response = controller.optionsPublisherById();

        // ASSERT
        assertFalse(response.getHeaders().getAllow().contains(HttpMethod.DELETE));
    }

    @Test
    void optionsPublisherById_shouldNeverAllowPut() {

        // ACT
        ResponseEntity<?> response = controller.optionsPublisherById();

        // ASSERT
        assertFalse(response.getHeaders().getAllow().contains(HttpMethod.PUT));
    }

    @Test
    void optionsPublisherById_shouldNeverAllowPatch() {

        // ACT
        ResponseEntity<?> response = controller.optionsPublisherById();

        // ASSERT
        assertFalse(response.getHeaders().getAllow().contains(HttpMethod.PATCH));
    }
}