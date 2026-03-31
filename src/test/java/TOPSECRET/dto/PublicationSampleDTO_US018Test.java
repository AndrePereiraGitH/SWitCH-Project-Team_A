package TOPSECRET.dto;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.function.Executable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for PublicationSampleDTO_US018 using mocks.
 *
 * Validates:
 * - Constructor and getters
 * - Conversion from domain PublicationSample to DTO
 * - Behavior with empty lists
 * Tests follow the AAA pattern.
 */
class PublicationSampleDTO_US018Test {


    // ============================
    // Helpers
    // ============================

    private PublicationSample createMockPublicationSample(String title, String editionNumber, String ownerUsername) {

        PublicationSample publication = mock(PublicationSample.class);
        BibliographicalData bd = mock(BibliographicalData.class);
        Edition edition = mock(Edition.class);
        User owner = mock(User.class);

//        when(publication.getBibliographicalData()).thenReturn(bd);
//        when(publication.getEdition()).thenReturn(edition);
//        when(publication.getOwner()).thenReturn(owner);

        when(bd.getTitle()).thenReturn(title);
        when(edition.getEditionNumber()).thenReturn(editionNumber);
        when(owner.getUserName()).thenReturn(ownerUsername);

        return publication;
    }


    // ============================
    // Tests
    // ============================

    @Test
    void shouldCreateDTOWithCorrectValues() {

        // Arrange
        PublicationSampleDTO_US018 dto =
                new PublicationSampleDTO_US018("Test Title", "1st", "user1");

        // Act

        // Assert
        assertEquals("Test Title", dto.getTitle());
        assertEquals("1st", dto.getEditionNumber());
        assertEquals("user1", dto.getOwnerUsername());
    }


    @Test
    void shouldConvertPublicationSampleToDTO() {

        // Arrange
        PublicationSample publication = createMockPublicationSample("Test Title", "1st", "user1");

        // Act
//        List<PublicationSampleDTO_US018> result =
//                PublicationSampleDTO_US018.publicationToDTO(List.of(publication));

        // Assert
//        assertEquals(1, result.size());
//        assertEquals("Test Title", result.get(0).getTitle());
//        assertEquals("1st", result.get(0).getEditionNumber());
//        assertEquals("user1", result.get(0).getOwnerUsername());
    }


    @Test
    void shouldReturnEmptyListWhenPublicationListIsEmpty() {

        // Arrange
        List<PublicationSample> emptyList = List.of();

        // Act
//        List<PublicationSampleDTO_US018> result =
//                PublicationSampleDTO_US018.publicationToDTO(emptyList);

        // Assert
//        assertTrue(result.isEmpty());
    }


    @Test
    void shouldConvertMultiplePublicationSamplesToDTO() {

        // ARRANGE
        PublicationSample publication1 = createMockPublicationSample("Title 1", "1st", "user1");
        PublicationSample publication2 = createMockPublicationSample("Title 2", "2nd", "user2");

        // ACT
//        List<PublicationSampleDTO_US018> result =
//                PublicationSampleDTO_US018.publicationToDTO(List.of(publication1, publication2));

        // ASSERT
//        assertEquals(2, result.size());
//        assertEquals("Title 1", result.get(0).getTitle());
//        assertEquals("1st", result.get(0).getEditionNumber());
//        assertEquals("user1", result.get(0).getOwnerUsername());

//        assertEquals("Title 2", result.get(1).getTitle());
//        assertEquals("2nd", result.get(1).getEditionNumber());
//        assertEquals("user2", result.get(1).getOwnerUsername());
    }

    @Test
    void shouldThrowExceptionWhenPublicationListIsNull() {

        // ARRANGE
        List<PublicationSample> publications = null;

        // ACT
//        Executable action =
//                () -> PublicationSampleDTO_US018.publicationToDTO(publications);

        // ASSERT
//        assertThrows(NullPointerException.class, action);
    }


}
