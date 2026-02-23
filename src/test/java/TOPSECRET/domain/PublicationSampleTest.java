package TOPSECRET.domain;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.ENGLISH;
import static org.junit.jupiter.api.Assertions.*;

class PublicationSampleTest {

    //Writing the variables for testing here instead of repeating them

    //Arrange BibliographicalData


    String title = "A Book";


    Country country = new Country("UNITED_STATES");
    Profile profile2 = new Profile("Henry A Kissinger", country, LocalDate.of(1923, 05, 27));
    Profile profile3 = new Profile("Eric Schmidt III", country, LocalDate.of(1955, 04, 27));
    Profile profile4 = new Profile("Daniel Huttenlocher", country, LocalDate.of(1959, 01, 01));
    Author author2 = new Author(profile2, "valid bio2");
    Author author3 = new Author(profile3, "valid bio3");
    Author author4 = new Author(profile4, "valid bio4");


    List<Author> authors = List.of(author2, author3, author4);


    Locale language = ENGLISH;
    LocalDate publishDate = LocalDate.of(2022, 8, 4);
    Genre genre1 = new Genre("Fiction");
    List<Genre> genres = List.of(genre1);
    String chunksOfText = "The sweet spot is in partnership—AI handles scale, I bring empathy, wisdom, and context.";

    BibliographicalData anyBd = new BibliographicalData(title, authors, language, publishDate, genres,
            chunksOfText);


    //Arrange User

    Address address1 = new Address("Main Street", "10", "12345", new Country("PORTUGAL"));
    Email email1 = new Email("test.user", "example.com");
    Contact contact1 = new Contact("987654321", "351", email1, address1);

    Profile profile5 = new Profile(
            "Test Profile",
            new Country("PORTUGAL"),
            LocalDate.of(1997, 8, 15)
    );

    User anyOwner = new User("ValidName", contact1, profile5, UserRole.NORMAL_USER);


    //Arrange PublicationStatus

    PublicationStatus anyPubStatus = PublicationStatus.READING;


    //Arrange Edition

    String editionNumber1 = "1st edition";
    Code code1 = Code.fromIsbn13("0123456789012");
    Dimensions dimensions1 = new Dimensions(4.5, 5.4, 2.3);
    PhysicalAttributes physicalAttributes1 = new PhysicalAttributes(dimensions1, 0.8, 250);
    PublicationType pubType1 = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Cool cover");
    Address address2 = new Address("Not Main Street", "11", "12346", new Country("SPAIN"));
    Email email2 = new Email("test.user2", "example2.com");
    Contact contact2 = new Contact("987438239", "351", email2, address2);
    Publisher publisher1 = new Publisher("Cool publisher", "MT1234567", contact2);
    Locale editionLanguage1 = Locale.JAPANESE;
    LocalDate editionDate1 = LocalDate.of(2015, 1, 12);

    Edition anyEdition = new Edition(editionNumber1, code1, physicalAttributes1, pubType1, publisher1,
            editionLanguage1, editionDate1);

    //Arrange Condition

    Condition anyCondition = Condition.GOOD;

    PublicationSample anySample = new PublicationSample(anyBd, anyOwner, anyPubStatus, anyEdition, anyCondition);


    @Test
    void shouldCreatePublicationSampleWithValidData() {

        //Arrange has been done above for valid attributes

        //Act
        PublicationSample validPubSample = new PublicationSample(anyBd, anyOwner, anyPubStatus,
                anyEdition, anyCondition);

        //Assert
        assertNotNull(validPubSample);

    }

    @Test
    void shouldThrowExceptionIfBiblioDataIsNull() {

        //Arrange biblioData that is null
        BibliographicalData nullBiblioData = null;

        //Act + assert
        assertThrows(IllegalArgumentException.class, () -> new PublicationSample(nullBiblioData, anyOwner, anyPubStatus,
                anyEdition, anyCondition));

    }

    @Test
    void shouldThrowExceptionIfOwnerIsEmpty() {

        //Arrange Owner that is null
        User nullOwner = null;

        //Act + assert
        assertThrows(IllegalArgumentException.class, () -> new PublicationSample(anyBd, nullOwner, anyPubStatus,
                anyEdition, anyCondition));

    }

    @Test
    void shouldThrowExceptionIfPubStatusIsNull() {

        //Arrange PubStatus that is null
        PublicationStatus nullPubStatus = null;

        //Act + assert
        assertThrows(IllegalArgumentException.class, () -> new PublicationSample(anyBd, anyOwner, nullPubStatus,
                anyEdition, anyCondition));

    }

