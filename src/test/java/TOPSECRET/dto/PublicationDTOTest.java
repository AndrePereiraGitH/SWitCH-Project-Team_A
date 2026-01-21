package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicationDTOTest {

    @Test
    void ensureConstructorShouldSetFieldsCorrectly() {
        // Arrange
        String expectedTitle = "Os Maias";
        List<String> expectedAuthors = Arrays.asList("Eça de Queirós");
        String expectedCode = "978-1234567890";
        String expectedType = "BOOK";

        // Act
        PublicationDTO dto = new PublicationDTO(expectedTitle, expectedAuthors, expectedCode, expectedType);

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
        List<String> authors = Arrays.asList("Robert C. Martin", "Dean Wampler");
        String code = "123-456";
        String type = "TECHNICAL_BOOK";

        PublicationDTO dto = new PublicationDTO(title, authors, code, type);

        String expectedString = "PublicationDTO{" +
                "pubTitle='Clean Code'" +
                ", authorName='[Robert C. Martin, Dean Wampler]'" +
                ", code='123-456'" +
                ", PublicationType='TECHNICAL_BOOK'" +
                '}';

        // Act
        String result = dto.toString();

        // Assert
        assertEquals(expectedString, result);
    }

    @Test
    void ensureConstructorWithNullValuesShouldNotCrash() {
        // Arrange & Act
        PublicationDTO dto = new PublicationDTO(null, null, null, null);

        // Assert
        assertNull(dto.pubTitle);
        assertNull(dto.authorName);
        assertNull(dto.code);
        assertNull(dto.publicationType);
        assertNotNull(dto.toString());
    }

}