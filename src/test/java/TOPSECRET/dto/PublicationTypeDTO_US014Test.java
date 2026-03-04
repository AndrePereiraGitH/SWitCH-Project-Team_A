package TOPSECRET.dto;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.FactoryPublicationType;
import TOPSECRET.domain.PublicationType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublicationTypeDTO_US014Test {

    @Test
    void fromDomainListValidPublicationType(){
        // Arrange
        FactoryPublicationType factory = new FactoryPublicationType();

        PublicationType pt = factory.create(
                MediaFormat.BOOK,
                TypeOfBinding.PAPERBACK,
                "Paperback"
        );

        // Act
        PublicationTypeDTO_US014 dto = new PublicationTypeDTO_US014(pt);

        // Assert
        assertEquals("BOOK", dto.getMediaFormat());
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


}