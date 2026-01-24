package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class US020PublicListByGenreControllerTest {

    @BeforeEach
    void resetRepository() {
        GenreRepository.getINSTANCE().clear();
    }

    @Test
    void shouldReturnAllGenresFromGenreRepository()
    {
        Genre genre1 = GenreRepository.getINSTANCE().createAndAddGenre("Sci-Fi");
        Genre genre2 = GenreRepository.getINSTANCE().createAndAddGenre("Action");
        Genre genre3 = GenreRepository.getINSTANCE().createAndAddGenre("History");

        List<Genre> genreList = GenreRepository.getINSTANCE().getAllGenres();

        assertNotNull(genreList);
        assertEquals(3, genreList.size());
        assertEquals(genre1, genreList.get(0));
        assertEquals(genre2, genreList.get(1));
        assertEquals(genre3, genreList.get(2));
    }

    @Test
    void shouldReturnPublicListByGenre()
    {
        CollectionOfPublicationsRepository repo = new CollectionOfPublicationsRepository();

        Genre desiredGenre = new Genre("Action");

        List<Publication> pubs = List.of();

        // User Creation
        Profile profile = new Profile("Joao",new Country("Portugal"), LocalDate.of(1974,7,24));
        Email email = new Email("john.doe25","hotmail.com");
        Address location = new Address("rua das pedras","29","4852-2524",new Country("Portugal"));
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "publication test");
        Contact contact = new Contact("919402245", "+351", email, location);
        User userName = new User("John Doe", contact, profile, UserRole.NORMAL_USER);

        // Publication sample creation
        Profile authorProfile = new Profile("John Doe",new Country("Portugal"), LocalDate.of(1940,2,24));
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(authorProfile, "Author Biography"));
        Locale language = Locale.getDefault();
        Email publisherEmail = new Email("publishing.inc", "gmail.com");
        Address publisherLocation = new Address("Avenida das ruas","63","2525-524",new Country("Portugal"));
        Contact publisherContact = new Contact("94839202", "+351", publisherEmail, publisherLocation);
        Publisher publisher = new Publisher("Publishing Inc", "249412852", publisherContact);
        List<Genre> genre = new ArrayList<>();
        genre.add(new Genre("Sci-Fi"));
        LocalDate publishDate = LocalDate.now();

        BibliographicalData data = new BibliographicalData(
                "bibliographical data test",
                authors,
                language,
                publisher,
                publishDate,
                genre,
                405,
                "Sample text"
        );


        PublicationStatus pubStatus = PublicationStatus.FOR_SALE;
        String edition = "4th Edition";
        Code code = Code.fromIsbn10("1234567890");

        Publication publicationSample = new Publication(data, pubType, userName, pubStatus, edition, code);

        Library privateLibrary = new Library("My Library", userName);

        privateLibrary.addPublication(publicationSample);


        // Creation of Collection of Publications

        CollectionOfPublications c1 = new CollectionOfPublications(
                "Collection1",
                new Genre("Action"),
                privateLibrary,
                false,
                userName
        );

        CollectionOfPublications c2 = new CollectionOfPublications(
                "Collectioion2",
                new Genre("Action"),
                privateLibrary,
                true,
                userName
        );

        CollectionOfPublications c3 = new CollectionOfPublications(
                "Collection3",
                new Genre("Sci-fi"),
                privateLibrary,
                false,
                userName
        );

        CollectionOfPublications c4 = new CollectionOfPublications(
                "Collection4",
                new Genre("Action"),
                privateLibrary,
                false,
                userName
        );



        repo.addPublicationToCollection(userName,c1,publicationSample);
        repo.addPublicationToCollection(userName,c2,publicationSample);
        repo.addPublicationToCollection(userName,c3,publicationSample);
        repo.addPublicationToCollection(userName,c4,publicationSample);

        repo.addCollectionToRepo(c1);
        repo.addCollectionToRepo(c2);
        repo.addCollectionToRepo(c3);
        repo.addCollectionToRepo(c4);


        assertEquals(2, repo.findPublicByGenre(desiredGenre).size()); // checks if the function correctly found the 2 public lists of the desired genre
        assertThrows(IllegalArgumentException.class, () -> repo.findPublicByGenre(null)); // Checks if exception is thrown wehn genre is null

    }

}