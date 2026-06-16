package TOPSECRET.dto.transaction;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.transaction.response.TransactionResponseDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TransactionMapper}.
 *
 * <p>Verifies the correct mapping of {@link Transaction} domain objects
 * to {@link TransactionResponseDTO}, and the correct conversion of raw
 * strings into strongly-typed domain value objects.</p>
 */
class TransactionMapperTest {

    // =========================================================================
    // toResponseDTO
    // =========================================================================

    @Test
    void toResponseDTO_shouldMapTransactionId() {

        // Arrange
        Transaction transaction = mockTransaction();

        // Act
        TransactionResponseDTO result = TransactionMapper.toResponseDTO(transaction);

        // Assert
        assertEquals("tx-1", result.getTransactionId());
    }

    @Test
    void toResponseDTO_shouldMapBuyerId() {

        // Arrange
        Transaction transaction = mockTransaction();

        // Act
        TransactionResponseDTO result = TransactionMapper.toResponseDTO(transaction);

        // Assert
        assertEquals("buyer-1", result.getBuyerId());
    }

    @Test
    void toResponseDTO_shouldMapPrice() {

        // Arrange
        Transaction transaction = mockTransaction();

        // Act
        TransactionResponseDTO result = TransactionMapper.toResponseDTO(transaction);

        // Assert
        assertEquals("10.00", result.getPrice());
    }

    @Test
    void toResponseDTO_shouldMapTransactionDate() {

        // Arrange
        Transaction transaction = mockTransaction();

        // Act
        TransactionResponseDTO result = TransactionMapper.toResponseDTO(transaction);

        // Assert
        assertNotNull(result.getTransactionDate());
    }

    @Test
    void toResponseDTO_shouldMapStreet() {

        // Arrange
        Transaction transaction = mockTransaction();

        // Act
        TransactionResponseDTO result = TransactionMapper.toResponseDTO(transaction);

        // Assert
        assertEquals("Rua das Flores", result.getStreet());
    }

    @Test
    void toResponseDTO_shouldMapNumber() {

        // Arrange
        Transaction transaction = mockTransaction();

        // Act
        TransactionResponseDTO result = TransactionMapper.toResponseDTO(transaction);

        // Assert
        assertEquals("10", result.getNumber());
    }

    @Test
    void toResponseDTO_shouldMapZipCode() {

        // Arrange
        Transaction transaction = mockTransaction();

        // Act
        TransactionResponseDTO result = TransactionMapper.toResponseDTO(transaction);

        // Assert
        assertEquals("4000-001", result.getZipCode());
    }

    @Test
    void toResponseDTO_shouldMapCityId() {

        // Arrange
        Transaction transaction = mockTransaction();

        // Act
        TransactionResponseDTO result = TransactionMapper.toResponseDTO(transaction);

        // Assert
        assertEquals("porto", result.getCityId());
    }

    @Test
    void toResponseDTO_shouldMapCardHolderName() {

        // Arrange
        Transaction transaction = mockTransaction();

        // Act
        TransactionResponseDTO result = TransactionMapper.toResponseDTO(transaction);

        // Assert
        assertEquals("John Doe", result.getCardHolderName());
    }

    @Test
    void toResponseDTO_shouldMapMaskedCardNumber() {

        // Arrange
        Transaction transaction = mockTransaction();

        // Act
        TransactionResponseDTO result = TransactionMapper.toResponseDTO(transaction);

        // Assert
        assertEquals("**** **** **** 1234", result.getMaskedCardNumber());
    }

    @Test
    void toResponseDTO_shouldMapExpiryMonth() {

        // Arrange
        Transaction transaction = mockTransaction();

        // Act
        TransactionResponseDTO result = TransactionMapper.toResponseDTO(transaction);

        // Assert
        assertEquals("09", result.getExpiryMonth());
    }

    @Test
    void toResponseDTO_shouldMapExpiryYear() {

        // Arrange
        Transaction transaction = mockTransaction();

        // Act
        TransactionResponseDTO result = TransactionMapper.toResponseDTO(transaction);

        // Assert
        assertEquals("2027", result.getExpiryYear());
    }

    @Test
    void toResponseDTO_shouldMapStatus() {

        // Arrange
        Transaction transaction = mockTransaction();

        // Act
        TransactionResponseDTO result = TransactionMapper.toResponseDTO(transaction);

        // Assert
        assertEquals(TransactionStatus.COMPLETED.toString(), result.getStatus());
    }

