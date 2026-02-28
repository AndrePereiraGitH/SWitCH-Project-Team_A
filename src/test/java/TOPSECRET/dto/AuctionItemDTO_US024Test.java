package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.Condition;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuctionItemDTO_US024Test {

    @Test
    void constructorShouldInitializeAllFields() {

        //Arrange
        String expectedTitle = "Title";
        String expectedCode = "12345678910";
        String expectedCondition = "NEW";
        String expectedSeller = "Seller1";

        //Act
        AuctionItemDTO_US024 result = new AuctionItemDTO_US024(expectedTitle,expectedCode,expectedCondition,expectedSeller);

        //Assert
        assertEquals(expectedTitle,result.title);
        assertEquals(expectedCode,result.code);
        assertEquals(expectedCondition,result.condition);
        assertEquals(expectedSeller,result.seller);

    }


    @Test
    void toDTOShouldConvert() {

        //Arrange
        PublicationSample pbSampleDouble = createMockPublicationSample("Title 1","12345678910","Seller1");

        //Act
        AuctionItemDTO_US024 result = AuctionItemDTO_US024.toDTO(pbSampleDouble);


        //Assert
        assertEquals("Title 1",result.title);
        assertEquals("12345678910",result.code);
        assertEquals("Seller1",result.seller);
        assertEquals(Condition.POOR.toString(),result.condition);
    }

    @Test
    void toDTOS_shouldReturnListOfDTOS(){

        //Arrange
        PublicationSample pbSampleDouble1 = createMockPublicationSample("Title 1","12345678910","Seller1");
        PublicationSample pbSampleDouble2 = createMockPublicationSample("Title 2","12345678910","Seller1");
        PublicationSample pbSampleDouble3 = createMockPublicationSample("Title 3","12345678910","Seller1");

        List<PublicationSample> pbSampleList = List.of(pbSampleDouble1,pbSampleDouble2,pbSampleDouble3);

        //Act
        List<AuctionItemDTO_US024> result = AuctionItemDTO_US024.toDTOs(pbSampleList);

        //Assert
        assertEquals("Title 1", result.get(0).title);
        assertEquals("Title 2", result.get(1).title);
        assertEquals("Title 3", result.get(2).title);

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