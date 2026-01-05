package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Country;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublisherTest {

    // Arrange
    String name = "Asas";
    String vatNumber = "123456";
    Address address = new Address("Rua Doutor David Paixão", "55", "333-332",
            Country.PORTUGAL);
    Email email = new Email("cool", "gmail.com");
    Contact contact = new Contact("91440055", "+351", email, address);

    @Test
    void constructorShouldCreatePublisherWhenDataIsValid() {

        //Act
        Publisher result = new Publisher(name, vatNumber, address, contact);

    }

    @Test
    void constructorShouldThrowAnExceptionWhenNameIsBlank() {

        // Arrange
        String blankName = "";

        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Publisher(blankName, vatNumber, address, contact));
    }

    @Test
    void constructorShouldThrowAnExceptionWhenVatNumberIsBlank() {

        // Arrange
        String blankVatNumber = "";

        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Publisher(name, blankVatNumber, address, contact));
    }

    @Test
    void constructorShouldThrowAnExceptionWhenNameIsNull() {

        // Arrange
        String nullName = null;

        // Assert
        assertThrows(IllegalArgumentException.class, () -> new Publisher(nullName, vatNumber, address, contact));
    }

    @Test
    void constructorShouldThrowAnExceptionWhenVatNumberIsNull() {

        // Arrange
        String nullVatNumber = null;

        // Assert
        assertThrows(IllegalArgumentException.class, () -> new Publisher(name, nullVatNumber, address, contact));
    }
}