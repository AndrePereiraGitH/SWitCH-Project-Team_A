package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.repository.PublicationTypeRepository;

class RegisterPubTypeControllerTest {

    //Samples
    PublicationTypeRepository repo = new PublicationTypeRepository();
    US002ToRegisterPubTypeController controller = new US002ToRegisterPubTypeController(repo);

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


    @Test
    void shouldThrowExceptionAndNotAddToRepository()
    {
        assertThrows(IllegalArgumentException.class, () -> controller.RegisterPubType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, ""));
        assertEquals(0, repo.getAllPublicationTypes().size());
    }
}
