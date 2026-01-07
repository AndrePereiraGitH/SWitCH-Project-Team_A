package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static TOPSECRET.domain.Enum.Country.SOUTH_AFRICA;
import static TOPSECRET.domain.Enum.Country.UNITED_STATES;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private BibliographicalData sampleInformation()
    {
        // Bibliographical Data creation

        String title = "The Lord of the Rings";

        Email publisherEmail = new Email("HarperCollins", "gmail.com");
        Address publisherAddress = new Address("Broadway","195","10007", Country.UNITED_STATES);
        Contact publisherContact = new Contact("987654321", "+351", publisherEmail, publisherAddress);
        Publisher publisher = new Publisher("HarperCollins Publishers","123456", publisherContact);

        LocalDate publishDate = LocalDate.of(1954,07,29);

        List<Genre> genres = new ArrayList<>();
        genres.add(Genre.FICTION);
        genres.add(Genre.FANTASY);
        genres.add(Genre.ADVENTURE);

        List<Profile> authors= new ArrayList<>();
        Profile author = new Profile("J.R.R Tolkien", SOUTH_AFRICA,LocalDate.of(1892,01,03), true);
        authors.add(author);

        int numOfPages = 1100;

        BibliographicalData information = new BibliographicalData(title, authors, Locale.ENGLISH, publisher, publishDate, genres, numOfPages, "I will take the Ring,' he said, 'Though I do not know the way.");

        return information;
    }

    private Item sampleItem()
    {
        //Item creation

        double weight = 0.7;

        List<Picture> pictures = new ArrayList<>();
        Picture picture1 = new Picture("picture1");
        Picture picture2 = new Picture("picture2");
        Picture picture3 = new Picture("picture3");

        Dimensions dimensions = new Dimensions(12.2,8.7,0.2);

        String edition = "Sample Edition";

        BibliographicalData information = sampleInformation();

        Item item = new Item (Condition.NEW, weight, pictures, dimensions, TypeOfBinding.HARDBACK, edition, information);

        return item;
    }


    @Test
    void createBookWithNoCodes()
    {
        Book book1 = Book.noCodes(sampleItem(), sampleInformation());

        Item item1 = book1.getItem();

        BibliographicalData information1 = book1.getInformation();

        assertNotNull(book1);
        assertNotNull(item1);
        assertNotNull(information1);
    }


    @Test
    void createBookWithISBN10Code()
    {
        Code isbn10 = Code.fromIsbn10("1234567890");
        Book book2 = Book.withIsbn10(sampleItem(), isbn10, sampleInformation());

        assertNotNull(book2);
        assertNotNull(book2.getIsbn10());
        assertNotNull(book2.getInformation());
        assertNotNull(book2.getItem());
    }

    @Test
    void createBookWithISBN13Code()
    {
        Code isbn13 = Code.fromIsbn13("1234567890000");
        Book book3 = Book.withIsbn13(sampleItem(), isbn13, sampleInformation());

        assertNotNull(book3);
        assertNotNull(book3.getIsbn13());
        assertNotNull(book3.getInformation());
        assertNotNull(book3.getItem());
    }

    @Test
    void createBookWithBothCodes()
    {
        Code isbn10 = Code.fromIsbn10("1234567890");
        Code isbn13 = Code.fromIsbn13("1234567890000");
        Book book4 = Book.withBoth(sampleItem(), isbn10, isbn13, sampleInformation());

        assertNotNull(book4);
        assertNotNull(book4.getIsbn10());
        assertNotNull(book4.getIsbn13());
        assertNotNull(book4.getInformation());
        assertNotNull(book4.getItem());
    }

}