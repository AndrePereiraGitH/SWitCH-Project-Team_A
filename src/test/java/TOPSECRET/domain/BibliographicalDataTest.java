package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.ENGLISH;
import static org.junit.jupiter.api.Assertions.*;

class BibliographicalDataTest {

    // Sample Builder With Valid Attributes
    private BibliographicalData sampleBibliographicalData() {

        // arrange
        String title = "The Age of AI: And Our Human Future";
        //List<Profile> authors= new ArrayList<>();
        List<Author> authors = new ArrayList<>();
        Country country = new Country("UNITED_STATES");
        Profile profile2 = new Profile("Henry A Kissinger", country,LocalDate.of(1923,05,27));
        Profile profile3 = new Profile("Eric Schmidt III", country,LocalDate.of(1955,04,27));
        Profile profile4 = new Profile("Daniel Huttenlocher", country,LocalDate.of(1959,01,01));
        Author author2 = new Author(profile2, "valid bio2");
        Author author3 = new Author(profile3, "valid bio3");
        Author author4 = new Author(profile4, "valid bio4");
        authors.add(author2);
        authors.add(author3);
        authors.add(author4);

        Locale language = ENGLISH;


        // publisher became an attribute of the Edition class
//        Country country1 = new Country("UNITED_KINGDOM");
//        Address address2 = new Address("Carmelite House","50", "EC4Y 0DZ", country1);
//        Email email2 = new Email("enquiries", "hachette.co.uk");
//        Contact contact2 = new Contact("2031227222", "+44", email2, address2);
//        Publisher publisher = new Publisher("John Murray Press", "GB205505305", contact2);

        LocalDate publishDate = LocalDate.of(2022, 8, 4);


        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre ("Fantasy"));

        // numberOfPages became an attribute of the PhysicalAttributes class
//        int numberOfPages = 272;

        String chunksOfText = "The sweet spot is in partnership—AI handles scale, I bring empathy, wisdom, and context.";

        // act
//        return new BibliographicalData(title, authors, language, publisher, publishDate, genres, numberOfPages, chunksOfText);
        return new BibliographicalData(title, authors, language, publishDate, genres, chunksOfText);
    }

    // Test With More Than One Author And Genre
    @Test
    void testConstructorInitializedMoreThanOneAuthorAndGenre() {

        // act
        BibliographicalData bibliographicalData = sampleBibliographicalData();

        // assert
        assertEquals("The Age of AI: And Our Human Future", bibliographicalData.getTitle());

        assertEquals(3, bibliographicalData.getAuthors().size());
//        assertEquals("Henry A Kissinger", bibliographicalData.getAuthors().get(0).getName());
//        assertEquals("Eric Schmidt III", bibliographicalData.getAuthors().get(1).getName());
//        assertEquals("Daniel Huttenlocher", bibliographicalData.getAuthors().get(2).getName());
        assertEquals("Henry A Kissinger", bibliographicalData.getAuthors().get(0).getProfile().getName());
        assertEquals("Eric Schmidt III", bibliographicalData.getAuthors().get(1).getProfile().getName());
        assertEquals("Daniel Huttenlocher", bibliographicalData.getAuthors().get(2).getProfile().getName());

        assertEquals(Locale.ENGLISH, bibliographicalData.getLanguage());

//        assertEquals("John Murray Press", bibliographicalData.getPublisher().getName());

        assertEquals(LocalDate.of(2022, 8, 4), bibliographicalData.getPublishDate());

        assertEquals("Fantasy", bibliographicalData.getGenres().get(0).getGenreType());

//        assertEquals(272, bibliographicalData.getNumberOfPages());

        assertEquals("The sweet spot is in partnership—AI handles scale, I bring empathy, wisdom, and context.", bibliographicalData.getChunksOfText());
    }

    // Test To Verify That Authors and Genre Lists Are Not Empty
    @Test
    void testAuthorsListAndGenreListAreNotEmpty() {

        // act
        BibliographicalData bibliographicalData = sampleBibliographicalData();

        // assert
        assertFalse(bibliographicalData.getAuthors().isEmpty());
        assertFalse(bibliographicalData.getGenres().isEmpty());
    }

    // Test Number of Pages Is Positive
//  @Test
//  void testNumberOfPagesIsPositive() {
//        BibliographicalData data = sampleBibliographicalData();
//        assertTrue(data.getNumberOfPages() > 0);
//  }


    // Sample Builder With Empty Authors List
    private BibliographicalData sampleBibliographicalDataWithEmptyAuthorsList() {

        // act
        BibliographicalData bibliographicalData = sampleBibliographicalData();

//      return new BibliographicalData(bibliographicalData.getTitle(), List.of(), bibliographicalData.getLanguage(),
//                    bibliographicalData.getPublisher(), bibliographicalData.getPublishDate(), bibliographicalData.getGenres(), bibliographicalData.getNumberOfPages(),
//                    bibliographicalData.getChunksOfText());
          return new BibliographicalData(bibliographicalData.getTitle(), List.of(), bibliographicalData.getLanguage(),
                    bibliographicalData.getPublishDate(), bibliographicalData.getGenres(), bibliographicalData.getChunksOfText());
    }

    // Test To Throw Error If Authors Lists Is Empty
    @Test
    void testConstructorThrowsExceptionWhenAuthorsListIsEmpty() {

        // assert
        assertThrows(IllegalArgumentException.class, this::sampleBibliographicalDataWithEmptyAuthorsList);
    }

    // Sample Builder With Empty Genres List
    private BibliographicalData sampleBibliographicalDataWithEmptyGenresList() {

        // act
        BibliographicalData bibliographicalData = sampleBibliographicalData();

//      return new BibliographicalData(bibliographicalData.getTitle(), bibliographicalData.getAuthors(), bibliographicalData.getLanguage(),
//                    bibliographicalData.getPublisher(), bibliographicalData.getPublishDate(), List.of(), bibliographicalData.getNumberOfPages(),
//                    bibliographicalData.getChunksOfText());
        return new BibliographicalData(bibliographicalData.getTitle(), bibliographicalData.getAuthors(), bibliographicalData.getLanguage(),
                bibliographicalData.getPublishDate(), List.of(),bibliographicalData.getChunksOfText());
    }

    // Test To Throw Error If Genre Lists Is Empty
    @Test
    void testConstructorThrowsExceptionWhenGenresListIsEmpty() {

        // assert
        assertThrows(IllegalArgumentException.class, this::sampleBibliographicalDataWithEmptyGenresList);
    }

    // Sample Builder With Number of Pages Not Positive
//  private BibliographicalData sampleBibliographicalDataWithNumberOfPagesNotPositive() {
//
//      // act
//      BibliographicalData bibliographicalData = sampleBibliographicalData();
//
//      return new BibliographicalData(bibliographicalData.getTitle(), bibliographicalData.getAuthors(), bibliographicalData.getLanguage(),
//                    bibliographicalData.getPublisher(), bibliographicalData.getPublishDate(), bibliographicalData.getGenres(), 0,
//                    bibliographicalData.getChunksOfText());
//  }

    // Test To Throw Error If NumberOfPages Is Not Positive
//    @Test
//    void testConstructorThrowsExceptionWhenNumberOfPagesNotPositive() {
//
//        // assert
//        assertThrows(IllegalArgumentException.class, this::sampleBibliographicalDataWithNumberOfPagesNotPositive);
//    }
}