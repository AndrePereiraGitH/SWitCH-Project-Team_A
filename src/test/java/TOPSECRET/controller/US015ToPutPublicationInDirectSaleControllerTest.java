//package TOPSECRET.controller;
//
//import TOPSECRET.domain.*;
//import TOPSECRET.domain.Author.Author;
//import TOPSECRET.domain.BibliographicalData.BibliographicalData;
//import TOPSECRET.domain.Edition.Edition;
//import TOPSECRET.domain.Enum.PublicationStatus;
//import TOPSECRET.domain.Enum.SaleType;
//import TOPSECRET.domain.Genre.Genre;
//import TOPSECRET.domain.Library.Library;
//import TOPSECRET.domain.PublicationSample.PublicationSample;
//import TOPSECRET.domain.PublicationType.PublicationType;
//import TOPSECRET.domain.ValueObjects.*;
//import TOPSECRET.dto.PublicationSampleDTO_US015;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.Locale;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class US015ToPutPublicationInDirectSaleControllerTest {
//
//
//    // ==============================================================
//    // ====================== Set Up ================================
//    // ==============================================================
//
//    private Library libraryDouble;
//    private ListingFactory listingFactoryDouble;
//
//    @BeforeEach
//    void setup() {
//
//        PublicationSample pubSample1Double = mock(PublicationSample.class);
//        PublicationSample pubSample2Double = mock(PublicationSample.class);
//        BibliographicalData bd1Double = mock(BibliographicalData.class);
//        BibliographicalData bd2Double = mock(BibliographicalData.class);
//        Author authorDouble = mock(Author.class);
//        Genre genre1Double = mock(Genre.class);
//        Genre genre2Double = mock(Genre.class);
//        User ownerDouble = mock(User.class);
//        PublicationStatus pubStatusDouble = mock(PublicationStatus.class);
//        Edition edition1Double = mock(Edition.class);
//        Edition edition2Double = mock(Edition.class);
//        PublicationType pubTypeDouble = mock(PublicationType.class);
//        String mediaFormatDouble = "BOOK";
//        Locale language1Double = mock(Locale.class);
//        Locale language2Double = mock(Locale.class);
//        Publisher publisherDouble = mock(Publisher.class);
//
//        PublicationSample pubSampleDouble = mock(PublicationSample.class);
//        Price priceDouble = mock(Price.class);
//        Picture pictureDouble = mock(Picture.class);
//
//        // Title
//        when(pubSample1Double.getBibliographicalData()).thenReturn(bd1Double);
//        when(bd1Double.getTitle()).thenReturn("title1");
//        when(pubSample2Double.getBibliographicalData()).thenReturn(bd2Double);
//        when(bd2Double.getTitle()).thenReturn("title2");
//
//        // Author Name
//        when(bd1Double.getAuthors()).thenReturn(List.of(authorDouble));
//        when(bd2Double.getAuthors()).thenReturn(List.of(authorDouble));
//        when(authorDouble.getName()).thenReturn("author name");
//
//        // Genre
//        when(bd1Double.getGenres()).thenReturn(List.of(genre1Double));
//        when(bd2Double.getGenres()).thenReturn(List.of(genre2Double));
//        when(genre1Double.getGenreType()).thenReturn("fiction");
//        when(genre2Double.getGenreType()).thenReturn("fantasy");
//
//        // Owner
//        when(pubSample1Double.getOwnerId()).thenReturn(ownerDouble);
//        when(pubSample2Double.getOwnerId()).thenReturn(ownerDouble);
//        when(ownerDouble.getUserName()).thenReturn("owner username");
//
//        // Publication Status
//        when(pubSample1Double.getPublicationStatus()).thenReturn(pubStatusDouble);
//        when(pubSample2Double.getPublicationStatus()).thenReturn(pubStatusDouble);
//        when(pubStatusDouble.toString()).thenReturn("READ");
//        when(pubStatusDouble.toString()).thenReturn("READ");
//
//        // Edition Number
//        when(pubSample1Double.getEdition()).thenReturn(edition1Double);
//        when(pubSample2Double.getEdition()).thenReturn(edition2Double);
//        when(edition1Double.getEditionNumber()).thenReturn("1st");
//        when(edition2Double.getEditionNumber()).thenReturn("2nd");
//
//        // Code
//        when(edition1Double.getCode()).thenReturn(Code.fromIsbn10("1234567890"));
//        when(edition2Double.getCode()).thenReturn(Code.fromIsbn10("9876543210"));
//
//        // Media Format
//        when(edition1Double.getPublicationType()).thenReturn(pubTypeDouble);
//        when(edition2Double.getPublicationType()).thenReturn(pubTypeDouble);
//        when(pubTypeDouble.getMediaFormat()).thenReturn(mediaFormatDouble);
//
//        // Edition Language
//        when(edition1Double.getEditionLanguage()).thenReturn(language1Double);
//        when(edition2Double.getEditionLanguage()).thenReturn(language2Double);
//        when(language1Double.toString()).thenReturn("ENGLISH");
//        when(language2Double.toString()).thenReturn("FRENCH");
//
//        // Publisher
//        when(edition1Double.getPublisher()).thenReturn(publisherDouble);
//        when(edition2Double.getPublisher()).thenReturn(publisherDouble);
//        when(publisherDouble.getName()).thenReturn("publisher name");
//
//        // Condition
//        when(pubSample1Double.getCondition()).thenReturn(TOPSECRET.domain.Enum.Condition.LIKE_NEW);
//        when(pubSample2Double.getCondition()).thenReturn(TOPSECRET.domain.Enum.Condition.NEW);
//
//        // Library
//        libraryDouble = mock(Library.class);
//        when(libraryDouble.findLibraryPublicationsAvailableForSale()).thenReturn(List.of(pubSample1Double, pubSample2Double));
//
//        // Listing Factory
//        listingFactoryDouble = mock(ListingFactory.class);
//
//    }
//
//    // ==============================================================
//    // ====================== Tests =================================
//    // ==============================================================
//
//    // Tests to constructor --------------------------------------------------------------------------------------------
//
//    @Test
//    void constructor_ShouldThrowExceptionWhenLibraryIsNull() {
//
//        // Arrange
//        US015ToPutPublicationInDirectSaleController controller =
//                new US015ToPutPublicationInDirectSaleController(libraryDouble, listingFactoryDouble);
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                new US015ToPutPublicationInDirectSaleController(null, listingFactoryDouble)
//        );
//    }
//
//    @Test
//    void constructor_ShouldThrowExceptionWhenListingFactoryIsNull() {
//
//        // Arrange
//        US015ToPutPublicationInDirectSaleController controller =
//                new US015ToPutPublicationInDirectSaleController(libraryDouble, listingFactoryDouble);
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                new US015ToPutPublicationInDirectSaleController(libraryDouble, null)
//        );
//    }
//
//    @Test
//    void constructor_ShouldCreateControllerWhenBothArgumentsAreValid() {
//
//        // Arrange
//        US015ToPutPublicationInDirectSaleController controller =
//                new US015ToPutPublicationInDirectSaleController(libraryDouble, listingFactoryDouble);
//
//        // Act + Assert
//        assertNotNull(controller);
//    }
//
//    @Test
//    void constructor_ShouldAssignDependenciesCorrectly() {
//
//        // Arrange
//        US015ToPutPublicationInDirectSaleController controller =
//                new US015ToPutPublicationInDirectSaleController(libraryDouble, listingFactoryDouble);
//
//        // Act + Assert
//        assertEquals(libraryDouble, controller.getLibrary());
//        assertEquals(listingFactoryDouble, controller.getListingFactory());
//    }
//
//
//    // Tests to findLibraryPublicationsAvailableForSale --------------------------------------------------------------------
//
//    @Test
//    void findLibraryPublicationsAvailableForSale_ShouldReturnDTOs() {
//
//        // Arrange
//        US015ToPutPublicationInDirectSaleController controller =
//                new US015ToPutPublicationInDirectSaleController(libraryDouble, listingFactoryDouble);
//
//        // Act
//        List<PublicationSampleDTO_US015> listDTOs = controller.findLibraryPublicationsAvailableForSale();
//
//        // Assert
//        assertEquals(2, listDTOs.size());
//        assertEquals("title1", listDTOs.get(0).getTitle());
//        assertEquals("title2", listDTOs.get(1).getTitle());
//        assertEquals(List.of("author name"), listDTOs.get(0).getAuthorsName());
//        assertEquals(List.of("author name"), listDTOs.get(1).getAuthorsName());
//        assertEquals(List.of("fiction"), listDTOs.get(0).getGenres());
//        assertEquals(List.of("fantasy"), listDTOs.get(1).getGenres());
//        assertEquals("owner username", listDTOs.get(0).getOwnerUsername());
//        assertEquals("owner username", listDTOs.get(1).getOwnerUsername());
//        assertEquals("READ", listDTOs.get(0).getPublicationStatus());
//        assertEquals("READ", listDTOs.get(1).getPublicationStatus());
//        assertEquals("1st", listDTOs.get(0).getEditionNumber());
//        assertEquals("2nd", listDTOs.get(1).getEditionNumber());
//        assertEquals("1234567890", listDTOs.get(0).getCode());
//        assertEquals("9876543210", listDTOs.get(1).getCode());
//        assertEquals("BOOK", listDTOs.get(0).getMediaFormat());
//        assertEquals("BOOK", listDTOs.get(1).getMediaFormat());
//        assertEquals("ENGLISH", listDTOs.get(0).getEditionLanguage());
//        assertEquals("FRENCH", listDTOs.get(1).getEditionLanguage());
//        assertEquals("publisher name", listDTOs.get(0).getPublisherName());
//        assertEquals("publisher name", listDTOs.get(1).getPublisherName());
//        assertEquals("LIKE_NEW", listDTOs.get(0).getCondition());
//        assertEquals("NEW", listDTOs.get(1).getCondition());
//    }
//
//    @Test
//    void findLibraryPublicationsAvailableForSale_ShouldReturnEmptyListWhenNoneAvailable() {
//
//        // Arrange
//        libraryDouble = mock(Library.class);
//        when(libraryDouble.findLibraryPublicationsAvailableForSale()).thenReturn(List.of());
//
//        US015ToPutPublicationInDirectSaleController controller =
//                new US015ToPutPublicationInDirectSaleController(libraryDouble, listingFactoryDouble);
//
//        // Act
//        List<PublicationSampleDTO_US015> listDTOs = controller.findLibraryPublicationsAvailableForSale();
//
//        // Assert
//        assertEquals(0, listDTOs.size());
//    }
//
//
//    // Tests to putPublicationForSale ----------------------------------------------------------------------------------
//
//    @Test
//    void putPublicationForSale_ShouldThrowExceptionWhenTitleIsInvalid() {
//
//        // Arrange
//        PublicationSample pubSampleDouble = mock(PublicationSample.class);
//        Price priceDouble = mock(Price.class);
//        Picture pictureDouble = mock(Picture.class);
//
//        US015ToPutPublicationInDirectSaleController controller =
//                new US015ToPutPublicationInDirectSaleController(libraryDouble, listingFactoryDouble);
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.putPublicationForSale(
//                        "   ",
//                        List.of(pubSampleDouble),
//                        SaleType.DIRECT_SALE,
//                        priceDouble,
//                        "desc",
//                        List.of(pictureDouble)
//                )
//        );
//    }
//
//    @Test
//    void putPublicationForSale_ShouldThrowExceptionWhenPublicationSamplesIsNull() {
//
//        // Arrange
//        Price priceDouble = mock(Price.class);
//        Picture pictureDouble = mock(Picture.class);
//
//        US015ToPutPublicationInDirectSaleController controller =
//                new US015ToPutPublicationInDirectSaleController(libraryDouble, listingFactoryDouble);
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.putPublicationForSale(
//                        "Valid Title",
//                        null,
//                        SaleType.DIRECT_SALE,
//                        priceDouble,
//                        "desc",
//                        List.of(pictureDouble)
//                )
//        );
//    }
//
//    @Test
//    void putPublicationForSale_ShouldThrowExceptionWhenSaleTypeIsNull() {
//
//        // Arrange
//        PublicationSample pubSampleDouble = mock(PublicationSample.class);
//        Price priceDouble = mock(Price.class);
//        Picture pictureDouble = mock(Picture.class);
//
//        US015ToPutPublicationInDirectSaleController controller =
//                new US015ToPutPublicationInDirectSaleController(libraryDouble, listingFactoryDouble);
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.putPublicationForSale(
//                        "Valid Title",
//                        List.of(pubSampleDouble),
//                        null,
//                        priceDouble,
//                        "desc",
//                        List.of(pictureDouble)
//                )
//        );
//    }
//
//
//    @Test
//    void putPublicationForSale_ShouldThrowExceptionWhenPriceIsNull() {
//
//        // Arrange
//        PublicationSample pubSampleDouble = mock(PublicationSample.class);
//        Picture pictureDouble = mock(Picture.class);
//
//        US015ToPutPublicationInDirectSaleController controller =
//                new US015ToPutPublicationInDirectSaleController(libraryDouble, listingFactoryDouble);
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.putPublicationForSale(
//                        "Valid Title",
//                        List.of(pubSampleDouble),
//                        SaleType.DIRECT_SALE,
//                        null,
//                        "desc",
//                        List.of(pictureDouble)
//                )
//        );
//    }
//
//    @Test
//    void putPublicationForSale_ShouldThrowExceptionWhenShortDescriptionIsNull() {
//
//        // Arrange
//        PublicationSample pubSampleDouble = mock(PublicationSample.class);
//        Price priceDouble = mock(Price.class);
//        Picture pictureDouble = mock(Picture.class);
//
//        US015ToPutPublicationInDirectSaleController controller =
//                new US015ToPutPublicationInDirectSaleController(libraryDouble, listingFactoryDouble);
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.putPublicationForSale(
//                        "Valid Title",
//                        List.of(pubSampleDouble),
//                        SaleType.DIRECT_SALE,
//                        priceDouble,
//                        null,
//                        List.of(pictureDouble)
//                )
//        );
//    }
//
//    @Test
//    void putPublicationForSale_ShouldThrowExceptionWhenPicturesIsNull() {
//
//        // Arrange
//        PublicationSample pubSampleDouble = mock(PublicationSample.class);
//        Price priceDouble = mock(Price.class);
//        Picture pictureDouble = mock(Picture.class);
//
//        US015ToPutPublicationInDirectSaleController controller =
//                new US015ToPutPublicationInDirectSaleController(libraryDouble, listingFactoryDouble);
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class, () ->
//                controller.putPublicationForSale(
//                        "Valid Title",
//                        List.of(pubSampleDouble),
//                        SaleType.DIRECT_SALE,
//                        priceDouble,
//                        "short description",
//                        null
//                )
//        );
//    }
//
//    @Test
//    void putPublicationForSale_ShouldCallListingFactory() {
//
//        // Arrange
//        PublicationSample pubSampleDouble = mock(PublicationSample.class);
//        Price priceDouble = mock(Price.class);
//        Picture pictureDouble = mock(Picture.class);
//
//        Listing listingDouble = mock(Listing.class);
//        when(listingDouble.getListingTitle()).thenReturn("Valid Title");
//        when(listingDouble.getItems()).thenReturn(List.of(pubSampleDouble));
//        when(listingDouble.getSaleType()).thenReturn(SaleType.DIRECT_SALE);
//        when(listingDouble.getPrice()).thenReturn(priceDouble);
//        when(listingDouble.getShortDescription()).thenReturn("Valid description");
//        when(listingDouble.getPictures()).thenReturn(List.of(pictureDouble));
//
//        when(listingFactoryDouble.create(anyString(), anyList(), any(), any(), anyString(), anyList())).thenReturn(listingDouble);
//
//        US015ToPutPublicationInDirectSaleController controller =
//                new US015ToPutPublicationInDirectSaleController(libraryDouble, listingFactoryDouble);
//
//        // Act
//        Listing listing = controller.putPublicationForSale(
//                "Valid Title",
//                List.of(pubSampleDouble),
//                SaleType.DIRECT_SALE,
//                priceDouble,
//                "Valid description",
//                List.of(pictureDouble)
//        );
//
//        // Assert
//        assertEquals("Valid Title", listing.getListingTitle());
//        assertEquals(List.of(pubSampleDouble), listing.getItems());
//        assertEquals(SaleType.DIRECT_SALE, listing.getSaleType());
//        assertEquals(priceDouble, listing.getPrice());
//        assertEquals("Valid description", listing.getShortDescription());
//        assertEquals(List.of(pictureDouble), listing.getPictures());
//    }
//
//    @Test
//    void putPublicationForSale_ShouldReturnListingCreatedByFactory() {
//
//        // Arrange
//        PublicationSample pubSampleDouble = mock(PublicationSample.class);
//        Price priceDouble = mock(Price.class);
//        Picture pictureDouble = mock(Picture.class);
//
//        Listing listingDouble = mock(Listing.class);
//        when(listingFactoryDouble.create(any(), any(), any(), any(), any(), any()))
//                .thenReturn(listingDouble);
//
//        US015ToPutPublicationInDirectSaleController controller =
//                new US015ToPutPublicationInDirectSaleController(libraryDouble, listingFactoryDouble);
//
//        // Act
//        Listing result = controller.putPublicationForSale(
//                "Valid Title",
//                List.of(pubSampleDouble),
//                SaleType.DIRECT_SALE,
//                priceDouble,
//                "Valid description",
//                List.of(pictureDouble)
//        );
//
//        // Assert
//        assertEquals(listingDouble, result);
//    }
//}