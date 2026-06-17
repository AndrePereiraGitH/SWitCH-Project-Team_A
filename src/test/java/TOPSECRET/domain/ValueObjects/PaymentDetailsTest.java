package TOPSECRET.domain.ValueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentDetailsTest {

    @Test
    void constructor_WithValidValues_CreatesPaymentDetails() {
        PaymentDetails paymentDetails = new PaymentDetails(
                "John Doe",
                "**** **** **** 1234",
                "09",
                "2027"
        );

        assertEquals("John Doe", paymentDetails.cardHolderName());
        assertEquals("**** **** **** 1234", paymentDetails.maskedCardNumber());
        assertEquals("09", paymentDetails.expiryMonth());
        assertEquals("2027", paymentDetails.expiryYear());
    }

    @Test
    void constructor_WithNullCardHolderName_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PaymentDetails(null, "**** **** **** 1234", "09", "2027")
        );

        assertEquals("Card holder name must not be null or blank", exception.getMessage());
    }

    @Test
    void constructor_WithBlankCardHolderName_ThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PaymentDetails("   ", "**** **** **** 1234", "09", "2027")
        );
    }

    @Test
    void constructor_WithNullMaskedCardNumber_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PaymentDetails("John Doe", null, "09", "2027")
        );

        assertEquals("Masked card number must not be null or blank", exception.getMessage());
    }

    @Test
    void constructor_WithBlankMaskedCardNumber_ThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PaymentDetails("John Doe", "   ", "09", "2027")
        );
    }

    @Test
    void constructor_WithNullExpiryMonth_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PaymentDetails("John Doe", "**** **** **** 1234", null, "2027")
        );

        assertEquals("Expiry month must not be null or blank", exception.getMessage());
    }

    @Test
    void constructor_WithBlankExpiryMonth_ThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PaymentDetails("John Doe", "**** **** **** 1234", "   ", "2027")
        );
    }

    @Test
    void constructor_WithNullExpiryYear_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PaymentDetails("John Doe", "**** **** **** 1234", "09", null)
        );

        assertEquals("Expiry year must not be null or blank", exception.getMessage());
    }

    @Test
    void constructor_WithBlankExpiryYear_ThrowsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PaymentDetails("John Doe", "**** **** **** 1234", "09", "   ")
        );
    }

    @Test
    void equals_WithSameValues_ReturnsTrue() {
        PaymentDetails first = new PaymentDetails("John Doe", "**** **** **** 1234", "09", "2027");
        PaymentDetails second = new PaymentDetails("John Doe", "**** **** **** 1234", "09", "2027");

        assertEquals(first, second);
    }

    @Test
    void equals_WithDifferentValues_ReturnsFalse() {
        PaymentDetails first = new PaymentDetails("John Doe", "**** **** **** 1234", "09", "2027");
        PaymentDetails second = new PaymentDetails("Jane Doe", "**** **** **** 5678", "10", "2028");

        assertNotEquals(first, second);
    }

    @Test
    void equals_WithNull_ReturnsFalse() {
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "**** **** **** 1234", "09", "2027");

        assertNotEquals(null, paymentDetails);
    }

    @Test
    void equals_WithDifferentType_ReturnsFalse() {
        PaymentDetails paymentDetails = new PaymentDetails("John Doe", "**** **** **** 1234", "09", "2027");

        assertNotEquals("not payment details", paymentDetails);
    }

    @Test
    void toString_ReturnsExpectedString() {
        PaymentDetails paymentDetails = new PaymentDetails(
                "John Doe",
                "**** **** **** 1234",
                "09",
                "2027"
        );

        assertEquals(
                "PaymentDetails{cardHolderName='John Doe', maskedCardNumber='**** **** **** 1234', expiryMonth='09', expiryYear='2027'}",
                paymentDetails.toString()
        );
    }
}