package TOPSECRET.dto.listing;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.listing.response.ListingDetailsDTO;
import TOPSECRET.dto.listing.response.ListingResponseDTO;
import TOPSECRET.dto.listing.response.ListingResponseExploreDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListingMapperTest {

    private final ListingMapper mapper = new ListingMapper();

    // ============================================================
    // --------------  toResponseDTO  -----------------------------
    // ============================================================

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

        ListingResponseDTO result = ListingMapper.toResponseDTO(listing, List.of());

        // assert

        assertEquals(result.getId(), listingId.getId());

    }

    @Test
    void toResponseDTO_shouldMapBibliographicalDataTitlesCorrectly() {

        // Arrange
        Listing listing = mock(Listing.class);

        ListingId listingId = mock(ListingId.class);
        UserId sellerId = mock(UserId.class);
        Price price = mock(Price.class);
        Picture picture = mock(Picture.class);

        BibliographicalData data1 = mock(BibliographicalData.class);
        BibliographicalData data2 = mock(BibliographicalData.class);

        LocalDate publicationDate = LocalDate.now();

        when(listing.id()).thenReturn(listingId);
        when(listingId.getId()).thenReturn("id1");

        when(listing.getListingTitle()).thenReturn("listing title");

        when(data1.getTitle()).thenReturn("data title 1");
        when(data2.getTitle()).thenReturn("data title 2");

        when(listing.getPrice()).thenReturn(price);
        when(price.getPrice()).thenReturn(new BigDecimal("10.00"));

        when(listing.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing.getShortDescription()).thenReturn("short description");

        when(listing.getSellerId()).thenReturn(sellerId);
        when(sellerId.getUserId()).thenReturn("joao");

        when(picture.getPicture()).thenReturn("pic1.jpg");
        when(listing.getPictures()).thenReturn(List.of(picture));

        when(listing.getPublicationDate()).thenReturn(publicationDate);

        // Act
        ListingResponseDTO result =
                mapper.toResponseDTO(listing, List.of(data1, data2));

        // Assert
        assertNotNull(result);
        assertEquals("id1", result.getId());
    }

    // ============================================================
    // --------------  toResponseExploreDTO  -----------------------------
    // ============================================================

    @Test
    void toResponseExploreDTO_shouldMapListingToExploreResponseDTO() {
        // Arrange
        Listing listing = mock(Listing.class);
        ListingId listingId = mock(ListingId.class);
        Price price = mock(Price.class);
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);
        GenreId genreId = mock(GenreId.class);

        when(listing.id()).thenReturn(listingId);
        when(listingId.getId()).thenReturn("listing-1");

        when(listing.getListingTitle()).thenReturn("Clean Code");

        when(bibliographicalData.getGenres()).thenReturn(List.of(genreId));
        when(genreId.getID()).thenReturn("genre-1");

        when(listing.getPrice()).thenReturn(price);
        when(price.getPrice()).thenReturn(new BigDecimal("25.00"));

        when(listing.getSaleType()).thenReturn(SaleType.DIRECT_SALE);

        List<BibliographicalData> bd = List.of(bibliographicalData);
        List<String> authors = List.of("Robert C. Martin");
        Condition condition = Condition.GOOD;

        // Act
        ListingResponseExploreDTO result =
                ListingMapper.toResponseExploreDTO(listing, bd, authors, condition);

        // Assert
        assertEquals("listing-1", result.getId());
        assertEquals("Clean Code", result.getListingTitle());
        assertEquals(List.of("Robert C. Martin"), result.getAuthors());
        assertEquals(List.of("genre-1"), result.getGenre());
        assertEquals("25.00", result.getPrice());
        assertEquals(Condition.GOOD.getDescription(), result.getCondition());
        assertEquals(SaleType.DIRECT_SALE.toString(), result.getSaleType());
    }

    // ============================================================
    // --------------  toPublicationId  ---------------------------
    // ============================================================

    @Test
    void toPublicationIdShouldReturnPublicationSampleId() {
        // Arrange
        String id = "PUB-001";

        // Act
        PublicationSampleId result = ListingMapper.toPublicationId(id);

        // Assert
        assertEquals(id, result.getID());
    }

    // ============================================================
    // --------------  toListingId  -------------------------------
    // ============================================================

    @Test
    void toListingId_shouldReturnListingId() {
        // Arrange
        String id = "LISTING-001";

        // Act
        ListingId result = ListingMapper.toListingId(id);

        // Assert
        assertEquals(id, result.getId());
    }

    // ============================================================
    // --------------  toGenreId  ---------------------------------
    // ============================================================

    @Test
    void toGenreId_shouldReturnGenreId() {
        // Arrange
        String id = "GENRE-001";

        // Act
        GenreId result = ListingMapper.toGenreId(id);

        // Assert
        assertEquals(id, result.getID());
    }

    // ============================================================
    // --------------  toUserId  ----------------------------------
    // ============================================================

    @Test
    void toUserId_shouldReturnUserId() {
        // Arrange
        String id = "USER-001";

        // Act
        UserId result = ListingMapper.toUserId(id);

        // Assert
        assertEquals(id, result.getUserId());
    }

    // ============================================================
    // --------------  toSaleType  --------------------------------
    // ============================================================

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
    void toSaleTypeShouldThrowExceptionWhenValueIsNull() {
        // Arrange
        String saleType = null;

        // Act & Assert
        assertThrows(Exception.class,
                () -> ListingMapper.toSaleType(saleType));
    }

    // ============================================================
    // --------------  toPrice  -----------------------------------
    // ============================================================

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
    void toPriceShouldThrowExceptionWhenValueIsNull() {
        // Arrange
        String price = null;

        // Act & Assert
        assertThrows(Exception.class,
                () -> ListingMapper.toPrice(price));
    }

    // ============================================================
    // --------------  toPicture  ---------------------------------
    // ============================================================

    @Test
    void toPicture_shouldReturnPicture() {
        // Arrange
        String pictureValue = "image-url.jpg";

        // Act
        Picture result = ListingMapper.toPicture(pictureValue);

        // Assert
        assertEquals(new Picture(pictureValue), result);
    }

    @Test
    void toPicture_shouldThrowException_whenValueIsNull() {
        // Arrange
        String pictureValue = null;

        // Act & Assert
        assertThrows(Exception.class,
                () -> ListingMapper.toPicture(pictureValue));
    }
    // ============================================================
    // --------------  toPublisherId  ---------------------------------
    // ============================================================

    @Test
    void toPublisherId_shouldReturn_PublisherId() {
        // arrange
        String publisherId = "publisherId";

        // act
        PublisherID result = ListingMapper.toPublisherId(publisherId);

        // assert
        assertEquals(new PublisherID(publisherId), result);
    }

    @Test
    void toPublisherId_shouldThrow_whenPublisherIdIsNull() {
        // act + assert
        assertThrows(Exception.class, () -> ListingMapper.toPublisherId(null));
    }

    // ============================================================
