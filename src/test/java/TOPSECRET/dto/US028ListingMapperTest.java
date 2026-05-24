package TOPSECRET.dto;

import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link US028ListingMapper}.
 *
 * <p>Verifies the correct behaviour of the mapping method:
 * <ul>
 *   <li>{@link US028ListingMapper#toDTO(List)} - maps a list of {@link Listing}
 *   to a list of {@link US028ListingDTO}</li>
 * </ul>
 *
 * <p>All {@link Listing} dependencies are replaced with Mockito doubles
 * to ensure tests are isolated from domain logic.
 */
class US028ListingMapperTest {

    // -------------------------
    // toDTO
    // -------------------------

    @Test
    void toDTO_ShouldReturnEmptyList_WhenListIsEmpty() {
        // Arrange
        US028ListingMapper mapper = new US028ListingMapper();
        List<Listing> listings = List.of();

        // Act
        List<US028ListingDTO> result = mapper.toDTO(listings);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void toDTO_ShouldReturnListWithOneDTO_WhenListHasOneListing() {
        // Arrange
        US028ListingMapper mapper = new US028ListingMapper();
        Listing listingDouble = createListingDouble("45", "shortDescription", "ListingTitle");
        List<Listing> listings = List.of(listingDouble);

        // Act
        List<US028ListingDTO> result = mapper.toDTO(listings);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("shortDescription", result.get(0).description());
        assertEquals("ListingTitle", result.get(0).listing());
    }

    @Test
    void toDTO_ShouldReturnListWithMultipleDTOs_WhenListHasMultipleListings() {
        // Arrange
        US028ListingMapper mapper = new US028ListingMapper();
        Listing listingDouble1 = createListingDouble("45", "shortDescription1", "ListingTitle1");
        Listing listingDouble2 = createListingDouble("50", "shortDescription2", "ListingTitle2");
        Listing listingDouble3 = createListingDouble("60", "shortDescription3", "ListingTitle3");
        List<Listing> listings = List.of(listingDouble1, listingDouble2, listingDouble3);

        // Act
        List<US028ListingDTO> result = mapper.toDTO(listings);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void toDTO_ShouldMapAllFieldsCorrectly_WhenListingIsValid() {
        // Arrange
        US028ListingMapper mapper = new US028ListingMapper();
        Listing listingDouble = createListingDouble("45", "shortDescription", "ListingTitle");
        List<Listing> listings = List.of(listingDouble);

        // Act
        List<US028ListingDTO> result = mapper.toDTO(listings);

        // Assert
        US028ListingDTO dto = result.get(0);

        assertNotNull(dto);
        assertNotNull(dto.listingId());
        assertEquals("45", dto.price());
        assertEquals("shortDescription", dto.description());
        assertEquals("ListingTitle", dto.listing());
    }
    @Test
    void shouldThrowNullPointerExceptionWhenListingsIsNull() {
        // arrange
        US028ListingMapper mapper = new US028ListingMapper();

        // act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> mapper.toDTO(null)
        );

        // assert
        assertEquals("List of listings cannot be null", exception.getMessage());
    }

    // -------------------------
    // Helpers
    // -------------------------

    private Listing createListingDouble(String price, String shortDescription, String listingTitle) {
        // Arrange
        ListingId listingId = mock(ListingId.class);
        when(listingId.toString()).thenReturn(UUID.randomUUID().toString());

        Price priceValue = mock(Price.class);
        when(priceValue.toString()).thenReturn(price);

        Listing listingDouble = mock(Listing.class);
        when(listingDouble.id()).thenReturn(listingId);
        when(listingDouble.getPrice()).thenReturn(priceValue);
        when(listingDouble.getShortDescription()).thenReturn(shortDescription);
        when(listingDouble.getListingTitle()).thenReturn(listingTitle);

        // Assert
        return listingDouble;
    }
}