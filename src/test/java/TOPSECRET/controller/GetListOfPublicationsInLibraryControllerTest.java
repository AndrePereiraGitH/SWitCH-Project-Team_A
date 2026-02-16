package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.dto.PublicationDetailsDTO;
import TOPSECRET.repository.LibraryRepository;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GetListOfPublicationsInLibraryControllerTest {




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
    User user2 = new User("niceGuy", contact1, profile2, userRole1);
    User user3 = new User("emptyGuy", contact1, profile2, userRole1);

    //PublicationStatus
    PublicationStatus pubStatus1 = PublicationStatus.READING;

    //Edition
    String edition1 = "2nd Edition";

    //Code
    Code code1 = Code.fromIsbn10("1234567890");
    Code code2 = Code.fromIsbn10("1234567899");

    //PublicationType
    PublicationType publicationType = new PublicationType(TypeOfBinding.PAPERBACK, "Paperback");


    Publication publication1 = new Publication(bibliographicalData1, publicationType, user1,
            pubStatus1, edition1, code1);

    Publication publication2= new Publication(bibliographicalData1, publicationType, user1,
            pubStatus1, edition1, code2);

    // Create library
    Library lib1 = new Library(List.of(publication1,publication2),"mylib1",user1);
    Library lib2 = new Library(List.of(publication1),"mylib2",user2);
    Library emptyLibrary = new Library(List.of(),"emptyLib",user3);

    // Instantiates LibraryRepo
    LibraryRepository userLib = new LibraryRepository(
            Map.of(
                    user1.getUserName(), lib1,
                    user2.getUserName(), lib2,
                    user3.getUserName(), emptyLibrary
            )
    );

    // Instantiates LibraryRepo
    GetListOfPublicationsInLibraryController ctrl = new GetListOfPublicationsInLibraryController(userLib);



    @Test
    void getUserLibraryIfItExists() throws InstanceNotFoundException {
        // Arrange
        String username = user1.getUserName();

        // Act
        Library lib = ctrl.getUserLibrary(user1.getUserName());

        // Assert
        assertNotNull(lib);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        // Arrange
        String nonExistentUser = "AAAA";

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () -> {
            ctrl.getUserLibrary(nonExistentUser);
        });
    }

    @Test
    void shouldThrowExceptionWhenUserNameIsNull(){
        // Arrange
        String nonExistentUser = null;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ctrl.getUserLibrary(nonExistentUser);
        });

    }

    @Test
    void shouldThrowExceptionWhenUserNameIsEmpty(){
        // Arrange
        String nonExistentUser = "";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ctrl.getUserLibrary(nonExistentUser);
        });

    }


    @Test
    void ensureListOfPublicationsInLibraryIsEmptyWhenLibraryReturnsEmptyAList() throws InstanceNotFoundException {

        // Arrange
        String username = user3.getUserName();
        ctrl.getUserLibrary(username);

        // Act
        List<PublicationDetailsDTO> result = ctrl.getListOfPublicationsInLibraryDTO(username);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void ensureListOfPublicationsInLibraryReturnsDTOs() throws InstanceNotFoundException {
        //Arrange
        String username = user1.getUserName();

        //Act
        List<PublicationDetailsDTO> result = ctrl.getListOfPublicationsInLibraryDTO(username);

        String expectedTitle1= publication1.getBibliographicalData().getTitle();
        String expectedTitle2=publication2.getBibliographicalData().getTitle();

        //Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedTitle1,result.get(0).pubTitle);
        assertEquals(expectedTitle2,result.get(1).pubTitle);

    }

}