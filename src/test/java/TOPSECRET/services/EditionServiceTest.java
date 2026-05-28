package TOPSECRET.services;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.EditionFactory;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EditionService}.
 *
 * <p>Covers two areas:</p>
 * <ul>
 *   <li><b>createAndSave — happy path</b>: verifies that an {@link Edition}
 *       is created via {@link EditionFactory} and persisted via
 *       {@link IEditionRepository}, returning the saved instance.</li>
 *   <li><b>createAndSave — null rejection</b>: verifies that mandatory parameters
 *       throw {@link NullPointerException} when {@code null}.</li>
 * </ul>
 *
 * <h2>Test setup</h2>
 * <p>
 * These are pure unit tests using Mockito only.
 * No Spring context is loaded.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class EditionServiceTest {

    // =========================================================================
    // Dependencies
    // =========================================================================

    @Mock
    private IEditionRepository editionRepository;

    @Mock
    private EditionFactory factory;

    @InjectMocks
    private EditionService service;

    // =========================================================================
    // Test Doubles
    // =========================================================================

    private Code codeDouble;
    private PhysicalAttributes physicalAttributesDouble;
    private PublicationTypeId publicationTypeIdDouble;
    private PublisherID publisherIDDouble;
    private TypeOfBinding typeOfBindingDouble;

    private Locale language;
    private LocalDate date;

    @BeforeEach
    void setUp() {

        codeDouble = mock(Code.class);
        physicalAttributesDouble = mock(PhysicalAttributes.class);
        publicationTypeIdDouble = mock(PublicationTypeId.class);
        publisherIDDouble = mock(PublisherID.class);
        typeOfBindingDouble = mock(TypeOfBinding.class);

        language = Locale.ENGLISH;
        date = LocalDate.of(2020, 1, 1);
    }

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void constructor_shouldThrowWhenRepositoryIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> new EditionService(null, factory));
    }

    @Test
    void constructor_shouldThrowWhenFactoryIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> new EditionService(editionRepository, null));
    }

    // =========================================================================
    // createAndSave
    // =========================================================================

    @Test
    void createAndSave_shouldReturnEdition() {

        // Arrange
        Edition editionCreatedDouble = mock(Edition.class);
        Edition editionSavedDouble = mock(Edition.class);

        when(factory.create(
                "1st",
                codeDouble,
                physicalAttributesDouble,
                publicationTypeIdDouble,
                publisherIDDouble,
                "Book",
                language,
                date,
                typeOfBindingDouble
        )).thenReturn(editionCreatedDouble);
        when(editionRepository.save(editionCreatedDouble))
                .thenReturn(editionSavedDouble);

        // Act
        Edition result = service.createAndSave(
                "1st",
                codeDouble,
                physicalAttributesDouble,
                publicationTypeIdDouble,
                publisherIDDouble,
                "Book",
                language,
                date,
                typeOfBindingDouble
        );

        // Assert
        assertEquals(editionSavedDouble, result);

    }
}