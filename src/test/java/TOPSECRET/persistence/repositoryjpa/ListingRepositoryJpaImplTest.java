package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.persistence.repositoryjpa.assembler.ListingAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.ListingDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IListingRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link ListingRepositoryJpaImpl}.
 *
 * <p>Verifies the repository's behaviour in isolation by mocking both the JPA
 * data source ({@link IListingRepositoryJpa}) and the domain assembler
 * ({@link ListingAssembler}). Tests are grouped by method under test.</p>
 *
 * <p>Each test follows the Arrange / Act / Assert structure and exercises
 * a single logical scenario. The full call chain is exercised where relevant
 * (e.g. {@code findAllByDirectSale} delegates to {@code findAll}, which in
 * turn delegates to the JPA layer), so infrastructure wiring is validated
 * alongside filtering logic.</p>
 */


class ListingRepositoryJpaImplTest {

    @Mock
    private ListingAssembler assembler;

    @Mock
    private IListingRepositoryJpa jpa;

    @InjectMocks
    private ListingRepositoryJpaImpl repo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    // ===========================================================
    // save
    // ===========================================================

    @Test
    void save_shouldReturnSavedListing() {
        // Arrange
        Listing listing = mock(Listing.class);
        ListingDM listingDM = mock(ListingDM.class);

        when(assembler.toDM(listing)).thenReturn(listingDM);
        when(jpa.save(listingDM)).thenReturn(listingDM);
        when(assembler.toDomain(listingDM)).thenReturn(listing);

        // Act
        Listing result = repo.save(listing);

        // Assert
        assertSame(listing, result);
    }

