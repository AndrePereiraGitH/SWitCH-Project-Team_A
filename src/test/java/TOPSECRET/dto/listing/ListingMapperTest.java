package TOPSECRET.dto.listing;


import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.listing.response.ListingResponseDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListingMapperTest {

    private final ListingMapper mapper = new ListingMapper();


    @Test
    void toResponseDTO_shouldMapAllFieldsCorrectly() {

        // arrange
        Listing listing = mock(Listing.class);

        ListingId listingId = mock(ListingId.class);
        UserId sellerId = mock(UserId.class);

        BibliographicalData data1 = mock(BibliographicalData.class);
        BibliographicalData data2 = mock(BibliographicalData.class);

        Price priceDouble = mock(Price.class);

        Picture picture1 = mock(Picture.class);
        Picture picture2 = mock(Picture.class);

        LocalDate publicationDate = LocalDate.now();

        when(listing.id()).thenReturn(listingId);
        when(listingId.getId()).thenReturn("id1");

        when(listing.getListingTitle()).thenReturn("listing title");

        when(data1.getTitle()).thenReturn("data title 1");
        when(data2.getTitle()).thenReturn("data title 1");


        when(listing.getPrice()).thenReturn(priceDouble);

        when(listing.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(priceDouble.getPrice()).thenReturn(new BigDecimal("10.00"));

        when(listing.getShortDescription()).thenReturn("short description");

        when(listing.getSellerId()).thenReturn(sellerId);
        when(sellerId.getUserId()).thenReturn("joao");

        when(picture1.getPicture()).thenReturn("pic1.jpg");
        when(picture2.getPicture()).thenReturn("pic2.jpg");
        when(listing.getPictures()).thenReturn(List.of(picture1, picture2));

        when(listing.getPublicationDate()).thenReturn(publicationDate);

        // act

        ListingResponseDTO result = mapper.toResponseDTO(listing, List.of());

        // assert

        assertEquals(result.getId(), listingId.getId());

    }

    @Test
    void toPublicationIdShouldReturnPublicationSampleId() {
        // Arrange
        String id = "PUB-001";

        // Act
        PublicationSampleId result = ListingMapper.toPublicationId(id);

        // Assert
        assertEquals(id, result.getID());
    }

    @Test
    void toSaleTypeShouldReturnDirectSaleWhenValueIsDirectSale() {
        // Arrange
        String saleType = "Direct Sale";

        // Act
        SaleType result = ListingMapper.toSaleType(saleType);

        // Assert
        assertEquals(SaleType.DIRECT_SALE, result);
    }

    @Test
    void toSaleTypeShouldReturnAuctionWhenValueIsAuction() {
        // Arrange
        String saleType = "Auction";

        // Act
        SaleType result = ListingMapper.toSaleType(saleType);

        // Assert
        assertEquals(SaleType.AUCTION, result);
    }

    @Test
    void toSaleTypeShouldThrowExceptionWhenValueIsInvalid() {
        // Arrange
        String saleType = "Invalid";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> ListingMapper.toSaleType(saleType));
    }

    @Test
    void toPriceShouldReturnPriceWhenValueIsValidDecimal() {
        // Arrange
        String price = "10.50";

        // Act
        Price result = ListingMapper.toPrice(price);

        // Assert
        assertEquals(new BigDecimal("10.50"), result.getPrice());
    }

    @Test
    void toPriceShouldThrowExceptionWhenValueIsInvalid() {
        // Arrange
        String price = "abc";

        // Act & Assert
        assertThrows(NumberFormatException.class,
                () -> ListingMapper.toPrice(price));
    }

    @Test
    void toPicture_shouldReturnPicture() {
        // Arrange
        String pictureValue = "image-url.jpg";

        // Act
        Picture result = ListingMapper.toPicture(pictureValue);

        // Assert
        assertNotNull(result);
        assertEquals(new Picture(pictureValue), result);
    }

}