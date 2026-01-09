package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PriceTest {



    @ParameterizedTest
    @CsvSource({
            "8.55",
            "10.14",
            "0.01",
            "100.9",
            "101",
            "5"
    })
    public void shouldCreateAValidConstructor(BigDecimal expectedPrice){
        //Arrange

        //Act
        Price price = new Price(expectedPrice);

        //Assert
        assertEquals(expectedPrice, price.getPrice());

    }

    @ParameterizedTest
    @CsvSource({
            "8.555",
            "10.1234",
            "0.001",
            "100.999"
    })
    void shouldThrowExceptionWhenPriceHasMoreThanTwoDecimals(BigDecimal invalidPrice) {

        //Arrange

        //Act

        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Price(invalidPrice));

    }

    @ParameterizedTest
    @CsvSource({
            "-8.55",
            "-10.12",
            "-0.01",
            "-100.9"
    })
    void shouldThrowExceptionWhenPriceIsNegative(BigDecimal invalidPrice) {

        //Arrange

        //Act

        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Price(invalidPrice));
    }

    @ParameterizedTest
    @CsvSource({
            "-8.554",
            "-10.127",
            "-0.019",
            "-100.947"
    })
    void shouldThrowExceptionWhenPriceIsNegativeAndHasMoreThanTwoDecimals(BigDecimal invalidPrice) {
        //Arrange

        //Act

        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Price(invalidPrice));

    }

    @Test
    void shouldThrowExceptionWhenPriceIsZero() {
        //Arrange
        BigDecimal zero = new BigDecimal(0);
        //Act

        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Price(zero));
    }

    // ============================================================
    // ======================= Equals and hashCode tests ==========
    // ============================================================

    @Test
    void testEqualsSamePrice() {

        // arrange + act
        Price price = new Price(new BigDecimal("45.50"));

        // assert
        assertEquals(price, price);

    }

    @Test
    void testEquals_null_andDifferentClass() {

        // arrange + act
        Price price = new Price(new BigDecimal("45.50"));

        // assert
        assertNotEquals(null, price);
        assertNotEquals("35.50", price);
    }

    @Test
    void equals_sameValue_true_andHashCodeSame() {
        Price price1 = new Price(new BigDecimal("45.50"));
        Price price2 = new Price(new BigDecimal("45.50"));

        assertEquals(price1, price2);
        assertEquals(price1.hashCode(), price2.hashCode());
    }

    @Test
    void equals_differentValue_false() {
        Price price1 = new Price(new BigDecimal("45.50"));
        Price price2 = new Price(new BigDecimal("45.51"));

        assertNotEquals(price1, price2);
    }


}