package TOPSECRET.repository;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
//import TOPSECRET.domain.Publisher; ...

class PublisherRepoTest {

    @Test
    void   addPublisher_with_valid_attributes_should_succeed() {
        // Arrange
        PublisherRepository repo = new PublisherRepository();
        Country country = new Country("Portugal");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345","12",email, address);


        // Act
        Publisher publisher = repo.add(
                "Penguin Books",
                "123456789",
                contact
        );

        // Assert
        assertNotNull(publisher);
    }

    @Test
    void addPublisher_with_same_vatNumber_should_be_rejected() {
        // Arrange
        PublisherRepository repo = new PublisherRepository();
        Country country = new Country("Portugal");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345","12",email, address);

        repo.add(
                "Penguin Books",
                "123456789",
                contact
        );

        // Act
        Publisher secondPublisher = repo.add(
                "Penguin Publishing",
                "123456789",
                contact

        );

        // Assert
        assertNull(secondPublisher);
    }
}

