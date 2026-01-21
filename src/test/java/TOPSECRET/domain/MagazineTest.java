package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
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
        // Author / Profile
        Country authorCountry = new Country("UNITED_STATES");
        Profile profile = new Profile("Author Name", authorCountry, LocalDate.of(1980, 1, 1));
        Author author = new Author(profile, "valio bio");

        // Publisher dependencies
        Country addressCountry = new Country("PARAGUAY");
        Address address = new Address("R Maria", "12", "156-5", addressCountry);
        Email email = new Email("Maria1", "gmail.com");
        Contact contact = new Contact("915945695", "+351", email, address);
        Publisher publisher = new Publisher("PublisherName", "VatNumber", contact);

        // BibliographicalData
        BibliographicalData bib = new BibliographicalData(
                "Title",
                List.of(author),
                Locale.ENGLISH,
                publisher,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre ("Nonfiction")),
                100,
                "Text"
        );
        // PublicationType
        PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");

        // User
        User user = new User("user01", contact, profile, UserRole.NORMAL_USER);

        // Code for the publication
        Code code = Code.fromIsbn13("9781234567890");

        // Publication
        Publication publication = new Publication(bib, publicationType, user, PublicationStatus.FOR_SALE, "1st edition", code);

        // Item (NEW signature)
        return new Item(Condition.NEW, 0.1, new Dimensions(1.0, 1.0, 1.0), publication);
    }
}