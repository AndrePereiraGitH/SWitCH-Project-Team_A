package TOPSECRET.repository;

import TOPSECRET.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BibliographicalDataRepositoryTest {

    // =====================================================
    // =================== SetUP ===========================
    // =====================================================

    private BibliographicalData bd1Double;
    private BibliographicalData bd2Double;
    private BibliographicalData bd3Double;

    @BeforeEach
    void setup() {

        bd1Double = mock(BibliographicalData.class);
        bd2Double = mock(BibliographicalData.class);
        bd3Double = mock(BibliographicalData.class);
    }

    // =====================================================
    // =================== Tests ===========================
    // =====================================================

    // Constructor -----------------------------------------------------------------------------------------------------
    @Test
    void constructor_CopiesListDefensively() {
        // Arrange
        List<BibliographicalData> original = new ArrayList<>();
        original.add(bd1Double);
        original.add(bd2Double);

        // Act
        BibliographicalDataRepository repo = new BibliographicalDataRepository(original);
        original.add(bd3Double);                                                           // Modifies the original list

        // Assert
        assertEquals(2, repo.getAllPublicationsBD().size());        // The repository should have the same size
    }

    @Test
    void constructor_ThrowsExceptionWhenListIsNull() {
        // Arrange
        List<BibliographicalData> nullList = null;

        // Act + Assert
        assertThrows(NullPointerException.class, () -> new BibliographicalDataRepository(nullList));

    }

    @Test
    void constructor_AllowsEmptyList() {
        // Arrange
        List<BibliographicalData> empty = new ArrayList<>();

        // Act
        BibliographicalDataRepository repo = new BibliographicalDataRepository(empty);

        // Assert
        assertTrue(repo.getAllPublicationsBD().isEmpty());
    }

    // Method getAllPublicationsBD -------------------------------------------------------------------------------------

    @Test
    void getAllPublicationsBD_ReturnsAllItems() {
        // Arrange
        List<BibliographicalData> original = List.of(bd1Double, bd2Double);
        BibliographicalDataRepository repo = new BibliographicalDataRepository(original);

        // Act
        List<BibliographicalData> result = repo.getAllPublicationsBD();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(bd1Double));
        assertTrue(result.contains(bd2Double));
    }

    @Test
    void getAllPublicationsBD_ReturnsUnmodifiableList() {
        // Arrange
        List<BibliographicalData> original = List.of(bd1Double, bd2Double);
        BibliographicalDataRepository repo = new BibliographicalDataRepository(original);

        // Act
        List<BibliographicalData> result = repo.getAllPublicationsBD();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(bd3Double));
    }

    @Test
    void getAllPublicationsBD_ReturnsNewCopyEachTime() {
        // Arrange
        List<BibliographicalData> original = List.of(bd1Double, bd2Double);
        BibliographicalDataRepository repo = new BibliographicalDataRepository(original);

        // Act
        List<BibliographicalData> first = repo.getAllPublicationsBD();
        List<BibliographicalData> second = repo.getAllPublicationsBD();

        // Assert
        assertNotSame(first, second);
    }
}