
//package TOPSECRET.controller;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
//import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
//import org.junit.jupiter.api.Test;
//import TOPSECRET.domain.Enum.MediaFormat;
//import TOPSECRET.domain.Enum.TypeOfBinding;
//import TOPSECRET.domain.PublicationType.PublicationType;
//import TOPSECRET.repository.PublicationTypeRepositoryMem;
//
///**
// * Unit tests for {@link US002ToRegisterPubTypeController}.
// * <p>
// * This test class verifies:
// * <ul>
// *     <li>Successful registration of a publication type</li>
// *     <li>Correct storage of the publication type in the repository</li>
// *     <li>Proper exception handling when invalid data is provided</li>
// * </ul>
// */
//
//class US002ToRegisterPubTypeControllerDependenciesTest {
//
//    /**
//     * Repository and controller instance used for testing.
//     */
//    IPublicationTypeRepository repo = new PublicationTypeRepositoryMem(new PublicationTypeFactory());
//    US002ToRegisterPubTypeController controller = new US002ToRegisterPubTypeController(repo);
//
//    /**
//     * Verifies that a publication type is successfully created
//     * and added to the repository through the controller.
//     */
//    @Test
//    void createAndAddPublicationTypesToRepository()
//    {
//        PublicationType pt1 = controller.RegisterPubType(
//                "Book"
//        );
//
//        assertEquals(1, repo.getAllPublicationTypes().size());
//        assertEquals(pt1, repo.getAllPublicationTypes().get(0));
//    }
//
//    /**
//     * Invalid data tests
//     * Verifies that an {@link IllegalArgumentException} is thrown when parameters are invalid
//     * and ensures that nothing is added to the repository.
//
//    /**
//     * Invalid Media format
//     */
//    @Test
//    void shouldThrowExceptionWhenMediaFormatIsInvalid()
//    {
//        assertThrows(IllegalArgumentException.class, () -> controller.RegisterPubType("");
//        assertEquals(0, repo.getAllPublicationTypes().size());
//    }
//
//    /**
//     * Invalid Type Of Binding
//     */
//    @Test
//    void shouldThrowExceptionWhenTypeOfBindingIsInvalid()
//    {
//        assertThrows(IllegalArgumentException.class, () -> controller.RegisterPubType("Book");
//        assertEquals(0, repo.getAllPublicationTypes().size());
//    }
//
//    /**
//     * Invalid details
//     */
//    @Test
//    void shouldThrowExceptionWhenDetailsAreInvalids()
//    {
//        assertThrows(IllegalArgumentException.class, () -> controller.RegisterPubType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, ""));
//        assertEquals(0, repo.getAllPublicationTypes().size());
//    }
//
//    /**
//     * Verifies that an {@link IllegalArgumentException} is thrown when trying to add a duplicate publication type
//     */
//    @Test
//    void shouldThrowExceptionWhenAddingDuplicatePublicationTypes()
//    {
//        PublicationType pt1 = controller.RegisterPubType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "detail");
//
//        assertThrows(IllegalArgumentException.class, () -> controller.RegisterPubType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "detail"));
//    }
//
//}

