package TOPSECRET.dto;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicationTypeMapperTest {

    @Test
    void toDTO_singlePublicationType_mapsCorrectly() {
        PublicationType publicationType = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "E-book"
        );

        PublicationTypeMapper mapper = new PublicationTypeMapper();

        PublicationTypeDTO dto = mapper.toDTO(publicationType);

        assertEquals(MediaFormat.BOOK, dto._mediaFormat);
        assertEquals(TypeOfBinding.HARDBACK, dto._typeOfBinding);
        assertEquals("E-book", dto._details);
    }

    @Test
    void toDTO_listOfPublicationTypes_mapsAllElements() {
        PublicationType pt1 = new PublicationType(
                MediaFormat.MAGAZINE,
                TypeOfBinding.PAPERBACK,
                "Paperback"
        );

        PublicationType pt2 = new PublicationType(
                MediaFormat.MAGAZINE,
                TypeOfBinding.HARDBACK,
                "E-MAGAZINE"
        );

        PublicationTypeMapper mapper = new PublicationTypeMapper();

        List<PublicationTypeDTO> result = mapper.toDTO(List.of(pt1, pt2));

        assertEquals(2, result.size());

        assertEquals(MediaFormat.MAGAZINE, result.get(0)._mediaFormat);
        assertEquals(TypeOfBinding.PAPERBACK, result.get(0)._typeOfBinding);
        assertEquals("Paperback", result.get(0)._details);

        assertEquals(MediaFormat.MAGAZINE, result.get(1)._mediaFormat);
        assertEquals(TypeOfBinding.HARDBACK, result.get(1)._typeOfBinding);
        assertEquals("E-MAGAZINE", result.get(1)._details);
    }


}