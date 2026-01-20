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

class MagazineTest {
    @Test
    void shouldCreateMagazine() {
        Code issn = Code.fromIssn("1234-5678");
        Item item = validItem();

        Magazine magazine = new Magazine(issn, item);

        assertEquals(issn, magazine.getIssn());
        assertEquals(item, magazine.getItem());
    }

    @Test
    void shouldThrowWhenIssnIsNull() {
        Item item = validItem();
        assertThrows(IllegalArgumentException.class, () -> new Magazine(null, item));
    }

    @Test
    void shouldThrowWhenItemIsNull() {
        Code issn = Code.fromIssn("1234-5678");
        assertThrows(IllegalArgumentException.class, () -> new Magazine(issn, null));
    }

    private Item validItem() {
        Profile author = new Profile("Author Name", Countries.UNITED_STATES, LocalDate.of(1980, 1, 1));

        Address address = new Address("R Maria", "12", "156-5", Countries.PARAGUAY);
        Email email = new Email("Maria1", "gmail.com");
        Contact contact = new Contact("915945695", "+351", email, address);

        Publisher publisher = new Publisher("PublisherName", "VatNumber", contact);

        BibliographicalData bib = new BibliographicalData(
                "Title",
                List.of(author),
                Locale.ENGLISH,
                publisher,
                LocalDate.of(2020, 1, 1),
                List.of(Genre.NONFICTION),
                100,
                "Text"
        );

        return new Item(
                Condition.NEW,
                0.1,
                new Dimensions(1.0, 1.0, 1.0),
                TypeOfBinding.HARDBACK,
                "1st edition",
                bib
        );
    }
}