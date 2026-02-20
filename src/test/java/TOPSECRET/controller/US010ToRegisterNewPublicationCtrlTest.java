package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.repository.PublicationRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class US010ToRegisterNewPublicationCtrlTest {

    @Test
    void shouldAddPublication() {
        PublicationRepository publicationRepository1 = new PublicationRepository();

        US010ToRegisterNewPublicationCtrl pubCtrl1 = new US010ToRegisterNewPublicationCtrl(publicationRepository1);

        //Instantiating Publication here, could also do that above to standardize future tests and make it more readable

        //Arrange
        Country country1 = new Country("United_States"); //reminder that multiple word countries are written like this
        Country country2 = new Country("Germany");


        Profile profile1 = new Profile("Tove Ditlevsen", country1, LocalDate.of(1917, 12, 14));
        Author author1 = new Author(profile1, "valid bio");
        Address address1 = new Address("R Maria", "12", "156-5", country2);
        Email email1 = new Email("Maria1", "gmail.com");
        Contact contact1 = new Contact("915945695", "+351", email1, address1);
        Publisher publisher1 = new Publisher("Some Publisher", "PT123456789", contact1);
        BibliographicalData bibliographicalData1 = new BibliographicalData(
                "Childhood, Youth, Dependency: The Copenhagen Trilogy",
                List.of(author1),
                Locale.ENGLISH,
//                publisher1,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
//                200,
                "Some text");

//        String edition1 = "2nd Edition";
        // Edition
        Code code = Code.fromIsbn13 ("9783161484123");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition1= new Edition(
                "1st",
                code,
                physicalAttributes,
                pubType,
                publisher,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        Code code1 = Code.fromIsbn10("1234567890");

        Country country3 = new Country("Italy");

        Address address2 = new Address("Main Street", "10", "12345", country3);
        Email email2 = new Email("test.user", "example.com");
        Contact contact2 = new Contact("987654321", "351", email2, address2);


        Country country4 = new Country("Uganda");
        Profile profile2 = new Profile(
                "Test Profile",
                country4,
                LocalDate.of(1997, 8, 15)
        );

        UserRole userRole1 = UserRole.ADMIN;
        User user1 = new User("coolGuy", contact2, profile2, userRole1);
        PublicationStatus pubStatus1 = PublicationStatus.READING;
        PublicationType publicationType1 = new PublicationType(TypeOfBinding.PAPERBACK, "Paperback");


        //Act is here
        boolean publicationAdded1 = pubCtrl1.createAndAddPublication(bibliographicalData1,
                publicationType1, user1, pubStatus1, edition1, code1);


        //Assert is here
        assertTrue(publicationAdded1);


    }

    //Should test here if it throws exception when a parameter comes null, for example?

}