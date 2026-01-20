package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.Countries;
import TOPSECRET.domain.Enum.Genre;
import TOPSECRET.domain.Enum.TypeOfBinding;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {


    @Test
    void shouldCreateItem() {
        // Act
        Item item = createValidItem();

        // Assert
        assertEquals(Condition.LIKE_NEW, item.getCondition());
        assertEquals(1.2, item.getWeight(), 0.0001);
        assertNotNull(item.getDimension());
        assertEquals(TypeOfBinding.PAPERBACK, item.getBinding());
        assertEquals("1st edition", item.getEdition());
        assertNotNull(item.getBibliographicalData());
    }

    @Test
    void shouldThrowWhenWeightIsZeroOrNegative() {
        // Arrange
        Item valid = createValidItem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Item(
                valid.getCondition(), 0.0, valid.getDimension(),
                valid.getBinding(), valid.getEdition(), valid.getBibliographicalData()
        ));

        assertThrows(IllegalArgumentException.class, () -> new Item(
                valid.getCondition(), -0.1, valid.getDimension(),
                valid.getBinding(), valid.getEdition(), valid.getBibliographicalData()
        ));
    }

    @Test
    void shouldThrowWhenEditionIsNull() {
        // Arrange
        Item valid = createValidItem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Item(
                valid.getCondition(), valid.getWeight(), valid.getDimension(),
                valid.getBinding(), null, valid.getBibliographicalData()
        ));
    }

    @Test
    void shouldThrowWhenEditionIsBlank() {
        // Arrange
        Item valid = createValidItem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Item(
                valid.getCondition(), valid.getWeight(), valid.getDimension(),
                valid.getBinding(), "   ", valid.getBibliographicalData()
        ));
    }

    @Test
    void shouldThrowWhenConditionIsNull() {
        // Arrange
        Item valid = createValidItem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Item(
                null, valid.getWeight(), valid.getDimension(),
                valid.getBinding(), valid.getEdition(), valid.getBibliographicalData()
        ));
    }



    @Test
    void shouldThrowWhenDimensionsIsNull() {
        // Arrange
        Item valid = createValidItem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Item(
                valid.getCondition(), valid.getWeight(), null,
                valid.getBinding(), valid.getEdition(), valid.getBibliographicalData()
        ));
    }

    @Test
    void shouldThrowWhenBindingIsNull() {
        // Arrange
        Item valid = createValidItem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Item(
                valid.getCondition(), valid.getWeight(), valid.getDimension(),
                null, valid.getEdition(), valid.getBibliographicalData()
        ));
    }

    @Test
    void shouldThrowWhenBibliographicalDataIsNull() {
        // Arrange
        Item valid = createValidItem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Item(
                valid.getCondition(), valid.getWeight(), valid.getDimension(),
                valid.getBinding(), valid.getEdition(), null
        ));
    }

    private Item createValidItem() {
        Profile author = new Profile("Tove Ditlevsen", new Country("DENMARK"), LocalDate.of(1917, 12, 14));
        Address address = new Address("R Maria", "12", "156-5", new Country("PARAGUAY"));
        Email email = new Email("Maria1", "gmail.com");
        Contact contact = new Contact("915945695", "+351", email, address);
        Publisher publisher = new Publisher("Some Publisher", "PT123456789", contact);
        BibliographicalData bibliographicalData = new BibliographicalData(
                "Childhood, Youth, Dependency: The Copenhagen Trilogy",
                List.of(author),
                Locale.ENGLISH,
                publisher,
                LocalDate.of(2020, 1, 1),
                List.of(Genre.FICTION),
                200,
                "Some text"
        );
        Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);


        return new Item(
                Condition.LIKE_NEW,
                1.2,
                dimensions,
                TypeOfBinding.PAPERBACK,
                "1st edition",
                bibliographicalData
        );
    }
}