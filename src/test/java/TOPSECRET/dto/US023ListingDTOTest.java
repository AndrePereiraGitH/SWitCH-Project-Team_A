package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.dto.listing.US023ListingDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for the {@link US023ListingDTO} record.
 * <p>
 * Verifies correct field initialization, null-guard validation,
 * and the record-generated equals, hashCode, and toString behaviour.
 * </p>
 */
class US023ListingDTOTest {

    @Test
    void shouldCreateDTOWhenArgumentsAreValid() {
        // Arrange
        String id = mock(ListingId.class).toString();
        List<String> itemsTitle = List.of("title1", "title2");
        List<String> authors = List.of("Author One", "Author Two");

        // Act
        US023ListingDTO dto = new US023ListingDTO(id, "Listing Title", "10.99",itemsTitle, authors);

        // Assert
        assertEquals(id, dto.listingId());
        assertEquals("Listing Title", dto.listingTitle());
        assertEquals("10.99", dto.price());
        assertEquals(itemsTitle, dto.pubTitles());
        assertEquals(authors, dto.authorsName());
    }

    @Test
    void shouldThrowWhenListingIdIsNull() {
        // Arrange
        List<String> itemsTitle = List.of("title1", "title2");
        List<String> authors = List.of("Author");

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new US023ListingDTO(null, "Title", "10", itemsTitle, authors)
        );
    }

    @Test
    void shouldThrowWhenListingTitleIsNullOrBlank() {
        // Arrange
        String id = mock(ListingId.class).toString();
        List<String> itemsTitle = List.of("title1", "title2");
        List<String> authors = List.of("Author One", "Author Two");

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US023ListingDTO(id, null, "10", itemsTitle, authors));

        assertThrows(IllegalArgumentException.class,
                () -> new US023ListingDTO(id, "   ", "10", itemsTitle, authors));
    }

    @Test
    void shouldThrowWhenPriceIsNullOrBlank() {
        // Arrange
        String id = mock(ListingId.class).toString();
        List<String> itemsTitle = List.of("title1", "title2");
        List<String> authors = List.of("Author One", "Author Two");

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US023ListingDTO(id, "Title", null, itemsTitle, authors));

        assertThrows(IllegalArgumentException.class,
                () -> new US023ListingDTO(id, "Title", "   ", itemsTitle, authors));
    }

    @Test
    void shouldThrowWhenItemTitlesIsNullOrEmptyList() {
        // Arrange
        String id = mock(ListingId.class).toString();
        List<String> authors = List.of("Author One", "Author Two");

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US023ListingDTO(id, "Title", "10", null, authors));

        assertThrows(IllegalArgumentException.class,
                () -> new US023ListingDTO(id, "Title", "10", List.of(), authors));
    }

    @Test
    void shouldThrowWhenAuthorsListIsNull() {
        // Arrange
        String id = mock(ListingId.class).toString();
        List<String> itemsTitle = List.of("title1", "title2");

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new US023ListingDTO(id, "Title", "10", itemsTitle, null));
    }

    @Test
    void shouldThrowWhenAuthorsListIsEmpty() {
        // Arrange
        String id = mock(ListingId.class).toString();
        List<String> itemsTitle = List.of("title1", "title2");

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US023ListingDTO(id, "Title", "10", itemsTitle, List.of()));
    }

    @Test
    void shouldThrowWhenAuthorsListContainsBlankNames() {
        // Arrange
        String id = mock(ListingId.class).toString();;
        List<String> itemsTitle = List.of("title1", "title2");
        List<String> authors = List.of("Author One", "   ");

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US023ListingDTO(id, "Title", "10", itemsTitle, authors));
    }
}