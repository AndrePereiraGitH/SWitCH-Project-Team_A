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
        assertEquals(mediaFormat, pt1.getMediaFormat());
        assertEquals(pt1.getTypeOfBinding(), typeOfBinding);
        assertEquals(pt1.getDetails(), details);

    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {
        PublicationType pt = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "A");
        assertFalse(pt.equals(null));
    }

    @Test
    void shouldReturnFalseWhenComparedWithDifferentClass() {
        PublicationType pt = new PublicationType(MediaFormat.MAGAZINE, TypeOfBinding.HARDBACK, "A");
        assertFalse(pt.equals("not a PublicationType"));
    }

    @Test
    void TestInvalidPublicationTypes()
    {
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(null, TypeOfBinding.HARDBACK, "A"));
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, ""));
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(MediaFormat.BOOK, null,"Book with gold engravings"));
    }

    // Test to see if identical objects have the same hash code
    @Test
    void equalObjectsMustHaveSameHashCode()
    {
        PublicationType a = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "A");
        PublicationType b = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "A");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

}