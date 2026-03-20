package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.AuctionItemDTO_US024;
import TOPSECRET.dto.BibliographicalDataDTO_US024;
import TOPSECRET.repository.AuctionRepositoryMem;
import TOPSECRET.repository.BibliographicalDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link US024GetTheListOfItemsOnAuctionOfAGivenPublicationController}.
 *
 * <p>All repository dependencies are replaced with Mockito doubles to ensure
 * tests are isolated from infrastructure concerns and focus solely on
 * controller behaviour.</p>
 */
class US024GetTheListOfItemsOnAuctionOfAGivenPublicationControllerTest {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    private BibliographicalDataRepository bdRepository;
    private AuctionRepositoryMem auctionRepository;

    // -------------------------------------------------------------------------
    // Setup
    // -------------------------------------------------------------------------

    /**
     * Initialises fresh mock repositories and a new controller instance
     * before each test to guarantee test isolation.
     */
    @BeforeEach
    void setUp() {
        bdRepository = mock(BibliographicalDataRepository.class);
        auctionRepository = mock(AuctionRepositoryMem.class);
    }

    // -------------------------------------------------------------------------
    // Tests — getAllPublicationBD()
    // -------------------------------------------------------------------------

    /**
     * Verifies that {@code getAllPublicationBD()} returns a correctly mapped list
     * of {@link BibliographicalDataDTO_US024} when the repository contains publications.
     *
     * <p>Expects two DTOs with titles matching the mocked domain objects.</p>
     */
    @Test
    void getAllPublicationBD_ShouldReturnListOfDTOsWhenPublicationBDExist() {
        // Arrange
        US024GetTheListOfItemsOnAuctionOfAGivenPublicationController controller =
                new US024GetTheListOfItemsOnAuctionOfAGivenPublicationController(bdRepository,auctionRepository);
        BibliographicalData bd1 = createMockBibliographicalData("Title1");
        BibliographicalData bd2 = createMockBibliographicalData("Title2");

        when(bdRepository.getAllPublicationsBD()).thenReturn(List.of(bd1, bd2));

        // Act
        List<BibliographicalDataDTO_US024> result = controller.getAllPublicationBD();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Title1", result.get(0)._publicationTitle);
        assertEquals("Title2", result.get(1)._publicationTitle);
    }

    /**
     * Verifies that {@code getAllPublicationBD()} returns an empty list
     * (rather than {@code null}) when the repository contains no publications.
     */
    @Test
    void getAllPublicationsShouldReturnEmptyListWhenRepositoryIsEmpty() {
        // Arrange
        US024GetTheListOfItemsOnAuctionOfAGivenPublicationController controller =
                new US024GetTheListOfItemsOnAuctionOfAGivenPublicationController(bdRepository,auctionRepository);
        BibliographicalDataRepository bdRepoDouble = mock(BibliographicalDataRepository.class);
        when(bdRepoDouble.getAllPublicationsBD()).thenReturn(List.of());

        // Act
        List<BibliographicalDataDTO_US024> result = controller.getAllPublicationBD();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // Tests — getListOfItemsOnAuctionByGivenPublicationDTO()
    // -------------------------------------------------------------------------

    /**
     * Verifies that {@code getListOfItemsOnAuctionByGivenPublicationDTO()} returns
     * a correctly mapped list of {@link AuctionItemDTO_US024} when auction items
     * exist for the specified publication.
     *
     * <p>Expects two DTOs with the correct title and seller usernames.</p>
     */
    @Test
    void getListOfItemsOnAuctionByGivenPublicationDTO_ShouldReturnListOfDTOsWhenItemsExist() {
        // Arrange
        US024GetTheListOfItemsOnAuctionOfAGivenPublicationController controller =
                new US024GetTheListOfItemsOnAuctionOfAGivenPublicationController(bdRepository,auctionRepository);
        PublicationSample sampleDouble1 = createMockPublicationSample("Title1", "12345678910", "User1");
        PublicationSample sampleDouble2 = createMockPublicationSample("Title1", "12345678910", "User2");

        when(auctionRepository.getListOfItemsOnAuctionByGivenPublication("Title1"))
                .thenReturn(List.of(sampleDouble1, sampleDouble2));

        // Act
        List<AuctionItemDTO_US024> result = controller.getListOfItemsOnAuctionByGivenPublicationDTO("Title1");

        // Assert
        assertEquals(2, result.size());
        assertEquals("Title1", result.get(0).title);
        assertEquals("User1", result.get(0).seller);
        assertEquals("User2", result.get(1).seller);
    }

    /**
     * Verifies that {@code getListOfItemsOnAuctionByGivenPublicationDTO()} returns
     * an empty list when no auction items match the given publication identifier.
     */
    @Test
    void getListOfItemsOnAuctionShouldReturnEmptyWhenNoItemsMatchPublicationDTO() {
        // Arrange
        US024GetTheListOfItemsOnAuctionOfAGivenPublicationController controller =
                new US024GetTheListOfItemsOnAuctionOfAGivenPublicationController(bdRepository,auctionRepository);

        // Act
        List<AuctionItemDTO_US024> result = controller.getListOfItemsOnAuctionByGivenPublicationDTO("Title2");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void ensure_getListOfItemsOnAuctionByGivenPublicationDTO_throwsExceptionWhenPublicationDTOIsNull() {
        // Arrange
        US024GetTheListOfItemsOnAuctionOfAGivenPublicationController controller =
                new US024GetTheListOfItemsOnAuctionOfAGivenPublicationController(bdRepository,auctionRepository);

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> controller.getListOfItemsOnAuctionByGivenPublicationDTO(null));
    }

    @Test
    void ensure_getListOfItemsOnAuctionByGivenPublicationDTO_throwsExceptionWhenPublicationDTOIsEmpty() {
        // Arrange
        US024GetTheListOfItemsOnAuctionOfAGivenPublicationController controller =
                new US024GetTheListOfItemsOnAuctionOfAGivenPublicationController(bdRepository,auctionRepository);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> controller.getListOfItemsOnAuctionByGivenPublicationDTO(""));
    }

