package TOPSECRET.dto;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicationTypeMapper_US014Test {

    @Test
    void toDTOGetPublicationTypeLisEmptyListReturnsEmptyList() {
        PublicationTypeMapper_US014 mapper = new PublicationTypeMapper_US014();

        List<PublicationTypeDTO_US014> result = mapper.toDTO(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOGetPublicationTypeListOneElementMapsCorrectly() {
        PublicationTypeMapper_US014 mapper = new PublicationTypeMapper_US014();

        PublicationType type =
                new PublicationType(TypeOfBinding.HARDBACK, "Hardback");

        List<PublicationTypeDTO_US014> result =
                mapper.toDTO(List.of(type));

        assertEquals(1, result.size());

        PublicationTypeDTO_US014 dto = result.get(0);
        assertEquals("HARDBACK", dto._typeOfBinding);
        assertEquals("Hardback", dto._details);
    }

    @Test
    void toDTOGetPublicationTypeListTwoElementsMapsBoth() {
        PublicationTypeMapper_US014 mapper = new PublicationTypeMapper_US014();

        PublicationType t1 =
                new PublicationType(TypeOfBinding.HARDBACK, "Hardback");

        PublicationType t2 =
                new PublicationType(TypeOfBinding.PAPERBACK, "Paperback");

        List<PublicationTypeDTO_US014> result =
                mapper.toDTO(List.of(t1, t2));

        assertEquals(2, result.size());
    }
}