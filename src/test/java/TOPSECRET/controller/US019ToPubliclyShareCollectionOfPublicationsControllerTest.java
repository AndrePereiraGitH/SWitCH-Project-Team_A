package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US019;
import TOPSECRET.repository.CollectionOfPublicationsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link US019ToPubliclyShareCollectionOfPublicationsController}.
 * <p>
 * These tests cover:
 * </p>
 * <ul>
 *   <li>Controller instantiation</li>
 *   <li>Retrieval of a user's collections as DTOs</li>
 *   <li>Input validation for null user and null collection DTO</li>
 *   <li>Sharing a collection request using a valid DTO</li>
 * </ul>
 */
class US019ToPubliclyShareCollectionOfPublicationsControllerTest {

        CollectionOfPublicationsRepository repo = new CollectionOfPublicationsRepository();
        US019ToPubliclyShareCollectionOfPublicationsController controller = new US019ToPubliclyShareCollectionOfPublicationsController(repo);

    /**
     * Verifies that the controller can be instantiated successfully.
     */
    @Test
    void testConstructor() {
        assertNotNull(controller);
    }

    /**
     * Verifies that requesting collections for a user with no stored collections returns an empty list of DTOs.
     */
    @Test
    void requestAllCollectionOfPublications_shouldReturn_EmptyListOfCollectionsDTO_ifThisUserHasNoCollections() {
        // arrange
        User owner = createUser("owner1");

        // act
        List<CollectionOfPublicationsDTO_US019> collections = controller.requestAllCollectionsOfPublicationsByUser(owner);

        // assert
        assertEquals(0, collections.size());

    }

    /**
     * Verifies that requesting collections for a valid user with existing collections returns a non-empty list of DTOs.
     */
    @Test
    void requestAllCollectionOfPublications_shouldReturn_ListOfCollectionsDTO_forValidUserWithCollections() {
        // arrange
        User owner = createUser("owner1");
        CollectionOfPublications collection = createCollectionOfPublications("My funny Collection", owner);
        repo.addCollectionToRepo(collection);

        // act
        List<CollectionOfPublicationsDTO_US019> result = controller.requestAllCollectionsOfPublicationsByUser(owner);

        // assert
        assertEquals(1, result.size());

    }

    /**
     * Verifies that requesting collections with a null user throws {@link IllegalArgumentException}
     * with the expected message.
     */
    @Test
    void requestAllCollectionOfPublications_shouldReturn_shouldThrow_IfUserIsEmpty() {
        // act + assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.requestAllCollectionsOfPublicationsByUser(null));

        assertEquals("User cannot be null", exception.getMessage());

    }

    /**
     * Verifies that sharing a collection using a valid DTO returns {@code true}.
     *
     * @throws InstanceNotFoundException if the DTO does not match any existing collection (not expected in this test)
     */
    @Test
    void shareCollectionOfPublications_shouldReturnTrue_IfCollectionOfPublicationsBecomesPublic() throws InstanceNotFoundException {
        // arrange
        User owner = createUser("owner1");
        CollectionOfPublications collection = createCollectionOfPublications("My funny Collection", owner);
        repo.addCollectionToRepo(collection);

        List<CollectionOfPublicationsDTO_US019> collectionListDTO = controller.requestAllCollectionsOfPublicationsByUser(owner);

        // act
        boolean result = controller.shareCollectionOfPublications(collectionListDTO.get(0));

        // assert
        assertTrue(result);
    }

    /**
     * Verifies that sharing a collection with a null DTO throws {@link IllegalArgumentException}
     * with the expected message.
     */
    @Test
    void shareCollectionOfPublications_shouldThrows_ifCollectionProvidedIdNull() {

        // act + assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.shareCollectionOfPublications(null));

        // assert
        assertEquals("you must send a collection to share", exception.getMessage());
    }



    private CollectionOfPublications createCollectionOfPublications(String collectionName, User user) {
        return new CollectionOfPublications(
                collectionName,
                new Genre("Fiction"),
                user
        );
    }

    private User createUser(String username) {
        return new User(
                username,
                createContact(),
                createProfile(username),
                UserRole.NORMAL_USER
                );
    }

    private Contact createContact() {
        return new Contact(
                "111111111",
                "+351",
                new Email("user", "example.com"),
                createAddress()

        );
    }

    private Address createAddress() {
        return new Address(
                "street xpto",
                "234A",
                "234567",
                new Country("PORTUGAL")
        );
    }

    private Profile createProfile(String name) {
        return new Profile(
                name,
                new Country("PORTUGAL"),
                LocalDate.of(1989, 9, 1)
        );
    }


}