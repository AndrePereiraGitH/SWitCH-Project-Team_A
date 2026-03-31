/*package TOPSECRET.dto;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PublicationTypeDTO_US014.
 *
 * <p>This class validates that the DTO correctly extracts
 * data from the PublicationType domain object.</p>
 *
 * <p>The tests ensure that:</p>
 * <ul>
 *     <li>Fields are correctly mapped</li>
 *     <li>The static conversion method works properly</li>
 * </ul>

class PublicationTypeDTO_US014DependenciesTest {

    // Factory setup
    PublicationTypeFactory factory = new PublicationTypeFactory();

    @Test
    void fromDomainListValidPublicationType(){
        // Arrange
        PublicationType pT = factory.create(
                MediaFormat.BOOK,
                TypeOfBinding.PAPERBACK,
                "Paperback"
        );
        // Act
        PublicationTypeDTO_US014 dto = new PublicationTypeDTO_US014(pT);

        // Assert
        assertEquals("PAPERBACK", dto.getTypeOfBinding());
        assertEquals("Paperback", dto.getDetails());
    }

    @Test
    void fromDomainListNullPublicationType(){
        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()->new PublicationTypeDTO_US014(null));

        // Assert
        assertEquals("PublicationTypeDTO cannot be null", ex.getMessage());
    }

    @Test
    void fromDomainListEmptyListPublicationType(){
        // Act
        List<PublicationTypeDTO_US014> result = PublicationTypeDTO_US014.fromDomainList(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void fromDomainListListOneElement(){
        // Arrange

        PublicationType pT = factory.create(
                MediaFormat.BOOK,
                TypeOfBinding.PAPERBACK,
                "Paperback"
        );

        // Act & Assert
        List<PublicationTypeDTO_US014> result = PublicationTypeDTO_US014.fromDomainList(List.of(pT));

        assertEquals(1, result.size());

        // Act & Assert
        PublicationTypeDTO_US014 dto = result.get(0);

        assertEquals("PAPERBACK", dto.getTypeOfBinding());
        assertEquals("Paperback", dto.getDetails());
    }

    @Test
    void fromDomainListListMultipleElements(){
        // Arrange
        PublicationType pT1 = factory.create(
                MediaFormat.BOOK,
                TypeOfBinding.PAPERBACK,
                "Paperback"
        );
        PublicationType pT2 = factory.create(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Hardback"
        );

        // Act & Assert
        List<PublicationTypeDTO_US014> result = PublicationTypeDTO_US014.fromDomainList(List.of(pT1, pT2));
        assertEquals(2, result.size());

    }

    @Test
    void fromDomainListNullListPublicationType(){

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, ()-> PublicationTypeDTO_US014.fromDomainList(null));

        assertEquals("PublicationTypeDTO list cannot be null", ex.getMessage());
    }


}*/