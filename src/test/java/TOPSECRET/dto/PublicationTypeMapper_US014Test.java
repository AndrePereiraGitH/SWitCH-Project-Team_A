package TOPSECRET.dto;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicationTypeMapper_US014Test {

    @Test
    void toDTOSinglePublicationTypeMapsCorrectly() {
        PublicationType publicationType = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Hardback"
        );

        PublicationTypeMapper_US014 mapper = new PublicationTypeMapper_US014();

        PublicationTypeDTO_US014 dto = mapper.toDTO(publicationType);

        assertEquals(MediaFormat.BOOK, dto._mediaFormat);
        assertEquals(TypeOfBinding.HARDBACK, dto._typeOfBinding);
        assertEquals("Hardback", dto._details);
    }

    @Test
    void getListOfPublicationTypeDTOMapsAllObjectsCorrectly() {
        PublicationType pubType1 = new PublicationType(
                MediaFormat.MAGAZINE,
                TypeOfBinding.PAPERBACK,
                "Paperback"
        );

        PublicationType pubType2 = new PublicationType(
                MediaFormat.MAGAZINE,
                TypeOfBinding.HARDBACK,
                "Hardback"
        );

        PublicationTypeMapper_US014 mapper = new PublicationTypeMapper_US014();

        List<PublicationTypeDTO_US014> result = mapper.toDTO(List.of(pubType1, pubType2));

        assertEquals(2, result.size());

        assertEquals(MediaFormat.MAGAZINE, result.get(0)._mediaFormat);
        assertEquals(TypeOfBinding.PAPERBACK, result.get(0)._typeOfBinding);
        assertEquals("Paperback", result.get(0)._details);

        assertEquals(MediaFormat.MAGAZINE, result.get(1)._mediaFormat);
        assertEquals(TypeOfBinding.HARDBACK, result.get(1)._typeOfBinding);
        assertEquals("Hardback", result.get(1)._details);
    }


}