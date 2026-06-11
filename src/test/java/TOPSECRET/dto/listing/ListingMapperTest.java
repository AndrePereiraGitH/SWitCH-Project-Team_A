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

/**
 * Unit tests for the {@link ListingMapper} class.
 * <p>
 * This test suite verifies the correct mapping of domain entities
 * (e.g., Listing, BibliographicalData, Edition) to their corresponding
 * Data Transfer Objects (DTOs). It also tests the conversion of raw
 * strings into strongly-typed domain Value Objects.
 * </p>
 */
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
        Condition condition = Condition.GOOD;

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, condition);

        // Assert
        assertEquals("listing-1", result.getId());
    }

    @Test
    void toResponseExploreDTO_shouldMapListingTitle() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());
        List<String> authors = List.of("Robert C. Martin");
        Condition condition = Condition.GOOD;

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, condition);

        // Assert
        assertEquals("Clean Code", result.getListingTitle());
    }

    @Test
    void toResponseExploreDTO_shouldMapAuthors() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());
        List<String> authors = List.of("Robert C. Martin");
        Condition condition = Condition.GOOD;

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, condition);

        // Assert
        assertEquals(List.of("Robert C. Martin"), result.getAuthors());
    }

    @Test
    void toResponseExploreDTO_shouldMapGenre() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());
        List<String> authors = List.of("Robert C. Martin");
        Condition condition = Condition.GOOD;

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, condition);

        // Assert
        assertEquals(List.of("genre-1"), result.getGenre());
    }

    @Test
    void toResponseExploreDTO_shouldMapPrice() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());
        List<String> authors = List.of("Robert C. Martin");
        Condition condition = Condition.GOOD;

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, condition);

        // Assert
        assertEquals("24.99", result.getPrice());
    }

    @Test
    void toResponseExploreDTO_shouldMapCondition() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());
        List<String> authors = List.of("Robert C. Martin");
        Condition condition = Condition.GOOD;

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, condition);

        // Assert
        assertEquals(Condition.GOOD.getDescription(), result.getCondition());
    }

    @Test
    void toResponseExploreDTO_shouldMapSaleType() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());
        List<String> authors = List.of("Robert C. Martin");
        Condition condition = Condition.GOOD;

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, condition);

        // Assert
        assertEquals(SaleType.DIRECT_SALE.toString(), result.getSaleType());
    }

    @Test
    void toResponseExploreDTO_shouldMapPicture() {
        // Arrange
        Listing listing = mockListing();
        List<BibliographicalData> bibliographicalDatas = List.of(mockBibliographicalData());
        List<String> authors = List.of("Robert C. Martin");
        Condition condition = Condition.GOOD;

        // Act
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(listing, bibliographicalDatas, authors, condition);

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
    void toListingDetailsDTO_shouldUseUnknownCondition_whenSampleIsMissing() {
        // Arrange
        Listing listing = mockListing();
        List<PublicationSample> pubSamples = List.of();
        List<BibliographicalData> bds = List.of();
        List<Edition> editions = List.of();
        List<Author> authors = List.of();
        List<Genre> genres = List.of();
        List<Publisher> publishers = List.of();

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing, pubSamples, bds, editions, authors, genres, publishers
        );

        // Assert
        assertEquals("Unknown", result.getCondition());
    }

    @Test
    void toListingDetailsDTO_shouldUseEmptyPicture_whenPicturesAreEmpty() {
        // Arrange
        Listing listing = mockListingWithoutPictures();
        List<PublicationSample> pubSamples = List.of();
        List<BibliographicalData> bds = List.of();
        List<Edition> editions = List.of();
        List<Author> authors = List.of();
        List<Genre> genres = List.of();
        List<Publisher> publishers = List.of();

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing, pubSamples, bds, editions, authors, genres, publishers
        );

        // Assert
        assertEquals("", result.getPicture());
    }

    @Test
    void toListingDetailsDTO_shouldUseDefaultPublisher_whenPublisherIsMissing() {
        // Arrange
        Listing listing = mockListing();
        List<PublicationSample> pubSamples = List.of();
        List<BibliographicalData> bds = List.of();
        List<Edition> editions = List.of();
        List<Author> authors = List.of();
        List<Genre> genres = List.of();
        List<Publisher> publishers = List.of();

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing, pubSamples, bds, editions, authors, genres, publishers
        );

        // Assert
        assertEquals("N/A", result.getPublisher());
    }

    @Test
    void toListingDetailsDTO_shouldUseDefaultIsbn_whenEditionIsMissing() {
        // Arrange
        Listing listing = mockListing();
        List<PublicationSample> pubSamples = List.of();
        List<BibliographicalData> bds = List.of();
        List<Edition> editions = List.of();
        List<Author> authors = List.of();
        List<Genre> genres = List.of();
        List<Publisher> publishers = List.of();

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing, pubSamples, bds, editions, authors, genres, publishers
        );

        // Assert
        assertEquals("N/A", result.getIsbn());
    }

    @Test
    void toListingDetailsDTO_shouldUseNullYear_whenEditionIsMissing() {
        // Arrange
        Listing listing = mockListing();
        List<PublicationSample> pubSamples = List.of();
        List<BibliographicalData> bds = List.of();
        List<Edition> editions = List.of();
        List<Author> authors = List.of();
        List<Genre> genres = List.of();
        List<Publisher> publishers = List.of();

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing, pubSamples, bds, editions, authors, genres, publishers
        );

        // Assert
        assertNull(result.getYear());
    }

    @Test
    void toListingDetailsDTO_shouldUseNullPages_whenEditionIsMissing() {
        // Arrange
        Listing listing = mockListing();
        List<PublicationSample> pubSamples = List.of();
        List<BibliographicalData> bds = List.of();
        List<Edition> editions = List.of();
        List<Author> authors = List.of();
        List<Genre> genres = List.of();
        List<Publisher> publishers = List.of();

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing, pubSamples, bds, editions, authors, genres, publishers
        );

        // Assert
        assertNull(result.getPages());
    }

    @Test
    void toListingDetailsDTO_shouldUseDefaultLanguage_whenEditionIsMissing() {
        // Arrange
        Listing listing = mockListing();
        List<PublicationSample> pubSamples = List.of();
        List<BibliographicalData> bds = List.of();
        List<Edition> editions = List.of();
        List<Author> authors = List.of();
        List<Genre> genres = List.of();
        List<Publisher> publishers = List.of();

        // Act
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing, pubSamples, bds, editions, authors, genres, publishers
        );

        // Assert
        assertEquals("N/A", result.getLanguage());
    }

    @Test
    void toPublicationId_shouldReturnPublicationSampleId() {
        // Arrange
        String id = "PUB-001";

        // Act
        PublicationSampleId result = ListingMapper.toPublicationId(id);

        // Assert
        assertEquals("PUB-001", result.getID());
    }

    @Test
    void toListingId_shouldReturnListingId() {
        // Arrange
        String id = "LISTING-001";

        // Act
        ListingId result = ListingMapper.toListingId(id);

        // Assert
        assertEquals("LISTING-001", result.getId());
    }

    @Test
    void toGenreId_shouldReturnGenreId() {
        // Arrange
        String id = "GENRE-001";

        // Act
        GenreId result = ListingMapper.toGenreId(id);

        // Assert
        assertEquals("GENRE-001", result.getID());
    }

    @Test
    void toUserId_shouldReturnUserId() {
        // Arrange
        String id = "USER-001";

        // Act
        UserId result = ListingMapper.toUserId(id);

        // Assert
        assertEquals("USER-001", result.getUserId());
    }

    @Test
    void toPublisherId_shouldReturnPublisherId() {
        // Arrange
        String id = "PUBLISHER-001";

        // Act
        PublisherID result = ListingMapper.toPublisherId(id);

        // Assert
        assertEquals("PUBLISHER-001", result.getId());
    }

    @Test
    void toSaleType_shouldReturnDirectSale() {
        // Arrange
        String saleType = "Direct Sale";

        // Act
        SaleType result = ListingMapper.toSaleType(saleType);

        // Assert
        assertEquals(SaleType.DIRECT_SALE, result);
    }

    @Test
    void toSaleType_shouldReturnAuction() {
        // Arrange
        String saleType = "Auction";

        // Act
        SaleType result = ListingMapper.toSaleType(saleType);

        // Assert
        assertEquals(SaleType.AUCTION, result);
    }

    @Test
    void toSaleType_shouldThrowException_whenValueIsInvalid() {
        // Arrange
        String saleType = "Invalid";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ListingMapper.toSaleType(saleType));
    }

    @Test
    void toSaleType_shouldThrowException_whenValueIsNull() {
        // Arrange
        String saleType = null;

        // Act & Assert
        assertThrows(Exception.class, () -> ListingMapper.toSaleType(saleType));
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

    // -------------------------------------------------------------------------
    // Helper Methods for Mocking
    // -------------------------------------------------------------------------

    private ListingDetailsDTO mockListingDetailsDTO() {
        Listing listing = mockListing();
        List<PublicationSample> samples = List.of(mockPublicationSample());
        List<BibliographicalData> bds = List.of(mockBibliographicalData());
        List<Edition> editions = List.of(mockEdition());
        List<Author> authors = List.of(mockAuthor());
        List<Genre> genres = List.of(mockGenre());
        List<Publisher> publishers = List.of(mockPublisher());

        return ListingMapper.toListingDetailsDTO(listing, samples, bds, editions, authors, genres, publishers);
    }

    private Listing mockListing() {
        Listing listing = mock(Listing.class);
        ListingId listingId = mock(ListingId.class);
        Price price = mock(Price.class);
        UserId sellerId = mock(UserId.class);
        Picture picture = mock(Picture.class);

        when(listing.id()).thenReturn(listingId);
        when(listingId.getId()).thenReturn("listing-1");
        when(listing.getListingTitle()).thenReturn("Clean Code");
        when(listing.getPrice()).thenReturn(price);
        when(price.getPrice()).thenReturn(new BigDecimal("24.99"));
        when(listing.getPublicationDate()).thenReturn(LocalDate.of(2024, 1, 10));
        when(listing.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(picture.getPicture()).thenReturn("image.jpg");
        when(listing.getPictures()).thenReturn(List.of(picture));
        when(listing.getShortDescription()).thenReturn("A book about clean code.");
        when(listing.getSellerId()).thenReturn(sellerId);
        when(sellerId.getUserId()).thenReturn("user-1");

        return listing;
    }

    private Listing mockListingWithoutPictures() {
        Listing listing = mock(Listing.class);
        ListingId listingId = mock(ListingId.class);
        Price price = mock(Price.class);
        UserId sellerId = mock(UserId.class);

        when(listing.id()).thenReturn(listingId);
        when(listingId.getId()).thenReturn("listing-1");
        when(listing.getListingTitle()).thenReturn("Clean Code");
        when(listing.getPrice()).thenReturn(price);
        when(price.getPrice()).thenReturn(new BigDecimal("24.99"));
        when(listing.getPublicationDate()).thenReturn(LocalDate.of(2024, 1, 10));
        when(listing.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(listing.getPictures()).thenReturn(List.of());
        when(listing.getShortDescription()).thenReturn("A book about clean code.");
        when(listing.getSellerId()).thenReturn(sellerId);
        when(sellerId.getUserId()).thenReturn("user-1");

        return listing;
    }

    private BibliographicalData mockBibliographicalData() {
        BibliographicalData bibliographicalData = mock(BibliographicalData.class);
        GenreId genreId = mock(GenreId.class);
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        when(bibliographicalData.getTitle()).thenReturn("Clean Code Book");
        when(bibliographicalData.getGenres()).thenReturn(List.of(genreId));
        when(genreId.getID()).thenReturn("genre-1");
        when(bibliographicalData.id()).thenReturn(bdId);
        when(bdId.toString()).thenReturn("bd-1");

        return bibliographicalData;
    }

    private PublicationSample mockPublicationSample() {
        PublicationSample sample = mock(PublicationSample.class);

        when(sample.getCondition()).thenReturn(Condition.GOOD);

        return sample;
    }

    private Edition mockEdition() {
        Edition edition = mock(Edition.class);
        Code code = mock(Code.class);
        PhysicalAttributes physicalAttributes = mock(PhysicalAttributes.class);

        when(edition.getCode()).thenReturn(code);
        when(code.toString()).thenReturn("9780132350884");
        when(edition.getEditionDate()).thenReturn(LocalDate.of(2008, 8, 1));
        when(edition.getPhysicalAttributes()).thenReturn(physicalAttributes);
        when(physicalAttributes.getNumberOfPages()).thenReturn(464);
        when(edition.getEditionLanguage()).thenReturn(Locale.ENGLISH);

        return edition;
    }

    private Author mockAuthor() {
        Author author = mock(Author.class);

        when(author.getName()).thenReturn("Robert C. Martin");

        return author;
    }

    private Genre mockGenre() {
        Genre genre = mock(Genre.class);

        when(genre.getGenreType()).thenReturn("Software");

        return genre;
    }

    private Publisher mockPublisher() {
        Publisher publisher = mock(Publisher.class);

        when(publisher.getName()).thenReturn("Prentice Hall");

        return publisher;
    }
}