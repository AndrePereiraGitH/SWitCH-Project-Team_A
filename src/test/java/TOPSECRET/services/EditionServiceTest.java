package TOPSECRET.services;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.EditionFactory;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
class EditionServiceTest {

    // =========================================================================
    // Dependencies
    // =========================================================================

    @Mock
    private IEditionRepository editionRepository;

    @Mock
    private EditionFactory factory;

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

        MockitoAnnotations.openMocks(this);

        service = new EditionService(editionRepository, factory);

        codeDouble = mock(Code.class);
        physicalAttributesDouble = mock(PhysicalAttributes.class);
        publicationTypeIdDouble = mock(PublicationTypeId.class);
        publisherIDDouble = mock(PublisherID.class);
        typeOfBindingDouble = mock(TypeOfBinding.class);

        language = Locale.ENGLISH;
        date = LocalDate.of(2020, 1, 1);
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

    @Test
    void createAndSave_shouldThrowWhenEditionNumberIsNull() {

        assertThrows(NullPointerException.class, () ->
                service.createAndSave(
                        null,
                        codeDouble,
                        physicalAttributesDouble,
                        publicationTypeIdDouble,
                        publisherIDDouble,
                        "Book",
                        language,
                        date,
                        typeOfBindingDouble
                ));
    }

    @Test
    void createAndSave_shouldThrowWhenCodeIsNull() {

        assertThrows(NullPointerException.class, () ->
                service.createAndSave(
                        "1st",
                        null,
                        physicalAttributesDouble,
                        publicationTypeIdDouble,
                        publisherIDDouble,
                        "Book",
                        language,
                        date,
                        typeOfBindingDouble
                ));
    }

    @Test
    void createAndSave_shouldThrowWhenPhysicalAttributesIsNull() {

        assertThrows(NullPointerException.class, () ->
                service.createAndSave(
                        "1st",
                        codeDouble,
                        null,
                        publicationTypeIdDouble,
                        publisherIDDouble,
                        "Book",
                        language,
                        date,
                        typeOfBindingDouble
                ));
    }

    @Test
    void createAndSave_shouldThrowWhenPublicationTypeIdIsNull() {

        assertThrows(NullPointerException.class, () ->
                service.createAndSave(
                        "1st",
                        codeDouble,
                        physicalAttributesDouble,
                        null,
                        publisherIDDouble,
                        "Book",
                        language,
                        date,
                        typeOfBindingDouble
                ));
    }

    @Test
    void createAndSave_shouldThrowWhenPublisherIdIsNull() {

        assertThrows(NullPointerException.class, () ->
                service.createAndSave(
                        "1st",
                        codeDouble,
                        physicalAttributesDouble,
                        publicationTypeIdDouble,
                        null,
                        "Book",
                        language,
                        date,
                        typeOfBindingDouble
                ));
    }

    @Test
    void createAndSave_shouldThrowWhenMediaFormatIsNull() {

        assertThrows(NullPointerException.class, () ->
                service.createAndSave(
                        "1st",
                        codeDouble,
                        physicalAttributesDouble,
                        publicationTypeIdDouble,
                        publisherIDDouble,
                        null,
                        language,
                        date,
                        typeOfBindingDouble
                ));
    }

    @Test
    void createAndSave_shouldThrowWhenLanguageIsNull() {

        assertThrows(NullPointerException.class, () ->
                service.createAndSave(
                        "1st",
                        codeDouble,
                        physicalAttributesDouble,
                        publicationTypeIdDouble,
                        publisherIDDouble,
                        "Book",
                        null,
                        date,
                        typeOfBindingDouble
                ));
    }

    @Test
    void createAndSave_shouldThrowWhenDateIsNull() {

        assertThrows(NullPointerException.class, () ->
                service.createAndSave(
                        "1st",
                        codeDouble,
                        physicalAttributesDouble,
                        publicationTypeIdDouble,
                        publisherIDDouble,
                        "Book",
                        language,
                        null,
                        typeOfBindingDouble
                ));
    }

    @Test
    void createAndSave_shouldThrowWhenTypeOfBindingIsNull() {

        assertThrows(NullPointerException.class, () ->
                service.createAndSave(
                        "1st",
                        codeDouble,
                        physicalAttributesDouble,
                        publicationTypeIdDouble,
                        publisherIDDouble,
                        "Book",
                        language,
                        date,
                        null
                ));
    }
}