package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.dto.PublicationSampleDTO_US016;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.ILibraryRepository;
import TOPSECRET.repository.ListingRepository;
import TOPSECRET.repository.PublicationSampleRepositoryMem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US016ToPutPublicationInAuctionController}.
 * <p>
 * This test validates:
 * <ul>
 *   <li>Retrieval of publications from a user's library</li>
 *   <li>Conversion of publications to DTOs</li>
 *   <li>Validation rules when creating auction listings</li>
 *   <li>Successful auction creation and duplicate listing behavior</li>
 * </ul>
 */
class US016ToPutPublicationInAuctionControllerTest {

    private ILibraryRepository libraryRepoDouble;
    private ListingRepository listingRepoDouble;
    private AuctionRepository auctionRepoDouble;
    private PublicationSampleRepositoryMem publicationSampleRepoDouble;
    private US016ToPutPublicationInAuctionController controller;

    @BeforeEach
    void setUp() {
        libraryRepoDouble = mock(ILibraryRepository.class);
        listingRepoDouble = mock(ListingRepository.class);
        auctionRepoDouble = mock(AuctionRepository.class);
        publicationSampleRepoDouble = mock(PublicationSampleRepositoryMem.class);
        controller = new US016ToPutPublicationInAuctionController(libraryRepoDouble, listingRepoDouble, auctionRepoDouble, publicationSampleRepoDouble);    }


    @Test
    void getAllPublicationsInUserLibrary_shouldThrow_ifUsernameNull() throws InstanceNotFoundException {

        User userDouble = mock(User.class);
        when(userDouble.getUserName()).thenReturn(null);


        // act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.getAllPublicationsInUserLibrary(userDouble.getUserName()));

        // assert message
        assertEquals("username cannot be null", exception.getMessage());

    }

    @Test
    void getAllPublicationsInUserLibrary_shouldThrow_ifLibraryIsEmpty() throws InstanceNotFoundException {

        // arrange
        String username = "username";

        Library libraryDouble = mock(Library.class);
        when(libraryDouble.getAllPublications()).thenReturn(List.of());

        when(libraryRepoDouble.getLibraryOfUser(username)).thenReturn(libraryDouble);

        // act + assert
        EmptyStackException exception = assertThrows(EmptyStackException.class, () -> controller.getAllPublicationsInUserLibrary(username));

    }

    @Test
    void getAllPublicationsInUserLibrary_shouldReturnAListOfSameSize() throws InstanceNotFoundException {

        // arrange
        String username = "username";

        PublicationSample p1Double = mock(PublicationSample.class);
        PublicationSample p2Double = mock(PublicationSample.class);

        BibliographicalData bd1Double = mock(BibliographicalData.class);
        BibliographicalData bd2Double = mock(BibliographicalData.class);

        when(bd1Double.getTitle()).thenReturn("Title 1");
        when(bd2Double.getTitle()).thenReturn("Title 2");

        when(p1Double.getBibliographicalData()).thenReturn(bd1Double);
        when(p2Double.getBibliographicalData()).thenReturn(bd2Double);

        User ownerDouble = mock(User.class);
        Condition condition = Condition.LIKE_NEW;

        when(p1Double.getOwner()).thenReturn(ownerDouble);
        when(p2Double.getOwner()).thenReturn(ownerDouble);

        when(p1Double.getCondition()).thenReturn(condition);
        when(p2Double.getCondition()).thenReturn(condition);

        Library libraryDouble = mock(Library.class);

        when(libraryRepoDouble.getLibraryOfUser(username)).thenReturn(libraryDouble);
        when(libraryDouble.getAllPublications()).thenReturn(List.of(p1Double, p2Double));

        // act
        List<PublicationSampleDTO_US016> result = controller.getAllPublicationsInUserLibrary(username);

        // assert
        assertEquals(2, result.size());
    }

    @Test
    void createAuctionListing_shouldThrow_ifListOfPublications_isEmpty() throws InstanceAlreadyExistsException {

        String listingTitle = "Listing Title";
        List<PublicationSampleDTO_US016> publicationDTOs = new ArrayList<>();
        Price listingPriceDouble = mock(Price.class);
        String shorDescription = "short descr";
        Picture pictureDouble = mock(Picture.class);
        List<Picture> pictures = new ArrayList<>(List.of(pictureDouble));
        LocalDateTime auctionStartDate = LocalDateTime.now().plusMinutes(10);
        LocalDateTime auctionEndDate = LocalDateTime.now().plusDays(10);
        Price buyoutPrice = mock(Price.class);

        // act + assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.createAuctionListing(listingTitle, publicationDTOs, listingPriceDouble, shorDescription, pictures, auctionStartDate, auctionEndDate, buyoutPrice));

        assertEquals("You should send at least one publication to Listing", exception.getMessage());
    }

