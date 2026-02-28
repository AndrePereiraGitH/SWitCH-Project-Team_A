package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class ItemRepositoryTest {
//
//    @Test
//    void emptyConstructorShouldCreateAnEmptyList() {
//        //arrange + act
//        ItemRepository repo = new ItemRepository();
//
//        //assert
//        assertNotNull(repo);
//        assertEquals(0,repo.get_Items().size());
//
//    }
//
//    @Test
//    void should_add_new_item_to_listOfItems() throws InstanceAlreadyExistsException {
//        // arrange
//        ItemRepository repo = new ItemRepository();
//        Dimensions dimensions = new Dimensions(10.3, 15.0, 4);
//        Publication publication = createValidPublication();
//
//        // act
//        repo.createAndAddItem(Condition.LIKE_NEW, 1.230, dimensions, publication);
//
//        // assert
//        assertEquals(1, repo.get_Items().size());
//
//    }
//
//    @Test
//    void should_throw_if_item_already_exists() throws InstanceAlreadyExistsException {
//
//        //arrange
//        ItemRepository repo = new ItemRepository();
//        Dimensions dimensions = new Dimensions(10.3, 15.0, 4);
//        Publication publication = createValidPublication();
//        repo.createAndAddItem(Condition.LIKE_NEW, 1.230, dimensions, publication);
//
//        assertNotNull(repo.get_Items());
//
//        assertThrows(InstanceAlreadyExistsException.class, () -> repo.createAndAddItem(Condition.LIKE_NEW, 1.230, dimensions, publication));
//
//    }
//
//    @Test
//    void createAndAddItem_should_return_the_created_item_and_it_should_be_in_repo() throws InstanceAlreadyExistsException {
//        // arrange
//        ItemRepository repo = new ItemRepository();
//        Dimensions dimensions = new Dimensions(10.3, 15.0, 4);
//        Publication publication = createValidPublication();
//
//        // act
//        Item created = repo.createAndAddItem(Condition.LIKE_NEW, 1.230, dimensions, publication);
//
//        // assert
//        assertNotNull(created);
//        assertTrue(repo.get_Items().contains(created));
//    }
//
//
//    // =======================================≃
//    // ========= Auxiliary Methods ============
//    // ========================================
//
//    // Edition
//    Code code = Code.fromIsbn13 ("9783161484123");
//    Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
//    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
//    PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
//    Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
//    Edition edition= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
//
//    private Publication createValidPublication() {
////        return new Publication(instantiateBibliographicalData(), crestePublicationType(), instantiateUser("Rodolph"), PublicationStatus.READING, "2", Code.fromIsbn13("gkgahgsdhjdgkjsag"));
//        return new Publication(instantiateBibliographicalData(), crestePublicationType(), instantiateUser("Rodolph"), PublicationStatus.READING, edition, Code.fromIsbn13("9781234567897"));
//    }
//
//    private static User instantiateUser(String name) {
//        Email email = new Email("switcher", "example.com");
//        Address add = new Address("Rua x", "12B", "4444-111", new Country("PORTUGAL"));
//        Contact contact = new Contact("911111111", "351", email, add);
//        LocalDate birthday = LocalDate.of(1982, 12, 3);
//        Profile profile = new Profile("Albert Trump", new Country("EL_SALVADOR"), birthday);
//        UserRole userRole = UserRole.NORMAL_USER;
//        return new User(name, contact, profile,userRole);
//    }
//
//    private static PublicationType crestePublicationType() {
//        return new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "some details");
//    }
//
//    private static BibliographicalData instantiateBibliographicalData() {
//
//        Profile authorProfile = new Profile("J. R. R. Tolkien", new Country("UNITED_KINGDOM"), LocalDate.of(1892, 1, 3));
//
//        Author author = new Author(authorProfile, "I'm an author");
//
//        List<Author> authors = new ArrayList<>();
//        authors.add(author);
//
//
//        List<Genre> genres = new ArrayList<>();
//        genres.add(new Genre("FANTASY"));
//        genres.add(new Genre("MYSTERY"));
//
//        String chunkOfText =
//                "Continuing the story begun in The Hobbit, all three parts of the epic masterpiece, " +
//                        "The Lord of the Rings, in one paperback. Features the definitive edition of the text, " +
//                        "fold-out flaps with the original two-colour maps, and a revised and expanded index.";
//
////        Contact contact = new Contact();
////        Publisher publisher = new Publisher("HarperCollins Publishers", "1111111111", contact);
//
//        return new BibliographicalData(
//                "The Lord Of The Rings",
//                authors,
//                Locale.ENGLISH,
////                publisher,
//                LocalDate.of(1991, 11, 1),
//                genres,
////                700,
//                chunkOfText
//        );
//    }

}
