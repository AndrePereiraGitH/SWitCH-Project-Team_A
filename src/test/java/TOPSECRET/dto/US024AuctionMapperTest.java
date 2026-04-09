package TOPSECRET.dto;

import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link US024AuctionMapper}.
 *
 * <p>Verifies the correct behaviour of the mapping methods:
 * <ul>
 *   <li>{@link US024AuctionMapper#toDTO(Listing)} - maps a single {@link Listing} to a {@link US024AuctionDTO}</li>
 *   <li>{@link US024AuctionMapper#toDTOs(List)} - maps a list of {@link Listing} to a list of {@link US024AuctionDTO}</li>
 * </ul>
 *
 * <p>All {@link Listing} dependencies are replaced with Mockito doubles
 * to ensure tests are isolated from domain logic.
 */

class US024AuctionMapperTest {


    // -------------------------
    // Constructor
    // -------------------------

    @Test
    void constructor_ShouldThrowUnsupportedOperationException_WhenInstantiated() throws Exception {
        //Arrange
        Constructor<US024AuctionMapper> constructor = US024AuctionMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        //Act + Assert
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }


    // -------------------------
    // toDTO
    // -------------------------

    @Test
    void toDTO_ShouldReturnDTO_WhenListingIsValid() {
        //Arrange
        Listing listingDouble = createListingDouble("45", "shortDescription", "ListingTitle");

        //Act
        US024AuctionDTO result = US024AuctionMapper.toDTO(listingDouble);

        //Assert
        assertNotNull(result);
    }

    @Test
    void toDTO_ShouldThrowException_WhenListingIsNull() {
        //Act + Assert
        assertThrows(NullPointerException.class, () ->
                US024AuctionMapper.toDTO(null)
        );
    }

    // -------------------------
    // toDTOs
    // -------------------------

    @Test
    void toDTOs_ShouldThrowIllegalArgumentException_WhenListIsNull() {
        //Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                US024AuctionMapper.toDTOs(null)
        );
    }

    @Test
    void toDTOs_ShouldReturnEmptyList_WhenListIsEmpty() {
        //Arrange
        List<Listing> listings = List.of();

        //Act
        List<US024AuctionDTO> result = US024AuctionMapper.toDTOs(listings);

        //Assert
        assertEquals(0, result.size()); // Bug fix: was 3
    }

    @Test
    void toDTOs_ShouldReturnListWithOneDTO_WhenListHasOneListing() {
        //Arrange
        Listing listingDouble = createListingDouble("45", "shortDescription", "ListingTitle");
        List<Listing> listings = List.of(listingDouble);

        //Act
        List<US024AuctionDTO> result = US024AuctionMapper.toDTOs(listings);

        //Assert
        assertEquals(1, result.size());
    }

    @Test
    void toDTOs_ShouldReturnListWithMultipleDTOs_WhenListHasMultipleListings() {
        //Arrange
        Listing listingDouble_1 = createListingDouble("45", "shortDescription", "ListingTitle");
        Listing listingDouble_2 = createListingDouble("45", "shortDescription", "ListingTitle");
        Listing listingDouble_3 = createListingDouble("45", "shortDescription", "ListingTitle");
        List<Listing> listings = List.of(listingDouble_1, listingDouble_2, listingDouble_3);

        //Act
        List<US024AuctionDTO> result = US024AuctionMapper.toDTOs(listings);

        //Assert
        assertEquals(3, result.size());
    }


    // -------------------------
    // Helpers
    // -------------------------

    private Listing createListingDouble(String price, String shortDescription, String listingTitle) {
        ListingId listingId = mock(ListingId.class);
        when(listingId.getId()).thenReturn(UUID.randomUUID().toString());

        Price priceValue = mock(Price.class);
        when(priceValue.getPrice()).thenReturn(new BigDecimal(price));

        Listing listingDouble = mock(Listing.class);
        when(listingDouble.id()).thenReturn(listingId);
        when(listingDouble.getPrice()).thenReturn(priceValue);
        when(listingDouble.getShortDescription()).thenReturn(shortDescription);
        when(listingDouble.getListingTitle()).thenReturn(listingTitle);

        return listingDouble;
    }
}