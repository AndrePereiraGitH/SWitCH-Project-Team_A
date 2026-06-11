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

    @Test
    void toResponseDTO_shouldMapId() {
        ListingResponseDTO result = ListingMapper.toResponseDTO(mockListing(), List.of(mockBibliographicalData()));

        assertEquals("listing-1", result.getId());
    }

    @Test
    void toResponseDTO_shouldMapListingTitle() {
        ListingResponseDTO result = ListingMapper.toResponseDTO(mockListing(), List.of(mockBibliographicalData()));

        assertEquals("Clean Code", result.getListingTitle());
    }

    @Test
    void toResponseDTO_shouldMapBibliographicalTitles() {
        ListingResponseDTO result = ListingMapper.toResponseDTO(mockListing(), List.of(mockBibliographicalData()));

        assertEquals(List.of("Clean Code Book"), result.getPublicationTitles());
    }

    @Test
    void toResponseDTO_shouldMapSaleType() {
        ListingResponseDTO result = ListingMapper.toResponseDTO(mockListing(), List.of(mockBibliographicalData()));

        assertEquals(SaleType.DIRECT_SALE.toString(), result.getSaleType());
    }

    @Test
    void toResponseDTO_shouldMapPrice() {
        ListingResponseDTO result = ListingMapper.toResponseDTO(mockListing(), List.of(mockBibliographicalData()));

        assertEquals("24.99", result.getPrice());
    }

    @Test
    void toResponseDTO_shouldMapShortDescription() {
        ListingResponseDTO result = ListingMapper.toResponseDTO(mockListing(), List.of(mockBibliographicalData()));

        assertEquals("A book about clean code.", result.getDescription());
    }

    @Test
    void toResponseDTO_shouldMapSellerId() {
        ListingResponseDTO result = ListingMapper.toResponseDTO(mockListing(), List.of(mockBibliographicalData()));

        assertEquals("user-1", result.getSellerId());
    }

    @Test
    void toResponseDTO_shouldMapPictures() {
        ListingResponseDTO result = ListingMapper.toResponseDTO(mockListing(), List.of(mockBibliographicalData()));

        assertEquals(List.of("image.jpg"), result.getPictures());
    }

    @Test
    void toResponseDTO_shouldMapPublicationDate() {
        ListingResponseDTO result = ListingMapper.toResponseDTO(mockListing(), List.of(mockBibliographicalData()));

        assertEquals(LocalDate.of(2024, 1, 10), result.getPublicationDate());
    }

    @Test
    void toResponseExploreDTO_shouldMapId() {
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(
                mockListing(),
                List.of(mockBibliographicalData()),
                List.of("Robert C. Martin"),
                Condition.GOOD
        );

        assertEquals("listing-1", result.getId());
    }

    @Test
    void toResponseExploreDTO_shouldMapListingTitle() {
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(
                mockListing(),
                List.of(mockBibliographicalData()),
                List.of("Robert C. Martin"),
                Condition.GOOD
        );

        assertEquals("Clean Code", result.getListingTitle());
    }

    @Test
    void toResponseExploreDTO_shouldMapAuthors() {
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(
                mockListing(),
                List.of(mockBibliographicalData()),
                List.of("Robert C. Martin"),
                Condition.GOOD
        );

        assertEquals(List.of("Robert C. Martin"), result.getAuthors());
    }

    @Test
    void toResponseExploreDTO_shouldMapGenre() {
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(
                mockListing(),
                List.of(mockBibliographicalData()),
                List.of("Robert C. Martin"),
                Condition.GOOD
        );

        assertEquals(List.of("genre-1"), result.getGenre());
    }

    @Test
    void toResponseExploreDTO_shouldMapPrice() {
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(
                mockListing(),
                List.of(mockBibliographicalData()),
                List.of("Robert C. Martin"),
                Condition.GOOD
        );

        assertEquals("24.99", result.getPrice());
    }

    @Test
    void toResponseExploreDTO_shouldMapCondition() {
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(
                mockListing(),
                List.of(mockBibliographicalData()),
                List.of("Robert C. Martin"),
                Condition.GOOD
        );

        assertEquals(Condition.GOOD.getDescription(), result.getCondition());
    }

    @Test
    void toResponseExploreDTO_shouldMapSaleType() {
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(
                mockListing(),
                List.of(mockBibliographicalData()),
                List.of("Robert C. Martin"),
                Condition.GOOD
        );

        assertEquals(SaleType.DIRECT_SALE.toString(), result.getSaleType());
    }

    @Test
    void toResponseExploreDTO_shouldMapPicture() {
        ListingResponseExploreDTO result = ListingMapper.toResponseExploreDTO(
                mockListing(),
                List.of(mockBibliographicalData()),
                List.of("Robert C. Martin"),
                Condition.GOOD
        );

        assertEquals("image.jpg", result.getPicture());
    }

    @Test
    void toListingDetailsDTO_shouldMapId() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals("listing-1", result.getId());
    }

    @Test
    void toListingDetailsDTO_shouldMapListingTitle() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals("Clean Code", result.getListingTitle());
    }

    @Test
    void toListingDetailsDTO_shouldMapAuthors() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals(List.of("Robert C. Martin"), result.getAuthors());
    }

    @Test
    void toListingDetailsDTO_shouldMapGenre() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals(List.of("Software"), result.getGenre());
    }

    @Test
    void toListingDetailsDTO_shouldMapPrice() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals("24.99", result.getPrice());
    }

    @Test
    void toListingDetailsDTO_shouldMapPublicationDate() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals(LocalDate.of(2024, 1, 10), result.getPublicationDate());
    }

    @Test
    void toListingDetailsDTO_shouldMapCondition() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals(Condition.GOOD.toString(), result.getCondition());
    }

    @Test
    void toListingDetailsDTO_shouldMapSaleType() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals(SaleType.DIRECT_SALE.toString(), result.getSaleType());
    }

    @Test
    void toListingDetailsDTO_shouldMapPicture() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals("image.jpg", result.getPicture());
    }

    @Test
    void toListingDetailsDTO_shouldMapShortDescription() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals("A book about clean code.", result.getShortDescription());
    }

    @Test
    void toListingDetailsDTO_shouldMapSellerId() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals("user-1", result.getSellerId());
    }

    @Test
    void toListingDetailsDTO_shouldMapPublisher() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals("Prentice Hall", result.getPublisher());
    }

    @Test
    void toListingDetailsDTO_shouldMapIsbn() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals("9780132350884", result.getIsbn());
    }

    @Test
    void toListingDetailsDTO_shouldMapYear() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals(2008, result.getYear());
    }

    @Test
    void toListingDetailsDTO_shouldMapPages() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals(464, result.getPages());
    }

    @Test
    void toListingDetailsDTO_shouldMapLanguage() {
        ListingDetailsDTO result = mockListingDetailsDTO();

        assertEquals(Locale.ENGLISH.getDisplayLanguage(), result.getLanguage());
    }

    @Test
    void toListingDetailsDTO_shouldUseUnknownCondition_whenSampleIsMissing() {
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                mockListing(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );

        assertEquals("Unknown", result.getCondition());
    }

    @Test
    void toListingDetailsDTO_shouldUseEmptyPicture_whenPicturesAreEmpty() {
        Listing listing = mockListingWithoutPictures();

        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                listing,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );

        assertEquals("", result.getPicture());
    }

    @Test
    void toListingDetailsDTO_shouldUseDefaultPublisher_whenPublisherIsMissing() {
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                mockListing(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );

        assertEquals("N/A", result.getPublisher());
    }

    @Test
    void toListingDetailsDTO_shouldUseDefaultIsbn_whenEditionIsMissing() {
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                mockListing(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );

        assertEquals("N/A", result.getIsbn());
    }

    @Test
    void toListingDetailsDTO_shouldUseNullYear_whenEditionIsMissing() {
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                mockListing(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );

        assertNull(result.getYear());
    }

    @Test
    void toListingDetailsDTO_shouldUseNullPages_whenEditionIsMissing() {
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                mockListing(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );

        assertNull(result.getPages());
    }

    @Test
    void toListingDetailsDTO_shouldUseDefaultLanguage_whenEditionIsMissing() {
        ListingDetailsDTO result = ListingMapper.toListingDetailsDTO(
                mockListing(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );

        assertEquals("N/A", result.getLanguage());
    }

    @Test
    void toPublicationId_shouldReturnPublicationSampleId() {
        PublicationSampleId result = ListingMapper.toPublicationId("PUB-001");

        assertEquals("PUB-001", result.getID());
    }

    @Test
    void toListingId_shouldReturnListingId() {
        ListingId result = ListingMapper.toListingId("LISTING-001");

        assertEquals("LISTING-001", result.getId());
    }

    @Test
    void toGenreId_shouldReturnGenreId() {
        GenreId result = ListingMapper.toGenreId("GENRE-001");

        assertEquals("GENRE-001", result.getID());
    }

    @Test
    void toUserId_shouldReturnUserId() {
        UserId result = ListingMapper.toUserId("USER-001");

        assertEquals("USER-001", result.getUserId());
    }

    @Test
    void toPublisherId_shouldReturnPublisherId() {
        PublisherID result = ListingMapper.toPublisherId("PUBLISHER-001");

        assertEquals("PUBLISHER-001", result.getId());
    }

    @Test
    void toSaleType_shouldReturnDirectSale() {
        SaleType result = ListingMapper.toSaleType("Direct Sale");

        assertEquals(SaleType.DIRECT_SALE, result);
    }

    @Test
    void toSaleType_shouldReturnAuction() {
        SaleType result = ListingMapper.toSaleType("Auction");

        assertEquals(SaleType.AUCTION, result);
    }

    @Test
    void toSaleType_shouldThrowException_whenValueIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> ListingMapper.toSaleType("Invalid"));
    }

    @Test
    void toSaleType_shouldThrowException_whenValueIsNull() {
        assertThrows(Exception.class, () -> ListingMapper.toSaleType(null));
    }

    @Test
    void toPrice_shouldReturnPrice() {
        Price result = ListingMapper.toPrice("10.50");

        assertEquals(new BigDecimal("10.50"), result.getPrice());
    }

    @Test
    void toPrice_shouldThrowException_whenValueIsInvalid() {
        assertThrows(NumberFormatException.class, () -> ListingMapper.toPrice("abc"));
    }

    @Test
    void toPrice_shouldThrowException_whenValueIsNull() {
        assertThrows(Exception.class, () -> ListingMapper.toPrice(null));
    }

    @Test
    void toPicture_shouldReturnPicture() {
        Picture result = ListingMapper.toPicture("image-url.jpg");

        assertEquals(new Picture("image-url.jpg"), result);
    }

    @Test
    void toPicture_shouldThrowException_whenValueIsNull() {
        assertThrows(Exception.class, () -> ListingMapper.toPicture(null));
    }

    @Test
    void toPublisherId_shouldThrowException_whenValueIsNull() {
        assertThrows(Exception.class, () -> ListingMapper.toPublisherId(null));
    }

    // =========================================================================
    // toBibliographicalDataId
    // =========================================================================

    @Test
    void toBibliographicalDataId_shouldReturnBibliographicalDataId_whenIdIsValid() {
        BibliographicalDataId result = ListingMapper.toBibliographicalDataId("BD-001");

        assertEquals("BD-001", result.getID());
    }

    @Test
    void toBibliographicalDataId_shouldReturnEquivalentValueObject_whenIdIsValid() {
        BibliographicalDataId result = ListingMapper.toBibliographicalDataId("BD-001");

        assertEquals(new BibliographicalDataId("BD-001"), result);
    }

    @Test
    void toBibliographicalDataId_shouldThrowException_whenIdIsNull() {
        assertThrows(Exception.class, () -> ListingMapper.toBibliographicalDataId(null));
    }

    @Test
    void toBibliographicalDataId_shouldThrowException_whenIdIsBlank() {
        assertThrows(Exception.class, () -> ListingMapper.toBibliographicalDataId("   "));
    }

    @Test
    void toBibliographicalDataId_shouldThrowException_whenIdIsEmpty() {
        assertThrows(Exception.class, () -> ListingMapper.toBibliographicalDataId(""));
    }

    private ListingDetailsDTO mockListingDetailsDTO() {
        return ListingMapper.toListingDetailsDTO(
                mockListing(),
                List.of(mockPublicationSample()),
                List.of(mockEdition()),
                List.of(mockAuthor()),
                List.of(mockGenre()),
                List.of(mockPublisher())
        );
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

        when(bibliographicalData.getTitle()).thenReturn("Clean Code Book");
        when(bibliographicalData.getGenres()).thenReturn(List.of(genreId));
        when(genreId.getID()).thenReturn("genre-1");

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