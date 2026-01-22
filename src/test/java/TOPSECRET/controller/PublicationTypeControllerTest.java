package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import TOPSECRET.controller.US002_PublicationTypeController;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.repository.PublicationTypeRepository;

class PublicationTypeControllerTest {

    @Test
    void createAndAddPublicationTypesToRepository()
    {
        PublicationTypeRepository repo = new PublicationTypeRepository();
        US002_PublicationTypeController controller = new US002_PublicationTypeController(repo);

        PublicationType pt1 = controller.createAndAddPublicationTypes( MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Book with gold engravings");

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
        US002_PublicationTypeController controller = new US002_PublicationTypeController(repo);

        assertThrows(IllegalArgumentException.class, () -> controller.createAndAddPublicationTypes(null, TypeOfBinding.HARDBACK, "test"));

        assertEquals(0, repo.getAllPublicationTypes().size());
    }
}
