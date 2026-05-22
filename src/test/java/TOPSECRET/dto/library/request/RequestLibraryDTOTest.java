package TOPSECRET.dto.library.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestLibraryDTOTest {

    @Test
    void record_ShouldStoreValuesCorrectly() {

        // ACT
        RequestLibraryDTO dto =
                new RequestLibraryDTO("My Library");

        // ASSERT
        assertEquals("My Library", dto.libraryName());
    }

    @Test
    void record_ShouldBeEqual_whenValuesAreSame() {

        // ARRANGE
        RequestLibraryDTO dto1 =
                new RequestLibraryDTO("Lib");

        RequestLibraryDTO dto2 =
                new RequestLibraryDTO("Lib");

        // ASSERT
        assertEquals(dto1, dto2);
    }

    @Test
    void record_ShouldNotBeEqual_whenValuesAreDifferent() {

        // ARRANGE
        RequestLibraryDTO dto1 =
                new RequestLibraryDTO("Lib1");

        RequestLibraryDTO dto2 =
                new RequestLibraryDTO("Lib2");

        // ASSERT
        assertNotEquals(dto1, dto2);
    }

    @Test
    void record_ShouldHaveConsistentHashCode() {

        // ARRANGE
        RequestLibraryDTO dto1 =
                new RequestLibraryDTO("Lib");

        RequestLibraryDTO dto2 =
                new RequestLibraryDTO("Lib");

        // ASSERT
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_ShouldContainValues() {

        // ACT
        RequestLibraryDTO dto =
                new RequestLibraryDTO("My Library");

        String result = dto.toString();

        // ASSERT
        assertTrue(result.contains("My Library"));
    }
}