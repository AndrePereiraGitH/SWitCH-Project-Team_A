package TOPSECRET.dto;

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
        when(pt.getMediaFormat()).thenReturn("Book");

        // Act
        PublicationTypeDTO_US014 dto = new PublicationTypeDTO_US014(pt);

        // Assert
        assertEquals("Book", dto.getMediaFormat());

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

        when(pT.getMediaFormat()).thenReturn("Book");

        // Act
        List<PublicationTypeDTO_US014> result =
                PublicationTypeDTO_US014.fromDomainList(List.of(pT));

        // Assert
        assertEquals(1, result.size());

        PublicationTypeDTO_US014 dto = result.get(0);

        assertEquals("Book", dto.getMediaFormat());
    }

    @Test
    void fromDomainListListMultipleElements(){
        // Arrange
        PublicationType pT1 = mock(PublicationType.class);
        PublicationType pT2 = mock(PublicationType.class);

        when(pT1.getMediaFormat()).thenReturn("Book");

        when(pT2.getMediaFormat()).thenReturn("Magazine");

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