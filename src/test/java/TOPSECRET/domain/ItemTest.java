package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    void shouldCreateItem() {
        Item item = createValidItem();

        assertEquals(Condition.LIKE_NEW, item.getCondition());
        assertEquals(1.2, item.getWeight(), 0.0001);
        assertNotNull(item.getDimension());
        assertNotNull(item.getPublication());

    }

    @Test
    void shouldThrowWhenConditionIsNull() {
        Item valid = createValidItem();

        assertThrows(IllegalArgumentException.class, () -> new Item(
                null,
                valid.getWeight(),
                valid.getDimension(),
                valid.getPublication()
        ));
    }

    @Test
    void shouldThrowWhenWeightIsZeroOrNegative() {
        Item valid = createValidItem();

        assertThrows(IllegalArgumentException.class, () -> new Item(
                valid.getCondition(),
                0.0,
                valid.getDimension(),
                valid.getPublication()
        ));

        assertThrows(IllegalArgumentException.class, () -> new Item(
                valid.getCondition(),
                -0.1,
                valid.getDimension(),
                valid.getPublication()
        ));
    }

    @Test
    void shouldThrowWhenDimensionsIsNull() {
        Item valid = createValidItem();

        assertThrows(IllegalArgumentException.class, () -> new Item(
                valid.getCondition(),
                valid.getWeight(),
                null,
                valid.getPublication()
        ));
    }

    @Test
    void shouldThrowWhenPublicationIsNull() {
        Item valid = createValidItem();

        assertThrows(IllegalArgumentException.class, () -> new Item(
                valid.getCondition(),
                valid.getWeight(),
                valid.getDimension(),
                null
        ));
    }

    // --------- Helper methods: create valid objects ---------

    private Item createValidItem() {
        return new Item(
                Condition.LIKE_NEW,
                1.2,
                new Dimensions(10.0, 0.2, 5.2),
                createValidPublication()
        );
    }

    private Publication createValidPublication() {
        Profile author = new Profile("Tove Ditlevsen", new Country("DENMARK"), LocalDate.of(1917, 12, 14));
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
                List.of(new Genre ("Fiction")),
                200,
                "Some text"
        );

        User user = new User("maria1", contact, author, UserRole.NORMAL_USER
        );

        Code code = Code.fromIsbn13("9781234567890");

        return new Publication(bd, publicationType, user, PublicationStatus.FOR_SALE, "1st edition", code
        );
    }
}
