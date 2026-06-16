package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.TransactionLinkProvider;
import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.transaction.TransactionMapper;
import TOPSECRET.dto.transaction.request.TransactionRequestDTO;
import TOPSECRET.dto.transaction.response.TransactionResponseDTO;
import TOPSECRET.services.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TransactionRESTController}.
 *
 * <p>Verifies the REST endpoints for creating and retrieving transactions,
 * ensuring proper HTTP status codes, correct response bodies, and appropriate
 * exception handling.</p>
 */
@ExtendWith(MockitoExtension.class)
class TransactionRESTControllerTest {

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionLinkProvider transactionLinkProvider;

    private TransactionRESTController sut;

    @BeforeEach
    void setUp() {
        sut = new TransactionRESTController(transactionService, transactionLinkProvider);
    }

    // =========================================================================
    // createTransaction
    // =========================================================================

    @Test
    void createTransaction_shouldReturnCreatedStatus_whenTransactionIsCreated() throws InstanceNotFoundException {

        // ARRANGE
        TransactionRequestDTO request = mock(TransactionRequestDTO.class);
        Transaction transaction       = mock(Transaction.class);
        TransactionID id              = mock(TransactionID.class);
        TransactionResponseDTO dto    = mock(TransactionResponseDTO.class);

        when(transaction.id()).thenReturn(id);
        when(id.getId()).thenReturn("tx-1");
        when(transactionService.createAndSave(any(), any(), any(), any(), any(), any()))
                .thenReturn(transaction);
        when(transactionLinkProvider.getLinks(any())).thenReturn(List.of());
        when(dto.add(any(Link.class))).thenReturn(dto);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            mapper.when(() -> TransactionMapper.toPrice(any())).thenReturn(mock(Price.class));
            mapper.when(() -> TransactionMapper.toTransactionDate(any())).thenReturn(LocalDateTime.now());
            mapper.when(() -> TransactionMapper.toAddress(any(), any(), any(), any())).thenReturn(mock(Address.class));
            mapper.when(() -> TransactionMapper.toPaymentDetails(any(), any(), any(), any())).thenReturn(mock(PaymentDetails.class));
            mapper.when(() -> TransactionMapper.toListingIds(any())).thenReturn(List.of());
            mapper.when(() -> TransactionMapper.toResponseDTO(transaction)).thenReturn(dto);

            // ACT
            ResponseEntity<TransactionResponseDTO> response =
                    sut.createTransaction(request, "buyer-1");

            // ASSERT
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }

