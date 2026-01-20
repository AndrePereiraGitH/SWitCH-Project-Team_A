package TOPSECRET.repository;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Type;
import java.util.List;

public class PublicationTypeRepositoryTest {

    MediaFormat mediaFormat = MediaFormat.BOOK;
    TypeOfBinding typeOfBinding = TypeOfBinding.HARDBACK;
    String details = "Hardback book with gold engravings";


    @Test
    void createPublicationTypeRepositoryTest()
    {
        PublicationType pubType = new PublicationType(mediaFormat, typeOfBinding, details);
        PublicationTypeRepository pubTypeRepo = new PublicationTypeRepository();

        pubTypeRepo.addAndValidatePublicationType(pubType);

        assertNotNull(pubTypeRepo);
    }


    @Test
    void shouldReturnAllPublicationTypes()
    {
        PublicationTypeRepository repo = new PublicationTypeRepository();

        PublicationType pt1 = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Book test");
        PublicationType pt2 = new PublicationType(MediaFormat.MAGAZINE, TypeOfBinding.PAPERBACK, "Magazine test");

        repo.addAndValidatePublicationType(pt1);
        repo.addAndValidatePublicationType(pt2);

        List<PublicationType> result = repo.getAllPublicationTypes();

        assertEquals(2, result.size());
        assertTrue(result.contains(pt1));
        assertTrue(result.contains(pt2));
    }

    @Test
    void shouldReturnEmptyListWhenNoPublicationTypesExist()
    {
        PublicationTypeRepository repo = new PublicationTypeRepository();

        List<PublicationType> result = repo.getAllPublicationTypes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
