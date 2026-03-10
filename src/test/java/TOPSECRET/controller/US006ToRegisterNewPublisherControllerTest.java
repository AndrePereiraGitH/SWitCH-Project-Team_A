package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.repository.PublisherRepository;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class   US006ToRegisterNewPublisherControllerTest {

    /**
     * Test that verifies the controller can be instantiated
     * when provided with valid dependencies.
     */
    @Test
    void newControllerNewPublisher_with_success() {

        // Arrange
        // Create a repository to store publishers
        PublisherRepository publisherRepo = new PublisherRepository();

        // Create supporting domain objects required to build a User
        CountryFactory countryFactory = new CountryFactory();
        Country country = countryFactory.create("Portugal");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345","12",email, address);
        Profile profile = new Profile("John", country, LocalDate.of(1990, 1, 1) );

        // Create an admin user who is allowed to register publishers
        User admin = UserFactory.create("admin", contact, profile, UserRole.ADMIN);

        // Act
        // Instantiate the controller with the repository and admin user
        US006ToRegisterNewPublisherController controller =
                new US006ToRegisterNewPublisherController(publisherRepo, admin);

        // Assert
        // Verify that the controller was successfully created

        assertNotNull(controller);
    }

    /**
     * Test that verifies a publisher can be registered
     * when all provided attributes are valid.
     */
    @Test
    //A publisher can be registered with valid attributes
    void newPublisher_with_valid_attributes() {

        // Arrange

        PublisherRepository publisherRepo = new PublisherRepository();
        CountryFactory countryFactory = new CountryFactory();
        Country country = countryFactory.create("Portugal");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345","12",email, address);
        Profile profile = new Profile("John", country, LocalDate.of(2001,6,20) );


        User admin = UserFactory.create("admin", contact, profile, UserRole.ADMIN );

        // Create the controller under test
        US006ToRegisterNewPublisherController controller =
                new US006ToRegisterNewPublisherController(publisherRepo, admin);

        // Act
        // Attempt to register a new publisher with valid data
        Publisher publisher = controller.createPublisher(
                "Penguin Books",
                "123456789",
                contact
        );

        // Assert
        // Verify that the publisher was successfully created
        assertNotNull(publisher);
    }

    /**
     * Test that verifies the system does not allow
     * two publishers to be registered with the same VAT number.
     */
    @Test
    //A publisher cannot be registered twice with the same VAT number
    void newPublisher_with_repeated_vatNumber_should_fail() {

        // Arrange
        PublisherRepository publisherRepo = new PublisherRepository();
        CountryFactory countryFactory = new CountryFactory();
        Country country = countryFactory.create("Portugal");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345","12",email, address);
        Profile profile = new Profile("John", country, LocalDate.of(2000,4,19) );

        User admin = UserFactory.create("admin", contact, profile, UserRole.ADMIN );
        US006ToRegisterNewPublisherController controller =
                new US006ToRegisterNewPublisherController(publisherRepo, admin);

        // Register the first publisher with a given VAT number
        controller.createPublisher(
                "Penguin Books",
                "123456789",
                contact
        );

        // Act
        // Attempt to register another publisher using the same VAT number
        Publisher publisher2 = controller.createPublisher(
                "Penguin Publishing",
                "123456789",
                contact
        );

        // Assert
        // Verify that the second registration fails
        assertNull(publisher2);
    }
}