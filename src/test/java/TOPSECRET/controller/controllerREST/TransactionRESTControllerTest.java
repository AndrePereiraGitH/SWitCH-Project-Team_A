package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.TransactionLinkProvider;
import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.transaction.TransactionMapper;
import TOPSECRET.dto.transaction.request.TransactionRequestDTO;
import TOPSECRET.dto.transaction.response.TransactionResponseDTO;
import TOPSECRET.services.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @InjectMocks
    private TransactionRESTController controller;

    // =========================================================================
    // createTransaction
    // =========================================================================

    @Test
    void createTransaction_shouldReturnCreatedStatus_whenTransactionIsCreated() {

        // Arrange
        TransactionRequestDTO request = validRequest();
        Transaction transaction       = transactionDouble();
        TransactionResponseDTO dto    = mock(TransactionResponseDTO.class);

        when(transactionService.createAndSave(any(), any(), any(), any(), any(), any()))
                .thenReturn(transaction);

        publicLinks();
        buyerLinks(transaction, dto);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            stubMapper(mapper, request, transaction, dto);

            // Act
            ResponseEntity<TransactionResponseDTO> response =
                    controller.createTransaction(request);

            // Assert
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }
    }

    @Test
    void createTransaction_shouldReturnCreatedBody_whenTransactionIsCreated() {

        // Arrange
        TransactionRequestDTO request = validRequest();
        Transaction transaction       = transactionDouble();
        TransactionResponseDTO dto    = mock(TransactionResponseDTO.class);

        when(transactionService.createAndSave(any(), any(), any(), any(), any(), any()))
                .thenReturn(transaction);

        publicLinks();
        buyerLinks(transaction, dto);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            stubMapper(mapper, request, transaction, dto);

            // Act
            ResponseEntity<TransactionResponseDTO> response =
                    controller.createTransaction(request);

            // Assert
            assertSame(dto, response.getBody());
        }
    }

    @Test
    void createTransaction_shouldReturnBadRequest_whenMapperThrowsIllegalArgumentException() {

        // Arrange
        TransactionRequestDTO request = mock(TransactionRequestDTO.class);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any()))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<TransactionResponseDTO> response =
                    controller.createTransaction(request);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void createTransaction_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {

        // Arrange
        TransactionRequestDTO request = mock(TransactionRequestDTO.class);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(any()))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<TransactionResponseDTO> response =
                    controller.createTransaction(request);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================================
    // findById
    // =========================================================================

    @Test
    void findById_shouldReturnOkStatus_whenTransactionExists() {

        // Arrange
        Transaction transaction    = transactionDouble();
        TransactionResponseDTO dto = mock(TransactionResponseDTO.class);

        publicLinks();
        buyerLinks(transaction, dto);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toTransactionId("tx-1"))
                    .thenReturn(mock(TransactionID.class));
            mapper.when(() -> TransactionMapper.toResponseDTO(transaction))
                    .thenReturn(dto);

            when(transactionService.findById(any())).thenReturn(transaction);

            // Act
            ResponseEntity<TransactionResponseDTO> response = controller.findById("tx-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void findById_shouldReturnDtoBody_whenTransactionExists() {

        // Arrange
        Transaction transaction    = transactionDouble();
        TransactionResponseDTO dto = mock(TransactionResponseDTO.class);

        publicLinks();
        buyerLinks(transaction, dto);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toTransactionId("tx-1"))
                    .thenReturn(mock(TransactionID.class));
            mapper.when(() -> TransactionMapper.toResponseDTO(transaction))
                    .thenReturn(dto);

            when(transactionService.findById(any())).thenReturn(transaction);

            // Act
            ResponseEntity<TransactionResponseDTO> response = controller.findById("tx-1");

            // Assert
            assertSame(dto, response.getBody());
        }
    }

    @Test
    void findById_shouldReturnNotFound_whenTransactionDoesNotExist() {

        // Arrange
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toTransactionId("tx-1"))
                    .thenReturn(mock(TransactionID.class));

            when(transactionService.findById(any()))
                    .thenThrow(new EntityNotFoundException());

            // Act
            ResponseEntity<TransactionResponseDTO> response = controller.findById("tx-1");

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void findById_shouldReturnBadRequest_whenIdIsInvalid() {

        // Arrange
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toTransactionId(""))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<TransactionResponseDTO> response = controller.findById("");

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void findById_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {

        // Arrange
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toTransactionId("tx-1"))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<TransactionResponseDTO> response = controller.findById("tx-1");

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================================
    // findAllByBuyerId
    // =========================================================================

    @Test
    void findAllByBuyerId_shouldReturnOkStatus_whenBuyerHasTransactions() {

        // Arrange
        Transaction transaction    = transactionDouble();
        TransactionResponseDTO dto = mock(TransactionResponseDTO.class);

        publicLinks();
        buyerLinks(transaction, dto);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId("buyer-1"))
                    .thenReturn(mock(UserId.class));
            mapper.when(() -> TransactionMapper.toResponseDTO(transaction))
                    .thenReturn(dto);

            when(transactionService.findAllByBuyerId(any()))
                    .thenReturn(List.of(transaction));

            // Act
            ResponseEntity<List<TransactionResponseDTO>> response =
                    controller.findAllByBuyerId("buyer-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void findAllByBuyerId_shouldReturnOneDtoInBody_whenBuyerHasOneTransaction() {

        // Arrange
        Transaction transaction    = transactionDouble();
        TransactionResponseDTO dto = mock(TransactionResponseDTO.class);

        publicLinks();
        buyerLinks(transaction, dto);

        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId("buyer-1"))
                    .thenReturn(mock(UserId.class));
            mapper.when(() -> TransactionMapper.toResponseDTO(transaction))
                    .thenReturn(dto);

            when(transactionService.findAllByBuyerId(any()))
                    .thenReturn(List.of(transaction));

            // Act
            ResponseEntity<List<TransactionResponseDTO>> response =
                    controller.findAllByBuyerId("buyer-1");

            // Assert
            assertEquals(1, response.getBody().size());
            assertSame(dto, response.getBody().get(0));
        }
    }

    @Test
    void findAllByBuyerId_shouldReturnOkStatus_whenBuyerHasNoTransactions() {

        // Arrange
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId("buyer-1"))
                    .thenReturn(mock(UserId.class));

            when(transactionService.findAllByBuyerId(any()))
                    .thenReturn(List.of());

            // Act
            ResponseEntity<List<TransactionResponseDTO>> response =
                    controller.findAllByBuyerId("buyer-1");

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void findAllByBuyerId_shouldReturnEmptyBody_whenBuyerHasNoTransactions() {

        // Arrange
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId("buyer-1"))
                    .thenReturn(mock(UserId.class));

            when(transactionService.findAllByBuyerId(any()))
                    .thenReturn(List.of());

            // Act
            ResponseEntity<List<TransactionResponseDTO>> response =
                    controller.findAllByBuyerId("buyer-1");

            // Assert
            assertTrue(response.getBody().isEmpty());
        }
    }

    @Test
    void findAllByBuyerId_shouldReturnBadRequest_whenBuyerIdIsInvalid() {

        // Arrange
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId(""))
                    .thenThrow(new IllegalArgumentException());

            // Act
            ResponseEntity<List<TransactionResponseDTO>> response =
                    controller.findAllByBuyerId("");

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void findAllByBuyerId_shouldReturnInternalServerError_whenUnexpectedExceptionOccurs() {

        // Arrange
        try (MockedStatic<TransactionMapper> mapper = mockStatic(TransactionMapper.class)) {
            mapper.when(() -> TransactionMapper.toUserId("buyer-1"))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<List<TransactionResponseDTO>> response =
                    controller.findAllByBuyerId("buyer-1");

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private Transaction transactionDouble() {
        Transaction transaction = mock(Transaction.class);
        TransactionID id        = mock(TransactionID.class);
        UserId buyerId          = mock(UserId.class);

        when(transaction.id()).thenReturn(id);
        when(id.getId()).thenReturn("tx-1");
        when(transaction.getBuyerId()).thenReturn(buyerId);
        when(buyerId.getUserId()).thenReturn("buyer-1");

        return transaction;
    }

    private TransactionRequestDTO validRequest() {
        TransactionRequestDTO request = mock(TransactionRequestDTO.class);

        when(request.getBuyerId()).thenReturn("buyer-1");
        when(request.getPrice()).thenReturn("10.00");
        when(request.getTransactionDate()).thenReturn(LocalDateTime.now().minusMinutes(1).toString());
        when(request.getStreet()).thenReturn("Rua das Flores");
        when(request.getNumber()).thenReturn("10");
        when(request.getZipCode()).thenReturn("4000-001");
        when(request.getCityId()).thenReturn("porto");
        when(request.getCardHolderName()).thenReturn("John Doe");
        when(request.getMaskedCardNumber()).thenReturn("**** **** **** 1234");
        when(request.getExpiryMonth()).thenReturn("09");
        when(request.getExpiryYear()).thenReturn("2027");
        when(request.getListingIds()).thenReturn(List.of("listing-1"));

        return request;
    }

    private void stubMapper(
            MockedStatic<TransactionMapper> mapper,
            TransactionRequestDTO request,
            Transaction transaction,
            TransactionResponseDTO dto) {

        mapper.when(() -> TransactionMapper.toUserId(request.getBuyerId()))
                .thenReturn(mock(UserId.class));
        mapper.when(() -> TransactionMapper.toPrice(request.getPrice()))
                .thenReturn(mock(Price.class));
        mapper.when(() -> TransactionMapper.toTransactionDate(request.getTransactionDate()))
                .thenReturn(LocalDateTime.now().minusMinutes(1));
        mapper.when(() -> TransactionMapper.toAddress(any(), any(), any(), any()))
                .thenReturn(mock(Address.class));
        mapper.when(() -> TransactionMapper.toPaymentDetails(any(), any(), any(), any()))
                .thenReturn(mock(PaymentDetails.class));
        mapper.when(() -> TransactionMapper.toListingIds(any()))
                .thenReturn(List.of(mock(ListingId.class)));
        mapper.when(() -> TransactionMapper.toResponseDTO(transaction))
                .thenReturn(dto);
    }

    private void publicLinks() {
        when(transactionLinkProvider.getPublicLinks())
                .thenReturn(List.of(Link.of("/transactions", "create-transaction")));
    }

    private void buyerLinks(Transaction transaction, TransactionResponseDTO dto) {
        when(transactionLinkProvider.getBuyerLinks(transaction.getBuyerId()))
                .thenReturn(List.of(Link.of("/transactions/buyer/buyer-1", "my-transactions")));
        when(dto.add(any(Link.class))).thenReturn(dto);
    }
}