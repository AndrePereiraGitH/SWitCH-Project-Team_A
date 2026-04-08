package TOPSECRET.controller;

import TOPSECRET.domain.Auction.Auction;
import TOPSECRET.domain.Auction.IAuctionRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Listing.IListingRepository;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.US016_PublicationSampleDTO;
import TOPSECRET.dto.US016_PublicationSampleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class US016ToPutPublicationInAuctionControllerTest {

    private ILibraryRepository libraryRepo;
    private IListingRepository listingRepository;
    private IAuctionRepository auctionRepository;
    private IPublicationSampleRepository publicationSampleRepository;
    private IBibliographicalDataRepository bdRepository;
    private Library library;
    private Listing listing;
    private Auction auction;

    private US016ToPutPublicationInAuctionController controller;

    private UserId userId;
    private LibraryId libraryId;
    private PublicationSample publicationSample;
    private BibliographicalData bibliographicalData;
    private BibliographicalDataID bibliographicalDataId;
    private PublicationSampleId publicationSampleId;
    private Price initialPrice;
    private Price buyOutPrice;
    private List<Picture> pictures;
    private List<PublicationSampleId> selectedPublications;
    private US016_PublicationSampleMapper mapper;

    @BeforeEach
    void setUp() {
        libraryRepo = mock(ILibraryRepository.class);
        listingRepository = mock(IListingRepository.class);
        auctionRepository = mock(IAuctionRepository.class);
        publicationSampleRepository = mock(IPublicationSampleRepository.class);
        bdRepository = mock(IBibliographicalDataRepository.class);


        userId = mock(UserId.class);
        library = mock(Library.class);
        libraryId = mock(LibraryId.class);
        listing = mock(Listing.class);
        publicationSample = mock(PublicationSample.class);
        bibliographicalData = mock(BibliographicalData.class);
        publicationSampleId = mock(PublicationSampleId.class);
        initialPrice = mock(Price.class);
        buyOutPrice = mock(Price.class);
        pictures = List.of(mock(Picture.class));
        selectedPublications = List.of(publicationSampleId);
        mapper = mock(US016_PublicationSampleMapper.class);
    }

    // ======================================
    // ========= CONSTRUCTOR ================
    // ======================================
    @Test
    void testConstructor() {
        controller = new US016ToPutPublicationInAuctionController(libraryRepo, listingRepository, auctionRepository, publicationSampleRepository, bdRepository, mapper);
        // assert
        assertNotNull(controller);
    }

    // ===============================================================
    // ========= GET ALL PUBLICATIONS IN USER LIBRARY ================
    // ===============================================================

    @Test
    void getAllPublicationsInUserLibrary_shouldThrow_whenUserIDisNULL() {

        controller = new US016ToPutPublicationInAuctionController(libraryRepo, listingRepository, auctionRepository, publicationSampleRepository, bdRepository, mapper);

        // act + assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> controller.getAllPublicationsInUserLibrary(null));
        assertEquals("UserId cannot be null!", ex.getMessage());
    }

    @Test
    void getAllPublicationsInUserLibrary_shouldThrow_whenLibraryDoesNotExist() throws InstanceNotFoundException {
        when(libraryRepo.getLibraryByUserId(userId)).thenReturn(null);

        controller = new US016ToPutPublicationInAuctionController(libraryRepo, listingRepository, auctionRepository, publicationSampleRepository, bdRepository, mapper);

        InstanceNotFoundException ex = assertThrows(InstanceNotFoundException.class,
                () -> controller.getAllPublicationsInUserLibrary(userId));

        assertEquals("There is no library for the userID: " + userId, ex.getMessage());
    }


    @Test
    void getAllPublicationsInUserLibrary_shouldReturnEmptyList_whenLibraryHasNoPublications() throws Exception {
        when(libraryRepo.getLibraryByUserId(userId)).thenReturn(library);
        when(library.id()).thenReturn(libraryId);
        when(publicationSampleRepository.getPublicationsByLibraryID(libraryId))
                .thenReturn(List.of());

        controller = new US016ToPutPublicationInAuctionController(libraryRepo, listingRepository, auctionRepository, publicationSampleRepository, bdRepository, mapper);

        List<US016_PublicationSampleDTO> result = controller.getAllPublicationsInUserLibrary(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(bdRepository);
    }

    @Test
    void getAllPublicationsInUserLibrary_shouldReturnDtoList_whenEverythingIsValid() throws Exception {
        US016_PublicationSampleDTO dto = mock(US016_PublicationSampleDTO.class);

        when(libraryRepo.getLibraryByUserId(userId)).thenReturn(library);
        when(library.id()).thenReturn(libraryId);
        when(publicationSampleRepository.getPublicationsByLibraryID(libraryId))
                .thenReturn(List.of(publicationSample));
        when(publicationSample.getBibliographicalDataID()).thenReturn(bibliographicalDataId);
        when(bdRepository.findBibliographicalDataById(bibliographicalDataId))
                .thenReturn(Optional.of(bibliographicalData));

        controller = new US016ToPutPublicationInAuctionController(libraryRepo, listingRepository, auctionRepository, publicationSampleRepository, bdRepository, mapper);

        try (MockedStatic<US016_PublicationSampleMapper> mapperDouble =
                     mockStatic(US016_PublicationSampleMapper.class)) {

            mapperDouble.when(() -> mapper.fromDomainList(
                    anyList()
            )).thenReturn(List.of(dto));

            List<US016_PublicationSampleDTO> result = controller.getAllPublicationsInUserLibrary(userId);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(dto, result.get(0));
        }
    }

    // ===============================================================
    // ================ CREATE AUCTION LISTING =======================
    // ===============================================================

    @Test
    void createAuctionListing_shouldReturnTrue_whenAuctionListingIsCreatedSuccessfully() throws Exception {
        ListingId listingId = mock(ListingId.class);
        LocalDateTime initialDate = LocalDateTime.of(2026, 4, 8, 10, 0);
        LocalDateTime finalDate = LocalDateTime.of(2026, 4, 10, 10, 0);

        controller = new US016ToPutPublicationInAuctionController(libraryRepo, listingRepository, auctionRepository, publicationSampleRepository, bdRepository, mapper);

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

        assertTrue(result);
    }

    @Test
    void createAuctionListing_shouldThrow_whenListingAlreadyExists() throws Exception {
        LocalDateTime initialDate = LocalDateTime.of(2026, 4, 8, 10, 0);
        LocalDateTime finalDate = LocalDateTime.of(2026, 4, 10, 10, 0);

        controller = new US016ToPutPublicationInAuctionController(libraryRepo, listingRepository, auctionRepository, publicationSampleRepository, bdRepository, mapper);

        when(listingRepository.createAndAdd(
                anyString(),
                anyList(),
                eq(SaleType.AUCTION),
                eq(initialPrice),
                anyString(),
                eq(userId),
                eq(pictures)
        )).thenThrow(new InstanceAlreadyExistsException("Listing already exists"));

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
        ListingId listingId = mock(ListingId.class);
        LocalDateTime initialDate = LocalDateTime.of(2026, 4, 8, 10, 0);
        LocalDateTime finalDate = LocalDateTime.of(2026, 4, 10, 10, 0);

        controller = new US016ToPutPublicationInAuctionController(libraryRepo, listingRepository, auctionRepository, publicationSampleRepository, bdRepository, mapper);

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


}