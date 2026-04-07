package TOPSECRET.controller;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.IAuthorRepository;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.Library.ILibraryRepository;
import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Listing.ListingFactory;
import TOPSECRET.domain.PublicationSample.IPublicationSampleRepository;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.PublicationSampleDTO_US015;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.awt.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test suite for the {@link US015ToPutPublicationInDirectSaleController}.
 *
 * <p>This class validates the behaviour of the controller responsible for
 * handling the US015 — placing a publication sample in direct sale.
 *
 * <p>The suite covers:</p>
 * <ul>
 *     <li>Constructor validation and defensive programming checks</li>
 *     <li>Retrieval and transformation of publication samples into DTOs</li>
 *     <li>Error handling when required domain objects or repositories are missing</li>
 *     <li>Filtering logic for available publication samples</li>
 *     <li>Delegation to the {@link ListingFactory} when creating a new listing</li>
 *     <li>Validation of input arguments for the direct sale operation</li>
 * </ul>
 */
class US015ToPutPublicationInDirectSaleControllerTest {


    // ==============================================================
    // ====================== Set Up ================================
    // ==============================================================

    private ILibraryRepository libraryRepoDouble;
    private IPublicationSampleRepository pubSampleRepoDouble;
    private IBibliographicalDataRepository bdRepoDouble;
    private IEditionRepository editionRepoDouble;
    private IAuthorRepository authorRepoDouble;
    private ListingFactory listingFactoryDouble;

    private UserId userId;
    private Library libraryDouble;
    private PublicationSample pubSample1Double;
    private PublicationSample pubSample2Double;
    private PublicationSampleID pubSampleId1;
    private PublicationSampleID pubSampleId2;
    private Author author1Double;
    private Author author2Double;
    private BibliographicalData bd1Double;
    private BibliographicalData bd2Double;
    private Price priceDouble;
    private List<Picture> picturesDouble;

    @BeforeEach
    void setup() throws InstanceNotFoundException {

        // Repos
        libraryRepoDouble = mock(ILibraryRepository.class);
        pubSampleRepoDouble = mock(IPublicationSampleRepository.class);
        bdRepoDouble = mock(IBibliographicalDataRepository.class);
        editionRepoDouble = mock(IEditionRepository.class);
        authorRepoDouble = mock(IAuthorRepository.class);

        // Factory
        listingFactoryDouble = mock(ListingFactory.class);

        // User
        userId = new UserId("userId1");

        // Library
        libraryDouble = mock(Library.class);
        LibraryId libraryId = new LibraryId("libId1");
        when(libraryDouble.id()).thenReturn(libraryId);
        when(libraryRepoDouble.getLibraryOfUser(userId)).thenReturn(libraryDouble);

        // Authors
        author1Double = mock(Author.class);
        AuthorId authorId1 = new AuthorId("authorId1");
        author2Double = mock(Author.class);
        AuthorId authorId2 = new AuthorId("authorId2");

        when(authorRepoDouble.findAuthorById(authorId1)).thenReturn(Optional.of(author1Double));
        when(authorRepoDouble.findAuthorById(authorId2)).thenReturn(Optional.of(author2Double));

        // Edition
        Edition edition1Double = mock(Edition.class);
        EditionID editionId1 = new EditionID("editionId1");
        Edition edition2Double = mock(Edition.class);
        EditionID editionId2 = new EditionID("editionId2");

        PublicationType pubType1Double = mock(PublicationType.class);
        PublicationType pubType2Double = mock(PublicationType.class);

        when(editionRepoDouble.findById(editionId1)).thenReturn(Optional.of(edition1Double));
        when(edition1Double.getCode()).thenReturn(Code.fromIsbn10("1234567890"));
        when(edition1Double.getPublicationType()).thenReturn(pubType1Double);
        when(pubType1Double.getMediaFormat()).thenReturn("BOOK");
        when(editionRepoDouble.findById(editionId2)).thenReturn(Optional.of(edition2Double));
        when(edition2Double.getPublicationType()).thenReturn(pubType2Double);
        when(pubType2Double.getMediaFormat()).thenReturn("BOOK");
        when(edition2Double.getCode()).thenReturn(Code.fromIsbn10("9876543210"));

        // Bibliographical data
        bd1Double = mock(BibliographicalData.class);
        BibliographicalDataID bdId1 = new BibliographicalDataID("bdId1");
        bd2Double = mock(BibliographicalData.class);
        BibliographicalDataID bdId2 = new BibliographicalDataID("bdId2");

        when(bd1Double.getTitle()).thenReturn("title1");
        when(bd2Double.getTitle()).thenReturn("title2");

        when(bd1Double.getAuthors()).thenReturn(List.of(authorId1));
        when(bd2Double.getAuthors()).thenReturn(List.of(authorId2));
        when(author1Double.getName()).thenReturn("author1");
        when(author2Double.getName()).thenReturn("author2");


        when(bdRepoDouble.findById(bdId1)).thenReturn(Optional.of(bd1Double));
        when(bdRepoDouble.findById(bdId2)).thenReturn(Optional.of(bd2Double));

        // Publication Samples
        pubSample1Double = mock(PublicationSample.class);
        pubSampleId1 = new PublicationSampleID("pubId1");
        pubSample2Double = mock(PublicationSample.class);
        pubSampleId2 = new PublicationSampleID("pubId2");

        when(pubSample1Double.isAvailableForSale()).thenReturn(true);
        when(pubSample2Double.isAvailableForSale()).thenReturn(true);

        when(pubSample1Double.getBibliographicalDataID()).thenReturn(bdId1);
        when(pubSample2Double.getBibliographicalDataID()).thenReturn(bdId2);

        when(pubSample1Double.getEditionID()).thenReturn(editionId1);
        when(pubSample2Double.getEditionID()).thenReturn(editionId2);

        when(pubSampleRepoDouble.findByLibraryId(libraryId)).thenReturn(List.of(pubSample1Double, pubSample2Double));

        // Price
        priceDouble = mock(Price.class);

        // Pictures
        Picture pictureDouble = mock(Picture.class);
        picturesDouble = List.of(pictureDouble);
    }

