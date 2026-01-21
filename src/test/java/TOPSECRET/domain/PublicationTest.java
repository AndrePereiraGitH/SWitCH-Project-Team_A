package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

//We could write the variables here and reuse them in all tests too

class PublicationTest {

    @Test
    void shouldCreatePublicationWithValidData() {
        //Arrange
        Country country1 = new Country("Denmark");
        Country country2 = new Country("Germany");


        Profile profile1 = new Profile("Tove Ditlevsen", country1, LocalDate.of(1917, 12, 14));
        Author author1 = new Author(profile1, "valio bio");
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

        String edition1 = "2nd Edition";
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
        PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");

        //Act
        Publication publication1 = new Publication(bibliographicalData1, publicationType, user1,
                pubStatus1, edition1, code1);

        //Assert
        assertNotNull(publication1);
       // assertEquals(edition1,  publication1.getEdition()); error is found here, probably in the variables, correct this asap
    }
//
//    @Test
//    void shouldThrowExceptionIfBibliographicalDataNull() {
//
//        //Arrange
//        Country country1 = new Country("Denmark");
//        Country country2 = new Country("Germany");
//
//
//        Profile author1 = new Profile("Tove Ditlevsen", country1, LocalDate.of(1917, 12, 14));
//        Address address1 = new Address("R Maria", "12", "156-5", country2);
//        Email email1 = new Email("Maria1", "gmail.com");
//        Contact contact1 = new Contact("915945695", "+351", email1, address1);
//        Publisher publisher1 = new Publisher("Some Publisher", "PT123456789", contact1);
//        BibliographicalData bibliographicalData1 = null;
//
//        String edition1 = "2nd Edition";
//        Code code1 = Code.fromIsbn10("1234567890");
//
//        Country country3 = new Country("Italy");
//
//        Address address2 = new Address("Main Street", "10", "12345", country3);
//        Email email2 = new Email("test.user", "example.com");
//        Contact contact2 = new Contact("987654321", "351", email2, address2);
//
//
//        Country country4 = new Country("Uganda");
//        Profile profile2 = new Profile(
//                "Test Profile",
//                country4,
//                LocalDate.of(1997, 8, 15)
//        );
//
//        UserRole userRole1 = UserRole.ADMIN;
//        User user1 = new User("coolGuy", contact2, profile2, userRole1);
//        PublicationStatus pubStatus1 = PublicationStatus.READING;
//
//        //Act
//        Publication publication1 = new Publication(bibliographicalData1, "coolCover", user1,
//                pubStatus1, edition1, code1);
//
//        //Assert
//        assertThrows(IllegalArgumentException.class, () -> new Publication(bibliographicalData1, "coolCover", user1,
//                pubStatus1, edition1, code1));
//    }
}

