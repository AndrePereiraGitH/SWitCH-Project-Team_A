package TOPSECRET.repository;


import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PublicationRepositoryTest {

    // BibliographicalData
    Country country1 = new Country("Denmark");
    Country country2 = new Country("Germany");
    Profile author1 = new Profile("Tove Ditlevsen", country1, LocalDate.of(1917, 12, 14));
    Address address1 = new Address("R Maria", "12", "156-5", country2);
    Email email1 = new Email("Maria1", "gmail.com");
    Contact contact1 = new Contact("915945695", "+351", email1, address1);
    Publisher publisher1 = new Publisher("Some Publisher", "PT123456789", contact1);
    BibliographicalData bibliographicalData1 = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author1),
            Locale.ENGLISH,
            publisher1,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre ("Fiction")),
            200,
            "Some text");

    // User
    Country country4 = new Country("Uganda");
    Profile profile2 = new Profile(
            "Test Profile",
            country4,
            LocalDate.of(1997, 8, 15)
    );


    UserRole userRole1 = UserRole.ADMIN;
    User user1 = new User("coolGuy", contact1, profile2, userRole1);

    //PublicationStatus
    PublicationStatus pubStatus1 = PublicationStatus.READING;

    //Edition
    String edition1 = "2nd Edition";

    //Code
    Code code1 = Code.fromIsbn10("1234567890");

    //PublicationType
    PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");


    Publication publication1 = new Publication(bibliographicalData1, publicationType, user1,
            pubStatus1, edition1, code1);



    // Happy path

    @Test
    void ensureANewPublicationIsCreatedAndAdded() {
        //Arrange
        List<Publication> emptyList = new ArrayList<>();
        PublicationRepository pr1 = new PublicationRepository(emptyList);

        //Act
        boolean realResult = pr1.createAndAdd(bibliographicalData1, publicationType, user1,
                pubStatus1, edition1, code1);

        // Assert
        assertTrue(realResult);

    }

    @Test
    void ensureThatADuplicatePublicationIsNotAdded(){
        //Arrange
        List<Publication> list = new ArrayList<>();
        list.add(publication1);
        PublicationRepository pr1 = new PublicationRepository(list);

        //Act
        boolean realResult=pr1.createAndAdd(bibliographicalData1, publicationType, user1,
                pubStatus1, edition1, code1);

        // Assert
        assertFalse(realResult);




    }

    @Test
    void ifPublicationIsExistentShouldReturnTrue() {
        //Arrange
        List<Publication> list = new ArrayList<>();
        list.add(publication1);
        PublicationRepository pr1 = new PublicationRepository(list);

        //Act
        boolean realResult = pr1.isExistent(publication1);

        // Assert
        assertTrue(realResult);
    }


    @Test
    void ifPublicationIsNotExistentShouldReturnFalse(){

        //Arrange
        List<Publication> list = new ArrayList<>();
        PublicationRepository pr1 = new PublicationRepository(list);

        //Act
        boolean realResult = pr1.isExistent(publication1);

        //Assert
        assertFalse(realResult);

    }


    @Test
    void ensureConstructorCopiesListNotOriginalReferences(){
        // Arrange
        List<Publication> list = new ArrayList<>();
        PublicationRepository pr1 = new PublicationRepository(list);
        list.add(publication1);

        //Act
        boolean realResult=pr1.isExistent(publication1);

        //Assert
        assertFalse(realResult);

    }

}