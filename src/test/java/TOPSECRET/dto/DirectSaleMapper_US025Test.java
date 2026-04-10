package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
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

class DirectSaleMapper_US025Test {

    /**
     * Unit tests for {@link DirectSaleMapper_US025}.
     *
     * This test class validates that DTO mapping from domain objects (Listing, PublicationSample)
     * is done correctly. All domain dependencies are mocked to isolate DTO conversion logic.
     */

    private IBibliographicalDataRepository bibliographicalDataRepository;
    private IAuthorRepository authorRepository;
    private IListingRepository listingRepository;
    private IPublicationSampleRepository publicationSampleRepository;
    private DirectSaleMapper_US025 mapper;

    @BeforeEach
    void setUp() {
        bibliographicalDataRepository = mock(IBibliographicalDataRepository.class);
        authorRepository = mock(IAuthorRepository.class);
        listingRepository = mock(IListingRepository.class);
        publicationSampleRepository = mock(IPublicationSampleRepository.class);

        mapper = new DirectSaleMapper_US025(
                bibliographicalDataRepository,
                authorRepository,
                listingRepository,
                publicationSampleRepository);
    }

    // ===============================
    // Helpers
    // ===============================

    private Listing buildListing(PublicationSampleId itemId, BigDecimal priceValue,
                                 String listingTitle, UserId sellerId) {
        Listing listing = mock(Listing.class);
        Price price = mock(Price.class);
        when(price.getPrice()).thenReturn(priceValue);
        when(listing.getItemsId()).thenReturn(List.of(itemId));
        when(listing.getPrice()).thenReturn(price);
        when(listing.getListingTitle()).thenReturn(listingTitle);
        when(listing.getSellerId()).thenReturn(sellerId);
        return listing;
    }

    private PublicationSample buildItem(PublicationSampleId itemId, BibliographicalDataID bdId) {
        PublicationSample item = mock(PublicationSample.class);
        when(item.id()).thenReturn(itemId);
        when(item.getBibliographicalDataID()).thenReturn(bdId);
        return item;
    }

    private BibliographicalData buildBD(String title, List<AuthorId> authorIds) {
        BibliographicalData bd = mock(BibliographicalData.class);
        when(bd.getTitle()).thenReturn(title);
        when(bd.getAuthors()).thenReturn(authorIds);
        return bd;
    }

    // ===============================
    // Tests for toDTO()
    // ===============================

    @Test
    void toDTO_shouldMapAllFieldsCorrectly() {

        // Arrange
        AuthorId authorId = mock(AuthorId.class);
        Author author = mock(Author.class);
        when(author.getName()).thenReturn("author1");
        when(authorRepository.findAuthorById(authorId)).thenReturn(Optional.of(author));

        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        BibliographicalData bd = buildBD("Title One", List.of(authorId));
        when(bibliographicalDataRepository.findBibliographicalDataById(bdId)).thenReturn(Optional.of(bd));

        PublicationSampleId itemId = mock(PublicationSampleId.class);
        PublicationSample item = buildItem(itemId, bdId);

        UserId sellerId = mock(UserId.class);
        when(sellerId.toString()).thenReturn("seller1");
        Listing listing = buildListing(itemId, new BigDecimal("10.00"), "Listing One", sellerId);

        // Act
        DirectSaleDTO_US025 dto = mapper.toDTO(item, listing);

        // Assert
        assertNotNull(dto);
        assertEquals("Title One", dto.publicationTitle);
        assertEquals(List.of("author1"), dto.authors);
        assertEquals(new BigDecimal("10.00"), dto.price);
        assertEquals("Listing One", dto.listingTitle);
        assertEquals("seller1", dto.seller);
    }

    // ===============================
    // Tests for toDTOList()
    // ===============================

