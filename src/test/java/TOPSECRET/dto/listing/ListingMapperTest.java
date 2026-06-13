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
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.listing.response.ListingDetailsDTO;
import TOPSECRET.dto.listing.response.ListingResponseDTO;
import TOPSECRET.dto.listing.response.ListingResponseExploreDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ListingMapperTest {

    @Test
    void toResponseDTO_shouldMapId() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());

        // Act
        ListingResponseDTO result = ListingMapper.toResponseDTO(listing, bibliographicalDatas);

        // Assert
        assertEquals("listing-1", result.getId());
    }

    @Test
    void toResponseDTO_shouldMapListingTitle() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());

        // Act
        ListingResponseDTO result = ListingMapper.toResponseDTO(listing, bibliographicalDatas);

        // Assert
        assertEquals("Clean Code", result.getListingTitle());
    }

    @Test
    void toResponseDTO_shouldMapBibliographicalTitles() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());

        // Act
        ListingResponseDTO result = ListingMapper.toResponseDTO(listing, bibliographicalDatas);

        // Assert
        assertEquals(List.of("Clean Code Book"), result.getPublicationTitles());
    }

    @Test
    void toResponseDTO_shouldMapSaleType() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());

        // Act
        ListingResponseDTO result = ListingMapper.toResponseDTO(listing, bibliographicalDatas);

        // Assert
        assertEquals(SaleType.DIRECT_SALE.toString(), result.getSaleType());
    }

    @Test
    void toResponseDTO_shouldMapPrice() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());

        // Act
        ListingResponseDTO result = ListingMapper.toResponseDTO(listing, bibliographicalDatas);

        // Assert
        assertEquals("24.99", result.getPrice());
    }

    @Test
    void toResponseDTO_shouldMapShortDescription() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());

        // Act
        ListingResponseDTO result = ListingMapper.toResponseDTO(listing, bibliographicalDatas);

        // Assert
        assertEquals("A book about clean code.", result.getDescription());
    }

    @Test
    void toResponseDTO_shouldMapSellerId() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());

        // Act
        ListingResponseDTO result = ListingMapper.toResponseDTO(listing, bibliographicalDatas);

        // Assert
        assertEquals("user-1", result.getSellerId());
    }

    @Test
    void toResponseDTO_shouldMapPictures() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());

        // Act
        ListingResponseDTO result = ListingMapper.toResponseDTO(listing, bibliographicalDatas);

        // Assert
        assertEquals(List.of("image.jpg"), result.getPictures());
    }

    @Test
    void toResponseDTO_shouldMapPublicationDate() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());

        // Act
        ListingResponseDTO result = ListingMapper.toResponseDTO(listing, bibliographicalDatas);

        // Assert
        assertEquals(LocalDate.of(2024, 1, 10), result.getPublicationDate());
    }

    @Test
    void toResponseExploreDTO_shouldMapId() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());
        List<String> authors = List.of("Robert C. Martin");

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, Condition.GOOD);

        // Assert
        assertEquals("listing-1", result.getId());
    }

    @Test
    void toResponseExploreDTO_shouldMapListingTitle() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());
        List<String> authors = List.of("Robert C. Martin");

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, Condition.GOOD);

        // Assert
        assertEquals("Clean Code", result.getListingTitle());
    }

    @Test
    void toResponseExploreDTO_shouldMapAuthors() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());
        List<String> authors = List.of("Robert C. Martin");

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, Condition.GOOD);

        // Assert
        assertEquals(List.of("Robert C. Martin"), result.getAuthors());
    }

    @Test
    void toResponseExploreDTO_shouldMapGenre() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());
        List<String> authors = List.of("Robert C. Martin");

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, Condition.GOOD);

        // Assert
        assertEquals(List.of("genre-1"), result.getGenre());
    }

    @Test
    void toResponseExploreDTO_shouldMapPrice() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());
        List<String> authors = List.of("Robert C. Martin");

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, Condition.GOOD);

        // Assert
        assertEquals("24.99", result.getPrice());
    }

    @Test
    void toResponseExploreDTO_shouldMapCondition() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());
        List<String> authors = List.of("Robert C. Martin");

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, Condition.GOOD);

        // Assert
        assertEquals(Condition.GOOD.getDescription(), result.getCondition());
    }

    @Test
    void toResponseExploreDTO_shouldMapSaleType() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());
        List<String> authors = List.of("Robert C. Martin");

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, Condition.GOOD);

        // Assert
        assertEquals(SaleType.DIRECT_SALE.toString(), result.getSaleType());
    }

    @Test
    void toResponseExploreDTO_shouldMapPicture() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());
        List<String> authors = List.of("Robert C. Martin");

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, Condition.GOOD);

        // Assert
        assertEquals("image.jpg", result.getPicture());
    }

    @Test
    void toListingDetailsDTO_shouldMapId() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals("listing-1", result.getId());
    }

    @Test
    void toListingDetailsDTO_shouldMapListingTitle() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals("Clean Code", result.getListingTitle());
    }

    @Test
    void toListingDetailsDTO_shouldMapAuthors() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals(List.of("Robert C. Martin"), result.getAuthors());
    }

    @Test
    void toListingDetailsDTO_shouldMapGenre() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals(List.of("Software"), result.getGenre());
    }

    @Test
    void toListingDetailsDTO_shouldMapPrice() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals("24.99", result.getPrice());
    }

    @Test
    void toListingDetailsDTO_shouldMapPublicationDate() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals(LocalDate.of(2024, 1, 10), result.getPublicationDate());
    }

    @Test
    void toListingDetailsDTO_shouldMapCondition() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals(Condition.GOOD.toString(), result.getCondition());
    }

    @Test
    void toListingDetailsDTO_shouldMapSaleType() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals(SaleType.DIRECT_SALE.toString(), result.getSaleType());
    }

    @Test
    void toListingDetailsDTO_shouldMapPicture() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals("image.jpg", result.getPicture());
    }

    @Test
    void toListingDetailsDTO_shouldMapShortDescription() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals("A book about clean code.", result.getShortDescription());
    }

    @Test
    void toListingDetailsDTO_shouldMapSellerId() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals("user-1", result.getSellerId());
    }

    @Test
    void toListingDetailsDTO_shouldMapPublisher() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals("Prentice Hall", result.getPublisher());
    }

    @Test
    void toListingDetailsDTO_shouldMapIsbn() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals("9780132350884", result.getIsbn());
    }

    @Test
    void toListingDetailsDTO_shouldMapYear() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals(2008, result.getYear());
    }

    @Test
    void toListingDetailsDTO_shouldMapPages() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals(464, result.getPages());
    }

    @Test
    void toListingDetailsDTO_shouldMapLanguage() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals(Locale.ENGLISH.getDisplayLanguage(), result.getLanguage());
    }

    @Test
    void toListingDetailsDTO_shouldMapBibliographicalDataId() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertNotNull(result.getBibliographicalDataId());
        assertTrue(result.getBibliographicalDataId().contains("bd-1"));
    }

    @Test
    void toListingDetailsDTO_shouldMapBuyer() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals("buyer-1", result.getBuyer());
    }

    @Test
    void toListingDetailsDTO_shouldMapPublicationStatus() {
        // Arrange
        ListingDetailsDTO result;

        // Act
        result = mockListingDetailsDTO();

        // Assert
        assertEquals("AVAILABLE", result.getPublicationStatus());
    }

    @Test
    void toListingDetailsDTO_shouldUseUnknownCondition_whenSampleIsMissing() {
        // Arrange
        Listing listing = mockListing();
        User buyer = mockBuyer();

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing, List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), buyer
        );

        // Assert
        assertEquals("Unknown", result.getCondition());
    }

    @Test
    void toListingDetailsDTO_shouldUseEmptyPicture_whenPicturesAreEmpty() {
        // Arrange
        Listing listing = mockListingWithoutPictures();
        User buyer = mockBuyer();

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing, List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), buyer
        );

        // Assert
        assertEquals("", result.getPicture());
    }

    @Test
    void toListingDetailsDTO_shouldUseDefaultPublisher_whenPublisherIsMissing() {
        // Arrange
        Listing listing = mockListing();
        User buyer = mockBuyer();

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing, List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), buyer
        );

        // Assert
        assertEquals("N/A", result.getPublisher());
    }

    @Test
    void toListingDetailsDTO_shouldUseDefaultIsbn_whenEditionIsMissing() {
        // Arrange
        Listing listing = mockListing();
        User buyer = mockBuyer();

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing, List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), buyer
        );

        // Assert
        assertEquals("N/A", result.getIsbn());
    }

    @Test
    void toListingDetailsDTO_shouldUseNullYear_whenEditionIsMissing() {
        // Arrange
        Listing listing = mockListing();
        User buyer = mockBuyer();

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing, List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), buyer
        );

        // Assert
        assertNull(result.getYear());
    }

    @Test
    void toListingDetailsDTO_shouldUseNullPages_whenEditionIsMissing() {
        // Arrange
        Listing listing = mockListing();
        User buyer = mockBuyer();

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing, List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), buyer
        );

        // Assert
        assertNull(result.getPages());
    }

    @Test
    void toListingDetailsDTO_shouldUseDefaultLanguage_whenEditionIsMissing() {
        // Arrange
        Listing listing = mockListing();
        User buyer = mockBuyer();

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing, List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), buyer
        );

        // Assert
        assertEquals("N/A", result.getLanguage());
    }

    @Test
    void toPublicationId_shouldReturnPublicationSampleId() {
        // Arrange
        String inputId = "PUB-001";

        // Act
        PublicationSampleId result = ListingMapper.toPublicationId(inputId);

        // Assert
        assertEquals("PUB-001", result.getID());
    }

    @Test
    void toListingId_shouldReturnListingId() {
        // Arrange
        String inputId = "LISTING-001";

        // Act
        ListingId result = ListingMapper.toListingId(inputId);

        // Assert
        assertEquals("LISTING-001", result.getId());
    }

    @Test
    void toGenreId_shouldReturnGenreId() {
        // Arrange
        String inputId = "GENRE-001";

        // Act
        GenreId result = ListingMapper.toGenreId(inputId);

        // Assert
        assertEquals("GENRE-001", result.getID());
    }

    @Test
    void toUserId_shouldReturnUserId() {
        // Arrange
        String inputId = "USER-001";

        // Act
        UserId result = ListingMapper.toUserId(inputId);

        // Assert
        assertEquals("USER-001", result.getUserId());
    }

    @Test
    void toPublisherId_shouldReturnPublisherId() {
        // Arrange
        String inputId = "PUBLISHER-001";

        // Act
        PublisherID result = ListingMapper.toPublisherId(inputId);

        // Assert
        assertEquals("PUBLISHER-001", result.getId());
    }

    @Test
    void toSaleType_shouldReturnDirectSale() {
        // Arrange
        String saleTypeStr = "Direct Sale";

        // Act
        SaleType result = ListingMapper.toSaleType(saleTypeStr);

        // Assert
        assertEquals(SaleType.DIRECT_SALE, result);
    }

    @Test
    void toSaleType_shouldReturnAuction() {
        // Arrange
        String saleTypeStr = "Auction";

        // Act
        SaleType result = ListingMapper.toSaleType(saleTypeStr);

        // Assert
        assertEquals(SaleType.AUCTION, result);
    }

    @Test
    void toSaleType_shouldThrowException_whenValueIsInvalid() {
        // Arrange
        String invalidSaleType = "Invalid";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ListingMapper.toSaleType(invalidSaleType));
    }

    @Test
    void toSaleType_shouldThrowException_whenValueIsNull() {
        // Arrange
        String nullSaleType = null;

        // Act & Assert
        assertThrows(Exception.class, () -> ListingMapper.toSaleType(nullSaleType));
    }

    @Test
    void toPrice_shouldReturnPrice() {
        // Arrange
        String priceValue = "10.50";

        // Act
        Price result = ListingMapper.toPrice(priceValue);

        // Assert
        assertEquals(new BigDecimal("10.50"), result.getPrice());
    }

    @Test
    void toPrice_shouldThrowException_whenValueIsInvalid() {
        // Arrange
        String invalidPrice = "abc";

        // Act & Assert
        assertThrows(NumberFormatException.class, () -> ListingMapper.toPrice(invalidPrice));
    }

    @Test
    void toPrice_shouldThrowException_whenValueIsNull() {
        // Arrange
        String nullPrice = null;

        // Act & Assert
        assertThrows(Exception.class, () -> ListingMapper.toPrice(nullPrice));
    }

    @Test
    void toPicture_shouldReturnPicture() {
        // Arrange
        String pictureUrl = "image-url.jpg";

        // Act
        Picture result = ListingMapper.toPicture(pictureUrl);

        // Assert
        assertEquals(new Picture("image-url.jpg"), result);
    }

    @Test
    void toPicture_shouldThrowException_whenValueIsNull() {
        // Arrange
        String nullPicture = null;

        // Act & Assert
        assertThrows(Exception.class, () -> ListingMapper.toPicture(nullPicture));
    }

    @Test
    void toPublisherId_shouldThrowException_whenValueIsNull() {
        // Arrange
        String nullPublisherId = null;

        // Act & Assert
        assertThrows(Exception.class, () -> ListingMapper.toPublisherId(nullPublisherId));
    }

    @Test
    void toBibliographicalDataId_shouldReturnBibliographicalDataId_whenIdIsValid() {
        // Arrange
        String validId = "BD-001";

        // Act
        BibliographicalDataId result = ListingMapper.toBibliographicalDataId(validId);

        // Assert
        assertEquals("BD-001", result.getID());
    }

    @Test
    void toBibliographicalDataId_shouldReturnEquivalentValueObject_whenIdIsValid() {
        // Arrange
        String validId = "BD-001";

        // Act
        BibliographicalDataId result = ListingMapper.toBibliographicalDataId(validId);

        // Assert
        assertEquals(new BibliographicalDataId("BD-001"), result);
    }

    @Test
    void toBibliographicalDataId_shouldThrowException_whenIdIsNull() {
        // Arrange
        String nullId = null;

        // Act & Assert
        assertThrows(Exception.class, () -> ListingMapper.toBibliographicalDataId(nullId));
    }

    @Test
    void toBibliographicalDataId_shouldThrowException_whenIdIsBlank() {
        // Arrange
        String blankId = "   ";

        // Act & Assert
        assertThrows(Exception.class, () -> ListingMapper.toBibliographicalDataId(blankId));
    }

    @Test
    void toBibliographicalDataId_shouldThrowException_whenIdIsEmpty() {
        // Arrange
        String emptyId = "";

        // Act & Assert
        assertThrows(Exception.class, () -> ListingMapper.toBibliographicalDataId(emptyId));
    }

    // =========================================================================
    // Helper Methods for Mocking
    // =========================================================================

    private ListingDetailsDTO mockListingDetailsDTO() {
        Listing listing = mockListing();
        List<PublicationSample> samples = List.of(mockPublicationSample());
        List<BibliographicalData> bds = List.of(mockBibliographicalData());
        List<Edition> editions = List.of(mockEdition());
        List<Author> authors = List.of(mockAuthor());
        List<Genre> genres = List.of(mockGenre());
        List<Publisher> publishers = List.of(mockPublisher());
        User buyer = mockBuyer();

        return ListingMapper.toListingDetailsDTO(listing, samples, bds, editions, authors, genres, publishers, buyer);
    }

    private User mockBuyer() {
        User buyer = mock(User.class);
        lenient().when(buyer.getUserName()).thenReturn("buyer-1");
        return buyer;
    }

    private Listing mockListing() {
        Listing listing = mock(Listing.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);
        ListingId listingId = mock(ListingId.class);
        Price price = mock(Price.class);
        UserId sellerId = mock(UserId.class);
        Picture picture = mock(Picture.class);

        lenient().when(listing.id()).thenReturn(listingId);
        lenient().when(listingId.getId()).thenReturn("listing-1");
        lenient().when(listing.getListingTitle()).thenReturn("Clean Code");
        lenient().when(listing.getPrice()).thenReturn(price);
        lenient().when(price.getPrice()).thenReturn(new BigDecimal("24.99"));
        lenient().when(listing.getPublicationDate()).thenReturn(LocalDate.of(2024, 1, 10));
        lenient().when(listing.getSaleType()).thenReturn(SaleType.DIRECT_SALE);

        lenient().when(picture.getPicture()).thenReturn("image.jpg");
        lenient().when(listing.getPictures()).thenReturn(List.of(picture));

        lenient().when(listing.getShortDescription()).thenReturn("A book about clean code.");
        lenient().when(listing.getSellerId()).thenReturn(sellerId);
        lenient().when(sellerId.getUserId()).thenReturn("user-1");

        lenient().when(listing.getListingStatus().toString()).thenReturn("AVAILABLE");

        return listing;
    }

    private Listing mockListingWithoutPictures() {
        Listing listing = mock(Listing.class, org.mockito.Mockito.RETURNS_DEEP_STUBS);
        ListingId listingId = mock(ListingId.class);
        Price price = mock(Price.class);
        UserId sellerId = mock(UserId.class);

        lenient().when(listing.id()).thenReturn(listingId);
        lenient().when(listingId.getId()).thenReturn("listing-1");
        lenient().when(listing.getListingTitle()).thenReturn("Clean Code");
        lenient().when(listing.getPrice()).thenReturn(price);
        lenient().when(price.getPrice()).thenReturn(new BigDecimal("24.99"));
        lenient().when(listing.getPublicationDate()).thenReturn(LocalDate.of(2024, 1, 10));
        lenient().when(listing.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        lenient().when(listing.getPictures()).thenReturn(List.of());
        lenient().when(listing.getShortDescription()).thenReturn("A book about clean code.");
        lenient().when(listing.getSellerId()).thenReturn(sellerId);
        lenient().when(sellerId.getUserId()).thenReturn("user-1");

        lenient().when(listing.getListingStatus().toString()).thenReturn("AVAILABLE");

        return listing;
    }

    private BibliographicalData mockBibliographicalData() {
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);
        GenreId genreId = mock(GenreId.class);
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        lenient().when(bibliographicalData.getTitle()).thenReturn("Clean Code Book");
        lenient().when(bibliographicalData.getGenres()).thenReturn(List.of(genreId));
        lenient().when(genreId.getID()).thenReturn("genre-1");
        lenient().when(bibliographicalData.id()).thenReturn(bdId);

        lenient().when(bdId.getID()).thenReturn("bd-1");
        lenient().when(bdId.toString()).thenReturn("bd-1");

        return bibliographicalData;
    }

    private PublicationSample mockPublicationSample() {
        PublicationSample sample = mock(PublicationSample.class);
        lenient().when(sample.getCondition()).thenReturn(Condition.GOOD);
        return sample;
    }

    private Edition mockEdition() {
        Edition edition = mock(Edition.class);
        Code code = mock(Code.class);
        PhysicalAttributes physicalAttributes = mock(PhysicalAttributes.class);

        lenient().when(edition.getCode()).thenReturn(code);
        lenient().when(code.toString()).thenReturn("9780132350884");
        lenient().when(edition.getEditionDate()).thenReturn(LocalDate.of(2008, 8, 1));
        lenient().when(edition.getPhysicalAttributes()).thenReturn(physicalAttributes);
        lenient().when(physicalAttributes.getNumberOfPages()).thenReturn(464);
        lenient().when(edition.getEditionLanguage()).thenReturn(Locale.ENGLISH);

        return edition;
    }

    private Author mockAuthor() {
        Author author = mock(Author.class);
        lenient().when(author.getName()).thenReturn("Robert C. Martin");
        return author;
    }

    private Genre mockGenre() {
        Genre genre = mock(Genre.class);
        lenient().when(genre.getGenreType()).thenReturn("Software");
        return genre;
    }

    private Publisher mockPublisher() {
        Publisher publisher = mock(Publisher.class);
        lenient().when(publisher.getName()).thenReturn("Prentice Hall");
        return publisher;
    }
}