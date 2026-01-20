package TOPSECRET.domain;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublicationTypeTest {

    @Test
    void TestValidPublicationType()
    {
        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding typeOfBinding = TypeOfBinding.HARDBACK;
        String details = "Hardback book with gold engravings";

        PublicationType pt1 = new PublicationType(mediaFormat, typeOfBinding, details);

        assertNotNull(pt1);
        assertEquals(pt1.getMediaFormat(), mediaFormat);
        assertEquals(pt1.getTypeOfBinding(), typeOfBinding);
        assertEquals(pt1.getDetails(), details);

    }

    @Test
    void TestInvalidPublicationTypes()
    {
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, ""));
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(null, TypeOfBinding.HARDBACK, "Book with gold engravings"));
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(MediaFormat.BOOK, null, "Book with gold engravings"));
    }

}