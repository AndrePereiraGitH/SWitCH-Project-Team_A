package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BibliographicalDataFactoryTest {

        @Test
        void create_shouldReturnValidBibliographicalData() {

            // Arrange
            BibliographicalDataFactory factory =
                    new BibliographicalDataFactory();

            Author author = mock(Author.class);
            Genre genre = mock(Genre.class);

            // Act
            BibliographicalData result = factory.create(
                    "Title",
                    List.of(author),
                    Locale.ENGLISH,
                    LocalDate.of(2010, 10, 10),
                    List.of(genre),
                    "Some text"
            );

            // Assert
            assertNotNull(result);
            assertEquals("Title", result.getTitle());
            assertEquals(1, result.getAuthors().size());
            assertEquals(1, result.getGenres().size());
        }


}