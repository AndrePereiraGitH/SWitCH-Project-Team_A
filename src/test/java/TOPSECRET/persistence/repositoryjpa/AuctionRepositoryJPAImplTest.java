package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.Bid;
import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.persistence.repositoryjpa.assembler.AuctionAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.AuctionDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IAuctionRepositoryJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuctionRepositoryJPAImpl}.
 *
 * <p>
 * These tests verify the behaviour of the repository adapter responsible for
 * persisting and retrieving {@link Auction} aggregates using the underlying
 * Spring Data JPA repository ({@link IAuctionRepositoryJPA}) and the
 * {@link AuctionAssembler} for domain–data model conversions.
 * </p>
 *
 * <p>
 * The suite ensures that:
 * <ul>
 *     <li>constructor validation enforces non-null dependencies</li>
 *     <li>{@code save} correctly handles creation, conversion and error cases</li>
 *     <li>{@code update} correctly handles updates, conversion and error cases,
 *         including throwing {@link IllegalArgumentException} when the auction
 *         does not exist</li>
 *     <li>{@code findById} returns the expected Optional semantics</li>
 *     <li>{@code findAll} maps all persistence models to domain objects</li>
 *     <li>{@code findByListingId} validates input and converts results</li>
 * </ul>
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class AuctionRepositoryJPAImplTest {

    // =========================================================
    // ================ Dependencies ===========================
    // =========================================================

    @Mock
    IAuctionRepositoryJPA auctionRepoJPA;

    @Mock
    AuctionAssembler auctionAssembler;

    @InjectMocks
    AuctionRepositoryJPAImpl repository;

    // =========================================================
    // =============== Doubles =================================
    // =========================================================

    private Auction auctionDouble;
    private AuctionDM auctionDMDouble;
    private AuctionId auctionIdDouble;
    private ListingId listingIdDouble;
    private Bid bidDouble;

    @BeforeEach
    void setUp() {
        auctionDouble = mock(Auction.class);
        auctionDMDouble = mock(AuctionDM.class);
        auctionIdDouble = mock(AuctionId.class);
        listingIdDouble = mock(ListingId.class);
        bidDouble = mock(Bid.class);
    }

    // =========================================================
    // =============== Constructor =============================
    // =========================================================

    @Test
    void constructor_shouldThrowErrorWhenRepositoryIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                new AuctionRepositoryJPAImpl(null, auctionAssembler)
        );
    }

    @Test
    void constructor_shouldThrowErrorWhenAssemblerIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                new AuctionRepositoryJPAImpl(auctionRepoJPA, null)
        );
    }


    // =========================================================
    // ================== save =================================
    // =========================================================

    @Test
    void save_shouldConvertAndReturnDomainAuction() {
        // Arrange
        Auction auctionInputDouble = mock(Auction.class);
        AuctionDM dmToSaveDouble = mock(AuctionDM.class);
        AuctionDM dmSavedDouble = mock(AuctionDM.class);
        Auction auctionOutputDouble = mock(Auction.class);

        when(auctionInputDouble.id()).thenReturn(auctionIdDouble);

        when(auctionRepoJPA.existsById(auctionIdDouble)).thenReturn(false);
        when(auctionAssembler.toDM(auctionInputDouble)).thenReturn(dmToSaveDouble);
        when(auctionRepoJPA.save(dmToSaveDouble)).thenReturn(dmSavedDouble);
        when(auctionAssembler.toDomain(dmSavedDouble)).thenReturn(auctionOutputDouble);

        AuctionRepositoryJPAImpl repository = new AuctionRepositoryJPAImpl(auctionRepoJPA, auctionAssembler);

        // Act
        Auction result = repository.save(auctionInputDouble);

        // Assert
        assertEquals(auctionOutputDouble, result);
    }

    @Test
    void save_shouldThrowErrorWhenAuctionIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> repository.save(null));
    }

    @Test
    void save_shouldThrowErrorWhenAuctionAlreadyExists() {
        // Arrange
        when(auctionDouble.id()).thenReturn(auctionIdDouble);
        when(auctionRepoJPA.existsById(auctionIdDouble)).thenReturn(true);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repository.save(auctionDouble)
        );
    }

    // =========================================================
    // ================== update ===============================
    // =========================================================

    @Test
    void update_shouldConvertAndReturnUpdatedDomainAuction() {
        // Arrange
        Auction auctionInputDouble = mock(Auction.class);
        AuctionDM dmToUpdateDouble = mock(AuctionDM.class);
        AuctionDM dmUpdatedDouble = mock(AuctionDM.class);
        Auction auctionOutputDouble = mock(Auction.class);

        when(auctionInputDouble.id()).thenReturn(auctionIdDouble);
        when(auctionRepoJPA.existsById(auctionIdDouble)).thenReturn(true);
        when(auctionAssembler.toDM(auctionInputDouble)).thenReturn(dmToUpdateDouble);
        when(auctionRepoJPA.save(dmToUpdateDouble)).thenReturn(dmUpdatedDouble);
        when(auctionAssembler.toDomain(dmUpdatedDouble)).thenReturn(auctionOutputDouble);

        // Act
        Auction result = repository.update(auctionInputDouble);

        // Assert
        assertEquals(auctionOutputDouble, result);
    }

    @Test
    void update_shouldThrowErrorWhenAuctionIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> repository.update(null));
    }

    @Test
    void update_shouldThrowErrorWhenAuctionDoesNotExist() {
        // Arrange
        when(auctionDouble.id()).thenReturn(auctionIdDouble);
        when(auctionRepoJPA.existsById(auctionIdDouble)).thenReturn(false);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repository.update(auctionDouble));
    }

    // =========================================================
    // ==================== findById ===========================
    // =========================================================

    @Test
    void findById_shouldReturnAuctionWhenFound() {
        // Arrange
        when(auctionRepoJPA.findById(auctionIdDouble)).thenReturn(Optional.of(auctionDMDouble));
        when(auctionAssembler.toDomain(auctionDMDouble)).thenReturn(auctionDouble);

        // Act
        Optional<Auction> result = repository.findById(auctionIdDouble);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(auctionDouble, result.get());
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        // Arrange
        when(auctionRepoJPA.findById(auctionIdDouble)).thenReturn(Optional.empty());

        // Act
        Optional<Auction> result = repository.findById(auctionIdDouble);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_shouldThrowErrorWhenIdIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> repository.findById(null));
    }

    // =========================================================
    // ================ findAll ================================
    // =========================================================

    @Test
    void findAll_shouldReturnListOfAuctions() {
        // Arrange
        List<AuctionDM> dmList = List.of(auctionDMDouble);

        when(auctionRepoJPA.findAll()).thenReturn(dmList);
        when(auctionAssembler.toDomain(auctionDMDouble)).thenReturn(auctionDouble);

        // Act
        List<Auction> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(auctionDouble, result.get(0));
    }

    @Test
    void findAll_shouldReturnEmptyListWhenNoData() {
        // Arrange
        when(auctionRepoJPA.findAll()).thenReturn(List.of());

        // Act
        List<Auction> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // =========================================================
    // ============== findByListingId ==========================
    // =========================================================

    @Test
    void findByListingId_shouldReturnAuctions() {
        // Arrange
        List<ListingId> listingIds = List.of(listingIdDouble);
        List<AuctionDM> dmList = List.of(auctionDMDouble);

        when(auctionRepoJPA.findByListingIdIn(listingIds)).thenReturn(dmList);
        when(auctionAssembler.toDomain(auctionDMDouble)).thenReturn(auctionDouble);

        // Act
        List<Auction> result = repository.findByListingIdIn(listingIds);

        // Assert
        assertEquals(1, result.size());
        assertEquals(auctionDouble, result.get(0));
    }


    @Test
    void findByListingId_shouldThrowWhenNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> repository.findByListingIdIn(null));
    }

    @Test
    void findByListingId_shouldReturnEmptyListWhenNoResults() {
        // Arrange
        List<ListingId> listingIds = List.of(listingIdDouble);

        when(auctionRepoJPA.findByListingIdIn(listingIds)).thenReturn(List.of());

        // Act
        List<Auction> result = repository.findByListingIdIn(listingIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByListingIdInOrderByAuctionDeadlineAsc_shouldReturnSortedAuctions() {

        // Arrange
        ListingId listingIdDouble = mock(ListingId.class);

        List<ListingId> listingIds = List.of(listingIdDouble);

        AuctionDM auctionDM1Double = mock(AuctionDM.class);
        AuctionDM auctionDM2Double = mock(AuctionDM.class);

        Auction auction1Double = mock(Auction.class);
        Auction auction2Double = mock(Auction.class);

        List<AuctionDM> auctionsDM = List.of(auctionDM1Double, auctionDM2Double);

        when(auctionRepoJPA.findByListingIdInOrderByAuctionEndDateTimeAsc(listingIds)).thenReturn(auctionsDM);

        when(auctionAssembler.toDomain(auctionDM1Double)).thenReturn(auction1Double);
        when(auctionAssembler.toDomain(auctionDM2Double)).thenReturn(auction2Double);

        // Act
        List<Auction> result = repository.findByListingIdInOrderByAuctionDeadlineAsc(listingIds);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(auction1Double, result.get(0));
        assertEquals(auction2Double, result.get(1));
    }
}