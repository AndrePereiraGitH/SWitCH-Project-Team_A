package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.repository.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class US020PublicListByGenreControllerTest {

    GenreRepository genreRepo = new GenreRepository(new ArrayList<>());
    CollectionOfPublicationsRepository collectionRepo =  new CollectionOfPublicationsRepository();
    US020PublicListByGenreController controller = new US020PublicListByGenreController(genreRepo, collectionRepo);

    @Test
    void shouldReturnAllGenresFromGenreRepository()
    {
        Genre genre1 = genreRepo.createAndAddGenre("Sci-Fi");
        Genre genre2 = genreRepo.createAndAddGenre("Action");
        Genre genre3 = genreRepo.createAndAddGenre("History");

        List<Genre> genreList = controller.getGenres();

        assertNotNull(genreList);
        assertEquals(3, genreList.size());
        assertEquals(genre1, genreList.get(0));
        assertEquals(genre2, genreList.get(1));
        assertEquals(genre3, genreList.get(2));
    }

    @Test
    void shouldReturnPublicListByGenre()
    {
        Genre desiredGenre = new Genre("Action");

        // User Creation
        Profile profile = new Profile("Joao",new Country("Portugal"), LocalDate.of(1974,7,24));
        Email email = new Email("john.doe25","hotmail.com");
        Address location = new Address("rua das pedras","29","4852-2524",new Country("Portugal"));
        PublicationType pubType = new PublicationType(TypeOfBinding.PAPERBACK, "publication test");
        Contact contact = new Contact("919402245", "+351", email, location);
        User userName = new User("John Doe", contact, profile, UserRole.NORMAL_USER);

        // Publication sample creation
        Profile authorProfile = new Profile("John Doe",new Country("Portugal"), LocalDate.of(1940,2,24));
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(authorProfile, "Author Biography"));
        Locale language = Locale.getDefault();
//        Email publisherEmail = new Email("publishing.inc", "gmail.com");
//        Address publisherLocation = new Address("Avenida das ruas","63","2525-524",new Country("Portugal"));
//        Contact publisherContact = new Contact("94839202", "+351", publisherEmail, publisherLocation);
//        Publisher publisher = new Publisher("Publishing Inc", "249412852", publisherContact);
        List<Genre> genre = new ArrayList<>();
        genre.add(new Genre("Sci-Fi"));
        LocalDate publishDate = LocalDate.now();

        BibliographicalData data = new BibliographicalData(
                "bibliographical data test",
                authors,
                language,
//                publisher,
                publishDate,
                genre,
//                405,
                "Sample text"
        );


        PublicationStatus pubStatus = PublicationStatus.FOR_SALE;
//        String edition = "4th Edition";
        // Edition
        Code code1 = Code.fromIsbn13 ("978316148412");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType1 = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition= new Edition("1st", code1, physicalAttributes, pubType1, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        Code code = Code.fromIsbn10("1234567890");

        Publication publicationSample = new Publication(data, pubType, userName, pubStatus, edition, code);

        Library privateLibrary = new Library("My Library", userName);


        // Creation of Collection of Publications

        CollectionOfPublications c1 = new CollectionOfPublications(
                "Collection1",
                new Genre("Action"),
//                privateLibrary,
                false,
                userName
        );

        CollectionOfPublications c2 = new CollectionOfPublications(
                "Collectioion2",
                new Genre("Action"),
//                privateLibrary,
                true,
                userName
        );

        CollectionOfPublications c3 = new CollectionOfPublications(
                "Collection3",
                new Genre("Sci-fi"),
//                privateLibrary,
                false,
                userName
        );

        CollectionOfPublications c4 = new CollectionOfPublications(
                "Collection4",
                new Genre("Action"),
//                privateLibrary,
                false,
                userName
        );


        collectionRepo.addPublicationToCollection(userName, privateLibrary, c1, publicationSample);
        collectionRepo.addPublicationToCollection(userName, privateLibrary, c2,publicationSample);
        collectionRepo.addPublicationToCollection(userName, privateLibrary, c3,publicationSample);
        collectionRepo.addPublicationToCollection(userName, privateLibrary, c4,publicationSample);

        collectionRepo.addCollectionToRepo(c1);
        collectionRepo.addCollectionToRepo(c2);
        collectionRepo.addCollectionToRepo(c3);
        collectionRepo.addCollectionToRepo(c4);


        assertEquals(2, controller.getPublicListsByGenre(desiredGenre).size()); // checks if the function correctly found the 2 public lists of the desired genre
    }

    @Test
    void shouldThrownWhenGenreIsNull()
    {
        assertThrows(IllegalArgumentException.class, () -> controller.getPublicListsByGenre(null)); // Checks if exception is thrown wehn genre is null
    }

}