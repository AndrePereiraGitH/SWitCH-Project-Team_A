package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class US018ToAddPublicationToPrivateListControllerTest {

    private US018ToAddPublicationToPrivateListController controller;

    @BeforeEach
    void setup() {
        controller = new US018ToAddPublicationToPrivateListController();
    }

    // HELPER
    private User createValidUser() {

        Profile profile = new Profile(
                "Test User",
                new Country("PORTUGAL"),
                LocalDate.of(1990, 1, 1)
        );

        return new User(
                "validUser",
                new Contact("912345678", "351", null, null),
                profile,
                UserRole.ADMIN
        );
    }

    private Publication createValidPublication(User user) {

        Profile authorProfile = new Profile(
                "Author Name",
                new Country("PORTUGAL"),
                LocalDate.of(1980, 1, 1)
        );

        Author author = new Author(
                authorProfile,
                "Short biography"
        );

        Publisher publisher = new Publisher(
                "Test Publisher",
                "PT123456789",
                new Contact("912999999", "351", null, null)
        );

        BibliographicalData bibliographicalData = new BibliographicalData(
                "Test Title",
                List.of(author),
                Locale.ENGLISH,
//                publisher,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
//                200,
                "Sample text"
        );

        PublicationType publicationType = new PublicationType(
                TypeOfBinding.HARDBACK,
                "Standard publication"
        );

        // Edition
        Code code = Code.fromIsbn13 ("978316148412");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher1 = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition= new Edition(
                "1st",
                code,
                physicalAttributes,
                pubType,
                publisher1,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        return new Publication(
                bibliographicalData,
                publicationType,
                user,
                PublicationStatus.READING,
//                "First Edition",
                edition,
                Code.fromIsbn13("9783161484100")
        );
    }

    private CollectionOfPublications createValidCollection(Library library, User user) {
        return new CollectionOfPublications(
                "Favorites",
                new Genre("Fiction"),
                user
        );
    }



    // SUCCESS TEST
    @Test
    void shouldAddPublicationSuccessfully() {

        // ARRANGE
        User user = createValidUser();                                  // Create valid user
        Publication publication = createValidPublication(user);         // Create valid publication
        user.getLibrary().addPublication(publication);                  // Add publication to user's library
        CollectionOfPublications collection = createValidCollection(user.getLibrary(), user);           // Create collection
        user.addCollection(collection);                                 // User owns the collection

        // ACT
        boolean result = controller.addPublicationToPrivateList(user, collection, publication);

        // ASSERT
        assertTrue(result);                                          // Publication should be added successfully
        assertTrue(collection.containsPublication(publication));    // Collection should contain the publication
    }

    // ERROR TESTS
    @Test
    void shouldReturnFalseWhenUserIsNull() {

        // ARRANGE AND ACT
        boolean result = controller.addPublicationToPrivateList(null, null, null);

        // ASSERT
        assertFalse(result, "User can´t be null");                                         // Null parameters are not allowed
    }

    @Test
    void shouldReturnFalseWhenCollectionDoesNotBelongToUser() {

        // ARRANGE
        User user = createValidUser();
        Publication publication = createValidPublication(user);

        user.getLibrary().addPublication(publication);               // Publication exists in library

        CollectionOfPublications collection = createValidCollection(user.getLibrary(), user);           // Collection NOT owned by user

        // ACT
        boolean result = controller.addPublicationToPrivateList(user, collection, publication);

        // ASSERT
        assertFalse(result);                                         // User does not own the collection
    }

    @Test
    void shouldReturnFalseWhenPublicationIsNotInLibrary() {

        // ARRANGE
        User user = createValidUser();
        Publication publication = createValidPublication(user);

        CollectionOfPublications collection = createValidCollection(user.getLibrary(), user);

        user.addCollection(collection);                              // User owns the collection

        // ACT
        boolean result = controller.addPublicationToPrivateList(user, collection, publication);

        // ASSERT
        assertFalse(result);                                         // Publication must exist in user's library
    }

    @Test
    void shouldReturnFalseWhenPublicationAlreadyExistsInCollection() {

        // ARRANGE
        User user = createValidUser();
        Publication publication = createValidPublication(user);

        user.getLibrary().addPublication(publication);

        CollectionOfPublications collection = createValidCollection(user.getLibrary(), user);

        user.addCollection(collection);

        controller.addPublicationToPrivateList(user, collection, publication);                      // First add succeeds

        // ACT
        boolean result = controller.addPublicationToPrivateList(user, collection, publication);     // Try to add again

        // ASSERT
        assertFalse(result);                                         // Duplicate publication is not allowed
    }
}

