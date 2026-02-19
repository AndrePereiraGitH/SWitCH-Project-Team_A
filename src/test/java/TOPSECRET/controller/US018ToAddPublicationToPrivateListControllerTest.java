package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

class US018ToAddPublicationToAListOfMineControllerTest {

    private US018ToAddPublicationToAListOfMineController controller;

    @BeforeEach
    void setup() {
        controller = new US018ToAddPublicationToAListOfMineController();
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

    private CollectionOfPublications createValidCollection (Library library, User user) {
        return new CollectionOfPublications(
                "Favorites",
                new Genre("Fiction"),
                user
        );
    }

}

