package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


class PublicationTest {

    //We could write the variables here and reuse them in all tests too

    //Arrange
    Country country1 = new Country("Denmark");
    Country country2 = new Country("Germany");


    Profile profile1 = new Profile("Tove Ditlevsen", country1, LocalDate.of(1917, 12, 14));
    Author author1 = new Author(profile1, "valio bio");
    Address address1 = new Address("R Maria", "12", "156-5", country2);
    Email email1 = new Email("Maria1", "gmail.com");
    Contact contact1 = new Contact("915945695", "+351", email1, address1);
    Publisher publisher1 = new Publisher("Some Publisher", "PT123456789", contact1);
    BibliographicalData anyBiblioData = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author1),
            Locale.ENGLISH,
            publisher1,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre("Fiction")),
            200,
            "Some text");

    String anyEdition = "2nd Edition";
    Code anyCode = Code.fromIsbn10("1234567890");

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
    User anyUser = new User("coolGuy", contact2, profile2, userRole1);
    PublicationStatus anyPubStatus = PublicationStatus.READING;
    PublicationType anyPubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");


    Publication anyPublication = new Publication(anyBiblioData, anyPubType, anyUser, anyPubStatus, anyEdition, anyCode);


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
                List.of(new Genre("Fiction")),
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
        PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK,
                "Paperback");

        //Act
        Publication publication1 = new Publication(bibliographicalData1, publicationType, user1,
                pubStatus1, edition1, code1);

        //Assert
        assertNotNull(publication1);
    }

    @Test
    void shouldThrowExceptionIfBibliographicalDataNull() {

        //Arrange
        Country country1 = new Country("Denmark");
        Country country2 = new Country("Germany");


        Profile author1 = new Profile("Tove Ditlevsen", country1, LocalDate.of(1917, 12, 14));
        Address address1 = new Address("R Maria", "12", "156-5", country2);
        Email email1 = new Email("Maria1", "gmail.com");
        Contact contact1 = new Contact("915945695", "+351", email1, address1);
        Publisher publisher1 = new Publisher("Some Publisher", "PT123456789", contact1);
        BibliographicalData nullBiblioData = null;

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
        PublicationType publicationType1 = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK,
                "Paperback");
        PublicationStatus pubStatus1 = PublicationStatus.READING;


        //Act and Assert
        // a new publication is created with the parameters declared above with nullBiblioData
        assertThrows(IllegalArgumentException.class, () -> new Publication(nullBiblioData, publicationType1, user1,
                pubStatus1, edition1, code1));
    }


    @Test
    void shouldReturnEdition() {

        // Constantly copying an instance of Publication is too cumbersome, let's use the variables we created above and
        // just alter that which changes, much cleaner


        // Variable that actually changes
        String expectedEdition = "coolEdition";

        //Act and assert
        // calling upon the getEdition method here
        Publication publicationWantEdition = new Publication(anyBiblioData, anyPubType, anyUser,
                anyPubStatus, expectedEdition, anyCode);

        assertEquals(expectedEdition, publicationWantEdition.getEdition());
    }


    @Test
    void shouldReturnPublicationType() {

        //Reusing instance here, again, just changing the PublicationType
        PublicationType expectedPubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK,
                "Cool binding");

        //Act and assert
        // calling upon the getPublicationType method here
        Publication publicationWantPubType = new Publication(anyBiblioData, expectedPubType, anyUser,
                anyPubStatus, anyEdition, anyCode);

        assertEquals(expectedPubType, publicationWantPubType.getPublicationType());

    }

    //testing the "getSomething" methods will now follow the structure laid previously

    @Test
    void shouldReturnBibliographicalData() {

        //Arrange
        BibliographicalData expectedBibliographicalData = new BibliographicalData("The Name of the Wind",
                List.of(author1),
                Locale.ENGLISH,
                publisher1,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
                700,
                "Some text");

        //Act and assert
        Publication publicationWantBiblioData = new Publication(expectedBibliographicalData, anyPubType, anyUser,
                anyPubStatus, anyEdition, anyCode);

        assertEquals(expectedBibliographicalData, publicationWantBiblioData.getBibliographicalData());
    }

    @Test
    void shouldReturnCode() {

        //Arrange
        Code expectedCode = Code.fromIsbn13("123456789101");

        //Act
        Publication publicationWantCode = new Publication(anyBiblioData, anyPubType, anyUser,
                anyPubStatus, anyEdition, expectedCode);

        assertEquals(expectedCode, publicationWantCode.getCode());
    }

    //Testing the override methods

    @Test
    void equals_shouldReturnTrueForSameObject() {
        assertEquals(anyPublication, anyPublication);
    }

    @Test
    void equals_shouldReturnFalseWhenComparedToNull() {

        //Arrange
        Publication nullPublication = null;

        assertNotEquals(anyPublication,  nullPublication);
    }

    @Test
    void equals_shouldReturnFalseWhenComparedToNotPublications() {

        assertNotEquals(anyPublication, anyPubType);
        assertNotEquals(anyPublication, "this is not a publication");
    }

    @Test
    void equals_shouldReturnTrueForEqualPublications() {

        Publication publicationA = anyPublication;
        Publication publicationB = anyPublication;

        assertEquals(publicationA, publicationB);
        assertEquals(publicationB, publicationA); // testing symmetry
    }

    @Test
    void equals_shouldReturnFalseIfDifferentAttribute() {

        // differing fields here
        String goldEdition = "gold";
        String silverEdition = "silver";

        Publication publicationReading = new Publication(anyBiblioData, anyPubType,
                anyUser, anyPubStatus, goldEdition, anyCode);
        Publication publicationRead = new Publication(anyBiblioData, anyPubType,
                anyUser, anyPubStatus, silverEdition, anyCode);

        assertNotEquals(publicationReading, publicationRead);
    }

    @Test
    void hashCode_SameIfEqualObjects() {
        assertEquals(anyPublication.hashCode(), anyPublication.hashCode());
    }

    @Test
    void hashCode_NotEqualIfDifferentAttributes() {

        // differing fields
        Code cloudCode = Code.fromIsbn10("cloud12345");
        Code squallCode = Code.fromIsbn13("squall1234567");

        Publication publicationCloud = new Publication(anyBiblioData, anyPubType,
                anyUser, anyPubStatus, anyEdition, cloudCode);

        Publication publicationSquall = new Publication(anyBiblioData, anyPubType,
                anyUser, anyPubStatus, anyEdition, squallCode);

        //Assert
        assertNotEquals(publicationCloud.hashCode(), publicationSquall.hashCode());
    }


    // US023 Start -----------------------------------------------------------------------------------------------------
    // Tests to method getDetails()

    @Test
    void verifiesIfMethodGetDetailsReturnsNonNullRecord() {
        // Arrange
        Publication publication = new Publication(anyBiblioData, anyPubType, anyUser, anyPubStatus, anyEdition, anyCode);

        // Act
        Publication.PublicationDetails details = publication.getDetails();

        // Assert
        assertNotNull(details);
    }

    @Test
    void verifiesIfMethodGetDetailsReturnsCorrectValues() {
        // Arrange
        Publication publication = new Publication(anyBiblioData, anyPubType, anyUser, anyPubStatus, anyEdition, anyCode);

        // Act
        Publication.PublicationDetails details = publication.getDetails();

        // Assert
        assertEquals(anyBiblioData, details.bibliographicalData());
        assertEquals(anyPubType, details.publicationType());
        assertEquals(anyUser, details.userName());
        assertEquals(anyPubStatus, details.publicationStatus());
        assertEquals(anyEdition, details.edition());
        assertEquals(anyCode, details.code());
    }

    @Test
    void verifiesIfMethodGetDetailsHandlesNullableFields() {
        // Arrange
        Publication publication = new Publication(anyBiblioData, anyPubType, anyUser, anyPubStatus, null, null);

        // Act
        Publication.PublicationDetails details = publication.getDetails();

        // Assert
        assertNull(details.edition());
        assertNull(details.code());
    }

    // US023 End -------------------------------------------------------------------------------------------------------


}


