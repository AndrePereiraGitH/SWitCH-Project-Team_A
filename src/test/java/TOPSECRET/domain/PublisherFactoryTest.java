package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublisherFactoryTest {

    @Test
    void create_with_valid_attributes_should_succeed() {
        PublisherFactory factory = new PublisherFactory();

        Country country = new Country("Portugal");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345", "12", email, address);

        Publisher publisher = factory.create("Penguin Books", "123456789", contact);

        assertNotNull(publisher);
        assertEquals("Penguin Books", publisher.getName());
        assertEquals("123456789", publisher.getVatNumber());
        assertEquals(contact, publisher.getContact());
    }

    @Test
    void create_should_trim_name_and_vatNumber() {
        PublisherFactory factory = new PublisherFactory();

        Country country = new Country("Portugal");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345", "12", email, address);

        Publisher publisher = factory.create("  Penguin Books  ", "  123456789  ", contact);

        assertEquals("Penguin Books", publisher.getName());
        assertEquals("123456789", publisher.getVatNumber());
    }

    @Test
    void create_with_blank_name_should_throw_exception() {
        PublisherFactory factory = new PublisherFactory();

        Country country = new Country("Portugal");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345", "12", email, address);

        assertThrows(IllegalArgumentException.class, () ->
                factory.create("   ", "123456789", contact)
        );
    }

    @Test
    void create_with_blank_vatNumber_should_throw_exception() {
        PublisherFactory factory = new PublisherFactory();

        Country country = new Country("Portugal");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345", "12", email, address);

        assertThrows(IllegalArgumentException.class, () ->
                factory.create("Penguin Books", "   ", contact)
        );
    }

    @Test
    void create_with_null_name_should_throw_nullPointerException() {
        PublisherFactory factory = new PublisherFactory();

        Country country = new Country("Portugal");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345", "12", email, address);

        assertThrows(NullPointerException.class, () ->
                factory.create(null, "123456789", contact)
        );
    }

    @Test
    void create_with_null_vatNumber_should_throw_nullPointerException() {
        PublisherFactory factory = new PublisherFactory();

        Country country = new Country("Portugal");
        Email email = new Email("contact", "penguin.com");
        Address address = new Address("PallMall", "1", "SW1", country);
        Contact contact = new Contact("+4412312345", "12", email, address);

        assertThrows(NullPointerException.class, () ->
                factory.create("Penguin Books", null, contact)
        );
    }

    @Test
    void create_with_null_contact_should_succeed_because_publisher_does_not_validate_contact() {
        PublisherFactory factory = new PublisherFactory();

        Publisher publisher = factory.create("Penguin Books", "123456789", null);

        assertNotNull(publisher);
        assertNull(publisher.getContact());
    }
}