package TOPSECRET.controller;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.US016PublicationSampleDTO;
import TOPSECRET.dto.US016PublicationSampleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalePublicationInAuctionControllerTest {

    private ILibraryRepository libraryRepo;
    private IListingRepository listingRepository;
    private IAuctionRepository auctionRepository;
    private IPublicationSampleRepository publicationSampleRepository;

    private Library library;
    private Listing listing;
    private Auction auction;

    private SalePublicationInAuctionController controller;

    private UserId userId;
    private LibraryId libraryId;
    private PublicationSample publicationSample;
    private PublicationSampleId publicationSampleId;
    private Price initialPrice;
    private Price buyOutPrice;
    private List<Picture> pictures;
    private List<PublicationSampleId> selectedPublications;
    private US016PublicationSampleMapper mapper;

    @BeforeEach
    void setUp() {
        // arrange
        libraryRepo = mock(ILibraryRepository.class);
        listingRepository = mock(IListingRepository.class);
        auctionRepository = mock(IAuctionRepository.class);
        publicationSampleRepository = mock(IPublicationSampleRepository.class);

        userId = mock(UserId.class);
        library = mock(Library.class);
        libraryId = mock(LibraryId.class);
        listing = mock(Listing.class);
        auction = mock(Auction.class);
        publicationSample = mock(PublicationSample.class);
        publicationSampleId = mock(PublicationSampleId.class);
        initialPrice = mock(Price.class);
        buyOutPrice = mock(Price.class);
        pictures = List.of(mock(Picture.class));
        selectedPublications = List.of(publicationSampleId);
        mapper = mock(US016PublicationSampleMapper.class);

        controller = new SalePublicationInAuctionController(
                libraryRepo,
                listingRepository,
                auctionRepository,
                publicationSampleRepository,
                mapper
        );
    }

    // ======================================
    // ========= CONSTRUCTOR ================
    // ======================================

    @Test
    void testConstructor() {

        // act
        SalePublicationInAuctionController localController =
                new SalePublicationInAuctionController(
                        libraryRepo,
                        listingRepository,
                        auctionRepository,
                        publicationSampleRepository,
                        mapper
                );

        // assert
        assertNotNull(localController);
    }

    // ===============================================================
    // ========= GET ALL PUBLICATIONS IN USER LIBRARY ================
    // ===============================================================

    @Test
    void getAllPublicationsInUserLibrary_shouldThrow_whenUserIDisNULL() {

        // act + assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.getAllPublicationsInUserLibrary(null)
        );

        assertEquals("UserId cannot be null!", ex.getMessage());
    }

    @Test
    void getAllPublicationsInUserLibrary_shouldThrow_whenLibraryDoesNotExist() throws InstanceNotFoundException {

        // arrange
        when(libraryRepo.getLibraryByUserId(userId)).thenReturn(null);

        // act + assert
        InstanceNotFoundException ex = assertThrows(
                InstanceNotFoundException.class,
                () -> controller.getAllPublicationsInUserLibrary(userId)
        );

        assertEquals("There is no library for the userID: " + userId, ex.getMessage());
    }

    @Test
    void getAllPublicationsInUserLibrary_shouldReturnEmptyList_whenLibraryHasNoPublications() throws Exception {

        // arrange
        when(libraryRepo.getLibraryByUserId(userId)).thenReturn(library);
        when(library.id()).thenReturn(libraryId);
        when(publicationSampleRepository.getPublicationsByLibraryID(libraryId)).thenReturn(List.of());
        when(mapper.fromDomainList(List.of())).thenReturn(List.of());

        // act
        List<US016PublicationSampleDTO> result = controller.getAllPublicationsInUserLibrary(userId);

        // assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPublicationsInUserLibrary_shouldReturnDtoList_whenEverythingIsValid() throws Exception {

        // arrange
        US016PublicationSampleDTO dto = mock(US016PublicationSampleDTO.class);

        when(libraryRepo.getLibraryByUserId(userId)).thenReturn(library);
        when(library.id()).thenReturn(libraryId);
        when(publicationSampleRepository.getPublicationsByLibraryID(libraryId))
                .thenReturn(List.of(publicationSample));
        when(mapper.fromDomainList(List.of(publicationSample))).thenReturn(List.of(dto));

        // act
        List<US016PublicationSampleDTO> result = controller.getAllPublicationsInUserLibrary(userId);

        // assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    // ===============================================================
    // ================ CREATE AUCTION LISTING =======================
    // ===============================================================

    @Test
    void createAuctionListing_shouldReturnTrue_whenAuctionListingIsCreatedSuccessfully() throws Exception {

        // arrange
        ListingId listingId = mock(ListingId.class);
        LocalDateTime initialDate = LocalDateTime.of(2026, 4, 8, 10, 0);
        LocalDateTime finalDate = LocalDateTime.of(2026, 4, 10, 10, 0);

        when(listingRepository.createAndAdd(
                "Auction Title",
                selectedPublications,
                SaleType.AUCTION,
                initialPrice,
                "Short description",
                userId,
                pictures
        )).thenReturn(listing);

        when(listing.id()).thenReturn(listingId);

        when(auctionRepository.createAndAdd(
                listingId,
                initialDate,
                finalDate,
                initialPrice,
                buyOutPrice
        )).thenReturn(auction);

        // act
        boolean result = controller.createAuctionListing(
                "Auction Title",
                selectedPublications,
                initialPrice,
                "Short description",
                userId,
                pictures,
                initialDate,
                finalDate,
                buyOutPrice
        );

        // assert
        assertTrue(result);
    }

    @Test
    void createAuctionListing_shouldThrow_whenListingAlreadyExists() throws Exception {

        // arrange
        LocalDateTime initialDate = LocalDateTime.of(2026, 4, 8, 10, 0);
        LocalDateTime finalDate = LocalDateTime.of(2026, 4, 10, 10, 0);

        when(listingRepository.createAndAdd(
                anyString(),
                anyList(),
                eq(SaleType.AUCTION),
                eq(initialPrice),
                anyString(),
                eq(userId),
                eq(pictures)
        )).thenThrow(new InstanceAlreadyExistsException("Listing already exists"));

        // act + assert
        InstanceAlreadyExistsException ex = assertThrows(
                InstanceAlreadyExistsException.class,
                () -> controller.createAuctionListing(
                        "Auction Title",
                        selectedPublications,
                        initialPrice,
                        "Short description",
                        userId,
                        pictures,
                        initialDate,
                        finalDate,
                        buyOutPrice
                )
        );

        assertEquals("Listing already exists", ex.getMessage());
    }

    @Test
    void createAuctionListing_shouldThrow_whenAuctionAlreadyExists() throws Exception {

        // arrange
        ListingId listingId = mock(ListingId.class);
        LocalDateTime initialDate = LocalDateTime.of(2026, 4, 8, 10, 0);
        LocalDateTime finalDate = LocalDateTime.of(2026, 4, 10, 10, 0);

        when(listingRepository.createAndAdd(
                anyString(),
                anyList(),
                eq(SaleType.AUCTION),
                eq(initialPrice),
                anyString(),
                eq(userId),
                eq(pictures)
        )).thenReturn(listing);

        when(listing.id()).thenReturn(listingId);

        when(auctionRepository.createAndAdd(
                eq(listingId),
                eq(initialDate),
                eq(finalDate),
                eq(initialPrice),
                eq(buyOutPrice)
        )).thenThrow(new InstanceAlreadyExistsException("Auction already exists"));

        // act + assert
        InstanceAlreadyExistsException ex = assertThrows(
                InstanceAlreadyExistsException.class,
                () -> controller.createAuctionListing(
                        "Auction Title",
                        selectedPublications,
                        initialPrice,
                        "Short description",
                        userId,
                        pictures,
                        initialDate,
                        finalDate,
                        buyOutPrice
                )
        );

        assertEquals("Auction already exists", ex.getMessage());
    }

    @Test
    void createAuctionListing_shouldThrow_whenListingTitleIsNull() {

        // act + assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.createAuctionListing(
                        null,
                        selectedPublications,
                        initialPrice,
                        "Short description",
                        userId,
                        pictures,
                        LocalDateTime.of(2026, 4, 8, 10, 0),
                        LocalDateTime.of(2026, 4, 10, 10, 0),
                        buyOutPrice
                )
        );

        assertEquals("All fields must be non-null.", ex.getMessage());
    }

    @Test
    void createAuctionListing_shouldThrow_whenSelectedPublicationsIDIsNull() {

        // act + assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.createAuctionListing(
                        "Auction Title",
                        null,
                        initialPrice,
                        "Short description",
                        userId,
                        pictures,
                        LocalDateTime.of(2026, 4, 8, 10, 0),
                        LocalDateTime.of(2026, 4, 10, 10, 0),
                        buyOutPrice
                )
        );

        assertEquals("All fields must be non-null.", ex.getMessage());
    }

    @Test
    void createAuctionListing_shouldThrow_whenInitialPriceIsNull() {

        // act + assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.createAuctionListing(
                        "Auction Title",
                        selectedPublications,
                        null,
                        "Short description",
                        userId,
                        pictures,
                        LocalDateTime.of(2026, 4, 8, 10, 0),
                        LocalDateTime.of(2026, 4, 10, 10, 0),
                        buyOutPrice
                )
        );

        assertEquals("All fields must be non-null.", ex.getMessage());
    }

    @Test
    void createAuctionListing_shouldThrow_whenShortDescriptionIsNull() {

        // act + assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.createAuctionListing(
                        "Auction Title",
                        selectedPublications,
                        initialPrice,
                        null,
                        userId,
                        pictures,
                        LocalDateTime.of(2026, 4, 8, 10, 0),
                        LocalDateTime.of(2026, 4, 10, 10, 0),
                        buyOutPrice
                )
        );

        assertEquals("All fields must be non-null.", ex.getMessage());
    }

    @Test
    void createAuctionListing_shouldThrow_whenSellerIdIsNull() {

        // act + assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.createAuctionListing(
                        "Auction Title",
                        selectedPublications,
                        initialPrice,
                        "Short description",
                        null,
                        pictures,
                        LocalDateTime.of(2026, 4, 8, 10, 0),
                        LocalDateTime.of(2026, 4, 10, 10, 0),
                        buyOutPrice
                )
        );

        assertEquals("All fields must be non-null.", ex.getMessage());
    }

    @Test
    void createAuctionListing_shouldThrow_whenPicturesIsNull() {

        // act + assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.createAuctionListing(
                        "Auction Title",
                        selectedPublications,
                        initialPrice,
                        "Short description",
                        userId,
                        null,
                        LocalDateTime.of(2026, 4, 8, 10, 0),
                        LocalDateTime.of(2026, 4, 10, 10, 0),
                        buyOutPrice
                )
        );

        assertEquals("All fields must be non-null.", ex.getMessage());
    }

    @Test
    void createAuctionListing_shouldThrow_whenInitialDateIsNull() {

        // act + assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.createAuctionListing(
                        "Auction Title",
                        selectedPublications,
                        initialPrice,
                        "Short description",
                        userId,
                        pictures,
                        null,
                        LocalDateTime.of(2026, 4, 10, 10, 0),
                        buyOutPrice
                )
        );

        assertEquals("All fields must be non-null.", ex.getMessage());
    }

    @Test
    void createAuctionListing_shouldThrow_whenFinalDateIsNull() {

        // act + assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.createAuctionListing(
                        "Auction Title",
                        selectedPublications,
                        initialPrice,
                        "Short description",
                        userId,
                        pictures,
                        LocalDateTime.of(2026, 4, 8, 10, 0),
                        null,
                        buyOutPrice
                )
        );

        assertEquals("All fields must be non-null.", ex.getMessage());
    }

    @Test
    void createAuctionListing_shouldThrow_whenBuyOutPriceIsNull() {

        // act + assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> controller.createAuctionListing(
                        "Auction Title",
                        selectedPublications,
                        initialPrice,
                        "Short description",
                        userId,
                        pictures,
                        LocalDateTime.of(2026, 4, 8, 10, 0),
                        LocalDateTime.of(2026, 4, 10, 10, 0),
                        null
                )
        );

        assertEquals("All fields must be non-null.", ex.getMessage());
    }
}