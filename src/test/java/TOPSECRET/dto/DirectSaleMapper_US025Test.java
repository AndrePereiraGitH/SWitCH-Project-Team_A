package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DirectSaleMapper_US025}.
 *
 * Verifies that domain entities are correctly converted to
 * {@link DirectSaleDTO_US025} representations, including filtering
 * by author and edge cases such as null inputs.
 */
class DirectSaleMapper_US025Test {

    private IBibliographicalDataRepository bibliographicalDataRepository;
    private IListingRepository listingRepository;
    private IPublicationSampleRepository publicationSampleRepository;
    private DirectSaleMapper_US025 DSMapper;

    @BeforeEach
    void setUp() {
        bibliographicalDataRepository = mock(IBibliographicalDataRepository.class);
        listingRepository = mock(IListingRepository.class);
        publicationSampleRepository = mock(IPublicationSampleRepository.class);

        DSMapper = new DirectSaleMapper_US025(
                bibliographicalDataRepository,
                listingRepository,
                publicationSampleRepository);
    }

    // ===============================
    // Helpers
    // ===============================

    private Listing buildListing(PublicationSampleId itemId, BigDecimal priceValue,
                                 String listingTitle, UserId sellerId) {
        Listing listingDouble = mock(Listing.class);
        Price priceDouble = mock(Price.class);
        when(priceDouble.getPrice()).thenReturn(priceValue);
        when(listingDouble.getItemsId()).thenReturn(List.of(itemId));
        when(listingDouble.getPrice()).thenReturn(priceDouble);
        when(listingDouble.getListingTitle()).thenReturn(listingTitle);
        when(listingDouble.getSellerId()).thenReturn(sellerId);
        return listingDouble;
    }

    private PublicationSample buildItem(PublicationSampleId itemId, BibliographicalDataID bdId) {
        PublicationSample itemDouble = mock(PublicationSample.class);
        when(itemDouble.id()).thenReturn(itemId);
        when(itemDouble.getBibliographicalDataID()).thenReturn(bdId);
        return itemDouble;
    }

    private BibliographicalData buildBD(String title, List<AuthorId> authorIds) {
        BibliographicalData bdDouble = mock(BibliographicalData.class);
        when(bdDouble.getTitle()).thenReturn(title);
        when(bdDouble.getAuthors()).thenReturn(authorIds);
        return bdDouble;
    }

    // ===============================
    // Tests for toDTOListByAuthor()
    // ===============================

    @Test
    void toDTOListByAuthor_shouldReturnOnlyItemsOfGivenAuthor() {

        // Arrange
        AuthorId authorId1Double = mock(AuthorId.class);
        AuthorId authorId2Double = mock(AuthorId.class);

        Author author1Double = mock(Author.class);
        when(author1Double.getName()).thenReturn("author1");
        when(author1Double.id()).thenReturn(authorId1Double);

        BibliographicalDataID bdId1Double = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId2Double = mock(BibliographicalDataID.class);

        BibliographicalData bd1Double = buildBD("Title One", List.of(authorId1Double));
        BibliographicalData bd2Double = buildBD("Title Two", List.of(authorId2Double));

        when(bibliographicalDataRepository.findBibliographicalDataById(bdId1Double)).thenReturn(Optional.of(bd1Double));
        when(bibliographicalDataRepository.findBibliographicalDataById(bdId2Double)).thenReturn(Optional.of(bd2Double));

        PublicationSampleId itemId1Double = mock(PublicationSampleId.class);
        PublicationSampleId itemId2Double = mock(PublicationSampleId.class);

        PublicationSample item1Double = buildItem(itemId1Double, bdId1Double);
        PublicationSample item2Double = buildItem(itemId2Double, bdId2Double);

        when(publicationSampleRepository.findPublicationSampleById(itemId1Double)).thenReturn(Optional.of(item1Double));
        when(publicationSampleRepository.findPublicationSampleById(itemId2Double)).thenReturn(Optional.of(item2Double));

        UserId sellerIdDouble = mock(UserId.class);
        Listing listing1Double = buildListing(itemId1Double, new BigDecimal("10.00"), "Listing One", sellerIdDouble);
        Listing listing2Double = buildListing(itemId2Double, new BigDecimal("15.00"), "Listing Two", sellerIdDouble);

        when(listingRepository.findAllDirectSaleListings()).thenReturn(List.of(listing1Double, listing2Double));

        // Act
        List<DirectSaleDTO_US025> dtos = DSMapper.toDTOListByAuthor(author1Double);

        // Assert
        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals("Title One", dtos.get(0).publicationTitle());
        assertEquals(List.of("author1"), dtos.get(0).authors());
    }

    @Test
    void toDTOListByAuthor_whenNoItemsMatchAuthor_shouldReturnEmptyList() {

        // Arrange
        AuthorId authorId1Double = mock(AuthorId.class);
        AuthorId authorId2Double = mock(AuthorId.class);

        Author author1Double = mock(Author.class);
        when(author1Double.id()).thenReturn(authorId1Double);

        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);
        BibliographicalData bdDouble = buildBD("Title One", List.of(authorId2Double));

        when(bibliographicalDataRepository.findBibliographicalDataById(bdIdDouble)).thenReturn(Optional.of(bdDouble));

        PublicationSampleId itemIdDouble = mock(PublicationSampleId.class);
        PublicationSample itemDouble = buildItem(itemIdDouble, bdIdDouble);

        when(publicationSampleRepository.findPublicationSampleById(itemIdDouble)).thenReturn(Optional.of(itemDouble));

        UserId sellerIdDouble = mock(UserId.class);
        Listing listingDouble = buildListing(itemIdDouble, new BigDecimal("10.00"), "Listing One", sellerIdDouble);

        when(listingRepository.findAllDirectSaleListings()).thenReturn(List.of(listingDouble));

        // Act
        List<DirectSaleDTO_US025> dtos = DSMapper.toDTOListByAuthor(author1Double);

        // Assert
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void toDTOListByAuthor_whenAuthorIsNull_shouldThrowException() {

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> DSMapper.toDTOListByAuthor(null)
        );
    }

    @Test
    void toDTOListByAuthor_whenNoListings_shouldReturnEmptyList() {

        // Arrange
        Author authorDouble = mock(Author.class);
        when(listingRepository.findAllDirectSaleListings()).thenReturn(List.of());

        // Act
        List<DirectSaleDTO_US025> dtos = DSMapper.toDTOListByAuthor(authorDouble);

        // Assert
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}