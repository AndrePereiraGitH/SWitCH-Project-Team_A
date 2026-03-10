package TOPSECRET.repository;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
//import TOPSECRET.domain.Publisher; ...

class PublisherRepositoryDependenciesTest {

    /**
     * Test that verifies a Publisher is successfully added
     * when all provided attributes are valid.
     */
    @Test
    void   addPublisher_with_valid_attributes_should_succeed() {

        // -------- Arrange --------
        // Create a new repository instance (empty at the start)
        PublisherRepository repo = new PublisherRepository();

        // Create supporting domain objects required to build a Contact
        //OLDCountry country = new Country("Portugal");
        CountryFactory countryFactory = new CountryFactory();
        Country country = countryFactory.create("Portugal");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345","12",email, address);


        // Act
        // Attempt to add a new publisher with valid data
        Publisher publisher = repo.createAndAdd(
                "Penguin Books",
                "123456789",
                contact
        );

        // Assert
        // Verify that the publisher was successfully created and stored
        assertNotNull(publisher);
    }


    /**
     * Test that verifies the repository prevents adding
     * two publishers with the same VAT number.
     */
    @Test
    void addPublisher_with_same_vatNumber_should_be_rejected() {
        // Arrange
        PublisherRepository repo = new PublisherRepository();

        // Create valid contact data for the publisher
        //OLDCountry country = new Country("Portugal");
        CountryFactory countryFactory = new CountryFactory();
        Country country = countryFactory.create("Portugal");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345","12",email, address);

        // Add the first publisher with a specific VAT number
        repo.createAndAdd(
                "Penguin Books",
                "123456789",
                contact
        );

        // Act
        // Try to add another publisher using the same VAT number
        Publisher secondPublisher = repo.createAndAdd(
                "Penguin Publishing",
                "123456789",
                contact

        );

        // Assert
        // Verify that the second publisher is rejected
        // (null indicates the repository blocked the duplicate)
        assertNull(secondPublisher);
    }
}

