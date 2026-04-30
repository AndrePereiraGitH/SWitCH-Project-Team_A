//package TOPSECRET.controller;
//
//import TOPSECRET.domain.PublicationType.PublicationType;
//import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.mockito.Mockito.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for the {@link ToRegisterPubTypeController}.
// *
// * <p>This test class verifies the behavior of the controller responsible
// * for registering new {@link PublicationType} objects. The repository
// * dependency is mocked using Mockito to isolate the controller logic
// * and verify interactions with the {@link IPublicationTypeRepository}.
// */
//
//class ToRegisterPubTypeControllerTest {
//
//    /**
//     * Mock repository used to simulate persistence behavior.
//     */
//    IPublicationTypeRepository repo;
//
//    /**
//     * Controller instance under test.
//     */
//    ToRegisterPubTypeController controller;
//
//    /**
//     * Initializes the test environment before each test execution.
//     *
//     * <p>A mocked {@link IPublicationTypeRepository} is created and injected
//     * into the controller to isolate the controller from the persistence layer.
//     */
//    @BeforeEach
//    void setup()
//    {
//        repo = mock(IPublicationTypeRepository.class);
//        controller = new ToRegisterPubTypeController(repo);
//    }
//
//    /**
//     * Register a new valid publication type with success
//     */
//    @Test
//    void registerPublicationTypeControllerDelegatesToRepository()
//    {
//        String mediaFormat = "Book";
//
//        PublicationType expected = mock(PublicationType.class);
//
//        when(repo.create(mediaFormat)).thenReturn(expected);
//
//        PublicationType result = controller.RegisterPubType(mediaFormat);
//
//        assertEquals(expected, result);
////        verify(repo, times(1)).create(mediaFormat);
////        verify(repo, times(1)).addToList(result);
////        verifyNoMoreInteractions(repo);
//    }
//
//    /**
//     * Invalid data tests
//     * Verifies that an {@link IllegalArgumentException} is thrown when parameters are invalid
//     * and ensures that nothing is added to the repository.
//     */
//
//    /**
//     * Invalid Media format
//     */
//    @Test
//    void shouldThrowExceptionWhenMediaFormatIsInvalid()
//    {
//        String mediaFormat = null;
//
//        when(repo.create(mediaFormat)).thenThrow(new IllegalArgumentException());
//
//        assertThrows(IllegalArgumentException.class, () -> controller.RegisterPubType(mediaFormat));
//    }
//
//
//
//    /**
//     * Registering a duplicate publication type should create, but not add
//     */
//    @Test
//    void registerDuplicatePublicationTypeShouldCreateThenThrowWhenAdding()
//    {
//        String mediaFormat = "Book";
//
//        PublicationType expected = mock(PublicationType.class);
//
//        when(repo.create(mediaFormat)).thenReturn(expected);
//        doThrow(new IllegalArgumentException()).when(repo).addToList(expected);
//
//        assertThrows(IllegalArgumentException.class, () -> controller.RegisterPubType(mediaFormat));
////        verify(repo, times(1)).create(mediaFormat, typeOfBinding, details);
////        verify(repo, times(1)).addToList(expected);
////        verifyNoMoreInteractions(repo);
//    }
//}