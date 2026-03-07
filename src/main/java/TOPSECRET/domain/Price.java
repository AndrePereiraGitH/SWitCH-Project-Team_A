package TOPSECRET.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a monetary price value.
 * <p>
 * Encapsulates a {@link BigDecimal} price ensuring it is a positive value
 * with at most 2 decimal places.
 * </p>
 */
public class Price {

    private BigDecimal _price;

    /**
     * Creates a new Price with the specified value.
     *
     * @param price the monetary value (must be positive and have at most 2 decimal places)
     * @throws IllegalArgumentException if the price has more than 2 decimal places
     * @throws IllegalArgumentException if the price is zero or negative
     */
    public Price(BigDecimal price) {

        if (price.scale() > 2) {
            throw new IllegalArgumentException("Price value is invalid");
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price value can not be equal or lower than 0");
        }

        _price = price;
    }

    /**
     * Returns the monetary value of this price.
     *
     * @return the price as a {@link BigDecimal}
     */
    public BigDecimal getPrice() {
        return _price;
    }

    /**
     * Indicates whether some other object is equal to this price.
     * Two prices are considered equal if they have the same {@link BigDecimal} value.
     *
     * @param o the object to compare with
     * @return {@code true} if the given object is a Price with the same value, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Objects.equals(_price, price._price);
    }

    /**
     * Returns a hash code value for this price, based on its {@link BigDecimal} value.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(_price);
    }
}