package TOPSECRET.dto;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PublicationTypeDTO_US014Test {

    @Test
    void fromDomainListValidPublicationType(){
        // Arrange
        PublicationTypeFactory factory = new PublicationTypeFactory();
        PublicationType pt = mock(PublicationType.class);
        when(pt.getMediaFormat()).thenReturn(MediaFormat.BOOK);
        when(pt.getTypeOfBinding()).thenReturn(TypeOfBinding.PAPERBACK);
        when(pt.getDetails()).thenReturn("Paperback");

        // Act
        PublicationTypeDTO_US014 dto = new PublicationTypeDTO_US014(pt);

        // Assert
        assertEquals("BOOK", dto.getMediaFormat());
        assertEquals("PAPERBACK", dto.getTypeOfBinding());
        assertEquals("Paperback", dto.getDetails());
    }

    @Test
    void fromDomainListEmptyListPublicationType(){
        // Act
        List<PublicationTypeDTO_US014> result =
                PublicationTypeDTO_US014.fromDomainList(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void fromDomainListListOneElement(){
        // Arrange
        PublicationType pT = mock(PublicationType.class);

        when(pT.getMediaFormat()).thenReturn(MediaFormat.BOOK);
        when(pT.getTypeOfBinding()).thenReturn(TypeOfBinding.PAPERBACK);
        when(pT.getDetails()).thenReturn("Paperback");

        // Act
        List<PublicationTypeDTO_US014> result =
                PublicationTypeDTO_US014.fromDomainList(List.of(pT));

        // Assert
        assertEquals(1, result.size());

        PublicationTypeDTO_US014 dto = result.get(0);

        assertEquals("PAPERBACK", dto.getTypeOfBinding());
        assertEquals("Paperback", dto.getDetails());
    }

    @Test
    void fromDomainListListMultipleElements(){
        // Arrange
        PublicationType pT1 = mock(PublicationType.class);
        PublicationType pT2 = mock(PublicationType.class);

        when(pT1.getMediaFormat()).thenReturn(MediaFormat.BOOK);
        when(pT1.getTypeOfBinding()).thenReturn(TypeOfBinding.PAPERBACK);
        when(pT1.getDetails()).thenReturn("Paperback");

        when(pT2.getMediaFormat()).thenReturn(MediaFormat.BOOK);
        when(pT2.getTypeOfBinding()).thenReturn(TypeOfBinding.HARDBACK);
        when(pT2.getDetails()).thenReturn("Hardback");

        // Act
        List<PublicationTypeDTO_US014> result =
                PublicationTypeDTO_US014.fromDomainList(List.of(pT1, pT2));

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void fromDomainListNullPublicationType(){
        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()->new PublicationTypeDTO_US014(null));

        // Assert
        assertEquals("PublicationTypeDTO cannot be null", ex.getMessage());

    }


}