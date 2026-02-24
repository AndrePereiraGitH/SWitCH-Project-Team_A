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
        Code issn = Code.fromIssn("12345678");
        Item item = validItemAfter1976();
        Magazine magazine = new Magazine(issn, item);

        assertEquals(issn, magazine.getIssn());
        assertEquals(item, magazine.getItem());
    }

    @Test
    void shouldThrowWhenIssnIsNull() {
        Item item = validItemBefore1976();
        assertThrows(IllegalArgumentException.class, () -> new Magazine(null, item));
    }

    @Test
    void shouldThrowWhenItemIsNull() {
        Code issn = Code.fromIssn("1234-5678");
        assertThrows(IllegalArgumentException.class, () -> new Magazine(issn, null));
    }

    @Test
    void shouldThrowWhenMagazineAfter1976HasNoIssn() {
        // Edition date >= 1976 but code has no ISSN → should throw on Edition creation
        Code codeWithoutIssn = Code.fromIsbn13("9781234567890");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.MAGAZINE, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = buildPublisher();

        assertThrows(IllegalArgumentException.class, () ->
                new Edition("1st", codeWithoutIssn, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1))
        );
    }

    @Test
    void shouldAllowMagazineBefore1976WithNoIssn() {
        // Edition date < 1976: no ISSN required, should not throw
        Code anyCode = Code.fromIsbn13("9781234567890");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.MAGAZINE, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = buildPublisher();

        assertDoesNotThrow(() ->
                new Edition("1st", anyCode, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(1950, 1, 1))
        );
    }

    // --- Helpers ---

    private Item validItemAfter1976() {
        Country authorCountry = new Country("UNITED_STATES");
        Profile profile = new Profile("Author Name", authorCountry, LocalDate.of(1980, 1, 1));
        Author author = new Author(profile, "valio bio");

        Publisher publisher = buildPublisher();
        Contact contact = buildContact();

        BibliographicalData bib = new BibliographicalData(
                "Title",
                List.of(author),
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Nonfiction")),
                "Text"
        );

        PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");
        User user = new User("user01", contact, profile, UserRole.NORMAL_USER);

        // Magazine edition after 1976 → ISSN required
        Code issnCode = Code.fromIssn("12345678");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.MAGAZINE, TypeOfBinding.CASE_BINDING, "Some details");
        Edition edition = new Edition("1st", issnCode, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        Publication publication = new Publication(bib, publicationType, user, PublicationStatus.FOR_SALE, edition, issnCode);

        return new Item(Condition.NEW, 0.1, new Dimensions(1.0, 1.0, 1.0), publication);
    }

    private Item validItemBefore1976() {
        Country authorCountry = new Country("UNITED_STATES");
        Profile profile = new Profile("Author Name", authorCountry, LocalDate.of(1950, 1, 1));
        Author author = new Author(profile, "valio bio");

        Publisher publisher = buildPublisher();
        Contact contact = buildContact();

        BibliographicalData bib = new BibliographicalData(
                "Title",
                List.of(author),
                Locale.ENGLISH,
                LocalDate.of(1950, 1, 1),
                List.of(new Genre("Nonfiction")),
                "Text"
        );

        PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");
        User user = new User("user01", contact, profile, UserRole.NORMAL_USER);

        // Magazine edition before 1976 → no ISSN required, but we use any valid code
        Code anyCode = Code.fromIsbn13("9781234567890");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.MAGAZINE, TypeOfBinding.CASE_BINDING, "Some details");
        Edition edition = new Edition("1st", anyCode, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(1950, 1, 1));

        Publication publication = new Publication(bib, publicationType, user, PublicationStatus.FOR_SALE, edition, anyCode);

        return new Item(Condition.NEW, 0.1, new Dimensions(1.0, 1.0, 1.0), publication);
    }

    private Publisher buildPublisher() {
        Country addressCountry = new Country("PARAGUAY");
        Address address = new Address("R Maria", "12", "156-5", addressCountry);
        Email email = new Email("Maria1", "gmail.com");
        Contact contact = new Contact("915945695", "+351", email, address);
        return new Publisher("PublisherName", "VatNumber", contact);
    }

    private Contact buildContact() {
        Country addressCountry = new Country("PARAGUAY");
        Address address = new Address("R Maria", "12", "156-5", addressCountry);
        Email email = new Email("Maria1", "gmail.com");
        return new Contact("915945695", "+351", email, address);
    }
}