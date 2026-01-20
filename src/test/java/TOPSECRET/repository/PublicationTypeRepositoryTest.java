package TOPSECRET.repository;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Type;

public class PublicationTypeRepositoryTest {

    @Test
    void createPublicationTypeRepositoryTest()
    {
        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding typeOfBinding = TypeOfBinding.HARDBACK;
        String details = "Hardback book with gold engravings";

        PublicationType pubType = new PublicationType(mediaFormat, typeOfBinding, details);
        PublicationTypeRepository pubTypeRepo = new PublicationTypeRepository();

        pubTypeRepo.addAndValidatePublicationType(pubType);

        assertNotNull(pubTypeRepo);
    }
/*
    @Test
    void shouldReturnPublicationTypeAlreadyExists()
    {
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Hardback book with gold engravings"));
    }

 */
}
