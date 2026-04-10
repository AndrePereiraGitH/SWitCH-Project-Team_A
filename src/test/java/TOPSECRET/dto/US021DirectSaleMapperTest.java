package TOPSECRET.dto;

import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class US021DirectSaleMapperTest {
    private final US021DirSaleListingMapper mapper = new US021DirSaleListingMapper();

    // ============================================================
    // toDTO() Tests
    // ============================================================

    @Test
    void shouldConvertListingToDTO() {
        // Arrange
        ListingId listingId = mock(ListingId.class);
        when(listingId.toString()).thenReturn("listing-123");

        Price price = mock(Price.class);
        when(price.getPrice()).thenReturn(new BigDecimal("15.99"));

        UserId sellerId = mock(UserId.class);
        when(sellerId.toString()).thenReturn("seller-456");

        Listing listing = mock(Listing.class);
        when(listing.id()).thenReturn(listingId);
        when(listing.getListingTitle()).thenReturn("Harry Potter");
        when(listing.getPrice()).thenReturn(price);
        when(listing.getShortDescription()).thenReturn("Book in excellent condition");
        when(listing.getSellerId()).thenReturn(sellerId);

        // Act
        US021DirSaleListingDTO result = mapper.toDTO(listing);

        // Assert
        assertEquals("listing-123", result.listingId());
        assertEquals("Harry Potter", result.listingTitle());
        assertEquals(new BigDecimal("15.99"), result.price());
        assertEquals("Book in excellent condition", result.shortDescription());
        assertEquals("seller-456", result.sellerId());
    }

    @Test
    void shouldConvertListingWithZeroPriceToDTO() {
        // Arrange
        ListingId listingId = mock(ListingId.class);
        when(listingId.toString()).thenReturn("listing-123");

        Price price = mock(Price.class);
        when(price.getPrice()).thenReturn(BigDecimal.ZERO);

        UserId sellerId = mock(UserId.class);
        when(sellerId.toString()).thenReturn("seller-456");

        Listing listing = mock(Listing.class);
        when(listing.id()).thenReturn(listingId);
        when(listing.getListingTitle()).thenReturn("Free Book");
        when(listing.getPrice()).thenReturn(price);
        when(listing.getShortDescription()).thenReturn("Free to a good home");
        when(listing.getSellerId()).thenReturn(sellerId);

        // Act
        US021DirSaleListingDTO result = mapper.toDTO(listing);

        // Assert
        assertEquals("listing-123", result.listingId());
        assertEquals("Free Book", result.listingTitle());
        assertEquals(BigDecimal.ZERO, result.price());
        assertEquals("Free to a good home", result.shortDescription());
        assertEquals("seller-456", result.sellerId());
    }

    @Test
    void shouldConvertListingWithEmptyDescriptionToDTO() {
        // Arrange
        ListingId listingId = mock(ListingId.class);
        when(listingId.toString()).thenReturn("listing-123");

        Price price = mock(Price.class);
        when(price.getPrice()).thenReturn(new BigDecimal("10.00"));

        UserId sellerId = mock(UserId.class);
        when(sellerId.toString()).thenReturn("seller-456");

        Listing listing = mock(Listing.class);
        when(listing.id()).thenReturn(listingId);
        when(listing.getListingTitle()).thenReturn("Book");
        when(listing.getPrice()).thenReturn(price);
        when(listing.getShortDescription()).thenReturn("");
        when(listing.getSellerId()).thenReturn(sellerId);

        // Act
        US021DirSaleListingDTO result = mapper.toDTO(listing);

        // Assert
        assertEquals("listing-123", result.listingId());
        assertEquals("Book", result.listingTitle());
        assertEquals(new BigDecimal("10.00"), result.price());
        assertEquals("", result.shortDescription());
        assertEquals("seller-456", result.sellerId());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenListingIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> mapper.toDTO(null));
    }

    // ============================================================
    // toDTOs() Tests
    // ============================================================

    @Test
    void shouldConvertEmptyListToEmptyList() {
        // Arrange
        List<Listing> emptyList = new ArrayList<>();

        // Act
        List<US021DirSaleListingDTO> result = mapper.toDTOs(emptyList);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldConvertSingleListingToListOfOneDTO() {
        // Arrange
        ListingId listingId = mock(ListingId.class);
        when(listingId.toString()).thenReturn("listing-123");

        Price price = mock(Price.class);
        when(price.getPrice()).thenReturn(new BigDecimal("15.99"));

        UserId sellerId = mock(UserId.class);
        when(sellerId.toString()).thenReturn("seller-456");

        Listing listing = mock(Listing.class);
        when(listing.id()).thenReturn(listingId);
        when(listing.getListingTitle()).thenReturn("Harry Potter");
        when(listing.getPrice()).thenReturn(price);
        when(listing.getShortDescription()).thenReturn("Good condition");
        when(listing.getSellerId()).thenReturn(sellerId);

        // Act

        List<US021DirSaleListingDTO> result = mapper.toDTOs(List.of(listing));

        // Assert
        assertEquals(1, result.size());
        assertEquals("listing-123", result.get(0).listingId());
        assertEquals("Harry Potter", result.get(0).listingTitle());
        assertEquals(new BigDecimal("15.99"), result.get(0).price());
        assertEquals("Good condition", result.get(0).shortDescription());
        assertEquals("seller-456", result.get(0).sellerId());
    }

    @Test
    void shouldConvertMultipleListingsToListOfMultipleDTOs() {
        // Arrange
        ListingId listingId1 = mock(ListingId.class);
        when(listingId1.toString()).thenReturn("listing-1");

        Price price1 = mock(Price.class);
        when(price1.getPrice()).thenReturn(new BigDecimal("10.00"));

        UserId sellerId1 = mock(UserId.class);
        when(sellerId1.toString()).thenReturn("seller-1");

        Listing listing1 = mock(Listing.class);
        when(listing1.id()).thenReturn(listingId1);
        when(listing1.getListingTitle()).thenReturn("Book One");
        when(listing1.getPrice()).thenReturn(price1);
        when(listing1.getShortDescription()).thenReturn("Description one");
        when(listing1.getSellerId()).thenReturn(sellerId1);

        ListingId listingId2 = mock(ListingId.class);
        when(listingId2.toString()).thenReturn("listing-2");

        Price price2 = mock(Price.class);
        when(price2.getPrice()).thenReturn(new BigDecimal("20.00"));

        UserId sellerId2 = mock(UserId.class);
        when(sellerId2.toString()).thenReturn("seller-2");

        Listing listing2 = mock(Listing.class);
        when(listing2.id()).thenReturn(listingId2);
        when(listing2.getListingTitle()).thenReturn("Book Two");
        when(listing2.getPrice()).thenReturn(price2);
        when(listing2.getShortDescription()).thenReturn("Description two");
        when(listing2.getSellerId()).thenReturn(sellerId2);

        List<Listing> listings = List.of(listing1, listing2);

        // Act
        List<US021DirSaleListingDTO> result = mapper.toDTOs(listings);

        // Assert
        assertEquals(2, result.size());

        assertEquals("listing-1", result.get(0).listingId());
        assertEquals("Book One", result.get(0).listingTitle());
        assertEquals(new BigDecimal("10.00"), result.get(0).price());

        assertEquals("Description one", result.get(0).shortDescription());
        assertEquals("seller-1", result.get(0).sellerId());
        assertEquals("listing-2", result.get(1).listingId());
        assertEquals("Book Two", result.get(1).listingTitle());
        assertEquals(new BigDecimal("20.00"), result.get(1).price());

        assertEquals("Description two", result.get(1).shortDescription());
        assertEquals("seller-2", result.get(1).sellerId());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenListingsContainsNull() {
        // Arrange
        ListingId listingId = mock(ListingId.class);
        when(listingId.toString()).thenReturn("listing-123");

        Price price = mock(Price.class);
        when(price.getPrice()).thenReturn(new BigDecimal("15.99"));

        UserId sellerId = mock(UserId.class);
        when(sellerId.toString()).thenReturn("seller-456");

        Listing listing = mock(Listing.class);
        when(listing.id()).thenReturn(listingId);
        when(listing.getListingTitle()).thenReturn("Harry Potter");
        when(listing.getPrice()).thenReturn(price);
        when(listing.getShortDescription()).thenReturn("Good condition");
        when(listing.getSellerId()).thenReturn(sellerId);

        List<Listing> listingsWithNull = new ArrayList<>();
        listingsWithNull.add(listing);
        listingsWithNull.add(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> mapper.toDTOs(listingsWithNull));
    }

    @Test
    void shouldMaintainOrderWhenConvertingList() {
        // Arrange
        ListingId listingId1 = mock(ListingId.class);
        when(listingId1.toString()).thenReturn("listing-1");

        Price price1 = mock(Price.class);
        when(price1.getPrice()).thenReturn(new BigDecimal("10.00"));

        UserId sellerId1 = mock(UserId.class);
        when(sellerId1.toString()).thenReturn("seller-1");

        Listing listing1 = mock(Listing.class);
        when(listing1.id()).thenReturn(listingId1);
        when(listing1.getListingTitle()).thenReturn("First");
        when(listing1.getPrice()).thenReturn(price1);
        when(listing1.getShortDescription()).thenReturn("First description");
        when(listing1.getSellerId()).thenReturn(sellerId1);

        ListingId listingId2 = mock(ListingId.class);
        when(listingId2.toString()).thenReturn("listing-2");

        Price price2 = mock(Price.class);
        when(price2.getPrice()).thenReturn(new BigDecimal("20.00"));

        UserId sellerId2 = mock(UserId.class);
        when(sellerId2.toString()).thenReturn("seller-2");

        Listing listing2 = mock(Listing.class);
        when(listing2.id()).thenReturn(listingId2);
        when(listing2.getListingTitle()).thenReturn("Second");
        when(listing2.getPrice()).thenReturn(price2);
        when(listing2.getShortDescription()).thenReturn("Second description");
        when(listing2.getSellerId()).thenReturn(sellerId2);

        List<Listing> listings = List.of(listing1, listing2);

        // Act
        List<US021DirSaleListingDTO> result = mapper.toDTOs(listings);

        // Assert
        assertEquals("listing-1", result.get(0).listingId());
        assertEquals("First", result.get(0).listingTitle());
        assertEquals("listing-2", result.get(1).listingId());
        assertEquals("Second", result.get(1).listingTitle());
    }

}