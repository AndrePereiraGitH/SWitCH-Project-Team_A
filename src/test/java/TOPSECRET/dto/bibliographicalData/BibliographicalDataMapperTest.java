package TOPSECRET.dto.bibliographicalData;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.dto.bibliographicalData.response.responseBibliographicalDataDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the BibliographicalDataMapper class.
 * <p>
 * This test suite validates the behavior of the mapper responsible for
 * converting BibliographicalData domain objects into
 * responseBibliographicalDataDTO objects. It covers constructor validation,
 * null argument handling, successful DTO mappings, exception scenarios when
 * authors are not found, and list conversion operations.
 */
@ExtendWith(MockitoExtension.class)
class BibliographicalDataMapperTest {

    // =============================================================
    // ==================== Constructor Tests ======================
    // =============================================================
    @Test
    void constructorShouldBePrivate() throws NoSuchMethodException {
        // Act
        Constructor<BibliographicalDataMapper> constructor =
                BibliographicalDataMapper.class.getDeclaredConstructor();

        // Assert
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    }

    @Test
    void constructorShouldThrowExceptionWhenInvoked() throws Exception {
        // Act
        Constructor<BibliographicalDataMapper> constructor =
                BibliographicalDataMapper.class.getDeclaredConstructor();

        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );

        // Assert
        assertInstanceOf(UnsupportedOperationException.class, exception.getCause());
    }

    // =============================================================
    // ======================== toDTO Tests ========================
    // =============================================================

    @Test
    void toDTO_nullBibliographicalData_throwsIllegalArgumentException() {
        // Arrange
        List<String> authorNames = List.of("Author 1", "Author 2", "Author 3");

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> BibliographicalDataMapper.toDTO(null, authorNames)
        );

        // Assert
        assertEquals("Bibliographical data cannot be null", exception.getMessage());
    }

    @Test
    void toDTO_nullAuthorNames_throwsIllegalArgumentException() {
        // Arrange
        BibliographicalData bd = mock(BibliographicalData.class);

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> BibliographicalDataMapper.toDTO(bd, null)
        );

        // Assert
        assertEquals("Author names cannot be null", exception.getMessage());
    }

    @Test
    void toDTO_validArguments_returnsDTO() throws InstanceNotFoundException {
        // Arrange
        List<String> authorNames = List.of("Author 1", "Author 2", "Author 3");

        BibliographicalData bd = mock(BibliographicalData.class);

        BibliographicalDataId bdId = mock(BibliographicalDataId.class);

        when(bd.id()).thenReturn(bdId);
        when(bdId.getID()).thenReturn("BD1");
        when(bd.getTitle()).thenReturn("Title example");

        // Act
        responseBibliographicalDataDTO dto = BibliographicalDataMapper.toDTO(bd, authorNames);

        // Assert
        assertEquals("BD1", dto.getBdId());
        assertEquals("Title example", dto.getPubTitle());
        assertEquals(List.of("Author 1", "Author 2", "Author 3"), dto.getAuthorName());
    }

    // =============================================================
    // ======================= toDTOs Tests ========================
    // =============================================================

    @Test
    void toDTOsShouldThrowNullPointerExceptionWhenBibliographicalDataListIsNull() {
        // Arrange
        Map<BibliographicalDataId, List<String>> authorNamesMap = Map.of();

        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> BibliographicalDataMapper.toDTOs(null, authorNamesMap)
        );

        // Assert
        assertEquals("Bibliographical data list can't be null", exception.getMessage());
    }

    @Test
    void toDTOsShouldThrowNullPointerExceptionWhenAuthorNamesMapIsNull() {
        // Arrange
        List<BibliographicalData> bibliographicalDataList = List.of(mock(BibliographicalData.class));

        // Act
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> BibliographicalDataMapper.toDTOs(bibliographicalDataList, null)
        );

        // Assert
        assertEquals("Author names map can't be null", exception.getMessage());
    }

    @Test
    void toDTOs_shouldThrowIllegalArgumentException_whenAuthorNamesMissingForBibliographicalData() {
        // Arrange
        BibliographicalDataId bdId = mock(BibliographicalDataId.class);
        BibliographicalData bd = mock(BibliographicalData.class);

        when(bd.id()).thenReturn(bdId);

        List<BibliographicalData> bibliographicalDataList = List.of(bd);
        Map<BibliographicalDataId, List<String>> authorNamesMap = Map.of();

        // Act
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> BibliographicalDataMapper.toDTOs(bibliographicalDataList, authorNamesMap)
        );

        // Assert
        assertTrue(exception.getMessage().contains("Author names not found for bibliographical data"));
    }

    @Test
    void toDTOsShouldReturnEmptyListWhenBibliographicalDataListIsEmpty() throws InstanceNotFoundException {
        // Act
        List<responseBibliographicalDataDTO> result =
                BibliographicalDataMapper.toDTOs(List.of(), Map.of());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOsShouldReturnDTOListWhenValidInput() throws InstanceNotFoundException {
        // Arrange
        BibliographicalDataId bdIdDouble = mock(BibliographicalDataId.class);
        BibliographicalData bdDouble = mock(BibliographicalData.class);

        List<String> authorNames = List.of("Author 1", "Author 2");

        when(bdDouble.id()).thenReturn(bdIdDouble);
        when(bdDouble.getTitle()).thenReturn("Some title");

        // Act
        List<responseBibliographicalDataDTO> result =
                BibliographicalDataMapper.toDTOs(
                        List.of(bdDouble),
                        Map.of(bdIdDouble, authorNames)
                );

        responseBibliographicalDataDTO dto = result.get(0);

        // Assert
        assertEquals(1, result.size());
        assertNotNull(dto);
        assertEquals("Some title", dto.getPubTitle());
        assertEquals(authorNames, dto.getAuthorName());
    }


    // =============================================================
    // ===================== toAuthorId Tests ======================
    // =============================================================

    @Test
    void toAuthorIdShouldReturnListOfIdsWhenAuthorNamesIsValid() {
        // Arrange
        List<String> authorNames = List.of("Author 1", "Author 2", "Author 3");

        // Act
        List<AuthorId> result = BibliographicalDataMapper.toAuthorId(authorNames);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenAuthorNamesListIsEmpty() {
        // Arrange
        List<String> authorNames = List.of();

        // Act
        List<AuthorId> result = BibliographicalDataMapper.toAuthorId(authorNames);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // =============================================================
    // ===================== toGenreId Tests =======================
    // =============================================================

    @Test
    void toGenreIdShouldReturnListOfIdsWhenGenreNamesIsValid(){
        // Arrange
        List<String> genreNames = List.of("Fantasy", "Sci-fi", "Horror");

        // Act
        List<GenreId> result = BibliographicalDataMapper.toGenreId(genreNames);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void shouldReturnEmptyListWhenGenreNamesListIsEmpty(){
        // Arrange
        List<String> genreNames = List.of();

        // Act
        List<GenreId> result = BibliographicalDataMapper.toGenreId(genreNames);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}