    @Test
    void toResponseDTO_shouldMapListingIds() {

        // Arrange
        Transaction transaction = mockTransaction();

        // Act
        TransactionResponseDTO result = TransactionMapper.toResponseDTO(transaction);

        // Assert
        assertEquals(List.of("listing-1"), result.getListingIds());
    }

    // =========================================================================
    // toTransactionId
    // =========================================================================

    @Test
    void toTransactionId_shouldReturnTransactionID() {

        // Act
        TransactionID result = TransactionMapper.toTransactionId("tx-1");

        // Assert
        assertEquals("tx-1", result.getId());
    }

    @Test
    void toTransactionId_shouldThrowException_whenIdIsNull() {

        // Act + Assert
        assertThrows(Exception.class,
                () -> TransactionMapper.toTransactionId(null));
    }

    @Test
    void toTransactionId_shouldThrowException_whenIdIsBlank() {

        // Act + Assert
        assertThrows(Exception.class,
                () -> TransactionMapper.toTransactionId("  "));
    }

    // =========================================================================
    // toUserId
    // =========================================================================

    @Test
    void toUserId_shouldReturnUserId() {

        // Act
        UserId result = TransactionMapper.toUserId("user-1");

        // Assert
        assertEquals("user-1", result.getUserId());
    }

    @Test
    void toUserId_shouldThrowException_whenIdIsNull() {

        // Act + Assert
        assertThrows(Exception.class,
                () -> TransactionMapper.toUserId(null));
    }

    @Test
    void toUserId_shouldThrowException_whenIdIsBlank() {

        // Act + Assert
        assertThrows(Exception.class,
                () -> TransactionMapper.toUserId("  "));
    }

    // =========================================================================
    // toPrice
    // =========================================================================

    @Test
    void toPrice_shouldReturnPrice() {

        // Act
        Price result = TransactionMapper.toPrice("10.50");

        // Assert
        assertEquals(new BigDecimal("10.50"), result.getPrice());
    }

    @Test
    void toPrice_shouldThrowException_whenValueIsInvalid() {

        // Act + Assert
        assertThrows(NumberFormatException.class,
                () -> TransactionMapper.toPrice("abc"));
    }

    @Test
    void toPrice_shouldThrowException_whenValueIsNull() {

        // Act + Assert
        assertThrows(Exception.class,
                () -> TransactionMapper.toPrice(null));
    }

    // =========================================================================
    // toTransactionDate
    // =========================================================================

    @Test
    void toTransactionDate_shouldReturnLocalDateTime() {

        // Arrange
        String dateStr = "2024-01-10T10:30:00";

        // Act
        LocalDateTime result = TransactionMapper.toTransactionDate(dateStr);

        // Assert
        assertEquals(LocalDateTime.of(2024, 1, 10, 10, 30, 0), result);
    }

    @Test
    void toTransactionDate_shouldThrowException_whenValueIsNull() {

        // Act + Assert
        assertThrows(Exception.class,
                () -> TransactionMapper.toTransactionDate(null));
    }

    @Test
    void toTransactionDate_shouldThrowException_whenValueIsInvalid() {

        // Act + Assert
        assertThrows(Exception.class,
                () -> TransactionMapper.toTransactionDate("not-a-date"));
    }

    // =========================================================================
    // toAddress
    // =========================================================================

    @Test
    void toAddress_shouldReturnAddress() {

        // Act
        Address result = TransactionMapper.toAddress("Rua das Flores", "10", "4000-001", "porto");

        // Assert
        assertNotNull(result);
        assertEquals("Rua das Flores", result.getStreet());
        assertEquals("10", result.getNumber());
        assertEquals("4000-001", result.getZipCode());
        assertEquals("porto", result.getCityID().getCityID());
    }

    @Test
    void toAddress_shouldThrowException_whenStreetIsNull() {

        // Act + Assert
        assertThrows(Exception.class,
                () -> TransactionMapper.toAddress(null, "10", "4000-001", "porto"));
    }

    @Test
    void toAddress_shouldThrowException_whenCityIdIsNull() {

        // Act + Assert
        assertThrows(Exception.class,
                () -> TransactionMapper.toAddress("Rua das Flores", "10", "4000-001", null));
    }

    // =========================================================================
    // toPaymentDetails
    // =========================================================================

