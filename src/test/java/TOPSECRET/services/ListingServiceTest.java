package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.ListingAuthorizationPolicy;
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
import org.springframework.security.access.AuthorizationServiceException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    private ListingAuthorizationPolicy policy;

    @InjectMocks
    private ListingService service;

    @BeforeEach
    void setUp() {
        listingRepository = mock(IListingRepository.class);
        listingFactory = mock(ListingFactory.class);
        userRepository = mock(IUserRepository.class);
        publicationRepository = mock(IPublicationSampleRepository.class);
        policy = mock(ListingAuthorizationPolicy.class);

        service = new ListingService(
                listingRepository,
                listingFactory,
                userRepository,
                publicationRepository,
                policy
        );
    }


    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void constructor_shouldThrowNullPointerException_whenRepositoryIsNull() {

        //Act + Assert
        assertThrows(NullPointerException.class,
                () -> new ListingService(null, listingFactory, userRepository, publicationRepository, policy));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {

        //Act + Assert
        assertThrows(NullPointerException.class,
                () -> new ListingService(listingRepository, null, userRepository, publicationRepository, policy));
    }


    // =========================================================================
    // createAndSave
    // =========================================================================

    @Test
    void createAndSave_shouldReturnSavedListing_whenUserCanCreateAndOwnsPublications() {

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

        Listing result = service.createAndSave(
                title,
                itemsId,
                saleType,
                price,
                description,
                sellerId,
                pictures
        );

        assertEquals(savedListing, result);
    }

    @Test
    void createAndSave_shouldThrowEntityNotFoundException_whenUserDoesNotExist() {

        String title = "Listing title";
        SaleType saleType = SaleType.DIRECT_SALE;
        Price price = mock(Price.class);
        String description = "Short description";
        UserId sellerId = mock(UserId.class);
        PublicationSampleId publicationSampleId = mock(PublicationSampleId.class);
        Picture picture = mock(Picture.class);

        when(userRepository.findByID(sellerId)).thenReturn(Optional.empty());

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
    void createAndSave_shouldThrowAuthorizationServiceException_whenPublicationIsNotOwnedByUser() {

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

        assertThrows(
                AuthorizationServiceException.class,
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
    //  findById()
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
    //  findAll()
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
    //  findAllDirectSale()
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
    //  findAllAuction()
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
    //  findIdsByPublicationSampleIdIn()
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
    //  findIdsByPublicationSampleIdIn()
    // =========================================================================

    @Test
    void findByAuctionAndPublicationSampleIdIn_shouldReturnAuctionListings() {
        List<PublicationSampleId> sampleIds = List.of(mock(PublicationSampleId.class));
        List<Listing> listings = List.of(mock(Listing.class), mock(Listing.class));

        when(listingRepository.findAllByAuctionAndPublicationSampleIdIn(sampleIds))
                .thenReturn(listings);

        List<Listing> result = service.findByAuctionAndPublicationSampleIdIn(sampleIds);

        assertSame(listings, result);
    }
    @Test
    void findByAuctionAndPublicationSampleIdIn_shouldReturnEmptyList_whenNoAuctionListingsMatch() {
        List<PublicationSampleId> sampleIds = List.of(mock(PublicationSampleId.class));

        when(listingRepository.findAllByAuctionAndPublicationSampleIdIn(sampleIds))
                .thenReturn(List.of());

        List<Listing> result = service.findByAuctionAndPublicationSampleIdIn(sampleIds);

        assertTrue(result.isEmpty());
    }

    // =========================================================================
    //  findAllByDirectSaleAndPublicationSampleIdIn()
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

}