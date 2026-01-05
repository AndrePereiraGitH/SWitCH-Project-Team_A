package TOPSECRET.domain;

import TOPSECRET.domain.Enum.Country;
import TOPSECRET.domain.Enum.Genre;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static TOPSECRET.domain.Enum.Country.DENMARK;
import static TOPSECRET.domain.Enum.Country.UNITED_STATES;
import static TOPSECRET.domain.Enum.Genre.*;
import static java.util.Locale.ENGLISH;
import static org.junit.jupiter.api.Assertions.*;

class BibliographicalDataTest {

    // Test with only one author and one genre
    @Test
    void testConstructorInitializedOneAuthorAndOneGenre() {

        // Arrange
        String title = "Childhood, Youth, Dependency: The Copenhagen Trilogy";
        Profile[] authors = { new Profile("Tove Ditlevsen", DENMARK, LocalDate.of(1917,12,14), true) };
//        List<String> authors= new ArrayList<>();
//        authors.add("Tove Ditlevsen");
        Locale language = ENGLISH;
//        Publisher publisher = new Publisher("Penguin Books LTD");
        String publisher = "Penguin Books LTD";
        LocalDate publishDate = LocalDate.of(2021, 1, 1);
        Genre[] genres = new Genre[]{FICTION};
//        List<String> genres = new ArrayList<>();
//        genres.add("Fiction");
        int numberOfPages = 370;
        String chunksOfText = "And I want so badly to own my own time instead of always having to sell it.";

        // Act
        BibliographicalData data = new BibliographicalData(title, authors, language, publisher,
                publishDate, genres, numberOfPages, chunksOfText);

        // Assert
        assertEquals("Childhood, Youth, Dependency: The Copenhagen Trilogy", title);
//        assertEquals(List.of("Tove Ditlevsen"), authors);
        assertEquals("Tove Ditlevsen", authors[0].getName());
        assertEquals(ENGLISH, language);
        assertEquals("Penguin Books LTD", publisher);
        assertEquals(LocalDate.of(2021, 1, 1),publishDate);
//        assertEquals(List.of("Fiction"), genres);
        assertArrayEquals(genres, data.getGenres());
        assertEquals(370, numberOfPages);
        assertEquals("And I want so badly to own my own time instead of always having to sell it.", chunksOfText);
    }

    // Test with more than one author and genre
    @Test
    void testConstructorInitializedMoreThanOneAuthorAndGenre() {

        // Arrange
        String title = "The Age of AI: And Our Human Future";
        Profile[] authors = {
                new Profile("Henry A Kissinger", UNITED_STATES,LocalDate.of(1923,05,27), true),
                new Profile("Eric Schmidt III", UNITED_STATES,LocalDate.of(1955,04,27), true),
                new Profile("Daniel Huttenlocher",UNITED_STATES,LocalDate.of(1959,01,01), true )
        };
//        List<String> authors = new ArrayList<>();
//        authors.add("Henry A Kissinger" + "Eric Schmidt III" + "Daniel Huttenlocher");
        Locale language = ENGLISH;
//        Publisher publisher = new Publisher("John Murray Press");
        String publisher = "John Murray Press";
        LocalDate publishDate = LocalDate.of(2022, 8, 4);
        Genre[] genres = {
                Genre.NONFICTION,
                Genre.TECHNOLOGY,
                Genre.SCIENCE,
                Genre.BUSINESS,
                Genre.SOCIAL_SCIENCES,
                Genre.PHILOSOPHY,
        };
//        List<String> genres = new ArrayList<>();
//        genres.add("Nonfiction" + "Technology" + "Science" + "Business" + "Social Sciences" + "Philosophy");
        int numberOfPages = 272;
        String chunksOfText = " ";

        // Act
        BibliographicalData data = new BibliographicalData(title, authors, language, publisher,
                publishDate, genres, numberOfPages, chunksOfText);

        // Assert
        assertEquals("The Age of AI: And Our Human Future", title);
//        assertEquals(List.of("Henry A Kissinger" + "Eric Schmidt III" + "Daniel Huttenlocher"), authors);
        assertEquals("Henry A Kissinger", data.getAuthors()[0].getName());
        assertEquals("Eric Schmidt III", data.getAuthors()[1].getName());
        assertEquals("Daniel Huttenlocher", data.getAuthors()[2].getName());
        assertEquals(ENGLISH, language);
        assertEquals("John Murray Press", publisher);
        assertEquals(LocalDate.of(2022, 8, 4), publishDate);
//        assertEquals(List.of(NONFICTION, TECHNOLOGY, SCIENCE, BUSINESS, SOCIAL_SCIENCES, PHILOSOPHY), genres);
        assertArrayEquals(genres, data.getGenres());
        assertEquals(272, numberOfPages);
        assertEquals(" ", chunksOfText);
    }
}