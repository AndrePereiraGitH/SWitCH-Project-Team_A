package TOPSECRET.dto.library.response;

import org.junit.jupiter.api.Test;
import org.springframework.hateoas.RepresentationModel;

import static org.junit.jupiter.api.Assertions.*;

class ResponseLibraryDTOTest {

    @Test
    void constructor_ShouldStoreValuesCorrectly() {

        // ACT
        ResponseLibraryDTO dto =
                new ResponseLibraryDTO("My Library", "USER-1");

        // ASSERT
        assertEquals("My Library", dto.getLibraryName());
        assertEquals("USER-1", dto.getUserId());
    }

    @Test
    void shouldBeEqual_whenValuesAreSame() {

        // ARRANGE
        ResponseLibraryDTO dto1 =
                new ResponseLibraryDTO("Lib", "USER-1");

        ResponseLibraryDTO dto2 =
                new ResponseLibraryDTO("Lib", "USER-1");

        // ASSERT
        assertEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqual_whenValuesAreDifferent() {

        // ARRANGE
        ResponseLibraryDTO dto1 =
                new ResponseLibraryDTO("Lib1", "USER-1");

        ResponseLibraryDTO dto2 =
                new ResponseLibraryDTO("Lib2", "USER-2");

        // ASSERT
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldHaveSameHashCode_whenObjectsAreEqual() {

        // ARRANGE
        ResponseLibraryDTO dto1 =
                new ResponseLibraryDTO("Lib", "USER-1");

        ResponseLibraryDTO dto2 =
                new ResponseLibraryDTO("Lib", "USER-1");

        // ASSERT
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_ShouldContainValues() {

        // ARRANGE
        ResponseLibraryDTO dto =
                new ResponseLibraryDTO("My Library", "USER-1");

        // ACT
        String result = dto.toString();

        // ASSERT
        assertNotNull(result);
        assertTrue(result.contains("My Library"));
        assertTrue(result.contains("USER-1"));
    }

    @Test
    void shouldExtendRepresentationModel() {

        // ACT
        ResponseLibraryDTO dto =
                new ResponseLibraryDTO("Lib", "USER-1");

        // ASSERT
        assertInstanceOf(RepresentationModel.class, dto);
    }
}