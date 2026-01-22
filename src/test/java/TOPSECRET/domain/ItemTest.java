package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    // -------------------------
    // Helpers (create valid objects once, reuse everywhere)
    // -------------------------

    private Condition validCondition() {
        return Condition.LIKE_NEW;
    }

    private double validWeight() {
        return 300.2;
    }

    private Dimensions validDimensions() {
        return new Dimensions(10.0, 0.2, 5.2);
    }

    private Publication validPublication() {
        Profile profile = new Profile("Tove Ditlevsen", new Country("DENMARK"), LocalDate.of(1917, 12, 14));
        Author author = new Author(profile, "valid bio");
        Address address = new Address("R Maria", "12", "156-5", new Country("PORTUGAL"));
        Email email = new Email("maria1", "gmail.com");
        Contact contact = new Contact("915945695", "+351", email, address);
        Publisher publisher = new Publisher("Some Publisher", "PT123456789", contact);
        PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");
        BibliographicalData bd = new BibliographicalData(
                "Childhood, Youth, Dependency: The Copenhagen Trilogy",
                List.of(author),
                Locale.ENGLISH,
                publisher,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
                200,
                "Some text"
        );

        User user = new User("Maria", contact, profile, UserRole.NORMAL_USER);
        Code code = Code.fromIsbn13("9781234567890");

        return new Publication(bd, publicationType, user, PublicationStatus.FOR_SALE, "1st edition", code);
    }

    private Item validItem() {
        return new Item(validCondition(), validWeight(), validDimensions(), validPublication());
    }
    //TODO Confirm this
    //helper: makes it easy to override one field and keep the rest valid
    private Item itemWith(Condition condition, double weight, Dimensions dimensions, Publication publication) {
        return new Item(
                condition != null ? condition : validCondition(),
                weight,
                dimensions != null ? dimensions : validDimensions(),
                publication != null ? publication : validPublication()
        );
    }

    // -------------------------
    // Happy path
    // -------------------------

    @Test
    void shouldCreateItem_withValidData() {
        Item item = validItem();

        assertNotNull(item);
        assertEquals(Condition.LIKE_NEW, item.getCondition());
        assertEquals(300.2, item.getWeight(), 0.0001);
        assertNotNull(item.getDimension());
        assertNotNull(item.getPublication());
    }

    // -------------------------
    // Necessary validations
    // -------------------------

    @Test
    void shouldThrow_whenConditionIsNull() {
        Item valid = validItem();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Item(null, valid.getWeight(), valid.getDimension(), valid.getPublication())
        );

        assertEquals("Condition cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrow_whenWeightIsZero() {
        Item valid = validItem();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Item(valid.getCondition(), 0.0, valid.getDimension(), valid.getPublication())
        );

        assertEquals("Weight must be greater than 0.", ex.getMessage());
    }

    @Test
    void shouldThrow_whenWeightIsNegative() {
        Item valid = validItem();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Item(valid.getCondition(), -0.1, valid.getDimension(), valid.getPublication())
        );

        assertEquals("Weight must be greater than 0.", ex.getMessage());
    }

    @Test
    void shouldThrow_whenDimensionsIsNull() {
        Item valid = validItem();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Item(valid.getCondition(), valid.getWeight(), null, valid.getPublication())
        );

        assertEquals("Dimensions cannot be null", ex.getMessage());
    }

    @Test
    void shouldThrow_whenPublicationIsNull() {
        Item valid = validItem();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Item(valid.getCondition(), valid.getWeight(), valid.getDimension(), null)
        );

        assertEquals("Publication cannot be null", ex.getMessage());
    }

    @Test
    void shouldAllow_weightJustAboveZero() {
        // Kills boundary mutants like weight <= 0 becoming weight < 0
        Item item = itemWith(validCondition(), Double.MIN_VALUE, validDimensions(), validPublication());

        assertEquals(Double.MIN_VALUE, item.getWeight(), 0.0);
    }
}
