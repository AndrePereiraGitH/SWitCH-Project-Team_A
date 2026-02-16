package TOPSECRET.domain;

import TOPSECRET.domain.Enum.TypeOfBinding;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublicationTypeTest {

    @Test
    void TestValidPublicationType()
    {
        TypeOfBinding typeOfBinding = TypeOfBinding.HARDBACK;
        String details = "Hardback book with gold engravings";

        PublicationType pt1 = new PublicationType(typeOfBinding, details);

        assertNotNull(pt1);
        assertEquals(pt1.getTypeOfBinding(), typeOfBinding);
        assertEquals(pt1.getDetails(), details);

    }

    @Test
    void shouldReturnFalseWhenComparedWithNull() {
        PublicationType pt = new PublicationType(TypeOfBinding.HARDBACK, "A");
        assertFalse(pt.equals(null));
    }

    @Test
    void shouldReturnFalseWhenComparedWithDifferentClass() {
        PublicationType pt = new PublicationType(TypeOfBinding.HARDBACK, "A");
        assertFalse(pt.equals("not a PublicationType"));
    }

    @Test
    void TestInvalidPublicationTypes()
    {
        assertThrows(IllegalArgumentException.class, () -> new PublicationType(TypeOfBinding.HARDBACK, ""));
        assertThrows(IllegalArgumentException.class, () -> new PublicationType( null, "Book with gold engravings"));
    }

    // Test to see if identical objects have the same hash code
    @Test
    void equalObjectsMustHaveSameHashCode()
    {
        PublicationType a = new PublicationType(TypeOfBinding.HARDBACK, "A");
        PublicationType b = new PublicationType(TypeOfBinding.HARDBACK, "A");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

}