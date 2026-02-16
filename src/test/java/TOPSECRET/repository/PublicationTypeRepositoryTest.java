package TOPSECRET.repository;

import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class PublicationTypeRepositoryTest {

    // Publication type sample
    // mediaFormat removed
    TypeOfBinding typeOfBinding = TypeOfBinding.HARDBACK;
    String details = "Book with gold engravings";


    @Test
    void createPublicationTypeRepositoryAndAddPublicationTest()
    {
        PublicationType pubType;

        PublicationTypeRepository pubTypeRepo = new PublicationTypeRepository();

        pubType = pubTypeRepo.createPublicationType(typeOfBinding, details);
        pubTypeRepo.addToList(pubType);

        assertNotNull(pubTypeRepo);
        assertEquals(pubType, pubTypeRepo.getAllPublicationTypes().get(0));
        assertEquals(typeOfBinding, pubType.getTypeOfBinding());
        assertEquals(details, pubType.getDetails());
    }


    @Test
    void shouldReturnAllPublicationTypes()
    {
        PublicationTypeRepository pubTypeRepo = new PublicationTypeRepository();

        PublicationType pt1;
        PublicationType pt2;

        pt1 = pubTypeRepo.createPublicationType(typeOfBinding, details);
        pubTypeRepo.addToList(pt1);

        pt2 = pubTypeRepo.createPublicationType(typeOfBinding, "different book");
        pubTypeRepo.addToList(pt2);


        List<PublicationType> result = pubTypeRepo.getAllPublicationTypes();

        assertEquals(2, result.size()); // If the 2 publication types were added, the size of the list should be 2
        assertTrue(result.contains(pt1));
        assertTrue(result.contains(pt2));
    }


    @Test
    void shouldReturnEmptyListWhenNoPublicationTypesExist()
    {
        PublicationTypeRepository repo = new PublicationTypeRepository();

        List<PublicationType> result = repo.getAllPublicationTypes();

        assertNotNull(repo);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void shouldReturnExceptionWhenAddingDuplicatePublicationType()
    {
        PublicationTypeRepository pubTypeRepo = new PublicationTypeRepository();

        PublicationType pt1;
        PublicationType pt2;

        pt1 = pubTypeRepo.createPublicationType(typeOfBinding, details);
        pubTypeRepo.addToList(pt1);

        pt2 = pt1;

        assertThrows(IllegalArgumentException.class, () -> pubTypeRepo.addToList(pt2));
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

        PublicationType pubType = repo.createPublicationType(typeOfBinding, details);

        PublicationType returned = repo.addToList(pubType);

        assertNotNull(returned);
        assertSame(pubType, returned);
    }
}
