package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.repository.PublicationTypeRepository;

class RegisterPubTypeControllerTest {

    @Test
    void createAndAddPublicationTypesToRepository()
    {
        PublicationTypeRepository repo = new PublicationTypeRepository();
        US002ToRegisterPubTypeController controller = new US002ToRegisterPubTypeController(repo);

        PublicationType pt1 = controller.RegisterPubType( MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Book with gold engravings");

        assertEquals(1, repo.getAllPublicationTypes().size());

        PublicationType pt2 = repo.getAllPublicationTypes().get(0);


        assertEquals(MediaFormat.BOOK, pt2.getMediaFormat());
        assertEquals(TypeOfBinding.PAPERBACK, pt2.getTypeOfBinding());
        assertEquals("Book with gold engravings", pt2.getDetails());

        assertSame(pt2, pt1);
    }


    @Test
    void shouldThrowExceptionAndNotAddToRepository()
    {
        PublicationTypeRepository repo = new PublicationTypeRepository();
        US002ToRegisterPubTypeController controller = new US002ToRegisterPubTypeController(repo);

        assertThrows(IllegalArgumentException.class, () -> controller.RegisterPubType(null, TypeOfBinding.HARDBACK, "test"));

        assertEquals(0, repo.getAllPublicationTypes().size());
    }
}
