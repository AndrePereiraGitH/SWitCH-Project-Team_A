package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.ListingAuthorizationPolicy;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Enum.ListingStatus;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Listing.ListingFactory;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ListingService}.
 *
 * <p>Verifies the service's behaviour in isolation by mocking both the
 * repository ({@link IListingRepository}) and the factory ({@link ListingFactory}).
 * Tests are grouped by method under test.</p>
 *
 * <p>Each test follows the Arrange / Act / Assert structure and exercises
 * a single logical scenario.</p>
 */
@ExtendWith(MockitoExtension.class)
class ListingServiceTest {

    @Mock
    private IListingRepository listingRepository;

    @Mock
    private ListingFactory listingFactory;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IPublicationSampleRepository publicationRepository;

    @Mock
    private IEditionRepository editionRepository;

    @Mock
    private IBibliographicalDataRepository bibliographicalDataRepository;

    @Mock
    private ListingAuthorizationPolicy policy;

    @InjectMocks
    private ListingService service;

    private ListingId listingId;
    private PublicationSampleId sampleId;

    @BeforeEach
    void setUp() {
        listingRepository = mock(IListingRepository.class);
        listingFactory = mock(ListingFactory.class);
        userRepository = mock(IUserRepository.class);
        publicationRepository = mock(IPublicationSampleRepository.class);
        editionRepository = mock(IEditionRepository.class);
        bibliographicalDataRepository = mock(IBibliographicalDataRepository.class);
        policy = mock(ListingAuthorizationPolicy.class);

        service = new ListingService(
                listingRepository,
                listingFactory,
                userRepository,
                publicationRepository,
                editionRepository,
                bibliographicalDataRepository,
                policy
        );

        listingId = mock(ListingId.class);
        sampleId = mock(PublicationSampleId.class);
    }

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void constructor_shouldThrowNullPointerException_whenRepositoryIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new ListingService(null, listingFactory, userRepository,
                        publicationRepository, editionRepository, bibliographicalDataRepository, policy));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new ListingService(listingRepository, null, userRepository,
                        publicationRepository, editionRepository, bibliographicalDataRepository, policy));
    }

    // =========================================================================
    // createAndSave
    // =========================================================================

    @Test
    void createAndSave_shouldReturnSavedListing_whenUserCanCreateAndOwnsPublications() {
        // Arrange
        String title = "Listing title";
        SaleType saleType = SaleType.DIRECT_SALE;
        Price price = mock(Price.class);
        String description = "Short description";
        UserId sellerId = mock(UserId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Picture picture = mock(Picture.class);

        User user = mock(User.class);
        PublicationSample publicationSample = mock(PublicationSample.class);
        Listing createdListing = mock(Listing.class);
        Listing savedListing = mock(Listing.class);

        List<PublicationSampleId> itemsId = List.of(publicationSampleId);
        List<Picture> pictures = List.of(picture);

        when(userRepository.findByID(sellerId)).thenReturn(Optional.of(user));
        when(policy.canCreateListing(user)).thenReturn(true);
        when(publicationRepository.findById(publicationSampleId))
                .thenReturn(Optional.of(publicationSample));
        when(publicationSample.getOwnerID()).thenReturn(sellerId);
        when(listingFactory.create(
                title,
                itemsId,
                saleType,
                price,
                description,
                sellerId,
                pictures
        )).thenReturn(createdListing);
        when(listingRepository.save(createdListing)).thenReturn(savedListing);

        // Act
        Listing result = service.createAndSave(
                title,
                itemsId,
                saleType,
                price,
                description,
                sellerId,
                pictures
        );

        // Assert
        assertEquals(savedListing, result);
    }

    @Test
    void createAndSave_shouldThrowEntityNotFoundException_whenUserDoesNotExist() {
        // Arrange
        String title = "Listing title";
        SaleType saleType = SaleType.DIRECT_SALE;
        Price price = mock(Price.class);
        String description = "Short description";
        UserId sellerId = mock(UserId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Picture picture = mock(Picture.class);

        when(userRepository.findByID(sellerId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                EntityNotFoundException.class,
                () -> service.createAndSave(
                        title,
                        List.of(publicationSampleId),
                        saleType,
                        price,
                        description,
                        sellerId,
                        List.of(picture)
                )
        );
    }

    @Test
    void createAndSave_shouldThrowAccessDeniedException_whenUserCannotCreateListing() {
        // Arrange
        String title = "Lord Of the Rings";
        List<PublicationSampleId> itemsId = List.of(mock(PublicationSampleId.class));
        SaleType saleType = mock(SaleType.class);
        Price price = mock(Price.class);
        String description = "description";
        UserId sellerId = mock(UserId.class);
        List<Picture> pictures = List.of(mock(Picture.class));

        User user = mock(User.class);

        when(userRepository.findByID(sellerId)).thenReturn(Optional.of(user));
        when(policy.canCreateListing(user)).thenReturn(false);

        // Act + Assert
        assertThrows(AccessDeniedException.class, () ->
                service.createAndSave(title, itemsId, saleType, price, description, sellerId, pictures)
        );
    }

    @Test
    void createAndSave_shouldThrowAccessDeniedException_whenPublicationIsNotOwnedByUser() {
        // Arrange
        String title = "Listing title";
        SaleType saleType = SaleType.DIRECT_SALE;
        Price price = mock(Price.class);
        String description = "Short description";
        UserId sellerId = mock(UserId.class);
        UserId otherUserId = mock(UserId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Picture picture = mock(Picture.class);

        User user = mock(User.class);
        PublicationSample publicationSample = mock(PublicationSample.class);

        when(userRepository.findByID(sellerId)).thenReturn(Optional.of(user));
        when(policy.canCreateListing(user)).thenReturn(true);
        when(publicationRepository.findById(publicationSampleId))
                .thenReturn(Optional.of(publicationSample));
        when(publicationSample.getOwnerID()).thenReturn(otherUserId);

        // Act + Assert
        assertThrows(
                AccessDeniedException.class,
                () -> service.createAndSave(
                        title,
                        List.of(publicationSampleId),
                        saleType,
                        price,
                        description,
                        sellerId,
                        List.of(picture)
                )
        );
    }

    @Test
    void createAndSave_shouldThrowEntityNotFoundException_whenPublicationDoesNotExist() {
        // Arrange
        String title = "Listing title";
        SaleType saleType = SaleType.DIRECT_SALE;
        Price price = mock(Price.class);
        String description = "Short description";
        UserId sellerId = mock(UserId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Picture picture = mock(Picture.class);

        User user = mock(User.class);

        when(userRepository.findByID(sellerId)).thenReturn(Optional.of(user));
        when(policy.canCreateListing(user)).thenReturn(true);
        when(publicationRepository.findById(publicationSampleId))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                EntityNotFoundException.class,
                () -> service.createAndSave(
                        title,
                        List.of(publicationSampleId),
                        saleType,
                        price,
                        description,
                        sellerId,
                        List.of(picture)
                )
        );
    }

    // =========================================================================
    // findById()
    // =========================================================================

    @Test
    void findById_shouldReturnListing_whenListingExists() {
        // Arrange
        ListingId listingId = mock(ListingId.class);
        Listing listing = mock(Listing.class);

        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));

        // Act
        Listing result = service.findById(listingId);

        // Assert
        assertSame(listing, result);
    }

    @Test
    void findById_shouldThrowEntityNotFoundException_whenListingDoesNotExist() {
        // Arrange
        ListingId listingId = mock(ListingId.class);

        when(listingRepository.findById(listingId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.findById(listingId)
        );
    }

    // =========================================================================
    // findAll()
    // =========================================================================

    @Test
    void findAll_shouldReturnAllListings() {
        // Arrange
        Listing listing1 = mock(Listing.class);
        Listing listing2 = mock(Listing.class);
        List<Listing> listings = List.of(listing1, listing2);

        when(listingRepository.findAll()).thenReturn(listings);

        // Act
        List<Listing> result = service.findAll();

        // Assert
        assertSame(listings, result);
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoListingsExist() {
        // Arrange
        when(listingRepository.findAll()).thenReturn(List.of());

        // Act
        List<Listing> result = service.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // findAllFiltered()
    // =========================================================================

    @Test
    void findAllFiltered_shouldReturnAllListings_whenNoFiltersAreProvided() {
        // Arrange
        Listing listing1 = mock(Listing.class);
        Listing listing2 = mock(Listing.class);

        when(listingRepository.findAll()).thenReturn(List.of(listing1, listing2));

        // Act
        List<Listing> result = service.findAllFiltered(null, null, null, null, null);

        // Assert
        assertEquals(List.of(listing1, listing2), result);
    }

    @Test
    void findAllFiltered_shouldFilterBySaleType() {
        // Arrange
        Listing directSale = mock(Listing.class);
        Listing auction = mock(Listing.class);

        when(directSale.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
        when(auction.getSaleType()).thenReturn(SaleType.AUCTION);
        when(listingRepository.findAll()).thenReturn(List.of(directSale, auction));

        // Act
        List<Listing> result =
                service.findAllFiltered(null, null, null, SaleType.DIRECT_SALE, null);

        // Assert
        assertEquals(List.of(directSale), result);
    }

    @Test
    void findAllFiltered_shouldFilterByPublisherId() {
        // Arrange
        PublisherID publisherId = mock(PublisherID.class);
        EditionId editionId = mock(EditionId.class);
        PublicationSampleId matchingPubId = mock(PublicationSampleId.class);
        PublicationSampleId nonMatchingPubId = mock(PublicationSampleId.class);

        Listing matchingListing = mock(Listing.class);
        Listing nonMatchingListing = mock(Listing.class);

        when(listingRepository.findAll()).thenReturn(List.of(matchingListing, nonMatchingListing));

        when(editionRepository.findIdsByPublisherId(publisherId))
                .thenReturn(List.of(editionId));

        when(publicationRepository.findIdsByEditionIdIn(List.of(editionId)))
                .thenReturn(List.of(matchingPubId));

        when(matchingListing.getItemsId()).thenReturn(List.of(matchingPubId));
        when(nonMatchingListing.getItemsId()).thenReturn(List.of(nonMatchingPubId));

        // Act
        List<Listing> result =
                service.findAllFiltered(null, null, publisherId, null, null);

        // Assert
        assertEquals(List.of(matchingListing), result);
    }

    @Test
    void findAllFiltered_shouldFilterByGenreId() {
        // Arrange
        GenreId genreId = mock(GenreId.class);
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        PublicationSampleId matchingPubId = mock(PublicationSampleId.class);
        PublicationSampleId nonMatchingPubId = mock(PublicationSampleId.class);

        Listing matchingListing = mock(Listing.class);
        Listing nonMatchingListing = mock(Listing.class);

        when(listingRepository.findAll()).thenReturn(List.of(matchingListing, nonMatchingListing));

        when(bibliographicalDataRepository.findAllByGenreId(genreId))
                .thenReturn(List.of(bdId));

        when(publicationRepository.findIdsByBibliographicalDataIdIn(List.of(bdId)))
                .thenReturn(List.of(matchingPubId));

        when(matchingListing.getItemsId()).thenReturn(List.of(matchingPubId));
        when(nonMatchingListing.getItemsId()).thenReturn(List.of(nonMatchingPubId));

        // Act
        List<Listing> result =
                service.findAllFiltered(genreId, null, null, null, null);

        // Assert
        assertEquals(List.of(matchingListing), result);
    }

    @Test
    void findAllFiltered_shouldFilterByAuthorId() {
        // Arrange
        AuthorId authorId = mock(AuthorId.class);
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        PublicationSampleId matchingPubId = mock(PublicationSampleId.class);
        PublicationSampleId nonMatchingPubId = mock(PublicationSampleId.class);

        Listing matchingListing = mock(Listing.class);
        Listing nonMatchingListing = mock(Listing.class);

        when(listingRepository.findAll()).thenReturn(List.of(matchingListing, nonMatchingListing));

        when(bibliographicalDataRepository.findIdByAuthorId(authorId))
                .thenReturn(List.of(bdId));

        when(publicationRepository.findIdsByBibliographicalDataIdIn(List.of(bdId)))
                .thenReturn(List.of(matchingPubId));

        when(matchingListing.getItemsId()).thenReturn(List.of(matchingPubId));
        when(nonMatchingListing.getItemsId()).thenReturn(List.of(nonMatchingPubId));

        // Act
        List<Listing> result =
                service.findAllFiltered(null, authorId, null, null, null);

        // Assert
        assertEquals(List.of(matchingListing), result);
    }

    @Test
    void findAllFiltered_shouldCombineSaleTypeAndPublisherId() {
        // Arrange
        PublisherID publisherId = mock(PublisherID.class);
        EditionId editionId = mock(EditionId.class);
        PublicationSampleId matchingPubId = mock(PublicationSampleId.class);

        Listing matchingDirectSale = mock(Listing.class);
        Listing matchingAuction = mock(Listing.class);

        when(matchingDirectSale.getSaleType()).thenReturn(SaleType.DIRECT_SALE);

        when(listingRepository.findAll())
                .thenReturn(List.of(matchingDirectSale, matchingAuction));

        when(editionRepository.findIdsByPublisherId(publisherId))
                .thenReturn(List.of(editionId));

        when(publicationRepository.findIdsByEditionIdIn(List.of(editionId)))
                .thenReturn(List.of(matchingPubId));

        when(matchingDirectSale.getItemsId()).thenReturn(List.of(matchingPubId));

        // Act
        List<Listing> result =
                service.findAllFiltered(null, null, publisherId, SaleType.DIRECT_SALE, null);

        // Assert
        assertEquals(List.of(matchingDirectSale), result);
    }

    // =========================================================================
    // findAllDirectSale()
    // =========================================================================

    @Test
    void findAllDirectSale_shouldReturnAllDirectSaleListings() {
        // Arrange
        Listing listing1 = mock(Listing.class);
        Listing listing2 = mock(Listing.class);
        List<Listing> listings = List.of(listing1, listing2);

        when(listingRepository.findAllByDirectSale()).thenReturn(listings);

        // Act
        List<Listing> result = service.findAllDirectSale();

        // Assert
        assertSame(listings, result);
    }

    @Test
    void findAllDirectSale_shouldReturnEmptyList_whenNoDirectSaleListingsExist() {
        // Arrange
        when(listingRepository.findAllByDirectSale()).thenReturn(List.of());

        // Act
        List<Listing> result = service.findAllDirectSale();

        // Assert
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // findAllAuction()
    // =========================================================================

    @Test
    void findAllAuction_shouldReturnAllAuctionListings() {
        // Arrange
        Listing listing1 = mock(Listing.class);
        Listing listing2 = mock(Listing.class);
        List<Listing> listings = List.of(listing1, listing2);

        when(listingRepository.findAllByAuction()).thenReturn(listings);

        // Act
        List<Listing> result = service.findAllAuction();

        // Assert
        assertSame(listings, result);
    }

    @Test
    void findAllAuction_shouldReturnEmptyList_whenNoAuctionListingsExist() {
        // Arrange
        when(listingRepository.findAllByAuction()).thenReturn(List.of());

        // Act
        List<Listing> result = service.findAllAuction();

        // Assert
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // findIdsByPublicationSampleIdIn()
    // =========================================================================

    @Test
    void findIdsByPublicationSampleIdIn_shouldReturnListingIds() {
        // Arrange
        PublicationSampleId sampleId1 = mock(PublicationSampleId.class);
        PublicationSampleId sampleId2 = mock(PublicationSampleId.class);
        List<PublicationSampleId> sampleIds = List.of(sampleId1, sampleId2);

        ListingId listingId1 = mock(ListingId.class);
        ListingId listingId2 = mock(ListingId.class);
        List<ListingId> listingIds = List.of(listingId1, listingId2);

        when(listingRepository.findIdsByPublicationSampleIdIn(sampleIds)).thenReturn(listingIds);

        // Act
        List<ListingId> result = service.findIdsByPublicationSampleIdIn(sampleIds);

        // Assert
        assertSame(listingIds, result);
    }

    @Test
    void findIdsByPublicationSampleIdIn_shouldReturnEmptyList_whenNoListingsMatch() {
        // Arrange
        List<PublicationSampleId> sampleIds = List.of(mock(PublicationSampleId.class));

        when(listingRepository.findIdsByPublicationSampleIdIn(sampleIds)).thenReturn(List.of());

        // Act
        List<ListingId> result = service.findIdsByPublicationSampleIdIn(sampleIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // findByPublicationSampleIdIn()
    // =========================================================================

    @Test
    void findByPublicationSampleIdIn_shouldReturnListings_whenMatchingPublicationSamplesExist() {
        // Arrange
        PublicationSampleId sampleId1 = mock(PublicationSampleId.class);
        PublicationSampleId sampleId2 = mock(PublicationSampleId.class);
        List<PublicationSampleId> sampleIds = List.of(sampleId1, sampleId2);

        Listing listing1 = mock(Listing.class);
        Listing listing2 = mock(Listing.class);
        List<Listing> listings = List.of(listing1, listing2);

        when(listingRepository.findByPublicationSampleIdIn(sampleIds)).thenReturn(listings);

        // Act
        List<Listing> result = service.findByPublicationSampleIdIn(sampleIds);

        // Assert
        assertSame(listings, result);
    }

    @Test
    void findByPublicationSampleIdIn_shouldReturnEmptyList_whenNoListingsMatch() {
        // Arrange
        List<PublicationSampleId> sampleIds = List.of(mock(PublicationSampleId.class));

        when(listingRepository.findByPublicationSampleIdIn(sampleIds)).thenReturn(List.of());

        // Act
        List<Listing> result = service.findByPublicationSampleIdIn(sampleIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByPublicationSampleIdIn_shouldReturnBothDirectSaleAndAuctionListings() {
        // Arrange
        PublicationSampleId sampleId = mock(PublicationSampleId.class);
        List<PublicationSampleId> sampleIds = List.of(sampleId);

        Listing directSaleListing = mock(Listing.class);
        Listing auctionListing = mock(Listing.class);
        List<Listing> listings = List.of(directSaleListing, auctionListing);

        when(listingRepository.findByPublicationSampleIdIn(sampleIds)).thenReturn(listings);

        // Act
        List<Listing> result = service.findByPublicationSampleIdIn(sampleIds);

        // Assert
        assertSame(listings, result);
        assertEquals(2, result.size());
    }

    @Test
    void findByPublicationSampleIdIn_shouldThrowIllegalArgumentException_whenListIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.findByPublicationSampleIdIn(null));
    }

    @Test
    void findByPublicationSampleIdIn_shouldThrowIllegalArgumentException_whenListIsEmpty() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.findByPublicationSampleIdIn(List.of()));
    }

    @Test
    void findByPublicationSampleIdIn_shouldReturnSingleListing_whenOnlyOneMatches() {
        // Arrange
        PublicationSampleId sampleId = mock(PublicationSampleId.class);
        List<PublicationSampleId> sampleIds = List.of(sampleId);

        Listing listing = mock(Listing.class);
        List<Listing> listings = List.of(listing);

        when(listingRepository.findByPublicationSampleIdIn(sampleIds)).thenReturn(listings);

        // Act
        List<Listing> result = service.findByPublicationSampleIdIn(sampleIds);

        // Assert
        assertSame(listings, result);
        assertEquals(1, result.size());
    }

    // =========================================================================
    // findByAuctionAndPublicationSampleIdIn
    // =========================================================================

    @Test
    void findByAuctionAndPublicationSampleIdIn_shouldReturnAuctionListings() {
        // Arrange
        List<PublicationSampleId> sampleIds = List.of(mock(PublicationSampleId.class));
        List<Listing> listings = List.of(mock(Listing.class), mock(Listing.class));

        when(listingRepository.findAllByAuctionAndPublicationSampleIdIn(sampleIds))
                .thenReturn(listings);

        // Act
        List<Listing> result = service.findByAuctionAndPublicationSampleIdIn(sampleIds);

        // Assert
        assertSame(listings, result);
    }

    @Test
    void findByAuctionAndPublicationSampleIdIn_shouldReturnEmptyList_whenNoAuctionListingsMatch() {
        // Arrange
        List<PublicationSampleId> sampleIds = List.of(mock(PublicationSampleId.class));

        when(listingRepository.findAllByAuctionAndPublicationSampleIdIn(sampleIds))
                .thenReturn(List.of());

        // Act
        List<Listing> result = service.findByAuctionAndPublicationSampleIdIn(sampleIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // findAllByDirectSaleAndPublicationSampleIdIn()
    // =========================================================================

    @Test
    void findAllDirectSaleAndPublicationSampleIdIn_shouldReturnDirectSaleListings() {
        // Arrange
        List<PublicationSampleId> sampleIds =
                List.of(mock(PublicationSampleId.class));

        List<Listing> listings =
                List.of(mock(Listing.class), mock(Listing.class));

        when(listingRepository.findAllByDirectSaleAndPublicationSampleIdIn(sampleIds))
                .thenReturn(listings);

        // Act
        List<Listing> result =
                service.findAllDirectSaleAndPublicationSampleIdIn(sampleIds);

        // Assert
        assertSame(listings, result);
    }

    @Test
    void findAllDirectSaleAndPublicationSampleIdIn_shouldReturnEmptyList_whenNoDirectSaleListingsMatch() {
        // Arrange
        List<PublicationSampleId> sampleIds =
                List.of(mock(PublicationSampleId.class));

        when(listingRepository.findAllByDirectSaleAndPublicationSampleIdIn(sampleIds))
                .thenReturn(List.of());

        // Act
        List<Listing> result =
                service.findAllDirectSaleAndPublicationSampleIdIn(sampleIds);

        // Assert
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // markListingAsSold
    // =========================================================================

    @Test
    void markListingAsSold_shouldReturnSavedListing_whenSuccessful() {
        // Arrange
        UserId buyerId = mock(UserId.class);
        UserId sellerId = mock(UserId.class);
        User buyer = mock(User.class);
        Listing listing = mock(Listing.class);
        PublicationSample item = mock(PublicationSample.class);
        Listing saved = mock(Listing.class);

        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(listing.getListingStatus()).thenReturn(ListingStatus.ACTIVE);
        when(listing.getSellerId()).thenReturn(sellerId);
        when(userRepository.findByID(buyerId)).thenReturn(Optional.of(buyer));
        when(policy.canBuyListing(buyer)).thenReturn(true);
        when(listing.getItemsId()).thenReturn(List.of(sampleId));
        when(publicationRepository.findById(sampleId)).thenReturn(Optional.of(item));
        when(listing.markListingAsSold(buyerId)).thenReturn(listing);
        when(listingRepository.save(listing)).thenReturn(saved);

        // Act
        Listing result = service.markListingAsSold(listingId, buyerId);

        // Assert
        assertEquals(saved, result);
    }

    @Test
    void markListingAsSold_shouldThrowEntityNotFoundException_whenListingNotFound() {
        // Arrange
        UserId buyerId = mock(UserId.class);

        when(listingRepository.findById(listingId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.markListingAsSold(listingId, buyerId));
    }

    @Test
    void markListingAsSold_shouldThrowIllegalStateException_whenListingAlreadySold() {
        // Arrange
        UserId buyerId = mock(UserId.class);
        Listing listing = mock(Listing.class);

        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(listing.getListingStatus()).thenReturn(ListingStatus.SOLD);

        // Act + Assert
        assertThrows(IllegalStateException.class, () ->
                service.markListingAsSold(listingId, buyerId));
    }

    @Test
    void markListingAsSold_shouldThrowAccessDeniedException_whenBuyerIsSeller() {
        // Arrange
        UserId buyerId = mock(UserId.class);
        Listing listing = mock(Listing.class);

        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(listing.getListingStatus()).thenReturn(ListingStatus.ACTIVE);
        when(listing.getSellerId()).thenReturn(buyerId);

        // Act + Assert
        assertThrows(AccessDeniedException.class, () ->
                service.markListingAsSold(listingId, buyerId));
    }

    @Test
    void markListingAsSold_shouldThrowEntityNotFoundException_whenBuyerNotFound() {
        // Arrange
        UserId buyerId = mock(UserId.class);
        UserId sellerId = mock(UserId.class);
        Listing listing = mock(Listing.class);

        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(listing.getListingStatus()).thenReturn(ListingStatus.ACTIVE);
        when(listing.getSellerId()).thenReturn(sellerId);
        when(userRepository.findByID(buyerId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.markListingAsSold(listingId, buyerId));
    }

    @Test
    void markListingAsSold_shouldThrowAccessDeniedException_whenPolicyDenied() {
        // Arrange
        UserId buyerId = mock(UserId.class);
        UserId sellerId = mock(UserId.class);
        User buyer = mock(User.class);
        Listing listing = mock(Listing.class);

        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(listing.getListingStatus()).thenReturn(ListingStatus.ACTIVE);
        when(listing.getSellerId()).thenReturn(sellerId);
        when(userRepository.findByID(buyerId)).thenReturn(Optional.of(buyer));
        when(policy.canBuyListing(buyer)).thenReturn(false);

        // Act + Assert
        assertThrows(AccessDeniedException.class, () ->
                service.markListingAsSold(listingId, buyerId));
    }

    @Test
    void markListingAsSold_shouldThrowEntityNotFoundException_whenItemNotFound() {
        // Arrange
        UserId buyerId = mock(UserId.class);
        UserId sellerId = mock(UserId.class);
        User buyer = mock(User.class);
        Listing listing = mock(Listing.class);

        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(listing.getListingStatus()).thenReturn(ListingStatus.ACTIVE);
        when(listing.getSellerId()).thenReturn(sellerId);
        when(userRepository.findByID(buyerId)).thenReturn(Optional.of(buyer));
        when(policy.canBuyListing(buyer)).thenReturn(true);
        when(listing.getItemsId()).thenReturn(List.of(sampleId));
        when(publicationRepository.findById(sampleId)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class, () ->
                service.markListingAsSold(listingId, buyerId));
    }

    @Test
    void markListingAsSold_shouldThrowNullPointerException_whenListingIdIsNull() {
        // Arrange
        UserId buyerId = mock(UserId.class);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.markListingAsSold(null, buyerId));
    }

    @Test
    void markListingAsSold_shouldThrowNullPointerException_whenBuyerIdIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.markListingAsSold(listingId, null));
    }
}