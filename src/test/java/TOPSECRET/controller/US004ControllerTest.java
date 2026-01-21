//package TOPSECRET.controller;
//
//import TOPSECRET.domain.*;
//import TOPSECRET.domain.Enum.Genre;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class US004ControllerTest {
//
//    private US004Controller testController;
//
//    // Runs before each test
//    @BeforeEach
//    void setup() {
//        testController = new US004Controller();
//    }
//
//    // Creates a valid Contact for testing
//    private Contact createValidContact() {
//        return new Contact("912345678", "351", null, null);
//    }
//
//    // Creates a valid PublicationType for testing
//    private PublicationType createValidPublicationType() {
//        return new PublicationType(); // Adjust when class is ready
//    }
//
//
//    // REGISTER APPRAISAL ENTITY
//    @Test
//    void shouldRegisterAppraisalEntitySuccessfully() {
//
//        // ARRANGE
//        String expectedName = "Entity One";
//        Contact expectedContact = createValidContact();
//        PublicationType expectedPublicationType = createValidPublicationType();
//        Genre expectedGenre = Genre.FICTION;
//
//        // ACT
//        AppraisalEntity createdEntity = testController.registerAppraisalEntity(
//                        expectedName,
//                        expectedContact,
//                        expectedPublicationType,
//                        expectedGenre
//                );
//
//        List<AppraisalEntity> storedEntities =
//                testController.getAllAppraisalEntity();
//
//        // ASSERT
//        assertNotNull(createdEntity);
//
//        AppraisalEntity storedEntity = storedEntities.get(0);
//
//        assertEquals(expectedName, storedEntity.getName());
//        assertEquals(expectedContact, storedEntity.getContact());
//        assertEquals(expectedPublicationType, storedEntity.getPublicationType());
//        assertEquals(expectedGenre, storedEntity.getGenre());
//
//        assertEquals(createdEntity, storedEntity);
//    }
//
//    @Test
//    void shouldReturnNullWhenTryingToRegisterInvalidEntity() {
//
//        // ARRANGE
//        String invalidName = "";
//        Contact contact = createValidContact();
//        PublicationType publicationType = createValidPublicationType();
//        Genre genre = Genre.FICTION;
//
//        // ACT
//        AppraisalEntity result =
//                testController.registerAppraisalEntity(
//                        invalidName,
//                        contact,
//                        publicationType,
//                        genre
//                );
//
//        List<AppraisalEntity> storedEntities =
//                testController.getAllAppraisalEntity();
//
//        // ASSERT
//        assertNull(result);
//        assertTrue(storedEntities.isEmpty());
//    }
//
//    @Test
//    void shouldNotAllowDuplicateAppraisalEntityRegistration() {
//
//        // ARRANGE
//        String name = "Entity One";
//        Contact contact = createValidContact();
//        PublicationType publicationType = createValidPublicationType();
//        Genre genre = Genre.FICTION;
//
//        testController.registerAppraisalEntity(name, contact, publicationType, genre);
//
//        // ACT
//        AppraisalEntity duplicated = testController.registerAppraisalEntity(name, contact, publicationType, genre);
//
//        List<AppraisalEntity> storedEntities = testController.getAllAppraisalEntity();
//
//        // ASSERT
//        assertNull(duplicated);
//
//        AppraisalEntity storedEntity = storedEntities.get(0);
//
//        assertEquals(name, storedEntity.getName());
//        assertEquals(contact, storedEntity.getContact());
//        assertEquals(publicationType, storedEntity.getPublicationType());
//        assertEquals(genre, storedEntity.getGenre());
//    }
//
//
//    // GET ALL APPRAISAL ENTITIES
//    @Test
//    void shouldReturnAllRegisteredEntities() {
//
//        // ARRANGE
//        Contact contact = createValidContact();
//        PublicationType publicationType = createValidPublicationType();
//
//        AppraisalEntity first = testController.registerAppraisalEntity(
//                        "First",
//                        contact,
//                        publicationType,
//                        Genre.FICTION
//                );
//
//        AppraisalEntity second = testController.registerAppraisalEntity(
//                        "Second",
//                        contact,
//                        publicationType,
//                        Genre.DRAMA
//                );
//
//        // ACT
//        List<AppraisalEntity> result = testController.getAllAppraisalEntity();
//
//        // ASSERT
//        AppraisalEntity firstStored = result.get(0);
//        AppraisalEntity secondStored = result.get(1);
//
//        assertEquals(first, firstStored);
//        assertEquals(second, secondStored);
//
//        assertEquals("First", firstStored.getName());
//        assertEquals("Second", secondStored.getName());
//    }
//}
