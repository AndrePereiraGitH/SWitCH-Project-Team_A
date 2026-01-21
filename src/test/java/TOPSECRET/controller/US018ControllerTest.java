//package TOPSECRET.controller;
//
//import TOPSECRET.domain.*;
//import TOPSECRET.domain.Enum.Genre;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class US018ControllerTest {
//
//    private US018Controller controller;
//    private User user;
//    private CollectionOfPublications collectionOfPublications;
//    private Publication publication;
//
//    // Runs before each test to ensure clean state
//    @BeforeEach
//    void setup() {
//        controller = new US018Controller();
//
//        user = new User("User One");              // Adjust constructor if needed
//        collectionOfPublications = new CollectionOfPublications("Favorites", Genre.FICTION);
//        publication = new Book();                // Adjust constructor if needed
//
//        // Prepare user state
//        user.addPrivateList(collectionOfPublications);
//        user.addPublicationToLibrary(publication);
//    }
//
//    // =========================
//    // SUCCESS SCENARIO
//    // =========================
//
//    // Verifies successful addition of publication to private list
//    @Test
//    void shouldAddPublicationToPrivateListSuccessfully() {
//
//        // -------- Arrange --------
//        int expectedInitialSize = 0;
//
//        // -------- Act --------
//        boolean result =
//                controller.addPublicationToMyList(user, collectionOfPublications, publication);
//
//        List<Publication> storedPublications =
//                collectionOfPublications.getPublications();
//
//        // -------- Assert --------
//        assertTrue(result);
//        assertEquals(expectedInitialSize + 1, storedPublications.size());
//        assertTrue(storedPublications.contains(publication));
//    }
//
//    // =========================
//    // FAILURE SCENARIOS
//    // =========================
//
//    // Verifies failure when publication does not belong to user's library
//    @Test
//    void shouldFailWhenPublicationIsNotInUserLibrary() {
//
//        // -------- Arrange --------
//        Publication foreignPublication = new Book();
//
//        // -------- Act --------
//        boolean result =
//                controller.addPublicationToMyList(user, collectionOfPublications, foreignPublication);
//
//        List<Publication> storedPublications =
//                collectionOfPublications.getPublications();
//
//        // -------- Assert --------
//        assertFalse(result);
//        assertTrue(storedPublications.isEmpty());
//    }
//
//    // Verifies failure when private list does not belong to user
//    @Test
//    void shouldFailWhenPrivateListDoesNotBelongToUser() {
//
//        // -------- Arrange --------
//        User anotherUser = new User("Another User");
//
//        // -------- Act --------
//        boolean result =
//                controller.addPublicationToMyList(anotherUser, collectionOfPublications, publication);
//
//        List<Publication> storedPublications =
//                collectionOfPublications.getPublications();
//
//        // -------- Assert --------
//        assertFalse(result);
//        assertTrue(storedPublications.isEmpty());
//    }
//
//    // Verifies failure when adding duplicate publication
//    @Test
//    void shouldFailWhenAddingDuplicatePublication() {
//
//        // -------- Arrange --------
//        controller.addPublicationToMyList(user, collectionOfPublications, publication);
//
//        // -------- Act --------
//        boolean result =
//                controller.addPublicationToMyList(user, collectionOfPublications, publication);
//
//        List<Publication> storedPublications =
//                collectionOfPublications.getPublications();
//
//        // -------- Assert --------
//        assertFalse(result);
//        assertEquals(1, storedPublications.size());
//        assertTrue(storedPublications.contains(publication));
//    }
//}
