package TOPSECRET.domain.ValueObjects;

/**
 * Represents payment details captured during a direct sale transaction.
 * <p>
 * No payment validation (e.g. Luhn check, expiry logic) is performed.
 * Only structural presence of each field is enforced.
 */
public record PaymentDetails(String _cardHolderName, String _maskedCardNumber, String _expiryMonth,
                             String _expiryYear) {

    /**
     * Constructs a {@code PaymentDetails} instance.
     *
     * @param _cardHolderName   the name on the card; must not be null or blank
     * @param _maskedCardNumber the masked card number (e.g. "**** **** **** 1234"); must not be null or blank
     * @param _expiryMonth      the two-digit expiry month (e.g. "09"); must not be null or blank
     * @param _expiryYear       the four-digit expiry year (e.g. "2027"); must not be null or blank
     * @throws IllegalArgumentException if any field is null or blank
     */
    public PaymentDetails {

        if (_cardHolderName == null || _cardHolderName.isBlank()) {
            throw new IllegalArgumentException("Card holder name must not be null or blank");
        }
        if (_maskedCardNumber == null || _maskedCardNumber.isBlank()) {
            throw new IllegalArgumentException("Masked card number must not be null or blank");
        }
        if (_expiryMonth == null || _expiryMonth.isBlank()) {
            throw new IllegalArgumentException("Expiry month must not be null or blank");
        }
        if (_expiryYear == null || _expiryYear.isBlank()) {
            throw new IllegalArgumentException("Expiry year must not be null or blank");
        }

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
