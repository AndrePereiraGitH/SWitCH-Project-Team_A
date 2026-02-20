package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ItemDetailsDTOTest {


    Profile profile = new Profile("Tove Ditlevsen", new Country("DENMARK"), LocalDate.of(1917, 12, 14));
    Author author = new Author(profile, "valid bio" );
    Address address = new Address("R Maria", "12", "156-5", new Country("PORTUGAL"));
    Email email = new Email("maria1", "gmail.com");
    Contact contact = new Contact("915945695", "+351", email, address);
    Publisher publisher = new Publisher("Some Publisher", "PT123456789", contact);
    PublicationType publicationType = new PublicationType(TypeOfBinding.PAPERBACK, "Paperback");

    BibliographicalData bd = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author),
            Locale.ENGLISH,
//            publisher,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre ("Fiction")),
//            200,
            "Some text"
    );

    User user = new User("maria1", contact, profile, UserRole.NORMAL_USER
    );

    Code code = Code.fromIsbn13("9781234567890");

    // Edition
    Code code1 = Code.fromIsbn13 ("9783161484123");
    Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
    PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
    Publisher publisher1 = new Publisher("Porto Editora", "PT987654321", null);
    Edition edition= new Edition(
            "1st",
            code1,
            physicalAttributes,
            pubType,
            publisher1,
            Locale.ENGLISH,
            LocalDate.of(2020, 1, 1)
    );

    Publication pub = new Publication(bd, publicationType, user, PublicationStatus.FOR_SALE, edition, code);

    Dimensions dim = new Dimensions(10,10,10);

    Item item1 = new Item(Condition.LIKE_NEW,50,dim,pub);

    Item item2 = new Item(Condition.LIKE_NEW,70,dim,pub);



    @Test
    void ensureDTOIsCreatedSuccessfully() {
        // Arrange
        String expectedCondition = Condition.NEW.toString();
        String expectedPublicationTitle = "Harry Potter";

        // Act
        ItemDetailsDTO dto = new ItemDetailsDTO(expectedCondition, expectedPublicationTitle);

        // Assert
        assertNotNull(dto);
        assertEquals(expectedCondition, dto.condition);
        assertEquals(expectedPublicationTitle, dto.publication);
    }


    // THEY SHOULD NOT HANDLE NULL VALUES

//    @Test
//    void ensureDTOHandlesNullValues() {
//
//        // Arrange & Act
//        ItemDTO_US024 dto = new ItemDTO_US024(null, null);
//
//        // Assert
//        assertNull(dto.condition);
//        assertNull(dto.publication);
//
//    }

    @Test
    void ensureToDTOCreatesADTOWithSuccess(){

        ItemDetailsDTO dto = ItemDetailsDTO.toDTO(item1);

        assertNotNull(dto);
        assertEquals(item1.getCondition().toString(), dto.condition);
        assertEquals(item1.getPublication().getBibliographicalData().getTitle(), dto.publication);
    }

    @Test
    void ensureListOfItemsAuctionDTOIsCreatedWithSuccess(){
        //Arrange
        List<Item> listItems = new ArrayList<>();
        listItems.add(item1);
        listItems.add(item2);

        // Act
        List<ItemDetailsDTO> expectedList = ItemDetailsDTO.toDTOs(listItems);

        //Arrange
        assertNotNull(expectedList);
        assertEquals(2, expectedList.size());
        assertEquals(item1.getPublication().getBibliographicalData().getTitle(), expectedList.get(0).publication);
        assertEquals(item2.getPublication().getBibliographicalData().getTitle(), expectedList.get(1).publication);
        assertEquals(item1.getCondition().toString(),expectedList.get(0).condition);
        assertEquals(item2.getCondition().toString(),expectedList.get(1).condition);

    }



}