package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static TOPSECRET.domain.Enum.Countries.SOUTH_AFRICA;
import static TOPSECRET.domain.Enum.Countries.UNITED_STATES;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private BibliographicalData sampleInformation()
    {
        // Bibliographical Data creation

        String title = "The Lord of the Rings";

//        Email publisherEmail = new Email("HarperCollins", "gmail.com");
//        Country country = new Country("UNITED_STATES");
//        Address publisherAddress = new Address("Broadway","195","10007", country);
//        Contact publisherContact = new Contact("987654321", "+351", publisherEmail, publisherAddress);
//        Publisher publisher = new Publisher("HarperCollins Publishers","123456", publisherContact);

        LocalDate publishDate = LocalDate.of(1954,07,29);

        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre ("Fiction"));
        genres.add(new Genre ("Fantasy"));
        genres.add(new Genre ("Adventure"));

        Country country1 = new Country("SOUTH_AFRICA");
        Profile authorProfile = new Profile("J.R.R Tolkien", country1,LocalDate.of(1892,01,03));
        List<Author> authors = new ArrayList<>();
        Author author = new Author (authorProfile, "valid bio");
        authors.add(author);

//        int numOfPages = 1100;

//        BibliographicalData information = new BibliographicalData(title, authors, Locale.ENGLISH, publisher, publishDate, genres, numOfPages, "I will take the Ring,' he said, 'Though I do not know the way.");
//        BibliographicalData information = new BibliographicalData(title, authors, Locale.ENGLISH, publisher, publishDate, genres, numOfPages, "I will take the Ring,' he said, 'Though I do not know the way.");
        BibliographicalData information = new BibliographicalData(title, authors, Locale.ENGLISH, publishDate, genres, "I will take the Ring,' he said, 'Though I do not know the way.");
        return information;
    }

    private Item sampleItem() {
        // Item basics
        double weight = 0.7;

        Dimensions dimensions = new Dimensions(12.2, 8.7, 0.2);

        // Publication basics
//        String edition = "Sample Edition";
        Code code1 = Code.fromIsbn13 ("978316148412");
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition= new Edition("1st", code1, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        BibliographicalData information = sampleInformation();

        PublicationType publicationType =
                new PublicationType(TypeOfBinding.HARDBACK, "Hardback");

        // --- minimal User created here ---

        Email email = new Email("user01", "example.com");
        Address address = new Address("Rua X", "1", "1000-000", new Country("PORTUGAL"));
        Contact contact = new Contact("911111111", "+351", email, address);

        Profile profile = new Profile("Sample User", new Country("PORTUGAL"), LocalDate.of(1990, 1, 1));
        User user = new User("user01", contact, profile, UserRole.NORMAL_USER);

        Code code = Code.fromIsbn13("9781234567890");

        Publication publication = new Publication(
                information,
                publicationType,
                user,
                PublicationStatus.FOR_SALE,
                edition,
                code
        );

        return new Item(Condition.NEW, weight, dimensions, publication);
    }


    @Test
    void createBookWithNoCodes()
    {
        Book book1 = Book.noCodes(sampleItem());

        assertNotNull(book1);
        assertNotNull(book1.getItem());
    }


    @Test
    void createBookWithISBN10Code()
    {
        Code isbn10 = Code.fromIsbn10("1234567890");
        Book book2 = Book.withIsbn10(sampleItem(), isbn10);

        assertNotNull(book2);
        assertNotNull(book2.getIsbn10());
        assertNotNull(book2.getItem());
    }

    @Test
    void createBookWithISBN13Code()
    {
        Code isbn13 = Code.fromIsbn13("1234567890000");
        Book book3 = Book.withIsbn13(sampleItem(), isbn13);

        assertNotNull(book3);
        assertNotNull(book3.getIsbn13());
        assertNotNull(book3.getItem());
    }

    @Test
    void createBookWithBothCodes()
    {
        Code isbn10 = Code.fromIsbn10("1234567890");
        Code isbn13 = Code.fromIsbn13("1234567890000");
        Book book4 = Book.withBoth(sampleItem(), isbn10, isbn13);

        assertNotNull(book4);
        assertNotNull(book4.getIsbn10());
        assertNotNull(book4.getIsbn13());
        assertNotNull(book4.getItem());
    }

}