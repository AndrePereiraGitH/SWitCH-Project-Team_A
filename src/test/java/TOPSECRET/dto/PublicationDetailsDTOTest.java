package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PublicationDetailsDTOTest {

    @Test
    void ensureConstructorShouldSetFieldsCorrectly() {
        // Arrange
        String expectedTitle = "Os Maias";
        List<String> expectedAuthors = Arrays.asList("Eça de Queirós");
        String expectedCode = "978-1234567890";
        String expectedType = "BOOK";

        // Act
        PublicationDetailsDTO dto = new PublicationDetailsDTO(expectedTitle, expectedAuthors, expectedCode, expectedType);

        // Assert
        assertEquals(expectedTitle, dto.pubTitle);
        assertEquals(expectedAuthors, dto.authorName);
        assertEquals(expectedCode, dto.code);
        assertEquals(expectedType, dto.publicationType);
    }


}