    @Test
    void toPaymentDetails_shouldReturnPaymentDetails() {

        // Act
        PaymentDetails result = TransactionMapper.toPaymentDetails(
                "John Doe", "**** **** **** 1234", "09", "2027");

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.cardHolderName());
        assertEquals("**** **** **** 1234", result.maskedCardNumber());
        assertEquals("09", result.expiryMonth());
        assertEquals("2027", result.expiryYear());
    }

    @Test
    void toPaymentDetails_shouldThrowException_whenCardHolderNameIsNull() {

        // Act + Assert
        assertThrows(Exception.class,
                () -> TransactionMapper.toPaymentDetails(null, "**** **** **** 1234", "09", "2027"));
    }

    @Test
    void toPaymentDetails_shouldThrowException_whenMaskedCardNumberIsNull() {

        // Act + Assert
        assertThrows(Exception.class,
                () -> TransactionMapper.toPaymentDetails("John Doe", null, "09", "2027"));
    }

    @Test
    void toPaymentDetails_shouldThrowException_whenExpiryMonthIsNull() {

        // Act + Assert
        assertThrows(Exception.class,
                () -> TransactionMapper.toPaymentDetails("John Doe", "**** **** **** 1234", null, "2027"));
    }

    @Test
    void toPaymentDetails_shouldThrowException_whenExpiryYearIsNull() {

        // Act + Assert
        assertThrows(Exception.class,
                () -> TransactionMapper.toPaymentDetails("John Doe", "**** **** **** 1234", "09", null));
    }

    // =========================================================================
    // toListingId
    // =========================================================================

    @Test
    void toListingId_shouldReturnListingId() {

        // Act
        ListingId result = TransactionMapper.toListingId("listing-1");

        // Assert
        assertEquals("listing-1", result.getId());
    }

    @Test
    void toListingId_shouldThrowException_whenIdIsNull() {

        // Act + Assert
        assertThrows(Exception.class,
                () -> TransactionMapper.toListingId(null));
    }

    @Test
    void toListingId_shouldThrowException_whenIdIsBlank() {

        // Act + Assert
        assertThrows(Exception.class,
                () -> TransactionMapper.toListingId("  "));
    }

    // =========================================================================
    // toListingIds
    // =========================================================================

    @Test
    void toListingIds_shouldReturnListOfListingIds() {

        // Act
        List<ListingId> result = TransactionMapper.toListingIds(List.of("listing-1", "listing-2"));

        // Assert
        assertEquals(2, result.size());
        assertEquals("listing-1", result.get(0).getId());
        assertEquals("listing-2", result.get(1).getId());
    }

    @Test
    void toListingIds_shouldReturnEmptyList_whenInputIsEmpty() {

        // Act
        List<ListingId> result = TransactionMapper.toListingIds(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    private Transaction mockTransaction() {

        Transaction transaction    = mock(Transaction.class);
        TransactionID transactionId = mock(TransactionID.class);
        UserId buyerId             = mock(UserId.class);
        Price price                = mock(Price.class);
        LocalDateTime date         = LocalDateTime.of(2024, 1, 10, 10, 30, 0);
        Address address            = mock(Address.class);
        CityId cityId              = mock(CityId.class);
        PaymentDetails payment     = mock(PaymentDetails.class);
        ListingId listingId        = mock(ListingId.class);

        when(transaction.id()).thenReturn(transactionId);
        when(transactionId.getId()).thenReturn("tx-1");

        when(transaction.getBuyerId()).thenReturn(buyerId);
        when(buyerId.getUserId()).thenReturn("buyer-1");

        when(transaction.getPrice()).thenReturn(price);
        when(price.getPrice()).thenReturn(new BigDecimal("10.00"));

        when(transaction.getTransactionDate()).thenReturn(date);

        when(transaction.getShippingAddress()).thenReturn(address);
        when(address.getStreet()).thenReturn("Rua das Flores");
        when(address.getNumber()).thenReturn("10");
        when(address.getZipCode()).thenReturn("4000-001");
        when(address.getCityID()).thenReturn(cityId);
        when(cityId.getCityID()).thenReturn("porto");

        when(transaction.getPaymentDetails()).thenReturn(payment);
        when(payment.cardHolderName()).thenReturn("John Doe");
        when(payment.maskedCardNumber()).thenReturn("**** **** **** 1234");
        when(payment.expiryMonth()).thenReturn("09");
        when(payment.expiryYear()).thenReturn("2027");

        when(transaction.getStatus()).thenReturn(TransactionStatus.COMPLETED);

        when(transaction.getListingIds()).thenReturn(List.of(listingId));
        when(listingId.getId()).thenReturn("listing-1");

        return transaction;
    }
}