    @Test
    void createTransaction_shouldReturnCreatedBody_whenTransactionIsCreated() throws InstanceNotFoundException {

        // ARRANGE
        TransactionRequestDTO request = mock(TransactionRequestDTO.class);
        Transaction transaction       = mock(Transaction.class);
        TransactionID id              = mock(TransactionID.class);
        TransactionResponseDTO dto    = mock(TransactionResponseDTO.class);

        when(transaction.id()).thenReturn(id);
        when(id.getId()).thenReturn("tx-1");
        when(transactionService.createAndSave(any(), any(), any(), any(), any(), any()))
                .thenReturn(transaction);
        when(transactionLinkProvider.getLinks(any())).thenReturn(List.of());
        when(dto.add(any(Link.class))).thenReturn(dto);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            mapper.when(() -> TransactionMapper.toPrice(any())).thenReturn(mock(Price.class));
            mapper.when(() -> TransactionMapper.toTransactionDate(any())).thenReturn(LocalDateTime.now());
            mapper.when(() -> TransactionMapper.toAddress(any(), any(), any(), any())).thenReturn(mock(Address.class));
            mapper.when(() -> TransactionMapper.toPaymentDetails(any(), any(), any(), any())).thenReturn(mock(PaymentDetails.class));
            mapper.when(() -> TransactionMapper.toListingIds(any())).thenReturn(List.of());
            mapper.when(() -> TransactionMapper.toResponseDTO(transaction)).thenReturn(dto);

            // ACT
            ResponseEntity<TransactionResponseDTO> response =
                    sut.createTransaction(request, "buyer-1");

            // ASSERT
            assertSame(dto, response.getBody());
        }
    }

    @Test
    void createTransaction_shouldReturnForbidden_whenAccessIsDenied() {

        // ARRANGE
        TransactionRequestDTO request = mock(TransactionRequestDTO.class);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            mapper.when(() -> TransactionMapper.toPrice(any())).thenReturn(mock(Price.class));
            mapper.when(() -> TransactionMapper.toTransactionDate(any())).thenReturn(LocalDateTime.now());
            mapper.when(() -> TransactionMapper.toAddress(any(), any(), any(), any())).thenReturn(mock(Address.class));
            mapper.when(() -> TransactionMapper.toPaymentDetails(any(), any(), any(), any())).thenReturn(mock(PaymentDetails.class));
            mapper.when(() -> TransactionMapper.toListingIds(any())).thenReturn(List.of());
            when(transactionService.createAndSave(any(), any(), any(), any(), any(), any()))
                    .thenThrow(new AccessDeniedException("forbidden"));

            // ACT
            ResponseEntity<TransactionResponseDTO> response =
                    sut.createTransaction(request, "buyer-1");

            // ASSERT
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Test
    void createTransaction_shouldReturnNotFound_whenUserDoesNotExist() {

        // ARRANGE
        TransactionRequestDTO request = mock(TransactionRequestDTO.class);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            mapper.when(() -> TransactionMapper.toPrice(any())).thenReturn(mock(Price.class));
            mapper.when(() -> TransactionMapper.toTransactionDate(any())).thenReturn(LocalDateTime.now());
            mapper.when(() -> TransactionMapper.toAddress(any(), any(), any(), any())).thenReturn(mock(Address.class));
            mapper.when(() -> TransactionMapper.toPaymentDetails(any(), any(), any(), any())).thenReturn(mock(PaymentDetails.class));
            mapper.when(() -> TransactionMapper.toListingIds(any())).thenReturn(List.of());
            when(transactionService.createAndSave(any(), any(), any(), any(), any(), any()))
                    .thenThrow(new EntityNotFoundException("not found"));

            // ACT
            ResponseEntity<TransactionResponseDTO> response =
                    sut.createTransaction(request, "buyer-1");

            // ASSERT
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void createTransaction_shouldReturnBadRequest_whenMapperThrowsIllegalArgumentException() {

        // ARRANGE
        TransactionRequestDTO request = mock(TransactionRequestDTO.class);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any()))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<TransactionResponseDTO> response =
                    sut.createTransaction(request, "buyer-1");

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void createTransaction_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {

        // ARRANGE
        TransactionRequestDTO request = mock(TransactionRequestDTO.class);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any()))
                    .thenThrow(new RuntimeException());

            // ACT
            ResponseEntity<TransactionResponseDTO> response =
                    sut.createTransaction(request, "buyer-1");

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================================
    // findById
    // =========================================================================

    @Test
    void findById_shouldReturnOkStatus_whenRequesterIsOwner() throws InstanceNotFoundException {

        // ARRANGE
        Transaction transaction    = mock(Transaction.class);
        TransactionID id           = mock(TransactionID.class);
        TransactionResponseDTO dto = mock(TransactionResponseDTO.class);

        when(transaction.id()).thenReturn(id);
        when(id.getId()).thenReturn("tx-1");
        when(transactionService.findById(any(), any())).thenReturn(transaction);
        when(transactionLinkProvider.getLinks(any())).thenReturn(List.of());
        when(dto.add(any(Link.class))).thenReturn(dto);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toTransactionId(any())).thenReturn(mock(TransactionID.class));
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            mapper.when(() -> TransactionMapper.toResponseDTO(transaction)).thenReturn(dto);

            // ACT
            ResponseEntity<TransactionResponseDTO> response =
                    sut.findById("tx-1", "buyer-1");

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void findById_shouldReturnDtoBody_whenRequesterIsOwner() throws InstanceNotFoundException {

        // ARRANGE
        Transaction transaction    = mock(Transaction.class);
        TransactionID id           = mock(TransactionID.class);
        TransactionResponseDTO dto = mock(TransactionResponseDTO.class);

        when(transaction.id()).thenReturn(id);
        when(id.getId()).thenReturn("tx-1");
        when(transactionService.findById(any(), any())).thenReturn(transaction);
        when(transactionLinkProvider.getLinks(any())).thenReturn(List.of());
        when(dto.add(any(Link.class))).thenReturn(dto);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toTransactionId(any())).thenReturn(mock(TransactionID.class));
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            mapper.when(() -> TransactionMapper.toResponseDTO(transaction)).thenReturn(dto);

            // ACT
            ResponseEntity<TransactionResponseDTO> response =
                    sut.findById("tx-1", "buyer-1");

            // ASSERT
            assertSame(dto, response.getBody());
        }
    }

    @Test
    void findById_shouldReturnForbidden_whenRequesterIsNotOwnerNorAdmin() {

        // ARRANGE
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toTransactionId(any())).thenReturn(mock(TransactionID.class));
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(transactionService.findById(any(), any()))
                    .thenThrow(new AccessDeniedException("forbidden"));

            // ACT
            ResponseEntity<TransactionResponseDTO> response =
                    sut.findById("tx-1", "other-user");

            // ASSERT
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Test
    void findById_shouldReturnNotFound_whenTransactionDoesNotExist() {

        // ARRANGE
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toTransactionId(any())).thenReturn(mock(TransactionID.class));
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(transactionService.findById(any(), any()))
                    .thenThrow(new EntityNotFoundException());

            // ACT
            ResponseEntity<TransactionResponseDTO> response =
                    sut.findById("tx-1", "buyer-1");

            // ASSERT
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void findById_shouldReturnBadRequest_whenIdIsInvalid() {

        // ARRANGE
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toTransactionId(any()))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<TransactionResponseDTO> response =
                    sut.findById("", "buyer-1");

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void findById_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {

        // ARRANGE
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toTransactionId(any()))
                    .thenThrow(new RuntimeException());

            // ACT
            ResponseEntity<TransactionResponseDTO> response =
                    sut.findById("tx-1", "buyer-1");

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================================
    // findAll
    // =========================================================================

    @Test
    void findAll_shouldReturnOkStatus_whenRequesterIsAdmin() throws InstanceNotFoundException {

        // ARRANGE
        Transaction transaction    = mock(Transaction.class);
        TransactionID id           = mock(TransactionID.class);
        TransactionResponseDTO dto = mock(TransactionResponseDTO.class);

        when(transaction.id()).thenReturn(id);
        when(id.getId()).thenReturn("tx-1");
        when(transactionService.findAll(any())).thenReturn(List.of(transaction));
        when(transactionLinkProvider.getLinks(any())).thenReturn(List.of());
        when(dto.add(any(Link.class))).thenReturn(dto);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            mapper.when(() -> TransactionMapper.toResponseDTO(transaction)).thenReturn(dto);

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAll("admin-1");

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void findAll_shouldReturnDtoList_whenRequesterIsAdmin() throws InstanceNotFoundException {

        // ARRANGE
        Transaction transaction    = mock(Transaction.class);
        TransactionID id           = mock(TransactionID.class);
        TransactionResponseDTO dto = mock(TransactionResponseDTO.class);

        when(transaction.id()).thenReturn(id);
        when(id.getId()).thenReturn("tx-1");
        when(transactionService.findAll(any())).thenReturn(List.of(transaction));
        when(transactionLinkProvider.getLinks(any())).thenReturn(List.of());
        when(dto.add(any(Link.class))).thenReturn(dto);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            mapper.when(() -> TransactionMapper.toResponseDTO(transaction)).thenReturn(dto);

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAll("admin-1");

            // ASSERT
            assertSame(dto, response.getBody().get(0));
        }
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoTransactionsExist() {

        // ARRANGE
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(transactionService.findAll(any())).thenReturn(List.of());

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAll("admin-1");

            // ASSERT
            assertTrue(response.getBody().isEmpty());
        }
    }

    @Test
    void findAll_shouldReturnForbidden_whenRequesterIsNotAdmin() {

        // ARRANGE
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(transactionService.findAll(any()))
                    .thenThrow(new AccessDeniedException("forbidden"));

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAll("user-1");

            // ASSERT
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Test
    void findAll_shouldReturnNotFound_whenRequesterDoesNotExist() {

        // ARRANGE
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(transactionService.findAll(any()))
                    .thenThrow(new EntityNotFoundException());

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAll("unknown");

            // ASSERT
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void findAll_shouldReturnBadRequest_whenRequesterIdIsInvalid() {

        // ARRANGE
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any()))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAll("");

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void findAll_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {

        // ARRANGE
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any()))
                    .thenThrow(new RuntimeException());

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAll("admin-1");

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================================
    // findAllByBuyerId
    // =========================================================================

    @Test
    void findAllByBuyerId_shouldReturnOkStatus_whenBuyerHasTransactions() throws InstanceNotFoundException {

        // ARRANGE
        Transaction transaction    = mock(Transaction.class);
        TransactionID id           = mock(TransactionID.class);
        TransactionResponseDTO dto = mock(TransactionResponseDTO.class);

        when(transaction.id()).thenReturn(id);
        when(id.getId()).thenReturn("tx-1");
        when(transactionService.findAllByBuyerId(any(), any())).thenReturn(List.of(transaction));
        when(transactionLinkProvider.getLinks(any())).thenReturn(List.of());
        when(dto.add(any(Link.class))).thenReturn(dto);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            mapper.when(() -> TransactionMapper.toResponseDTO(transaction)).thenReturn(dto);

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAllByBuyerId("buyer-1", "buyer-1");

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void findAllByBuyerId_shouldReturnOneDtoInBody_whenBuyerHasOneTransaction() throws InstanceNotFoundException {

        // ARRANGE
        Transaction transaction    = mock(Transaction.class);
        TransactionID id           = mock(TransactionID.class);
        TransactionResponseDTO dto = mock(TransactionResponseDTO.class);

        when(transaction.id()).thenReturn(id);
        when(id.getId()).thenReturn("tx-1");
        when(transactionService.findAllByBuyerId(any(), any())).thenReturn(List.of(transaction));
        when(transactionLinkProvider.getLinks(any())).thenReturn(List.of());
        when(dto.add(any(Link.class))).thenReturn(dto);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            mapper.when(() -> TransactionMapper.toResponseDTO(transaction)).thenReturn(dto);

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAllByBuyerId("buyer-1", "buyer-1");

            // ASSERT
            assertEquals(1, response.getBody().size());
        }
    }

    @Test
    void findAllByBuyerId_shouldReturnExpectedDto_whenBuyerHasOneTransaction() throws InstanceNotFoundException {

        // ARRANGE
        Transaction transaction    = mock(Transaction.class);
        TransactionID id           = mock(TransactionID.class);
        TransactionResponseDTO dto = mock(TransactionResponseDTO.class);

        when(transaction.id()).thenReturn(id);
        when(id.getId()).thenReturn("tx-1");
        when(transactionService.findAllByBuyerId(any(), any())).thenReturn(List.of(transaction));
        when(transactionLinkProvider.getLinks(any())).thenReturn(List.of());
        when(dto.add(any(Link.class))).thenReturn(dto);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            mapper.when(() -> TransactionMapper.toResponseDTO(transaction)).thenReturn(dto);

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAllByBuyerId("buyer-1", "buyer-1");

            // ASSERT
            assertSame(dto, response.getBody().get(0));
        }
    }

    @Test
    void findAllByBuyerId_shouldReturnOkStatus_whenBuyerHasNoTransactions() {

        // ARRANGE
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(transactionService.findAllByBuyerId(any(), any())).thenReturn(List.of());

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAllByBuyerId("buyer-1", "buyer-1");

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void findAllByBuyerId_shouldReturnEmptyBody_whenBuyerHasNoTransactions() {

        // ARRANGE
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(transactionService.findAllByBuyerId(any(), any())).thenReturn(List.of());

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAllByBuyerId("buyer-1", "buyer-1");

            // ASSERT
            assertTrue(response.getBody().isEmpty());
        }
    }

    @Test
    void findAllByBuyerId_shouldReturnForbidden_whenRequesterIsNotOwnerNorAdmin() {

        // ARRANGE
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(transactionService.findAllByBuyerId(any(), any()))
                    .thenThrow(new AccessDeniedException("forbidden"));

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAllByBuyerId("buyer-1", "other-user");

            // ASSERT
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Test
    void findAllByBuyerId_shouldReturnNotFound_whenRequesterDoesNotExist() {

        // ARRANGE
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any())).thenReturn(mock(UserId.class));
            when(transactionService.findAllByBuyerId(any(), any()))
                    .thenThrow(new EntityNotFoundException());

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAllByBuyerId("buyer-1", "buyer-1");

            // ASSERT
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void findAllByBuyerId_shouldReturnBadRequest_whenBuyerIdIsInvalid() {

        // ARRANGE
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any()))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAllByBuyerId("", "buyer-1");

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void findAllByBuyerId_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {

        // ARRANGE
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any()))
                    .thenThrow(new RuntimeException());

            // ACT
            ResponseEntity<List<TransactionResponseDTO>> response =
                    sut.findAllByBuyerId("buyer-1", "buyer-1");

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }
}