    @Test
    void toDTOList_shouldReturnDTOForEachItem() {

        // Arrange
        AuthorId authorId = mock(AuthorId.class);
        Author author = mock(Author.class);
        when(author.getName()).thenReturn("author1");
        when(authorRepository.findAuthorById(authorId)).thenReturn(Optional.of(author));

        BibliographicalDataID bdId1 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId2 = mock(BibliographicalDataID.class);

        BibliographicalData bd1 = buildBD("Title One", List.of(authorId));
        BibliographicalData bd2 = buildBD("Title Two", List.of(authorId));

        when(bibliographicalDataRepository.findBibliographicalDataById(bdId1)).thenReturn(Optional.of(bd1));
        when(bibliographicalDataRepository.findBibliographicalDataById(bdId2)).thenReturn(Optional.of(bd2));

        PublicationSampleId itemId1 = mock(PublicationSampleId.class);
        PublicationSampleId itemId2 = mock(PublicationSampleId.class);

        PublicationSample item1 = buildItem(itemId1, bdId1);
        PublicationSample item2 = buildItem(itemId2, bdId2);

        when(publicationSampleRepository.findPublicationSampleById(itemId1)).thenReturn(Optional.of(item1));
        when(publicationSampleRepository.findPublicationSampleById(itemId2)).thenReturn(Optional.of(item2));

        UserId sellerId = mock(UserId.class);
        Listing listing1 = buildListing(itemId1, new BigDecimal("10.00"), "Listing One", sellerId);
        Listing listing2 = buildListing(itemId2, new BigDecimal("15.00"), "Listing Two", sellerId);

        // Act
        List<DirectSaleDTO_US025> dtos = mapper.toDTOList(List.of(listing1, listing2));

        // Assert
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("Title One", dtos.get(0).publicationTitle);
        assertEquals("Title Two", dtos.get(1).publicationTitle);
    }

    @Test
    void toDTOList_emptyListings_shouldReturnEmptyList() {

        // Act
        List<DirectSaleDTO_US025> dtos = mapper.toDTOList(List.of());

        // Assert
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    // ===============================
    // Tests for toDTOListByAuthor()
    // ===============================

    @Test
    void toDTOListByAuthor_shouldReturnOnlyItemsOfGivenAuthor() {

        // Arrange
        AuthorId authorId1 = mock(AuthorId.class);
        AuthorId authorId2 = mock(AuthorId.class);

        Author author1 = mock(Author.class);
        when(author1.getName()).thenReturn("author1");
        when(author1.id()).thenReturn(authorId1);
        when(authorRepository.findAuthorById(authorId1)).thenReturn(Optional.of(author1));

        BibliographicalDataID bdId1 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId2 = mock(BibliographicalDataID.class);

        BibliographicalData bd1 = buildBD("Title One", List.of(authorId1));
        BibliographicalData bd2 = buildBD("Title Two", List.of(authorId2)); // autor diferente

        when(bibliographicalDataRepository.findBibliographicalDataById(bdId1)).thenReturn(Optional.of(bd1));
        when(bibliographicalDataRepository.findBibliographicalDataById(bdId2)).thenReturn(Optional.of(bd2));

        PublicationSampleId itemId1 = mock(PublicationSampleId.class);
        PublicationSampleId itemId2 = mock(PublicationSampleId.class);

        PublicationSample item1 = buildItem(itemId1, bdId1);
        PublicationSample item2 = buildItem(itemId2, bdId2);

        when(publicationSampleRepository.findPublicationSampleById(itemId1)).thenReturn(Optional.of(item1));
        when(publicationSampleRepository.findPublicationSampleById(itemId2)).thenReturn(Optional.of(item2));

        UserId sellerId = mock(UserId.class);
        Listing listing1 = buildListing(itemId1, new BigDecimal("10.00"), "Listing One", sellerId);
        Listing listing2 = buildListing(itemId2, new BigDecimal("15.00"), "Listing Two", sellerId);

        when(listingRepository.findAllDirectSaleListings()).thenReturn(List.of(listing1, listing2));

        // Act
        List<DirectSaleDTO_US025> dtos = mapper.toDTOListByAuthor(author1);

        // Assert
        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals("Title One", dtos.get(0).publicationTitle);
    }

    @Test
    void toDTOListByAuthor_whenNoItemsMatchAuthor_shouldReturnEmptyList() {

        // Arrange
        AuthorId authorId1 = mock(AuthorId.class);
        AuthorId authorId2 = mock(AuthorId.class);

        Author author1 = mock(Author.class);
        when(author1.id()).thenReturn(authorId1);

        BibliographicalDataID bdId = mock(BibliographicalDataID.class);
        BibliographicalData bd = buildBD("Title One", List.of(authorId2)); // autor diferente

        when(bibliographicalDataRepository.findBibliographicalDataById(bdId)).thenReturn(Optional.of(bd));

        PublicationSampleId itemId = mock(PublicationSampleId.class);
        PublicationSample item = buildItem(itemId, bdId);

        when(publicationSampleRepository.findPublicationSampleById(itemId)).thenReturn(Optional.of(item));

        UserId sellerId = mock(UserId.class);
        Listing listing = buildListing(itemId, new BigDecimal("10.00"), "Listing One", sellerId);

        when(listingRepository.findAllDirectSaleListings()).thenReturn(List.of(listing));

        // Act
        List<DirectSaleDTO_US025> dtos = mapper.toDTOListByAuthor(author1);

        // Assert
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}