    @Test
    void save_shouldThrowNullPointerException_whenListingIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.save(null));
    }


    // ===========================================================
    // findAll
    // ===========================================================

    @Test
    void findAll_shouldReturnAllListings() {
        // Arrange
        Listing listing_1 = mock(Listing.class);
        Listing listing_2 = mock(Listing.class);
        ListingDM dm1 = mock(ListingDM.class);
        ListingDM dm2 = mock(ListingDM.class);

        when(jpa.findAll()).thenReturn(List.of(dm1, dm2));
        when(assembler.toDomain(dm1)).thenReturn(listing_1);
        when(assembler.toDomain(dm2)).thenReturn(listing_2);

        // Act
        List<Listing> result = repo.findAll();

        // Assert
        assertEquals(2, result.size());
    }


    // ===========================================================
    // findAllByDirectSale
    // ===========================================================

    @Test
    void findAllByDirectSale_shouldReturnOnlyDirectSaleListings() {
        // Arrange
        Listing listing_1 = mock(Listing.class);
        when(listing_1.isListingLinkedToADirectSale()).thenReturn(true);

        Listing listing_2 = mock(Listing.class);
        when(listing_2.isListingLinkedToADirectSale()).thenReturn(true);

        Listing listing_3 = mock(Listing.class);
        when(listing_3.isListingLinkedToADirectSale()).thenReturn(false);

        ListingDM dm1 = mock(ListingDM.class);
        ListingDM dm2 = mock(ListingDM.class);
        ListingDM dm3 = mock(ListingDM.class);

        when(jpa.findAll()).thenReturn(List.of(dm1, dm2, dm3));
        when(assembler.toDomain(dm1)).thenReturn(listing_1);
        when(assembler.toDomain(dm2)).thenReturn(listing_2);
        when(assembler.toDomain(dm3)).thenReturn(listing_3);

        // Act
        List<Listing> result = repo.findAllByDirectSale();

        // Assert
        assertEquals(2, result.size());
    }


    // ===========================================================
    // findAllByAuction
    // ===========================================================

    @Test
    void findAllByAuction_shouldReturnOnlyAuctionListings() {
        // Arrange
        Listing listing_1 = mock(Listing.class);
        when(listing_1.isListingLinkedToAnAuction()).thenReturn(true);

        Listing listing_2 = mock(Listing.class);
        when(listing_2.isListingLinkedToAnAuction()).thenReturn(true);

        Listing listing_3 = mock(Listing.class);
        when(listing_3.isListingLinkedToAnAuction()).thenReturn(false);

        ListingDM dm1 = mock(ListingDM.class);
        ListingDM dm2 = mock(ListingDM.class);
        ListingDM dm3 = mock(ListingDM.class);

        when(jpa.findAll()).thenReturn(List.of(dm1, dm2, dm3));
        when(assembler.toDomain(dm1)).thenReturn(listing_1);
        when(assembler.toDomain(dm2)).thenReturn(listing_2);
        when(assembler.toDomain(dm3)).thenReturn(listing_3);

        // Act
        List<Listing> result = repo.findAllByAuction();

        // Assert
        assertEquals(2, result.size());
    }


    // ===========================================================
    // findById
    // ===========================================================

    @Test
    void findById_shouldReturnListing_whenIdExists() {
        // Arrange
        Listing listing_1 = mock(Listing.class);
        ListingId id_1 = mock(ListingId.class);
        ListingDM dm1 = mock(ListingDM.class);

        when(jpa.findById(id_1)).thenReturn(Optional.of(dm1));
        when(assembler.toDomain(dm1)).thenReturn(listing_1);

        // Act
        Optional<Listing> result = repo.findById(id_1);

        // Assert
        assertEquals(Optional.of(listing_1), result);
    }

    @Test
    void findById_shouldReturnEmpty_whenIdDoesNotExist() {
        // Arrange
        ListingId id = mock(ListingId.class);
        when(jpa.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Listing> result = repo.findById(id);

        // Assert
        assertEquals(Optional.empty(), result);
    }


    // ===========================================================
    // findAllByAuctionAndPublicationSampleIdIn
    // ===========================================================

    @Test
    void findAllByAuctionAndPublicationSampleIdIn_shouldReturnAuctionListings_whenSingleIdMatches() {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);

        Listing listing_1 = mock(Listing.class);
        when(listing_1.isListingLinkedToAnAuction()).thenReturn(true);

        Listing listing_2 = mock(Listing.class);
        when(listing_2.isListingLinkedToAnAuction()).thenReturn(true);

        Listing listing_3 = mock(Listing.class);
        when(listing_3.isListingLinkedToAnAuction()).thenReturn(false);

        ListingDM dm1 = mock(ListingDM.class);
        ListingDM dm2 = mock(ListingDM.class);

        when(jpa.findAllByItemsId(List.of(pbId_1))).thenReturn(List.of(dm1, dm2));
        when(assembler.toDomain(dm1)).thenReturn(listing_1);
        when(assembler.toDomain(dm2)).thenReturn(listing_2);

        // Act
        List<Listing> result = repo.findAllByAuctionAndPublicationSampleIdIn(List.of(pbId_1));

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void findAllByAuctionAndPublicationSampleIdIn_shouldReturnAuctionListings_whenMultipleIdsMatch() {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);

        Listing listing_1 = mock(Listing.class);
        when(listing_1.isListingLinkedToAnAuction()).thenReturn(true);

        Listing listing_2 = mock(Listing.class);
        when(listing_2.isListingLinkedToAnAuction()).thenReturn(true);

        Listing listing_3 = mock(Listing.class);
        when(listing_3.isListingLinkedToAnAuction()).thenReturn(false);

        ListingDM dm1 = mock(ListingDM.class);
        ListingDM dm2 = mock(ListingDM.class);

        when(jpa.findAllByItemsId(List.of(pbId_1, pbId_2))).thenReturn(List.of(dm1, dm2));
        when(assembler.toDomain(dm1)).thenReturn(listing_1);
        when(assembler.toDomain(dm2)).thenReturn(listing_2);

        // Act
        List<Listing> result = repo.findAllByAuctionAndPublicationSampleIdIn(List.of(pbId_1, pbId_2));

        // Assert
        assertEquals(2, result.size());
    }

    // ===========================================================
    // findIdsByPublicationSampleIdIn (TO BE IMPLEMENTED)
    // ===========================================================

    @Test
    void findIdsByPublicationSampleIdIn_shouldReturnEmptyList_whenNoListingsFound() {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);

        when(jpa.findAllByItemsId(List.of(pbId_1, pbId_2)))
                .thenReturn(List.of());

        // Act
        List<ListingId> result = repo.findIdsByPublicationSampleIdIn(List.of(pbId_1, pbId_2));

        // Assert
        assertTrue(result.isEmpty());

    }

    @Test
    void findIdsByPublicationSampleIdIn_shouldThrow_whenPublicationSamplesIdsIsNull() {

        // act + assert
        NullPointerException ex = assertThrows(NullPointerException.class, () -> repo.findIdsByPublicationSampleIdIn(null));

        assertEquals("List of PublicationSampleIds cannot be null", ex.getMessage());
    }

    @Test
    void findIdsByPublicationSampleIdIn_shouldReturnListingIds_whenPublicationSamplesAreInListings() {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);

        ListingDM listingDM_1 = mock(ListingDM.class);
        ListingDM listingDM_2 = mock(ListingDM.class);

        ListingId listingId_1 = mock(ListingId.class);
        ListingId listingId_2 = mock(ListingId.class);

        when(jpa.findAllByItemsId(List.of(pbId_1, pbId_2)))
                .thenReturn(List.of(listingDM_1, listingDM_2));

        when(listingDM_1.getListingId()).thenReturn(listingId_1);
        when(listingDM_2.getListingId()).thenReturn(listingId_2);

        // Act
        List<ListingId> result = repo.findIdsByPublicationSampleIdIn(List.of(pbId_1, pbId_2));

        // Assert
        assertEquals(2, result.size());
        assertEquals(listingId_1, result.get(0));
        assertEquals(listingId_2, result.get(1));

    }





    @Test
    void findIdsByPublicationSampleIdIn_shouldReturnEmptyList_whenInputIsEmpty() {
        // Act
        List<ListingId> result = repo.findIdsByPublicationSampleIdIn(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }


    // ===========================================================
    // findAllByDirectSaleAndPublicationSampleIdIn
    // ===========================================================

    @Test
    void findAllByDirectSaleAndPublicationSampleIdIn_shouldThrowNullPointerException_whenIdsIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.findAllByDirectSaleAndPublicationSampleIdIn(null));
    }

    @Test
    void findAllByDirectSaleAndPublicationSampleIdIn_shouldReturnEmptyList_whenNoListingsMatchIds() {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);

        when(jpa.findAllByItemsId(List.of(pbId_1))).thenReturn(List.of());

        // Act
        List<Listing> result = repo.findAllByDirectSaleAndPublicationSampleIdIn(List.of(pbId_1));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllByDirectSaleAndPublicationSampleIdIn_shouldReturnOnlyDirectSaleListings_whenSomeAreNot() {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);

        Listing listing_1 = mock(Listing.class);
        when(listing_1.isListingLinkedToADirectSale()).thenReturn(true);

        Listing listing_2 = mock(Listing.class);
        when(listing_2.isListingLinkedToADirectSale()).thenReturn(false);

        ListingDM dm1 = mock(ListingDM.class);
        ListingDM dm2 = mock(ListingDM.class);

        when(jpa.findAllByItemsId(List.of(pbId_1))).thenReturn(List.of(dm1, dm2));
        when(assembler.toDomain(dm1)).thenReturn(listing_1);
        when(assembler.toDomain(dm2)).thenReturn(listing_2);

        // Act
        List<Listing> result = repo.findAllByDirectSaleAndPublicationSampleIdIn(List.of(pbId_1));

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void findAllByDirectSaleAndPublicationSampleIdIn_shouldReturnAllListings_whenAllAreDirectSale() {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);

        Listing listing_1 = mock(Listing.class);
        when(listing_1.isListingLinkedToADirectSale()).thenReturn(true);

        Listing listing_2 = mock(Listing.class);
        when(listing_2.isListingLinkedToADirectSale()).thenReturn(true);

        ListingDM dm1 = mock(ListingDM.class);
        ListingDM dm2 = mock(ListingDM.class);

        when(jpa.findAllByItemsId(List.of(pbId_1))).thenReturn(List.of(dm1, dm2));
        when(assembler.toDomain(dm1)).thenReturn(listing_1);
        when(assembler.toDomain(dm2)).thenReturn(listing_2);

        // Act
        List<Listing> result = repo.findAllByDirectSaleAndPublicationSampleIdIn(List.of(pbId_1));

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnListingIdsByPublicationSampleIds() {

        // Arrange
        PublicationSampleId sampleId = mock(PublicationSampleId.class);
        ListingId listingId = mock(ListingId.class);
        ListingDM dm = mock(ListingDM.class);

        when(dm.getListingId()).thenReturn(listingId);
        when(jpa.findAllByItemsId(List.of(sampleId))).thenReturn(List.of(dm));

        // Act
        List<ListingId> result = repo.findIdsByPublicationSampleIdIn(List.of(sampleId));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(listingId, result.get(0));

    }

    @Test
    void shouldReturnEmptyListWhenNoListingsExist() {

        // Arrange
        PublicationSampleId sampleId = mock(PublicationSampleId.class);

        when(jpa.findAllByItemsId(List.of(sampleId))).thenReturn(List.of());

        // Act
        List<ListingId> result = repo.findIdsByPublicationSampleIdIn(List.of(sampleId));

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

    }

    @Test
    void shouldThrowExceptionWhenPublicationSampleIdsIsNull() {

        // Act & Assert
        Exception ex = assertThrows(
                NullPointerException.class,
                () -> repo.findIdsByPublicationSampleIdIn(null));

        assertEquals(
                "List of PublicationSampleIds cannot be null",
                ex.getMessage());
    }

    // ===========================================================
    // findByPublicationSampleIdIn
    // ===========================================================

    @Test
    void findByPublicationSampleIdIn_shouldThrowNullPointerException_whenPublicationSampleIdsIsNull() {
        // Act + Assert
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> repo.findByPublicationSampleIdIn(null));

        assertEquals("List of PublicationSampleIds cannot be null", ex.getMessage());
    }

    @Test
    void findByPublicationSampleIdIn_shouldReturnEmptyList_whenNoListingsFound() {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);

        when(jpa.findAllByItemsId(List.of(pbId_1))).thenReturn(List.of());

        // Act
        List<Listing> result = repo.findByPublicationSampleIdIn(List.of(pbId_1));

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByPublicationSampleIdIn_shouldReturnEmptyList_whenInputListIsEmpty() {
        // Arrange
        when(jpa.findAllByItemsId(List.of())).thenReturn(List.of());

        // Act
        List<Listing> result = repo.findByPublicationSampleIdIn(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByPublicationSampleIdIn_shouldReturnListing_whenSingleIdMatches() {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);

        ListingDM dm1 = mock(ListingDM.class);
        Listing listing_1 = mock(Listing.class);

        when(jpa.findAllByItemsId(List.of(pbId_1))).thenReturn(List.of(dm1));
        when(assembler.toDomain(dm1)).thenReturn(listing_1);

        // Act
        List<Listing> result = repo.findByPublicationSampleIdIn(List.of(pbId_1));

        // Assert
        assertEquals(1, result.size());
        assertSame(listing_1, result.get(0));
    }

    @Test
    void findByPublicationSampleIdIn_shouldReturnMultipleListings_whenMultipleIdsMatch() {
        // Arrange
        PublicationSampleId pbId_1 = mock(PublicationSampleId.class);
        PublicationSampleId pbId_2 = mock(PublicationSampleId.class);

        ListingDM dm1 = mock(ListingDM.class);
        ListingDM dm2 = mock(ListingDM.class);

        Listing listing_1 = mock(Listing.class);
        Listing listing_2 = mock(Listing.class);

        when(jpa.findAllByItemsId(List.of(pbId_1, pbId_2))).thenReturn(List.of(dm1, dm2));
        when(assembler.toDomain(dm1)).thenReturn(listing_1);
        when(assembler.toDomain(dm2)).thenReturn(listing_2);

        // Act
        List<Listing> result = repo.findByPublicationSampleIdIn(List.of(pbId_1, pbId_2));

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(listing_1));
        assertTrue(result.contains(listing_2));
    }
}