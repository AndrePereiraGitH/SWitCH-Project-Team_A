package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PriceValidatorTest {

    @Test
    public void shouldReturnDoubleWhenPriceIsValid(){
        Double expectedPrice = 8.0;
        Double realPrice = PriceValidator.validatePrice(expectedPrice);

        assertEquals(expectedPrice,realPrice);
    }

    @Test
    void shouldThrowExceptionWhenPriceHasMoreThanTwoDecimals() {

        double invalidPrice = 8.555;
        assertThrows(IllegalArgumentException.class, () -> {
            PriceValidator.validatePrice(invalidPrice);
        });
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        double invalidPrice = -8.55;
        assertThrows(IllegalArgumentException.class, () -> {
            PriceValidator.validatePrice(invalidPrice);
        });
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZero() {
        double invalidPrice = 0;
        assertThrows(IllegalArgumentException.class, () -> {
            PriceValidator.validatePrice(invalidPrice);
        });
    }

}