package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.dto.PublicationDTO;
import TOPSECRET.repository.LibraryRepository;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GetListOfPublicationsInLibraryControllerTest {



    private Map<String, Library> publicationList = new HashMap<>();

    private LibraryRepository libraryRepo = new LibraryRepository(publicationList);

    private GetListOfPublicationsInLibraryController ctrl = new GetListOfPublicationsInLibraryController(libraryRepo);

    // BibliographicalData
    Country country1 = new Country("Denmark");
    Country country2 = new Country("Germany");


    Profile profileAuthor1 = new Profile("Tove Ditlevsen", country1, LocalDate.of(1917, 12, 14));
    Author author1 = new Author(profileAuthor1, "valid bio1");
    Address address1 = new Address("R Maria", "12", "156-5", country2);
    Email email1 = new Email("Maria1", "gmail.com");
    Contact contact1 = new Contact("915945695", "+351", email1, address1);
    Publisher publisher1 = new Publisher("Some Publisher", "PT123456789", contact1);
    BibliographicalData bibliographicalData1 = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author1),
            Locale.ENGLISH,
            publisher1,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre ("Fiction")),
            200,
            "Some text");

    // User
    Country country4 = new Country("Uganda");
    Profile profile2 = new Profile(
            "Test Profile",
            country4,
            LocalDate.of(1997, 8, 15)
    );


    UserRole userRole1 = UserRole.ADMIN;
    User user1 = new User("coolGuy", contact1, profile2, userRole1);

    //PublicationStatus
    PublicationStatus pubStatus1 = PublicationStatus.READING;

    //Edition
    String edition1 = "2nd Edition";

    //Code
    Code code1 = Code.fromIsbn10("1234567890");
    Code code2 = Code.fromIsbn10("1234567899");

    //PublicationType
    PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");


    Publication publication1 = new Publication(bibliographicalData1, publicationType, user1,
            pubStatus1, edition1, code1);

    Publication publication2= new Publication(bibliographicalData1, publicationType, user1,
            pubStatus1, edition1, code2);



//    @Test
//    void getUserLibraryIfItExists() throws InstanceNotFoundException {
//        // Arrange
//        String username = "testUser";
//
//        // Act
//        Library lib = ctrl.getUserLibrary(username);
//
//        // Assert
//        assertNotNull(lib);
//        assertTrue(publicationList.containsKey(username));
//    }


    @Test
    void getUserLibraryNullUserThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            ctrl.getUserLibrary(null);
        });
    }

    @Test
    void getUserLibraryEmptyUserThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            ctrl.getUserLibrary("");
        });
    }

//    @Test
//    void getListOfPublicationsInLibrary_EmptyLibraryReturnsEmptyList() throws InstanceNotFoundException {
//        // Arrange
//        String username = "user";
//        ctrl.getUserLibrary(username);
//
//        // Act
//        List<PublicationDTO> result = ctrl.getListOfPublicationsInLibrary(username);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }

//    @Test
//    void getListOfPublicationsInLibraryShouldReturnsDTOs() throws InstanceNotFoundException {
//        //Arrange
//        String username = "user";
//        Library userLib = ctrl.getUserLibrary(username);
//        userLib.addPublication(publication1);
//        userLib.addPublication(publication2);
//
//        //Act
//        List<PublicationDTO> result = ctrl.getListOfPublicationsInLibrary(username);
//
//        //Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//
//    }

}