    // -------------------------------------------------------------------------
    // Helper / factory methods
    // -------------------------------------------------------------------------

    /**
     * Creates a fully mocked {@link PublicationSample} with the supplied
     * title, edition code, and seller username.
     *
     * @param title  the publication title returned by the bibliographical data mock
     * @param code   the edition code returned by the edition mock
     * @param seller the username returned by the seller mock
     * @return a configured {@link PublicationSample} mock
     */
    private PublicationSample createMockPublicationSample(String title, String code, String seller) {
        BibliographicalData bdDouble = createMockBibliographicalData(title);
        Edition editionDouble = createMockEdition(code);
        User sellerDouble = createMockSeller(seller);
        Condition conditionDouble = createMockCondition();

        PublicationSample pbSampleDouble = mock(PublicationSample.class);
        when(pbSampleDouble.getBibliographicalData()).thenReturn(bdDouble);
        when(pbSampleDouble.getEdition()).thenReturn(editionDouble);
        when(pbSampleDouble.getCondition()).thenReturn(conditionDouble);
        when(pbSampleDouble.getOwner()).thenReturn(sellerDouble);

        return pbSampleDouble;
    }

    /**
     * Creates a mocked {@link BibliographicalData} that returns the given title.
     *
     * @param title the value to be returned by {@code getTitle()}
     * @return a configured {@link BibliographicalData} mock
     */
    private BibliographicalData createMockBibliographicalData(String title) {
        BibliographicalData bdDouble = mock(BibliographicalData.class);
        when(bdDouble.getTitle()).thenReturn(title);
        return bdDouble;
    }

    /**
     * Creates a mocked {@link Edition} whose {@link Code} returns the given string.
     *
     * @param code the value to be returned by {@code getCode().toString()}
     * @return a configured {@link Edition} mock
     */
    private Edition createMockEdition(String code) {
        Code codeDouble = mock(Code.class);
        when(codeDouble.toString()).thenReturn(code);

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getCode()).thenReturn(codeDouble);

        return editionDouble;
    }

    /**
     * Creates a mocked {@link User} that returns the given username.
     *
     * @param seller the value to be returned by {@code getUserName()}
     * @return a configured {@link User} mock
     */
    private User createMockSeller(String seller) {
        User sellerDouble = mock(User.class);
        when(sellerDouble.getUserName()).thenReturn(seller);
        return sellerDouble;
    }

    /**
     * Creates a mocked {@link Condition} whose description matches
     * {@link Condition#POOR}.
     *
     * @return a configured {@link Condition} mock
     */
    private Condition createMockCondition() {
        Condition conditionDouble = mock(Condition.class);
        when(conditionDouble.getDescription()).thenReturn(String.valueOf(Condition.POOR));
        return conditionDouble;
    }
}