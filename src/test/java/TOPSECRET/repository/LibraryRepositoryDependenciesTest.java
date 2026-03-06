package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link LibraryRepository} class.
 * <p>
 * This test class verifies the behavior of library creation, validation,
 * and user library ownership checks in the LibraryRepository.
 * </p>
 */

class LibraryRepositoryDependenciesTest {

    // BibliographicalData
    Profile profile = new Profile("Tove Ditlevsen", new Country("DENMARK"), LocalDate.of(1917, 12, 14));
    AuthorFactory authorFactory = new AuthorFactory();
    Author author = authorFactory.createWithExistingProfile(profile, "valio bio");
    Address address = new Address("R Maria", "12", "156-5", new Country("PARAGUAY"));
    Email email = new Email("Maria1", "gmail.com");
    Contact contact = new Contact("915945695", "+351", email, address);
    BibliographicalData bibliographicalData = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author),
            Locale.ENGLISH,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre("Fiction")),
            "Some text"
    );

    // User
    User user = new User("maria1", contact, profile, UserRole.NORMAL_USER);

    // Edition
    Code code = Code.fromIsbn13("9783161484123");
    PhysicalAttributes physicalAttributes = new PhysicalAttributes(new Dimensions(10.0, 0.2, 5.2), 400.0, 250);
    PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
    Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
    EditionFactory editionFactory = new EditionFactory();
    Edition edition1 = editionFactory.create("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    Edition edition2 = editionFactory.create("2nd", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    Edition edition3 = editionFactory.create("3rd", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

    // PublicationSample
    PublicationSample validItem1 = new PublicationSample(bibliographicalData, user, PublicationStatus.READING, edition1, Condition.LIKE_NEW);
    PublicationSample validItem2 = new PublicationSample(bibliographicalData, user, PublicationStatus.READ, edition2, Condition.LIKE_NEW);
    PublicationSample validItem3 = new PublicationSample(bibliographicalData, user, PublicationStatus.READING, edition3, Condition.LIKE_NEW);


    // System Under Test
    LibraryFactory factory = new LibraryFactory();
    Map<String, Library> libraries = new HashMap<>();
    LibraryRepository libraryRepository = new LibraryRepository(libraries,factory);


    // ============ Constructor Tests ============

    /**
     * Should throw an {@link IllegalArgumentException} when the factory parameter is null.
     * <p>
     * This test verifies that the LibraryRepository constructor properly validates
     * its dependencies and rejects null factory instances.
     * </p>
     */
    @Test
    void shouldThrowAnExceptionWhenFactoryIsNull() {
        //Arrange

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new LibraryRepository(libraries,null));
    }


    // ============ getLibraryOfUser Tests ============

    /**
     * Tests that getLibraryOfUser successfully retrieves a library for a valid username.
     * <p>
     * This test verifies that when a valid username is provided, the correct library
     * that was previously created is returned from the repository.
     * </p>
     *
     * @throws InstanceAlreadyExistsException if the user already has a library
     * @throws InstanceNotFoundException if no library is found for the specified username
     */
    @Test
    void ensureGetLibraryOfUserSuccessfully() throws InstanceAlreadyExistsException, InstanceNotFoundException {
        // Arrange
        String libraryName = "lib1";
        Library expectedLib = libraryRepository.createAndAdd(libraryName, user);

        // Act
        Library realLib = libraryRepository.getLibraryOfUser(user.getUserName());

        // Assert
        assertEquals(expectedLib, realLib);
    }

    /**
     * Tests that getLibraryOfUser throws NullPointerException when username is null.
     * <p>
     * This test verifies that the method properly validates and rejects null username
     * input by throwing the appropriate exception.
     * </p>
     */
    @Test
    void throwExceptionWhenUsernameIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> libraryRepository.getLibraryOfUser(null));
    }

    /**
     * Tests that getLibraryOfUser throws IllegalArgumentException when username is empty.
     * <p>
     * This test verifies that the method properly validates and rejects empty string
     * username input by throwing the appropriate exception.
     * </p>
     */
    @Test
    void throwExceptionWhenUsernameIsEmpty() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> libraryRepository.getLibraryOfUser(""));
    }

    /**
     * Tests that getLibraryOfUser throws InstanceNotFoundException when no library exists for the user.
     * <p>
     * This test verifies that the method throws the appropriate exception when attempting
     * to retrieve a library that does not exist in the repository (null library entry).
     * </p>
     */
    @Test
    void throwExceptionWhenLibraryIsNull() {
        // Arrange
        String libUser = user.getUserName();
        libraries.put(libUser, null);

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () -> libraryRepository.getLibraryOfUser(libUser));
    }

    // ============ create Tests ============

    /**
     * Tests that a library is successfully created and added to the repository
     * when provided with valid parameters.
     * <p>
     * Verifies that:
     * <ul>
     *   <li>The returned library is not null</li>
     *   <li>The library name matches the provided name</li>
     *   <li>The owner username is correctly set</li>
     * </ul>
     * </p>
     *
     * @throws InstanceAlreadyExistsException if the user already has a library
     */
    @Test
    void shouldCreateAndAddLibrarySuccessfully() throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "My Library";

        // Act
        Library newLibrary = libraryRepository.createAndAdd(libraryName, user);

        // Assert
        assertNotNull(newLibrary);
        assertEquals(libraryName, newLibrary.getLibraryName());
        assertEquals(user.getUserName(), newLibrary.getUser().getUserName());
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when attempting
     * to create a library with an empty name.
     * <p>
     * Validates that library names must contain at least one character.
     * </p>
     */
    @Test
    void shouldReturnAnExceptionWhenTheLibraryNameIsEmpty() {
        // Arrange
        String libraryName = "";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> libraryRepository.createAndAdd(libraryName, user));
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when attempting
     * to create a library with a null user.
     * <p>
     * Validates that a valid user must be provided as the library owner.
     * </p>
     */
    @Test
    void shouldReturnAnExceptionWhenTheUserIsNull() {
        // Arrange
        String libraryName = "mylib";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> libraryRepository.createAndAdd(libraryName, null));
    }

    /**
     * Tests that an {@link InstanceAlreadyExistsException} is thrown when attempting
     * to create a second library for a user who already has one.
     * <p>
     * Validates the business rule that users can only have one library.
     * </p>
     *
     * @throws InstanceAlreadyExistsException if the initial library creation fails
     */
    @Test
    void shouldReturnAnExceptionWhenTheUserAlreadyHasALibrary() throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "My Library";

        // Act
        libraryRepository.createAndAdd(libraryName, user);

        // Assert
        assertThrows(InstanceAlreadyExistsException.class, () -> libraryRepository.createAndAdd(libraryName, user));
    }

    // ============ verifyIfUserHasALibrary Tests ============

    /**
     * Tests that the verification method returns {@code true} when checking
     * a user who already has a library.
     * <p>
     * Verifies the correct detection of existing library ownership.
     * </p>
     *
     * @throws InstanceAlreadyExistsException if library creation fails
     */
    @Test
    void returnTrueWhenUserAlreadyHasALibrary() throws InstanceAlreadyExistsException {
        // Arrange
        String libraryName = "My Library";
        libraryRepository.createAndAdd(libraryName, user);

        // Act
        boolean result = libraryRepository.verifyIfUserHasALibrary(user);

        // Assert
        assertTrue(result);
    }

    /**
     * Tests that the verification method returns {@code false} when checking
     * a user who does not have a library.
     * <p>
     * Verifies the correct detection when a user has no library ownership.
     * </p>
     */
    @Test
    void returnFalseWhenUserDoesNotHaveALibrary() {
        // Act
        boolean result = libraryRepository.verifyIfUserHasALibrary(user);

        // Assert
        assertFalse(result);
    }

    // ============ getAllPublicationsFromUserLibrary Tests ============

    /**
     * Tests that all publications from a user's library are correctly retrieved.
     * <p>
     * Verifies that:
     * <ul>
     *   <li>Multiple publications can be added to a library</li>
     *   <li>The repository returns all publications associated with the user's library</li>
     *   <li>The returned list matches the expected publications</li>
     * </ul>
     * </p>
     *
     * @throws InstanceAlreadyExistsException if the user already has a library
     * @throws InstanceNotFoundException if the user's library cannot be found
     */
    @Test
    void shouldReturnAllPublicationsFromUserLibrary() throws InstanceAlreadyExistsException, InstanceNotFoundException {
        // Arrange
        Library lib1 = libraryRepository.createAndAdd("lib1", user);
        lib1.addPublication(validItem1);
        lib1.addPublication(validItem2);
        lib1.addPublication(validItem3);

        // Act
        List<PublicationSample> expectedPublications = List.of(validItem1, validItem2, validItem3);
        List<PublicationSample> realPublications = libraryRepository.getAllPublicationsFromUserLibrary(user.getUserName());

        // Assert
        assertEquals(expectedPublications, realPublications);
    }
}
