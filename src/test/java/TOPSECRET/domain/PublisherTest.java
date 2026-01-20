package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Countries;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublisherTest {

    // Arrange
    String name = "Asas";
    String vatNumber = "123456";

    Country country = new Country("PORTUGAL");
    Address address = new Address("Rua Doutor David Paixão", "55", "333-332",
            country);
    Email email = new Email("cool", "gmail.com");
    Contact contact = new Contact("91440055", "+351", email, address);

    @Test
    void constructorShouldCreatePublisherWhenDataIsValid() {

        //Act
        Publisher result = new Publisher(name, vatNumber, contact);

        //Assert
        assertNotNull(result);

    }

    @Test
    void constructorShouldThrowAnExceptionWhenNameIsBlank() {

        // Arrange
        String blankName = "";

        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Publisher(blankName, vatNumber, contact));
    }

    @Test
    void constructorShouldThrowAnExceptionWhenVatNumberIsBlank() {

        // Arrange
        String blankVatNumber = "";

        //Assert
        assertThrows(IllegalArgumentException.class, () -> new Publisher(name, blankVatNumber, contact));
    }


}