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

/**
 * Unit tests for {@link DirectSaleMapper_US025}.
 *
 * Verifies that domain entities are correctly converted to
 * {@link DirectSaleDTO_US025} representations, including filtering
 * by author and edge cases such as null and empty inputs.
 */

class DirectSaleMapper_US025Test {

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
        Listing listingdouble = mock(Listing.class);
        Price pricedouble = mock(Price.class);
        when(pricedouble.getPrice()).thenReturn(priceValue);
        when(listingdouble.getItemsId()).thenReturn(List.of(itemId));
        when(listingdouble.getPrice()).thenReturn(pricedouble);
        when(listingdouble.getListingTitle()).thenReturn(listingTitle);
        when(listingdouble.getSellerId()).thenReturn(sellerId);
        return listingdouble;
    }

    private PublicationSample buildItem(PublicationSampleId itemId, BibliographicalDataID bdId) {
        PublicationSample itemdouble = mock(PublicationSample.class);
        when(itemdouble.id()).thenReturn(itemId);
        when(itemdouble.getBibliographicalDataID()).thenReturn(bdId);
        return itemdouble;
    }

    private BibliographicalData buildBD(String title, List<AuthorId> authorIds) {
        BibliographicalData bddouble = mock(BibliographicalData.class);
        when(bddouble.getTitle()).thenReturn(title);
        when(bddouble.getAuthors()).thenReturn(authorIds);
        return bddouble;
    }

    // ===============================
    // Tests for toDTO()
    // ===============================

    @Test
    void toDTO_shouldMapAllFieldsCorrectly() {

        // Arrange
        AuthorId authorIddouble = mock(AuthorId.class);
        Author authordouble = mock(Author.class);
        when(authordouble.getName()).thenReturn("author1");
        when(authorRepository.findAuthorById(authorIddouble)).thenReturn(Optional.of(authordouble));

        BibliographicalDataID bdIddouble = mock(BibliographicalDataID.class);
        BibliographicalData bddouble = buildBD("Title One", List.of(authorIddouble));
        when(bibliographicalDataRepository.findBibliographicalDataById(bdIddouble)).thenReturn(Optional.of(bddouble));

        PublicationSampleId itemIddouble = mock(PublicationSampleId.class);
        PublicationSample itemdouble = buildItem(itemIddouble, bdIddouble);

        UserId sellerIddouble = mock(UserId.class);
        when(sellerIddouble.toString()).thenReturn("seller1");
        Listing listingdouble = buildListing(itemIddouble, new BigDecimal("10.00"), "Listing One", sellerIddouble);

        // Act
        DirectSaleDTO_US025 dto = mapper.toDTO(itemdouble, listingdouble);

        // Assert
        assertNotNull(dto);
        assertEquals("Title One", dto.publicationTitle());
        assertEquals(List.of("author1"), dto.authors());
        assertEquals(new BigDecimal("10.00"), dto.price());
        assertEquals("Listing One", dto.listingTitle());
        assertEquals("seller1", dto.seller());
    }

    // ===============================
    // Tests for toDTOList()
    // ===============================

    @Test
    void toDTOList_shouldReturnDTOForEachItem() {

        // Arrange
        AuthorId authorIddouble = mock(AuthorId.class);
        Author authordouble = mock(Author.class);
        when(authordouble.getName()).thenReturn("author1");
        when(authorRepository.findAuthorById(authorIddouble)).thenReturn(Optional.of(authordouble));

        BibliographicalDataID bdId1double = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId2double = mock(BibliographicalDataID.class);

        BibliographicalData bd1 = buildBD("Title One", List.of(authorIddouble));
        BibliographicalData bd2 = buildBD("Title Two", List.of(authorIddouble));

        when(bibliographicalDataRepository.findBibliographicalDataById(bdId1double)).thenReturn(Optional.of(bd1));
        when(bibliographicalDataRepository.findBibliographicalDataById(bdId2double)).thenReturn(Optional.of(bd2));

        PublicationSampleId itemId1double = mock(PublicationSampleId.class);
        PublicationSampleId itemId2double = mock(PublicationSampleId.class);

        PublicationSample item1 = buildItem(itemId1double, bdId1double);
        PublicationSample item2 = buildItem(itemId2double, bdId2double);

        when(publicationSampleRepository.findPublicationSampleById(itemId1double)).thenReturn(Optional.of(item1));
        when(publicationSampleRepository.findPublicationSampleById(itemId2double)).thenReturn(Optional.of(item2));

        UserId sellerIddouble = mock(UserId.class);
        Listing listing1 = buildListing(itemId1double, new BigDecimal("10.00"), "Listing One", sellerIddouble);
        Listing listing2 = buildListing(itemId2double, new BigDecimal("15.00"), "Listing Two", sellerIddouble);

        // Act
        List<DirectSaleDTO_US025> dtos = mapper.toDTOList(List.of(listing1, listing2));

        // Assert
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("Title One", dtos.get(0).publicationTitle());
        assertEquals("Title Two", dtos.get(1).publicationTitle());
    }

    @Test
    void toDTOList_nullListings_shouldReturnEmptyList() {

        // Act
        List<DirectSaleDTO_US025> dtos = mapper.toDTOList(null);

        // Assert
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
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
        AuthorId authorId1double = mock(AuthorId.class);
        AuthorId authorId2double = mock(AuthorId.class);

        Author author1double = mock(Author.class);
        when(author1double.getName()).thenReturn("author1");
        when(authorRepository.findAuthorById(authorId1double)).thenReturn(Optional.of(author1double));

        BibliographicalDataID bdId1double = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId2double = mock(BibliographicalDataID.class);

        BibliographicalData bd1 = buildBD("Title One", List.of(authorId1double));
        BibliographicalData bd2 = buildBD("Title Two", List.of(authorId2double));

        when(bibliographicalDataRepository.findBibliographicalDataById(bdId1double)).thenReturn(Optional.of(bd1));
        when(bibliographicalDataRepository.findBibliographicalDataById(bdId2double)).thenReturn(Optional.of(bd2));

        PublicationSampleId itemId1double = mock(PublicationSampleId.class);
        PublicationSampleId itemId2double = mock(PublicationSampleId.class);

        PublicationSample item1 = buildItem(itemId1double, bdId1double);
        PublicationSample item2 = buildItem(itemId2double, bdId2double);

        when(publicationSampleRepository.findPublicationSampleById(itemId1double)).thenReturn(Optional.of(item1));
        when(publicationSampleRepository.findPublicationSampleById(itemId2double)).thenReturn(Optional.of(item2));

        UserId sellerIddouble = mock(UserId.class);
        Listing listing1 = buildListing(itemId1double, new BigDecimal("10.00"), "Listing One", sellerIddouble);
        Listing listing2 = buildListing(itemId2double, new BigDecimal("15.00"), "Listing Two", sellerIddouble);

        when(listingRepository.findAllDirectSaleListings()).thenReturn(List.of(listing1, listing2));

        //Act
        List<DirectSaleDTO_US025> dtos = mapper.toDTOListByAuthor(authorId1double);

        // Assert
        assertNotNull(dtos);
        assertEquals(1, dtos.size());
        assertEquals("Title One", dtos.get(0).publicationTitle());
    }

    @Test
    void toDTOListByAuthor_whenNoItemsMatchAuthor_shouldReturnEmptyList() {

        // Arrange
        AuthorId authorId1double = mock(AuthorId.class);
        AuthorId authorId2double = mock(AuthorId.class);

        BibliographicalDataID bdIddouble = mock(BibliographicalDataID.class);
        BibliographicalData bd = buildBD("Title One", List.of(authorId2double));

        when(bibliographicalDataRepository.findBibliographicalDataById(bdIddouble)).thenReturn(Optional.of(bd));

        PublicationSampleId itemIddouble = mock(PublicationSampleId.class);
        PublicationSample item = buildItem(itemIddouble, bdIddouble);

        when(publicationSampleRepository.findPublicationSampleById(itemIddouble)).thenReturn(Optional.of(item));

        UserId sellerIddouble = mock(UserId.class);
        Listing listing = buildListing(itemIddouble, new BigDecimal("10.00"), "Listing One", sellerIddouble);

        when(listingRepository.findAllDirectSaleListings()).thenReturn(List.of(listing));

        // Act
        List<DirectSaleDTO_US025> dtos = mapper.toDTOListByAuthor(authorId1double);

        // Assert
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void toDTOListByAuthor_whenAuthorIdIsNull_shouldThrowException() {

        // Act + Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> mapper.toDTOListByAuthor(null)
        );
    }
}