// --------------  toListingDetailsDTO  ------------------------
// ============================================================

    @Test
    void toListingDetailsDTO_shouldMapAllFieldsCorrectly() {
        // Arrange
        Listing listing = mock(Listing.class);
        ListingId listingId = mock(ListingId.class);
        UserId sellerId = mock(UserId.class);
        Price price = mock(Price.class);
        Picture picture = mock(Picture.class);

        PublicationSample sample = mock(PublicationSample.class);
        Edition edition = mock(Edition.class);
        Publisher publisher = mock(Publisher.class);
        Author author = mock(Author.class);
        Genre genre = mock(Genre.class);

        Code code = mock(Code.class);
        PhysicalAttributes physicalAttributes = mock(PhysicalAttributes.class);

        when(listing.id()).thenReturn(listingId);
        when(listingId.toString()).thenReturn("listing-1");

        when(listing.getListingTitle()).thenReturn("Clean Code");

        when(author.getName()).thenReturn("Robert C. Martin");
        when(genre.getGenreType()).thenReturn("Software");

        when(listing.getPrice()).thenReturn(price);
        when(price.getPrice()).thenReturn(new BigDecimal("24.99"));

        when(sample.getCondition()).thenReturn(Condition.GOOD);

        when(listing.getSaleType()).thenReturn(SaleType.DIRECT_SALE);

        when(picture.getPicture()).thenReturn("image.jpg");
        when(listing.getPictures()).thenReturn(List.of(picture));

        when(listing.getShortDescription()).thenReturn("A book about clean code.");

        when(listing.getSellerId()).thenReturn(sellerId);
        when(sellerId.getUserId()).thenReturn("user-1");

        when(publisher.getName()).thenReturn("Prentice Hall");

        when(edition.getCode()).thenReturn(code);
        when(code.toString()).thenReturn("9780132350884");

        when(edition.getEditionDate()).thenReturn(LocalDate.of(2008, 8, 1));

        when(edition.getPhysicalAttributes()).thenReturn(physicalAttributes);
        when(physicalAttributes.getNumberOfPages()).thenReturn(464);

        when(edition.getEditionLanguage()).thenReturn(Locale.ENGLISH);

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing,
                List.of(),
                List.of(sample),
                List.of(edition),
                List.of(author),
                List.of(genre),
                List.of(publisher)
        );

        // Assert
        assertNotNull(result);
        assertEquals("listing-1", result.getId());
        assertEquals("Clean Code", result.getListingTitle());
        assertEquals(List.of("Robert C. Martin"), result.getAuthors());
        assertEquals(List.of("Software"), result.getGenre());
        assertEquals(new BigDecimal("24.99"), result.getPrice());
        assertEquals(Condition.GOOD.toString(), result.getCondition());
        assertEquals(SaleType.DIRECT_SALE.toString(), result.getSaleType());
        assertEquals("image.jpg", result.getPicture());
        assertEquals("A book about clean code.", result.getShortDescription());
        assertEquals("user-1", result.getSellerId());
        assertEquals("Prentice Hall", result.getPublisher());
        assertEquals("9780132350884", result.getIsbn());
        assertEquals(2008, result.getYear());
        assertEquals(464, result.getPages());
        assertEquals(Locale.ENGLISH.getDisplayLanguage(), result.getLanguage());
    }

    @Test
    void toListingDetailsDTO_shouldUseDefaultValues_whenOptionalDataIsMissing() {
        // Arrange
        Listing listing = mock(Listing.class);
        ListingId listingId = mock(ListingId.class);
        UserId sellerId = mock(UserId.class);
        Price price = mock(Price.class);

        when(listing.id()).thenReturn(listingId);
        when(listingId.toString()).thenReturn("listing-1");

        when(listing.getListingTitle()).thenReturn("Listing title");

        when(listing.getPrice()).thenReturn(price);
        when(price.getPrice()).thenReturn(new BigDecimal("10.00"));

        when(listing.getSaleType()).thenReturn(SaleType.AUCTION);
        when(listing.getPictures()).thenReturn(List.of());
        when(listing.getShortDescription()).thenReturn("Short description");

        when(listing.getSellerId()).thenReturn(sellerId);
        when(sellerId.getUserId()).thenReturn("seller-1");

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );

        // Assert
        assertNotNull(result);
        assertEquals("listing-1", result.getId());
        assertEquals("Listing title", result.getListingTitle());
        assertEquals(List.of(), result.getAuthors());
        assertEquals(List.of(), result.getGenre());
        assertEquals(new BigDecimal("10.00"), result.getPrice());
        assertEquals("Unknown", result.getCondition());
        assertEquals(SaleType.AUCTION.toString(), result.getSaleType());
        assertEquals("", result.getPicture());
        assertEquals("Short description", result.getShortDescription());
        assertEquals("seller-1", result.getSellerId());
        assertEquals("N/A", result.getPublisher());
        assertEquals("N/A", result.getIsbn());
        assertNull(result.getYear());
        assertNull(result.getPages());
        assertEquals("N/A", result.getLanguage());
    }
}