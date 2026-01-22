package TOPSECRET.repository;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class PublicationTypeRepositoryTest {

    MediaFormat mediaFormat = MediaFormat.BOOK;
    TypeOfBinding typeOfBinding = TypeOfBinding.HARDBACK;
    String details = "Book with gold engravings";


    @Test
    void createPublicationTypeRepositoryTest()
    {
        PublicationType pubType;

        PublicationTypeRepository pubTypeRepo = new PublicationTypeRepository();

        pubType = pubTypeRepo.createPublicationType(mediaFormat, typeOfBinding, details);
        pubTypeRepo.addToList(pubType);

        assertNotNull(pubTypeRepo);
    }


    @Test
    void shouldReturnAllPublicationTypes()
    {
        PublicationTypeRepository pubTypeRepo = new PublicationTypeRepository();

        PublicationType pt1;
        PublicationType pt2;

        pt1 = pubTypeRepo.createPublicationType(mediaFormat, typeOfBinding, details);
        pubTypeRepo.addToList(pt1);

        pt2 = pubTypeRepo.createPublicationType(mediaFormat, typeOfBinding, "different book");
        pubTypeRepo.addToList(pt2);



        List<PublicationType> result = pubTypeRepo.getAllPublicationTypes();

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


    @Test
    void shouldReturnExceptionWhenAddingDuplicatePublicationType()
    {
        PublicationTypeRepository pubTypeRepo = new PublicationTypeRepository();

        PublicationType pt1;
        PublicationType pt2;

        pt1 = pubTypeRepo.createPublicationType(mediaFormat, typeOfBinding, details);
        pubTypeRepo.addToList(pt1);

        pt2 = pubTypeRepo.createPublicationType(mediaFormat, typeOfBinding, details);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> pubTypeRepo.addToList(pt2));

        assertEquals("PublicationType already exists or is null", ex.getMessage());
        assertEquals(1, pubTypeRepo.getAllPublicationTypes().size());
    }

    @Test
    void shouldThrowWhenAddingNullPubType() {
        PublicationTypeRepository repo = new PublicationTypeRepository();

        assertThrows(IllegalArgumentException.class, () -> repo.addToList(null));
    }

    @Test
    void shouldReturnTheAddedPublicationTypeWhenAddingToList() {

        PublicationTypeRepository repo = new PublicationTypeRepository();

        PublicationType pubType = repo.createPublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Test");

        PublicationType returned = repo.addToList(pubType);

        assertNotNull(returned);
        assertSame(pubType, returned);
    }
}
