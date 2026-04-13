package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.ListingId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the US027ListingDTO record.
 * <p>
 * These tests verify that the DTO is correctly created with valid data, while
 * ensuring that invalid arguments (null, blank fields, or invalid author lists)
 * trigger the expected exceptions.
 * </p>
 */
class US027ListingDTOTest {

    @Test
    void shouldCreateDTOWhenArgumentsAreValid() {
        // Arrange
        ListingId id = new ListingId("listingId1");
        List<String> itemsTitle = List.of("title1", "title2");
        List<String> authors = List.of("Author One", "Author Two");

        // Act
        US027ListingDTO dto = new US027ListingDTO(id, "Listing Title", "10.99",itemsTitle, authors);

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
                () -> new US027ListingDTO(null, "Title", "10", itemsTitle, authors)
        );
    }

    @Test
    void shouldThrowWhenListingTitleIsNullOrBlank() {
        // Arrange
        ListingId id = new ListingId("listingId1");
        List<String> itemsTitle = List.of("title1", "title2");
        List<String> authors = List.of("Author One", "Author Two");

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US027ListingDTO(id, null, "10", itemsTitle, authors));

        assertThrows(IllegalArgumentException.class,
                () -> new US027ListingDTO(id, "   ", "10", itemsTitle, authors));
    }

    @Test
    void shouldThrowWhenPriceIsNullOrBlank() {
        // Arrange
        ListingId id = new ListingId("listingId1");
        List<String> itemsTitle = List.of("title1", "title2");
        List<String> authors = List.of("Author One", "Author Two");

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US027ListingDTO(id, "Title", null, itemsTitle, authors));

        assertThrows(IllegalArgumentException.class,
                () -> new US027ListingDTO(id, "Title", "   ", itemsTitle, authors));
    }

    @Test
    void shouldThrowWhenItemTitlesIsNullOrEmptyList() {
        // Arrange
        ListingId id = new ListingId("listingId1");
        List<String> authors = List.of("Author One", "Author Two");

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US027ListingDTO(id, "Title", "10", null, authors));

        assertThrows(IllegalArgumentException.class,
                () -> new US027ListingDTO(id, "Title", "10", List.of(), authors));
    }

    @Test
    void shouldThrowWhenAuthorsListIsNull() {
        // Arrange
        ListingId id = new ListingId("listingId1");
        List<String> itemsTitle = List.of("title1", "title2");

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new US027ListingDTO(id, "Title", "10", itemsTitle, null));
    }

    @Test
    void shouldThrowWhenAuthorsListIsEmpty() {
        // Arrange
        ListingId id = new ListingId("listingId1");
        List<String> itemsTitle = List.of("title1", "title2");

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US027ListingDTO(id, "Title", "10", itemsTitle, List.of()));
    }

    @Test
    void shouldThrowWhenAuthorsListContainsBlankNames() {
        // Arrange
        ListingId id = new ListingId("listingId1");
        List<String> itemsTitle = List.of("title1", "title2");
        List<String> authors = List.of("Author One", "   ");

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US027ListingDTO(id, "Title", "10", itemsTitle, authors));
    }
}