    @Test
    void shouldThrowExceptionIfEditionIsNull() {

        //Arrange Edition that is null
        Edition nullEdition = null;

        //Act + assert
        assertThrows(IllegalArgumentException.class, () -> new PublicationSample(anyBd, anyOwner, anyPubStatus,
                nullEdition, anyCondition));

    }

    @Test
    void shouldThrowExceptionIfConditionIsNull() {

        //Arrange Condition that is null
        Condition nullCondition = null;

        //Act + assert
        assertThrows(IllegalArgumentException.class, () -> new PublicationSample(anyBd, anyOwner, anyPubStatus,
                anyEdition, nullCondition));
    }

    @Test
    void shouldReturnBiblioData() {

        //Arrange
        PublicationSample wantBiblioData = new PublicationSample(anyBd, anyOwner, anyPubStatus,
                anyEdition, anyCondition);

        //Act + assert
        assertEquals(anyBd, wantBiblioData.getBibliographicalData());

    }

    @Test
    void shouldReturnOwner() {

        //Arrange
        PublicationSample wantOwner = new PublicationSample(anyBd, anyOwner, anyPubStatus,
                anyEdition, anyCondition);

        //Act + assert
        assertEquals(anyOwner, wantOwner.getOwner());

    }

    @Test
    void shouldReturnPubStatus() {

        //Arrange
        PublicationSample wantPubStatus = new PublicationSample(anyBd, anyOwner, anyPubStatus,
                anyEdition, anyCondition);

        //Act + assert
        assertEquals(anyPubStatus, wantPubStatus.getPublicationStatus());

    }

    @Test
    void shouldReturnEdition() {

        //Arrange
        PublicationSample wantEdition = new PublicationSample(anyBd, anyOwner, anyPubStatus,
                anyEdition, anyCondition);

        //Act + assert
        assertEquals(anyEdition, wantEdition.getEdition());

    }

    //Testing override methods

    @Test
    void equals_shouldReturnTrueForSameObject() {
        assertEquals(anySample, anySample);
    }

    @Test
    void equals_shouldReturnFalseWhenComparedToNull() {

        //Arrange
        PublicationSample nullSample = null;

        assertNotEquals(anySample, nullSample);

    }

    @Test
    void equals_shouldReturnFalseWhenComparedToNotPublicationSamples() {

        assertNotEquals(anySample, anyPubStatus);
        assertNotEquals(anySample, "Wait, this isn't a sample!");

    }

    @Test
    void equals_shouldReturnTrueForEqualSamples() {

        PublicationSample sampleA = anySample;
        PublicationSample sampleB = anySample;

        assertEquals(sampleA, sampleB);
        assertEquals(sampleB, sampleA); // testing symmetry

    }

    @Test
    void equals_shouldReturnFalseIfDifferentAttribute() {

        //Arrange, Condition is the differing field
        Condition goldCondition = Condition.NEW;
        Condition silverCondition = Condition.LIKE_NEW;

        PublicationSample goldSample = new PublicationSample(anyBd, anyOwner, anyPubStatus,
                anyEdition, goldCondition);

        PublicationSample silverSample = new PublicationSample(anyBd, anyOwner, anyPubStatus,
                anyEdition, silverCondition);

        //Assert
        assertNotEquals(goldSample, silverSample);

    }

    @Test
    void hashCode_SameIfEqualObjects() {
        assertEquals(anySample.hashCode(), anySample.hashCode());
    }

    @Test
    void hashCode_NotEqualIfDifferentAttributes() {

        //Arrange, User is the differing field
        Address address1 = new Address("Sunset Boulevard", "10", "54321", new Country
                ("UNITED_STATES"));
        Email email1 = new Email("iamjohn.user", "doe.com");
        Contact contact1 = new Contact("873839332", "+1", email1, address1);

        Profile profile5 = new Profile(
                "John Doe Profile",
                new Country("UNITED_STATES"),
                LocalDate.of(1996, 9, 16)
        );

        User johnDoe = new User("John Doe", contact1, profile5, UserRole.NORMAL_USER);

        PublicationSample doeSample = new PublicationSample(anyBd, johnDoe, anyPubStatus,
                anyEdition, anyCondition);

        //Assert
        assertNotEquals(anySample.hashCode(), doeSample.hashCode());


    }

}