package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.EditionFactory;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.persistence.repositoryjpa.datamodel.EditionDM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Unit tests for {@link EditionAssembler}.
 *
 * <p>
 * This test class verifies the correct conversion between
 * {@link Edition} domain objects and {@link EditionDM} data models.
 * </p>
 *
 * <p>
 * Dependencies such as {@link EditionFactory} are mocked to isolate
 * the assembler logic and ensure that mapping behavior is tested
 * independently from domain creation.
 * </p>
 *
 * <p>
 * The test suite covers:
 * <ul>
 *     <li>Conversion from domain to data model ({@code toDM}).</li>
 *     <li>Conversion from data model to domain ({@code toDomain}).</li>
 *     <li>Correct mapping of all relevant fields.</li>
 * </ul>
 * </p>
 */
class EditionAssemblerTest {

    private EditionFactory factory;
    private EditionAssembler assembler;

    private EditionDM editionDMDouble;
    private Edition editionDouble;

    private Code codeDouble;
    private PhysicalAttributes physicalAttributesDouble;
    private PublicationTypeId publicationTypeIdDouble;
    private PublisherID publisherIDdouble;
    private TypeOfBinding typeOfBindingDouble;
    private EditionID editionIDDouble;

    @BeforeEach
    void setup() {

        factory = mock(EditionFactory.class);
        assembler = new EditionAssembler(factory);

        editionDouble = mock(Edition.class);
        editionDMDouble = mock(EditionDM.class);

        codeDouble = mock(Code.class);
        physicalAttributesDouble = mock(PhysicalAttributes.class);
        publicationTypeIdDouble = mock(PublicationTypeId.class);
        publisherIDdouble = mock(PublisherID.class);
        typeOfBindingDouble = mock(TypeOfBinding.class);
        editionIDDouble = mock(EditionID.class);

        when(editionDMDouble.getCode()).thenReturn(codeDouble);
        when(editionDMDouble.getPhysicalAttributes()).thenReturn(physicalAttributesDouble);
    }

    // ─────────────────────────────────────────────
    // toDM
    // ─────────────────────────────────────────────

    @Test
    void toDM_shouldMapFieldsCorrectly() {

        // Arrange
        when(editionDouble.id()).thenReturn(editionIDDouble);
        when(editionDouble.getEditionNumber()).thenReturn("1st");
        when(editionDouble.getCode()).thenReturn(codeDouble);
        when(editionDouble.getPhysicalAttributes()).thenReturn(physicalAttributesDouble);
        when(editionDouble.getPublicationTypeID()).thenReturn(publicationTypeIdDouble);
        when(editionDouble.getPublisherID()).thenReturn(publisherIDdouble);
        when(editionDouble.getMediaFormat()).thenReturn("Book");
        when(editionDouble.getEditionLanguage()).thenReturn(Locale.ENGLISH);
        when(editionDouble.getEditionDate()).thenReturn(LocalDate.of(2020,1,1));
        when(editionDouble.getTypeOfBinding()).thenReturn(typeOfBindingDouble);

        // Act
        EditionDM result = assembler.toDM(editionDouble);

        // Assert
        assertNotNull(result);
        assertEquals("1st", result.getEditionNumber());
        assertEquals(codeDouble, result.getCode());
        assertEquals(publicationTypeIdDouble, result.getPublicationTypeId());
        assertEquals(publisherIDdouble, result.getPublisherId());
    }

    // ─────────────────────────────────────────────
    // toDomain
    // ─────────────────────────────────────────────

    @Test
    void toDomain_shouldCallFactoryWithCorrectParameters() {

        // Arrange
        when(editionDMDouble.getEditionID()).thenReturn(editionIDDouble);
        when(editionDMDouble.getEditionNumber()).thenReturn("1st");
        when(editionDMDouble.getPhysicalAttributes()).thenReturn(physicalAttributesDouble);
        when(editionDMDouble.getPublicationTypeId()).thenReturn(publicationTypeIdDouble);
        when(editionDMDouble.getPublisherId()).thenReturn(publisherIDdouble);
        when(editionDMDouble.getMediaFormat()).thenReturn("Book");
        when(editionDMDouble.getEditionLanguage()).thenReturn(Locale.ENGLISH);
        when(editionDMDouble.getEditionDate()).thenReturn(LocalDate.of(2020,1,1));
        when(editionDMDouble.getTypeOfBinding()).thenReturn(typeOfBindingDouble);

        Edition expectedEdition = mock(Edition.class);

        when(factory.create(
                eq(editionIDDouble),
                eq("1st"),
                eq(codeDouble),
                eq(physicalAttributesDouble),
                eq(publicationTypeIdDouble),
                eq(publisherIDdouble),
                eq("Book"),
                eq(Locale.ENGLISH),
                eq(LocalDate.of(2020,1,1)),
                eq(typeOfBindingDouble)
        )).thenReturn(expectedEdition);

        // Act
        Edition result = assembler.toDomain(editionDMDouble);

        // Assert
        assertEquals(expectedEdition, result);
    }
}