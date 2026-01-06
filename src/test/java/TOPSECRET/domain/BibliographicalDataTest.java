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
        List<Profile> authors= new ArrayList<>();
        Profile author1 = new Profile("Tove Ditlevsen", DENMARK, LocalDate.of(1917,12,14), true);
        authors.add(author1);
        Locale language = ENGLISH;
        Address address1 = new Address("One Embassy Gardens","8", "SW11 7BW", Country.UNITED_KINGDOM);
        Email email1 = new Email("webfeedback", "penguin.co.uk");
        Contact contact1 = new Contact("2071393000", "+44", email1, address1);
        Publisher publisher = new Publisher("Penguin Books LTD", "GB102838980", address1, contact1);
        LocalDate publishDate = LocalDate.of(2021, 1, 1);
        List <Genre> genres = new ArrayList<>();
        genres.add(FICTION);
        int numberOfPages = 370;
        String chunksOfText = "And I want so badly to own my own time instead of always having to sell it.";

        // Act
        BibliographicalData data = new BibliographicalData(title, authors, language, publisher,
                publishDate, genres, numberOfPages, chunksOfText);

        // Assert
        assertEquals("Childhood, Youth, Dependency: The Copenhagen Trilogy", title);
        assertEquals("Tove Ditlevsen", author1.getName());
        assertEquals(ENGLISH, language);
        assertEquals("Penguin Books LTD", publisher.getName());
        assertEquals(LocalDate.of(2021, 1, 1),publishDate);
        assertEquals(genres, data.getGenres());
        assertEquals(370, numberOfPages);
        assertEquals("And I want so badly to own my own time instead of always having to sell it.", chunksOfText);
    }

    // Test with more than one author and genre
    @Test
    void testConstructorInitializedMoreThanOneAuthorAndGenre() {

        // Arrange
        String title = "The Age of AI: And Our Human Future";
        List<Profile> authors= new ArrayList<>();
        Profile author2 = new Profile("Henry A Kissinger", UNITED_STATES,LocalDate.of(1923,05,27), true);
        Profile author3 = new Profile("Eric Schmidt III", UNITED_STATES,LocalDate.of(1955,04,27), true);
        Profile author4 = new Profile("Daniel Huttenlocher",UNITED_STATES,LocalDate.of(1959,01,01), true );
        authors.add(author2);
        authors.add(author3);
        authors.add(author4);
        Locale language = ENGLISH;
        Address address2 = new Address("Carmelite House","50", "EC4Y 0DZ", Country.UNITED_KINGDOM);
        Email email2 = new Email("enquiries", "hachette.co.uk");
        Contact contact2 = new Contact("2031227222", "+44", email2, address2);
        Publisher publisher = new Publisher("John Murray Press", "GB205505305", address2, contact2);
        LocalDate publishDate = LocalDate.of(2022, 8, 4);
        List<Genre> genres = new ArrayList<>();
        genres.add(Genre.NONFICTION);
        genres.add(Genre.TECHNOLOGY);
        genres.add(Genre.SCIENCE);
        genres.add(Genre.SOCIAL_SCIENCES);
        genres.add(Genre.PHILOSOPHY);
        int numberOfPages = 272;
        String chunksOfText = " ";

        // Act
        BibliographicalData data = new BibliographicalData(title, authors, language, publisher,
                publishDate, genres, numberOfPages, chunksOfText);

        // Assert
        assertEquals("The Age of AI: And Our Human Future", title);
        assertEquals("Henry A Kissinger", author2.getName());
        assertEquals("Eric Schmidt III", author3.getName());
        assertEquals("Daniel Huttenlocher", author4.getName());
        assertEquals(ENGLISH, language);
        assertEquals("John Murray Press", publisher.getName());
        assertEquals(LocalDate.of(2022, 8, 4), publishDate);
        assertEquals(List.of(Genre.NONFICTION, Genre.TECHNOLOGY, Genre.SCIENCE, Genre.SOCIAL_SCIENCES, Genre.PHILOSOPHY),genres);
        assertEquals(272, numberOfPages);
        assertEquals(" ", chunksOfText);
    }
}