    // ==============================================================
    // ================ CONSTRUCTOR =================================
    // ==============================================================

    @Test
    void constructor_ShouldThrowExceptionWhenLibraryRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US015ToPutPublicationInDirectSaleController(null, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble)
        );
    }

    @Test
    void constructor_ShouldThrowExceptionWhenPubSampleRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, null, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble)
        );
    }

    @Test
    void constructor_ShouldThrowExceptionWhenBDRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, null,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble)
        );
    }

    @Test
    void constructor_ShouldThrowExceptionWhenEditionRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        null, authorRepoDouble, listingFactoryDouble)
        );
    }

    @Test
    void constructor_ShouldThrowExceptionWhenAuthorRepoIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, null, listingFactoryDouble)
        );
    }

    @Test
    void constructor_ShouldThrowExceptionWhenListingFactoryIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, null)
        );
    }

    @Test
    void constructor_ShouldCreateControllerWhenAllArgumentsAreValid() {
        // Arrange
        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);

        // Act + Assert
        assertNotNull(controller);
    }


    // ==============================================================
    // ======= findLibraryPublicationsAvailableForSale ==============
    // ==============================================================

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldReturnDTOs() throws InstanceNotFoundException {
        // Arrange
        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);

        // Act
        List<PublicationSampleDTO_US015> listDTOs = controller.findLibraryPublicationsAvailableForSale(userId);

        // Assert
        assertNotNull(listDTOs);
        assertEquals(2, listDTOs.size());
        assertEquals("title1", listDTOs.get(0).getTitle());
        assertEquals("title2", listDTOs.get(1).getTitle());
        assertEquals(List.of("author1"), listDTOs.get(0).getAuthorsName());
        assertEquals(List.of("author2"), listDTOs.get(1).getAuthorsName());
        assertEquals("1234567890", listDTOs.get(0).getCode());
        assertEquals("9876543210", listDTOs.get(1).getCode());
        assertEquals("BOOK", listDTOs.get(0).getMediaFormat());
        assertEquals("BOOK", listDTOs.get(1).getMediaFormat());
    }

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldThrowExceptionWhenLibraryNotFound() throws InstanceNotFoundException {
        // Arrange
        when(libraryRepoDouble.getLibraryOfUser(userId)).thenReturn(null);

        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(
                        libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> controller.findLibraryPublicationsAvailableForSale(userId));
    }

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldReturnEmptyListWhenNoSamplesAvailable() throws Exception {
        // Arrange
        when(pubSampleRepoDouble.findByLibraryId(libraryDouble.id())).thenReturn(List.of());

        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);

        // Act
        List<PublicationSampleDTO_US015> result =
                controller.findLibraryPublicationsAvailableForSale(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldIgnoreUnavailableSamples() throws Exception {
        // Arrange
        when(pubSample1Double.isAvailableForSale()).thenReturn(false);
        when(pubSample2Double.isAvailableForSale()).thenReturn(true);

        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);

        // Act
        List<PublicationSampleDTO_US015> result =
                controller.findLibraryPublicationsAvailableForSale(userId);

        // Assert
        assertEquals(1, result.size());
        assertEquals("title2", result.get(0).getTitle());
    }

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldThrowWhenBibliographicalDataNotFound() {
        // Arrange
        when(bdRepoDouble.findById(any())).thenReturn(Optional.empty());

        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> controller.findLibraryPublicationsAvailableForSale(userId));
    }

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldThrowWhenEditionNotFound() {
        // Arrange
        when(editionRepoDouble.findById(any())).thenReturn(Optional.empty());

        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);

        // Act + Assert
        assertThrows(InstanceNotFoundException.class,
                () -> controller.findLibraryPublicationsAvailableForSale(userId));
    }

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldThrowWhenAuthorNotFound() {
        // Arrange
        when(authorRepoDouble.findAuthorById(any())).thenReturn(Optional.empty());

        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);

        // Act + Assert
        assertThrows(RuntimeException.class,
                () -> controller.findLibraryPublicationsAvailableForSale(userId));
    }

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldReturnSingleDTOWhenOneSampleAvailable() throws Exception {
        // Arrange
        when(pubSampleRepoDouble.findByLibraryId(libraryDouble.id())).thenReturn(List.of(pubSample1Double));

        when(bd1Double.getTitle()).thenReturn("title1");
        when(author1Double.getName()).thenReturn("author1");

        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);

        // Act
        List<PublicationSampleDTO_US015> result =
                controller.findLibraryPublicationsAvailableForSale(userId);

        // Assert
        assertEquals(1, result.size());
        assertEquals("title1", result.get(0).getTitle());
    }


    // ==============================================================
    // ================= putPublicationForSale ======================
    // ==============================================================

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenTitleIsInvalid() {
        // Arrange
        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.putPublicationForSale("   ", List.of(pubSampleId1), priceDouble, "desc",
                        userId, picturesDouble));
    }

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenPublicationSampleIdsAreNull() {
        // Arrange
        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.putPublicationForSale("Valid Title", null, priceDouble, "desc",
                        userId, picturesDouble));
    }

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenPriceIsNull() {
        // Arrange
        PublicationSample pubSampleDouble = mock(PublicationSample.class);
        Picture pictureDouble = mock(Picture.class);

        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.putPublicationForSale("Valid Title", List.of(pubSampleId1), null, "desc",
                        userId, picturesDouble));
    }

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenShortDescriptionIsNull() {
        // Arrange
        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.putPublicationForSale("Valid Title", List.of(pubSampleId1), priceDouble, null,
                        userId, picturesDouble));
    }

    @Test
    void putPublicationForSale_ShouldThrowExceptionWhenPicturesIsNull() {
        // Arrange
        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                controller.putPublicationForSale("Valid Title", List.of(pubSampleId1), priceDouble,
                        "short description", userId, null));
    }

