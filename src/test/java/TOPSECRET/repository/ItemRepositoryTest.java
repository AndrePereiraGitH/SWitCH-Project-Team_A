package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.domain.Genre;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ItemRepositoryTest {

    @Test
    void emptyConstructorShouldCreateAnEmptyList() {
        //arrange + act
        ItemRepository repo = new ItemRepository();

        //assert
        assertNotNull(repo);
        assertEquals(0,repo.get_Items().size());

    }

    @Test
    void shouldAddNewItemToListOfItems() {
        // arrange
        ItemRepository repo = new ItemRepository();
        Dimensions dimensions = new Dimensions(10.3, 15.0, 4);
        Publication publication = createValidPublication();

        // act
        repo.createAndAddItem(Condition.LIKE_NEW, 1.230, dimensions, publication);

        // assert
        assertEquals(1, repo.get_Items().size());

    }

    // =======================================≃
    // ========= Auxiliary Methods ============
    // ========================================

    private Publication createValidPublication() {
        return new Publication(instantiateBibliographicalData(), crestePublicationType(), instantiateUser("Rodolph"), PublicationStatus.READING, "2", Code.fromIsbn13("gkgahgsdhjdgkjsag"));
    }

    private static User instantiateUser(String name) {
        Email email = new Email("switcher", "example.com");
        Address add = new Address("Rua x", "12B", "4444-111", new Country("PORTUGAL"));
        Contact contact = new Contact("911111111", "351", email, add);
        LocalDate birthday = LocalDate.of(1982, 12, 3);
        Profile profile = new Profile("Albert Trump", new Country("EL_SALVADOR"), birthday);
        UserRole userRole = UserRole.NORMAL_USER;
        return new User(name, contact, profile,userRole);
    }

    private static PublicationType crestePublicationType() {
        return new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "some details");
    }

    private static BibliographicalData instantiateBibliographicalData() {

        Profile authorProfile = new Profile("J. R. R. Tolkien", new Country("UNITED_KINGDOM"), LocalDate.of(1892, 1, 3));

        Author author = new Author(authorProfile, "I'm an author");

        List<Author> authors = new ArrayList<>();
        authors.add(author);


        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("FANTASY"));
        genres.add(new Genre("MYSTERY"));

        String chunkOfText =
                "Continuing the story begun in The Hobbit, all three parts of the epic masterpiece, " +
                        "The Lord of the Rings, in one paperback. Features the definitive edition of the text, " +
                        "fold-out flaps with the original two-colour maps, and a revised and expanded index.";

        Contact contact = new Contact();
        Publisher publisher = new Publisher("HarperCollins Publishers", "1111111111", contact);

        return new BibliographicalData(
                "The Lord Of The Rings",
                authors,
                Locale.ENGLISH,
                publisher,
                LocalDate.of(1991, 11, 1),
                genres,
                700,
                chunkOfText
        );
    }


}
