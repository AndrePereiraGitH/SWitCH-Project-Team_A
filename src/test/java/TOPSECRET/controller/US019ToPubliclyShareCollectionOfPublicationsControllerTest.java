package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US019;
import TOPSECRET.repository.CollectionOfPublicationsRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class US019ToPubliclyShareCollectionOfPublicationsControllerTest {

    @Test
    void testConstructor() {
        CollectionOfPublicationsRepository repo = new CollectionOfPublicationsRepository();
        US019ToPubliclyShareCollectionOfPublicationsController controller = new US019ToPubliclyShareCollectionOfPublicationsController(repo);

        assertNotNull(controller);
    }

    @Test
    void requestAllCollectionOfPublications_shouldReturn_EmptyListOfCollectionsDTO_ifThisUserHasNoCollections() {
        // arrange
        CollectionOfPublicationsRepository repo = new CollectionOfPublicationsRepository();
        US019ToPubliclyShareCollectionOfPublicationsController controller = new US019ToPubliclyShareCollectionOfPublicationsController(repo);
        User owner = createUser("owner1");

        // act
        List<CollectionOfPublicationsDTO_US019> collections = controller.requestAllCollectionsOfPublicationsByUser(owner);

        // assert
        assertEquals(0, collections.size());

    }

    @Test
    void requestAllCollectionOfPublications_shouldReturn_ListOfCollectionsDTO_forValidUserWithCollections() {
        // arrange
        CollectionOfPublicationsRepository repo = new CollectionOfPublicationsRepository();
        US019ToPubliclyShareCollectionOfPublicationsController controller = new US019ToPubliclyShareCollectionOfPublicationsController(repo);
        User owner = createUser("owner1");
        CollectionOfPublications collection = createCollectionOfPublications("My funny Collection", owner);
        repo.addCollectionToRepo(collection);

        // act
        List<CollectionOfPublicationsDTO_US019> result = controller.requestAllCollectionsOfPublicationsByUser(owner);

        // assert
        assertEquals(1, result.size());

    }

    @Test
    void requestAllCollectionOfPublications_shouldReturn_shouldThrow_IfUserIsEmpty() {
        // arrange
        CollectionOfPublicationsRepository repo = new CollectionOfPublicationsRepository();
        US019ToPubliclyShareCollectionOfPublicationsController controller = new US019ToPubliclyShareCollectionOfPublicationsController(repo);

        // act + assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.requestAllCollectionsOfPublicationsByUser(null));

        assertEquals("User cannot be null", exception.getMessage());

    }


    private CollectionOfPublications createCollectionOfPublications(String collectionName, User user) {
        return new CollectionOfPublications(
                collectionName,
                new Genre("Fiction"),
                true,
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