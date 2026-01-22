package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.repository.PublisherRepository;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class US006ToRegisterNewPublisherControllerTest {
//The controller can be created
    @Test
    void newControllerNewPublisher_with_success() {
        // Arrange
        PublisherRepository publisherRepo = new PublisherRepository();
        Country country = new Country("PORTUGAL");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345","12",email, address);
        Profile profile = new Profile("John", country, LocalDate.of(1990, 1, 1) );

        User admin = new User("admin", contact, profile, UserRole.ADMIN);

        // Act
        US006ToRegisterNewPublisherController controller =
                new US006ToRegisterNewPublisherController(publisherRepo, admin);

        // Assert
        assertNotNull(controller);
    }

    @Test
    //A publisher can be registered with valid attributes
    void newPublisher_with_valid_attributes() {
        // Arrange
        PublisherRepository publisherRepo = new PublisherRepository();
        Country country = new Country("PORTUGAL");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345","12",email, address);
        Profile profile = new Profile("John", country, LocalDate.of(2001,6,20) );


        User admin = new User("admin", contact, profile, UserRole.ADMIN );
        US006ToRegisterNewPublisherController controller =
                new US006ToRegisterNewPublisherController(publisherRepo, admin);

        // Act
        Publisher publisher = controller.createPublisher(
                "Penguin Books",
                "123456789",
                contact
        );

        // Assert
        assertNotNull(publisher);
    }

    @Test
    //A publisher cannot be registered twice with the same VAT number
    void newPublisher_with_repeated_vatNumber_should_fail() {
        // Arrange
        PublisherRepository publisherRepo = new PublisherRepository();
        Country country = new Country("PORTUGAL");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345","12",email, address);
        Profile profile = new Profile("John", country, LocalDate.of(2000,4,19) );

        User admin = new User("admin", contact, profile, UserRole.ADMIN );
        US006ToRegisterNewPublisherController controller =
                new US006ToRegisterNewPublisherController(publisherRepo, admin);

        controller.createPublisher(
                "Penguin Books",
                "123456789",
                contact
        );

        // Act
        Publisher publisher2 = controller.createPublisher(
                "Penguin Publishing",
                "123456789",
                contact
        );

        // Assert
        assertNull(publisher2);
    }
}