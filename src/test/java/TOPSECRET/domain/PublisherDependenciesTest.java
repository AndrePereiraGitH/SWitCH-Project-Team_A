package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublisherDependenciesTest {

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

    @Test
    void getVatNumber_shouldReturnVatNumber() {
        // Arrange
        String vatNumber = "123456";
        Publisher publisher = new Publisher(name, vatNumber, contact);

        // Act
        String result = publisher.getVatNumber();

        // Assert
        assertEquals(vatNumber, result);
    }

    @Test
    void equals_sameInstance_returnsTrue() {
        Publisher publisher = new Publisher(name, "123456", contact);

        assertEquals(publisher, publisher);
    }

    @Test
    void equals_sameVatNumber_returnsTrue() {
        Publisher p1 = new Publisher(name, "123456", contact);
        Publisher p2 = new Publisher(name, "123456", contact);

        assertEquals(p1, p2);
    }

    @Test
    void equals_differentVatNumber_returnsFalse() {
        Publisher p1 = new Publisher(name, "123456", contact);
        Publisher p2 = new Publisher(name, "789012", contact);

        assertNotEquals(p1, p2);
    }

    @Test
    void equals_nullObject_returnsFalse() {
        Publisher publisher = new Publisher(name, "123456", contact);

        assertNotEquals(null, publisher);
    }

    @Test
    void equals_differentClass_returnsFalse() {
        Publisher publisher = new Publisher(name, "123456", contact);

        assertNotEquals("string", publisher);
    }

    @Test
    void hashCode_sameVatNumber_sameHashCode() {
        Publisher p1 = new Publisher(name, "123456", contact);
        Publisher p2 = new Publisher(name, "123456", contact);

        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void hashCode_differentVatNumber_differentHashCode() {
        Publisher p1 = new Publisher(name, "123456", contact);
        Publisher p2 = new Publisher(name, "789012", contact);

        assertNotEquals(p1.hashCode(), p2.hashCode());
    }
}