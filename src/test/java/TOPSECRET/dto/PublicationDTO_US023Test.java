package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicationDTO_US023Test {

    @Test
    void ensureConstructorShouldSetFieldsCorrectly() {
        // Arrange
        String expectedTitle = "Os Maias";
        List<String> expectedAuthors = List.of("Eça de Queirós");
        String expectedCode = "978-1234567890";
        String expectedType = "BOOK";

        // Act
        PublicationDTO_US023 dto = new PublicationDTO_US023(expectedTitle, expectedAuthors, expectedCode, expectedType);

        // Assert
        assertEquals(expectedTitle, dto.pubTitle);
        assertEquals(expectedAuthors, dto.authorName);
        assertEquals(expectedCode, dto.code);
        assertEquals(expectedType, dto.publicationType);
    }

    @Test
    void toStringShouldReturnFormattedString() {
        // Arrange
        String title = "Clean Code";
        List<String> authors = List.of("Prince", "Madonna");
        String code = "123456";
        String type = "BOOK";

        PublicationDTO_US023 dto = new PublicationDTO_US023(title, authors, code, type);

        String expectedString = "PublicationDTO_US023 {" +
                "pubTitle='Clean Code'" +
                ", authorName='[Prince, Madonna]'" +
                ", code='123456'" +
                ", PublicationType='BOOK'" +
                '}';

        // Act
        String result = dto.toString();

        // Assert
        assertEquals(expectedString, result);
    }

    @Test
    void ensureDTOHandlesNullableCode() {
        // Arrange
        String title = "Os Maias";
        List<String> authors = List.of("Eça de Queirós");
        String type = "BOOK";

        // Act
        PublicationDTO_US023 dto = new PublicationDTO_US023(title, authors, null, type);

        // Assert
        assertEquals(title, dto.pubTitle);
        assertEquals(authors, dto.authorName);
        assertNull(dto.code);
        assertEquals(type, dto.publicationType);
    }

}