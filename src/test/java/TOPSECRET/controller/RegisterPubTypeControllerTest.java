package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.repository.PublicationTypeRepository;

/**
 * Unit tests for {@link US002ToRegisterPubTypeController}.
 * <p>
 * This test class verifies:
 * <ul>
 *     <li>Successful registration of a publication type</li>
 *     <li>Correct storage of the publication type in the repository</li>
 *     <li>Proper exception handling when invalid data is provided</li>
 * </ul>
 */

class RegisterPubTypeControllerTest {

    /**
     * Repository and controller instance used for testing.
     */
    PublicationTypeRepository repo = new PublicationTypeRepository();
    US002ToRegisterPubTypeController controller = new US002ToRegisterPubTypeController(repo);

    /**
     * Verifies that a publication type is successfully created
     * and added to the repository through the controller.
     */
    @Test
    void createAndAddPublicationTypesToRepository()
    {
        PublicationType pt1 = controller.RegisterPubType(
                MediaFormat.BOOK,
                TypeOfBinding.PAPERBACK,
                "Book with gold engravings"
        );

        assertEquals(1, repo.getAllPublicationTypes().size());
        assertEquals(pt1, repo.getAllPublicationTypes().get(0));
    }

    /**
     * Verifies that an {@link IllegalArgumentException} is thrown
     * when attempting to register an invalid publication type,
     * and ensures that nothing is added to the repository.
     */
    @Test
    void shouldThrowExceptionAndNotAddToRepository()
    {
        assertThrows(IllegalArgumentException.class, () -> controller.RegisterPubType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, ""));
        assertEquals(0, repo.getAllPublicationTypes().size());
    }
}
