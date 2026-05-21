package TOPSECRET.dto.library.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestLibraryDTOTest {

    @Test
    void record_ShouldStoreValuesCorrectly() {

        // ACT
        RequestLibraryDTO dto =
                new RequestLibraryDTO("My Library", "USER-1");

        // ASSERT
        assertEquals("My Library", dto.libraryName());
        assertEquals("USER-1", dto.userId());
    }

    @Test
    void record_ShouldBeEqual_whenValuesAreSame() {

        // ARRANGE
        RequestLibraryDTO dto1 =
                new RequestLibraryDTO("Lib", "USER-1");

        RequestLibraryDTO dto2 =
                new RequestLibraryDTO("Lib", "USER-1");

        // ASSERT
        assertEquals(dto1, dto2);
    }

    @Test
    void record_ShouldNotBeEqual_whenValuesAreDifferent() {

        // ARRANGE
        RequestLibraryDTO dto1 =
                new RequestLibraryDTO("Lib1", "USER-1");

        RequestLibraryDTO dto2 =
                new RequestLibraryDTO("Lib2", "USER-2");

        // ASSERT
        assertNotEquals(dto1, dto2);
    }

    @Test
    void record_ShouldHaveConsistentHashCode() {

        // ARRANGE
        RequestLibraryDTO dto1 =
                new RequestLibraryDTO("Lib", "USER-1");

        RequestLibraryDTO dto2 =
                new RequestLibraryDTO("Lib", "USER-1");

        // ASSERT
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_ShouldContainValues() {

        // ACT
        RequestLibraryDTO dto =
                new RequestLibraryDTO("My Library", "USER-1");

        String result = dto.toString();

        // ASSERT
        assertTrue(result.contains("My Library"));
        assertTrue(result.contains("USER-1"));
    }
}