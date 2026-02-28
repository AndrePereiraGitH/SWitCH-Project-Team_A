package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;

import TOPSECRET.dto.AuctionItemDTO_US024;
import TOPSECRET.dto.BibliographicalDataDTO_US024;
import TOPSECRET.repository.AuctionRepository;
import TOPSECRET.repository.BibliographicalDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class US024GetTheListOfItemsOnAuctionOfAGivenPublicationControllerTest {

    private BibliographicalDataRepository bdRepository;
    private AuctionRepository auctionRepository;
    private US024GetTheListOfItemsOnAuctionOfAGivenPublicationController controller;


    @BeforeEach
    void setUp() {
        bdRepository = mock(BibliographicalDataRepository.class);
        auctionRepository = mock(AuctionRepository.class);
        controller = new US024GetTheListOfItemsOnAuctionOfAGivenPublicationController(
                bdRepository,
                auctionRepository
        );
    }

    @Test
    void getAllPublicationBD_ShouldReturnListOfDTOsWhenPublicationBDExist() {
        // Arrange
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

    @Test
    void getAllPublicationsShouldReturnEmptyListWhenRepositoryIsEmpty() {
        // Arrange
        BibliographicalDataRepository bdRepoDouble = mock(BibliographicalDataRepository.class);
        when(bdRepoDouble.getAllPublicationsBD()).thenReturn(List.of());

        // Act
        List <BibliographicalDataDTO_US024> result = controller.getAllPublicationBD();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getListOfItemsOnAuctionByGivenPublicationDTO_ShouldReturnListOfDTOsWhenItemsExist() {

        // Arrange
        PublicationSample sampleDouble1 = createMockPublicationSample("Title1","12345678910","User1");
        PublicationSample sampleDouble2 = createMockPublicationSample("Title1","12345678910","User2");

        when(auctionRepository.getListOfItemsOnAuctionByGivenPublication("Title1")).thenReturn(List.of(sampleDouble1,sampleDouble2));

        //Act
        List<AuctionItemDTO_US024> result = controller.getListOfItemsOnAuctionByGivenPublicationDTO("Title1");

        //Arrange
        assertEquals(2, result.size());
        assertEquals("Title1", result.get(0).title);
        assertEquals("User1",result.get(0).seller);
        assertEquals("User2",result.get(1).seller);

    }

    @Test
    void getListOfItemsOnAuctionShouldReturnEmptyWhenNoItemsMatchPublicationDTO() {
        // Arrange

        // Act
        List<AuctionItemDTO_US024> result = controller.getListOfItemsOnAuctionByGivenPublicationDTO("Title2");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    private PublicationSample createMockPublicationSample(String title, String code, String seller){

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

    private BibliographicalData createMockBibliographicalData(String title){
        BibliographicalData bdDouble = mock(BibliographicalData.class);
        when(bdDouble.getTitle()).thenReturn(title);

        return bdDouble;
    }

    private Edition createMockEdition(String code){

        Code codeDouble = mock(Code.class);
        when(codeDouble.toString()).thenReturn(code);

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getCode()).thenReturn(codeDouble);

        return editionDouble;
    }

    private User createMockSeller(String seller){
        User sellerDouble = mock(User.class);
        when(sellerDouble.getUserName()).thenReturn(seller);
        return sellerDouble;
    }

    private Condition createMockCondition(){
        Condition conditionDouble = mock(Condition.class);
        when(conditionDouble.getDescription()).thenReturn(String.valueOf(Condition.POOR));
        return conditionDouble;
    }

}