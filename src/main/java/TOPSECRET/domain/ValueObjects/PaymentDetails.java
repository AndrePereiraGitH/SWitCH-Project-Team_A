package TOPSECRET.domain.ValueObjects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents payment details captured during a direct sale transaction.
 * <p>
 * No payment validation (e.g. Luhn check, expiry logic) is performed.
 * Only structural presence of each field is enforced.
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentDetails {

    private String _cardHolderName;
    private String _maskedCardNumber;
    private String _expiryMonth;
    private String _expiryYear;

    /**
     * Constructs a {@code PaymentDetails} instance.
     *
     * @param cardHolderName   the name on the card; must not be null or blank
     * @param maskedCardNumber the masked card number (e.g. "**** **** **** 1234"); must not be null or blank
     * @param expiryMonth      the two-digit expiry month (e.g. "09"); must not be null or blank
     * @param expiryYear       the four-digit expiry year (e.g. "2027"); must not be null or blank
     * @throws IllegalArgumentException if any field is null or blank
     */
    public PaymentDetails(String cardHolderName, String maskedCardNumber,
                          String expiryMonth, String expiryYear) {

        if (cardHolderName == null || cardHolderName.isBlank()) {
            throw new IllegalArgumentException("Card holder name must not be null or blank");
        }
        if (maskedCardNumber == null || maskedCardNumber.isBlank()) {
            throw new IllegalArgumentException("Masked card number must not be null or blank");
        }
        if (expiryMonth == null || expiryMonth.isBlank()) {
            throw new IllegalArgumentException("Expiry month must not be null or blank");
        }
        if (expiryYear == null || expiryYear.isBlank()) {
            throw new IllegalArgumentException("Expiry year must not be null or blank");
        }

        this._cardHolderName = cardHolderName;
        this._maskedCardNumber = maskedCardNumber;
        this._expiryMonth = expiryMonth;
        this._expiryYear = expiryYear;
    }

    public String cardHolderName() { return _cardHolderName; }

    public String maskedCardNumber() { return _maskedCardNumber; }

    public String expiryMonth() { return _expiryMonth; }

    public String expiryYear() { return _expiryYear; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PaymentDetails)) return false;

        PaymentDetails other = (PaymentDetails) obj;

        return _cardHolderName.equals(other._cardHolderName) &&
                _maskedCardNumber.equals(other._maskedCardNumber) &&
                _expiryMonth.equals(other._expiryMonth) &&
                _expiryYear.equals(other._expiryYear);
    }

    @Override
    public String toString() {
        return "PaymentDetails{" +
                "cardHolderName='" + _cardHolderName + '\'' +
                ", maskedCardNumber='" + _maskedCardNumber + '\'' +
                ", expiryMonth='" + _expiryMonth + '\'' +
                ", expiryYear='" + _expiryYear + '\'' +
                '}';
    }
}