    @Test
    void createAuctionListing_shouldThrow_ifListOfPublications_isNull() {

        // arrange
        String listingTitle = "Listing Title";
        Price listingPriceDouble = mock(Price.class);
        String shorDescription = "short descr";
        Picture pictureDouble = mock(Picture.class);
        List<Picture> pictures = new ArrayList<>(List.of(pictureDouble));
        LocalDateTime auctionStartDate = LocalDateTime.now().plusMinutes(10);
        LocalDateTime auctionEndDate = LocalDateTime.now().plusDays(10);
        Price buyoutPrice = mock(Price.class);

        // act + assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.createAuctionListing(listingTitle, null, listingPriceDouble, shorDescription, pictures, auctionStartDate, auctionEndDate, buyoutPrice));

        assertEquals("You should send at least one publication to Listing", exception.getMessage());
    }

    @Test
    void createAuctionListing_shouldThrow_ifStartDateNull() {
        // arrange
        String listingTitle = "Listing Title";
        List<PublicationSampleDTO_US016> publicationDTOs = List.of(mock(PublicationSampleDTO_US016.class));
        Price listingPriceDouble = mock(Price.class);
        String shorDescription = "short descr";
        List<Picture> picturesDouble = new ArrayList<>(List.of(mock(Picture.class)));
        Price buyoutDouble = mock(Price.class);

        // act + assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.createAuctionListing(
                listingTitle,
                publicationDTOs,
                listingPriceDouble,
                shorDescription,
                picturesDouble,
                null,
                LocalDateTime.now().plusDays(10),
                buyoutDouble
                ));

        assertEquals("auctionStartDateTime, auctionEndDateTime and buyoutPrice cannot be null", exception.getMessage());

    }

    @Test
    void createAuctionListing_shouldThrow_whenEndDateTimeIsNull() {
        // arrange
        String listingTitle = "Listing Title";
        List<PublicationSampleDTO_US016> publicationDTOs = List.of(mock(PublicationSampleDTO_US016.class));
        Price listingPriceDouble = mock(Price.class);
        String shorDescription = "short descr";
        List<Picture> picturesDouble = new ArrayList<>(List.of(mock(Picture.class)));
        Price buyoutDouble = mock(Price.class);

        // act + assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.createAuctionListing(
                listingTitle,
                publicationDTOs,
                listingPriceDouble,
                shorDescription,
                picturesDouble,
                LocalDateTime.now().plusDays(10),
                null,
                buyoutDouble
        ));

        assertEquals("auctionStartDateTime, auctionEndDateTime and buyoutPrice cannot be null", exception.getMessage());
    }

    @Test
    void createAuctionListing_shouldThrow_whenBuyoutPriceIsNull() {
        // arrange
        String listingTitle = "Listing Title";
        List<PublicationSampleDTO_US016> publicationDTOs = List.of(mock(PublicationSampleDTO_US016.class));
        Price listingPriceDouble = mock(Price.class);
        String shorDescription = "short descr";
        List<Picture> picturesDouble = new ArrayList<>(List.of(mock(Picture.class)));

        // act + assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.createAuctionListing(
                listingTitle,
                publicationDTOs,
                listingPriceDouble,
                shorDescription,
                picturesDouble,
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(12),
                null
        ));

        assertEquals("auctionStartDateTime, auctionEndDateTime and buyoutPrice cannot be null", exception.getMessage());
    }

    @Test
    void createAuctionListing_happyPath_shouldCreateListingAndAuction_andReturneTrue() throws InstanceAlreadyExistsException {
        // arrange
        String listingTitle = "Listing Title";
        Price listingPriceDouble = mock(Price.class);
        String shorDescription = "short descr";
        Picture pictureDouble = mock(Picture.class);
        List<Picture> pictures = List.of(pictureDouble);
        LocalDateTime auctionStartDate = LocalDateTime.now().plusMinutes(10);
        LocalDateTime auctionEndDate = LocalDateTime.now().plusDays(5);
        Price buyoutPrice = mock(Price.class);

        PublicationSampleDTO_US016 pubDTODouble1 = mock(PublicationSampleDTO_US016.class);
        PublicationSampleDTO_US016 pubDTODouble2 = mock(PublicationSampleDTO_US016.class);
        List<PublicationSampleDTO_US016> dtoDoubles = List.of(pubDTODouble1, pubDTODouble2);

        PublicationSample pub1Double = mock(PublicationSample.class);
        PublicationSample pub2Double = mock(PublicationSample.class);
        List<PublicationSample> pubsDouble = List.of(pub1Double, pub2Double);

        Listing createdListing = mock(Listing.class);

        when(publicationSampleRepoDouble.getPublicationsByDTO(dtoDoubles)).thenReturn(pubsDouble);
        when(listingRepoDouble.createAndAdd(
                listingTitle,
                pubsDouble,
                SaleType.AUCTION,
                listingPriceDouble,
                shorDescription,
                pictures
        )).thenReturn(createdListing);

        // act

        boolean result = controller.createAuctionListing(listingTitle, dtoDoubles, listingPriceDouble, shorDescription, pictures, auctionStartDate, auctionEndDate, buyoutPrice);

        // assert
        assertTrue(result);

        // verify
        verify(publicationSampleRepoDouble).getPublicationsByDTO(dtoDoubles);
        verify(listingRepoDouble).createAndAdd(listingTitle, pubsDouble, SaleType.AUCTION, listingPriceDouble, shorDescription, pictures);
        verify(auctionRepoDouble).createAndAdd(createdListing, auctionStartDate, auctionEndDate, buyoutPrice);

    }

}