//    @Test
//    void putPublicationForSale_ShouldCallListingFactory() {
//        // Arrange
//        US015ToPutPublicationInDirectSaleController controller =
//                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
//                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);
//
//        Listing expectedListing = mock(Listing.class);
//
//        when(listingFactoryDouble.create("title", List.of(pubSampleId1), SaleType.DIRECT_SALE,
//                priceDouble, "short description", userId, picturesDouble)).thenReturn(expectedListing);
//
//        // Act
//        Listing listing = controller.putPublicationForSale("title", List.of(pubSampleId1),
//                priceDouble, "short description", userId, picturesDouble);
//
//        // Assert
//        assertNotNull(listing);
//        assertSame(expectedListing, listing);
//    }

    @Test
    void putPublicationForSale_ShouldReturnListingCreatedByFactory() {
        // Arrange
        US015ToPutPublicationInDirectSaleController controller =
                new US015ToPutPublicationInDirectSaleController(libraryRepoDouble, pubSampleRepoDouble, bdRepoDouble,
                        editionRepoDouble, authorRepoDouble, listingFactoryDouble);

        Listing expectedListing = mock(Listing.class);

        when(listingFactoryDouble.create("title", List.of(pubSampleId1), SaleType.DIRECT_SALE, priceDouble,
                "short description", userId, picturesDouble)).thenReturn(expectedListing);

        // Act
        Listing result = controller.putPublicationForSale("title", List.of(pubSampleId1), priceDouble,
                "short description", userId, picturesDouble);

        // Assert
        assertNotNull(result);
        assertSame(expectedListing, result);
    }
}